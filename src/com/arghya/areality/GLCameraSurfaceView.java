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
import java.util.ArrayList;

/**
 *
 * @author sur
 */
public class GLCameraSurfaceView extends GLSurfaceView {

    GLCameraRenderer mRenderer;

    public GLCameraSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        
        /** To make it translucent but it isn't necessary in the current implementation
        setZOrderMediaOverlay(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        
        getHolder().setFormat(PixelFormat.RGBA_8888);
        */
        
        mRenderer = new GLCameraRenderer((MainActivity) context);
        setRenderer(mRenderer);
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
                mRenderer.changeRecordingState(recordingEnabled);
            }
        });
    }
    
    public void togglePauseRecording(final boolean paused) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.togglePauseRecording(paused);
            }
        });
    }
    
    public void setMedia(final String filePath, final int type) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.setMedia(filePath, type);
            }
        });
    }
    
    public void setSelectMode(final int mode) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.setSelectMode(mode);
            }
        });
    }
    
    public void clearKeyList() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.clearKeys();
            }
        });
    }
    
    public ArrayList<float[]> getKeys() {
        return mRenderer.getKeys();
    }
    
    public TransparentColorController getTCController() {
        return mRenderer.getTCController();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mRenderer != null) {
                    // Ensure we call switchMode() on the OpenGL thread.
                    // queueEvent() is a method of GLSurfaceView that will do this for us.
                    
                    final Point p = new Point();
                    p.x = (int)event.getX();
                    p.y = (int)event.getY();
                    
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            mRenderer.handleTouchAtCoordinate(p);
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
                mRenderer.release();
            }
        });
    }
}
