/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author sur
 */
public class DirectVideo {
    
    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mTextureCoordHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 2;
    static float squareVertices[] = {
        -1.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, -1.0f,
        1.0f, 1.0f
    };

    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    static float textureVertices[] = { // in counterclockwise order:
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    };

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public DirectVideo() {
        vertexBuffer = OpenGLESUtility.createBuffer(squareVertices);
        drawListBuffer = OpenGLESUtility.createBuffer(drawOrder);
        textureVerticesBuffer = OpenGLESUtility.createBuffer(textureVertices);

        int vertexShader = OpenGLESUtility.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.VertexShader.normal());
        int fragmentShader = OpenGLESUtility.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.FragmentShader.textureChromaKeyYUV());
        
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] key) {
        GLES20.glUseProgram(mProgram);
        
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, 
                        GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, 
                                false, vertexStride, textureVerticesBuffer);
        
        //float key[] = {0.121f, 0.275f, 0.738f, 0.5f};
        
        int chromaKeyHandle = GLES20.glGetUniformLocation(mProgram, "key");
        GLES20.glUniform4fv(chromaKeyHandle, 1, key, 0);
        
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
    }
}
