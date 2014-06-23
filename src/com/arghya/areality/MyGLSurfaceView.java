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
public class MyGLSurfaceView extends GLSurfaceView {

    MyGL20Renderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        renderer = new MyGL20Renderer((MainActivity) context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    public MyGL20Renderer getRenderer() {
        return renderer;
    }
}
