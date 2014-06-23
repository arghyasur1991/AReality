package com.arghya.areality;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static com.arghya.areality.DirectVideo.COORDS_PER_VERTEX;
import static com.arghya.areality.DirectVideo.textureVertices;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Square {

    private final String vertexShaderCode
            = // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;"
            + "attribute vec4 vPosition;"
            + "attribute vec2 inputTextureCoordinate;"
            + "varying vec2 textureCoordinate;"
            + "void main() {"
            + // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;"
            + "textureCoordinate = inputTextureCoordinate;"
            + "}";

    private final String fragmentShaderColorCode
            = "precision mediump float;"
            + "uniform vec4 vColor;"
            + "void main() {"
            + "  gl_FragColor = vColor;"
            + "}";

    private final String fragmentShaderTexCode
            = "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float; \n"
            + "varying vec2 textureCoordinate;                            \n"
            + "uniform samplerExternalOES s_texture;               \n"
            + "void main() {"
            + "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n"
            + "}";

    private final FloatBuffer vertexBuffer, textureVerticesBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int mTextureCoordHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static final int ISTEX = -1;
    private float squareCoords[] = {
        -1.0f, 1.0f, 0.0f, // top left
        -1.0f, -1.0f, 0.0f, // bottom left
        1.0f, -1.0f, 0.0f, // bottom right
        1.0f, 1.0f, 0.0f // top right
    };

    private final short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    static float textureVertices[] = { // in counterclockwise order:
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    };

    float color[] = {0.0f, 1.0f, 0.0f, 1.0f};
    //float color[] = {0.2f, 0.709803922f, 0.898039216f, 1.0f};
    private int texture;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Square(float z, float scale, float alpha, int _texture) {
        texture = _texture;

        for (int i = 0; i < squareCoords.length; i++) {
            squareCoords[i] *= scale;
            if ((i + 1) % 3 == 0) {
                squareCoords[i] += z;
            }
        }

        color[3] = alpha;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = OpenGLESUtility.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = 0;
        if (texture != ISTEX) {
            fragmentShader = OpenGLESUtility.loadShader(
                    GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderTexCode);
        } else {
            fragmentShader = OpenGLESUtility.loadShader(
                    GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderColorCode);
        }

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw this
     * shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        if (texture != ISTEX) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);
        }

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        if (texture != ISTEX) {
            mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
            GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
            GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureVerticesBuffer);

            mColorHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");
        } else {
            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

            // Set color for drawing the triangle
            GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        }

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //MyGL20Renderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        //MyGL20Renderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
