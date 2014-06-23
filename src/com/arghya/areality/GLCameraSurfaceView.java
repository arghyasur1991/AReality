/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

/**
 *
 * @author sur
 */
public class GLCameraSurfaceView extends GLSurfaceView {

    GLCameraRenderer renderer;

    public GLCameraSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        
        getHolder().setFormat(PixelFormat.RGBA_8888);
        
        renderer = new GLCameraRenderer((MainActivity) context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    public GLCameraRenderer getRenderer() {
        return renderer;
    }
}
