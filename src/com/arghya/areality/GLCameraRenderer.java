/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @author sur
 */
public class GLCameraRenderer implements GLSurfaceView.Renderer {

    DirectVideo mDirectVideo;
    int texture;
    private SurfaceTexture surface;
    MainActivity delegate;

    float[] key = {0, 0, 0, 0};

    public GLCameraRenderer(MainActivity _delegate) {
        delegate = _delegate;
    }

    public void onSurfaceCreated(GL10 unused, javax.microedition.khronos.egl.EGLConfig config) {
        texture = createExternalTexture();
        mDirectVideo = new DirectVideo();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        delegate.startCamera(texture);
    }
    
    public void setKey(Point p)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(4);
        bb.order(ByteOrder.nativeOrder());
        bb.position(0);
        GLES20.glReadPixels(p.x, p.y , 1, 1, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, bb);
        byte b[] = new byte[4];
        bb.get(b);
        key[0] = (float)(bb.get(0) & 0xFF)/ 255.0f;
        key[1] = (float)(bb.get(1) & 0xFF)/ 255.0f;
        key[2] = (float)(bb.get(2) & 0xFF)/ 255.0f;
    }

    public void onDrawFrame(GL10 gl) {
        float[] mtx = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        surface.updateTexImage();
        surface.getTransformMatrix(mtx);
        
        mDirectVideo.draw(key);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) {       //Prevent A Divide By Zero By
            height = 1;         //Making Height Equal One
        }
        GLES20.glViewport(0, 0, width, height);
    }

    private static int createExternalTexture() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void setSurface(SurfaceTexture _surface) {
        surface = _surface;
    }
}
