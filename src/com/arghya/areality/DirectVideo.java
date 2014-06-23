/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author sur
 */
public class DirectVideo {

    private final String vertexShaderCode
            = "attribute vec4 position;"
            + "attribute vec2 inputTextureCoordinate;"
            + "varying vec2 textureCoordinate;"
            + "void main()"
            + "{"
            + "gl_Position = position;"
            + "textureCoordinate = inputTextureCoordinate;"
            + "}";

    private final String fragmentShaderCode
            = "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float;"
            + "varying vec2 textureCoordinate;                            \n"
            + "uniform samplerExternalOES s_texture;               \n"
            + "void main() {"
            + "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n"
            + "}";
    
    private final String fragmentShaderBWCode
            = "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float;"
            + "varying vec2 textureCoordinate;                            \n"
            + "uniform samplerExternalOES s_texture;               \n"
            + "void main() {"
            + "vec4 Ca = texture2D(s_texture, textureCoordinate); \n"
            + "float lum = 0.2126 * Ca.r + 0.7152 * Ca.g + 0.0722 * Ca.b; \n"
            + "float alpha = 1.0; \n"
            + "if(Ca.r < 0.8 && Ca.g < 0.8 && Ca.b > 0.7) \n"
            + "alpha = 0.0; \n"
            + "  gl_FragColor = vec4(Ca.r, Ca.g, Ca.b, alpha);\n"
            + "}";
    
    
    
    /*private final String vertexShaderCode
            = "#extension GL_OES_EGL_image_external : require\n"
            + "attribute vec4 position;"
            + "attribute vec4 inputTextureCoordinate;"
            + "varying vec2 textureCoordinate;"
            + "void main()"
            + "{"
            + "gl_Position = position;"
            + "textureCoordinate = inputTextureCoordinate.xy;"
            + "}";

    private final String fragmentShaderCode
            = "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float;"
            + "uniform vec4 vColor;"
            + "void main() {"
            + "  gl_FragColor = vColor;"
            + "}";*/

    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
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

    private int texture;

    public DirectVideo(int _texture) {
        texture = _texture;

        ByteBuffer bb = ByteBuffer.allocateDirect(squareVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareVertices);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);

        int vertexShader = MyGL20Renderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        //int fragmentShader = MyGL20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        int fragmentShaderBW = MyGL20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderBWCode);
        
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShaderBW); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
    }

    public void draw() {
        GLES20.glUseProgram(mProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);
        
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureVerticesBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");
        
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, squareVertices.length / 3);
        
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
    }
}
