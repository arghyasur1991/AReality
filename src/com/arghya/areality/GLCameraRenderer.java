/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @author sur
 */
public class GLCameraRenderer implements GLSurfaceView.Renderer {
    int texture;
    private SurfaceTexture surface;
    MainActivity delegate;
    
    private Square mSquare;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mSTMatrix = new float[16];

    float[] key = {0, 0, 0, 0};

    public GLCameraRenderer(MainActivity _delegate) {
        delegate = _delegate;
    }

    public void onSurfaceCreated(GL10 unused, javax.microedition.khronos.egl.EGLConfig config) {
        texture = createExternalTexture();
        mSquare = new Square();
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
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        surface.updateTexImage();
        surface.getTransformMatrix(mSTMatrix);
        
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw square
        mSquare.draw(mMVPMatrix, mSTMatrix, key);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) {       //Prevent A Divide By Zero By
            height = 1;         //Making Height Equal One
        }
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    private static int createExternalTexture() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void setSurface(SurfaceTexture _surface) {
        surface = _surface;
    }
}
