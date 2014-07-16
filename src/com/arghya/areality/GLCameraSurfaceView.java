/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Bitmap;
//import android.graphics.PixelFormat;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 *
 * @author sur
 */
public class GLCameraSurfaceView extends GLSurfaceView {

    GLCameraRenderer renderer;

    public GLCameraSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        
        /** To make it translucent but it isn't necessary in the current implementation
        setZOrderMediaOverlay(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        
        getHolder().setFormat(PixelFormat.RGBA_8888);
        */
        
        renderer = new GLCameraRenderer((MainActivity) context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    public void capture(final Screenshot screenShot) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                final int[] b = OpenGLESUtility.getGLBuffer(getWidth(), getHeight());
                
                Thread writeScreenThread = new Thread(new Runnable() {

                    public void run() {
                        Bitmap bmp = OpenGLESUtility.getGLBitmap(b, getWidth(), getHeight());
                        screenShot.setCameraBitmap(bmp);
                    }
                });
                
                writeScreenThread.start();
            }
        });
    }
    
    public void changeRecordingState(final boolean recordingEnabled) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.changeRecordingState(recordingEnabled);
            }
        });
    }
    
    public void setMedia(final String filePath) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.setMedia(filePath);
            }
        });
    }
    
    public void setSelectMode(final int mode) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.setSelectMode(mode);
            }
        });
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (renderer != null) {
                    // Ensure we call switchMode() on the OpenGL thread.
                    // queueEvent() is a method of GLSurfaceView that will do this for us.
                    
                    final Point p = new Point();
                    p.x = (int)event.getX();
                    p.y = (int)event.getY();
                    
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            renderer.handleTouchAtCoordinate(p);
                        }
                    });

                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }
    
    public void release() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.release();
            }
        });
    }
}
