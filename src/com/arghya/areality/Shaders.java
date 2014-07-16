/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.opengl.GLES20;
import java.util.ArrayList;

/**
 *
 * @author sur
 */
public class Shaders {
    public static int VERTEX_SHADER_TEXTURE = 0;
    
    public static int FRAGMENT_SHADER_COLOR = 0;
    public static int FRAGMENT_SHADER_TEXTURE = 1;
    public static int FRAGMENT_SHADER_TEXTURE_2D = 2;
    public static int FRAGMENT_SHADER_TEXTURE_BW = 3;
    public static int FRAGMENT_SHADER_TEXTURE_CHROMA_KEY = 4;
    public static int FRAGMENT_SHADER_TEXTURE_CHROMA_KEY_YUV = 5;
    public static int FRAGMENT_SHADER_TEXTURE_CHROMA_KEY_BLEND = 6;
    
    private int mProgram;
    private final GLCameraRenderer mRenderer;
    private final VertexShader mVertexShader;
    private final FragmentShader mFragmentShader;
    private int mTextureIndex;
    
    public Shaders(GLCameraRenderer renderer, int vShader, int fShader) {
        mRenderer = renderer;
        
        mVertexShader = new VertexShader(vShader);
        mFragmentShader = new FragmentShader(fShader);
    }
    
    public static void init() {
        VertexShader.initVertexShaders();
        FragmentShader.initFragmentShaders();
    }
    
    public void setTextureIndex(int index) {
        mTextureIndex = index;
    }
    
    public String getVertexShaderCode() {
        return mVertexShader.getCode();
    }
    
    public String getFragmentShaderCode() {
        return mFragmentShader.getCode();
    }
    
    public void setProgram(int program) {
        mProgram = program;
    }
    
    public void doShaderSpecificTasks() {
        int texHandle = GLES20.glGetUniformLocation(mProgram, "sTexture");
        
        switch(mFragmentShader.getIndex()) {
            case 0:
                break;
            case 1:
            case 2:
            case 3:
                GLES20.glUniform1i(texHandle, mTextureIndex);
                break;
            case 4:
            case 5:
                float[] key = {-100.0f, -100.0f, -100.0f, -100.f};
                int chromaKeyHandle = GLES20.glGetUniformLocation(mProgram, "uKey");
                GLES20.glUniform4fv(chromaKeyHandle, 1, key, 0);
                GLES20.glUniform1i(texHandle, mTextureIndex);
                break;
            case 6:
                texHandle = GLES20.glGetUniformLocation(mProgram, "sTexture1");
                int texHandle2 = GLES20.glGetUniformLocation(mProgram, "sTexture2");
                
                if(true) {
                    ArrayList<float[]> keys = mRenderer.getKeys();
                    ArrayList<float[]> tolerances = mRenderer.getTolerances();

                    int numKeysHandle = GLES20.glGetUniformLocation(mProgram, "uNumOfKeys");
                    GLES20.glUniform1i(numKeysHandle, keys.size());

                    float[] keyBuffer = getBuffer(keys);

                    float[] toleranceBuffer = getBuffer(tolerances);

                    int keysHandle = GLES20.glGetUniformLocation(mProgram, "uKeys");
                    GLES20.glUniform3fv(keysHandle, TransparentColorController.MAX_KEYS, keyBuffer, 0);

                    int toleranceHandle = GLES20.glGetUniformLocation(mProgram, "uThresholds");
                    GLES20.glUniform3fv(toleranceHandle, TransparentColorController.MAX_KEYS, toleranceBuffer, 0);
                }
                else if(false){
                    float[] key2 = {-100.0f, -100.0f, -100.0f, -100.f};
                    int chromaKeyHandle2 = GLES20.glGetUniformLocation(mProgram, "uKey");
                    GLES20.glUniform4fv(chromaKeyHandle2, 1, key2, 0);
                }
                
                GLES20.glUniform1i(texHandle, mTextureIndex);
                GLES20.glUniform1i(texHandle2, mTextureIndex + 1);
                break;
            default:
        }
    }
    
    private float[] getBuffer(ArrayList<float[]> list) {
        float[] buffer = new float[3 * TransparentColorController.MAX_KEYS];
        for (int i = 0; i < list.size(); i++) {
            buffer[i * 3] = list.get(i)[0];
            buffer[i * 3 + 1] = list.get(i)[1];
            buffer[i * 3 + 2] = list.get(i)[2];
        }
        
        for(int j = list.size(); j < TransparentColorController.MAX_KEYS; j++) {
            buffer[j * 3] = -100.0f;
            buffer[j * 3 + 1] = -100.0f;
            buffer[j * 3 + 2] = -100.0f;
        }
        
        return buffer;
    }
    
    public static class VertexShader {
        private final int mIndex;
        private final static ArrayList<String> mShaderCodes = new ArrayList<String>();
        
        public VertexShader(int index) {
            mIndex = index;
        }

        private static void initVertexShaders() {
            mShaderCodes.add(texture());
        }

        public int getIndex() {
            return mIndex;
        }

        public String getCode() {
            return mShaderCodes.get(mIndex);
        }
        
        private static String texture() {
            final String vertexShaderCode
                    = "uniform mat4 uMVPMatrix;\n"
                    + "uniform mat4 uSTMatrix;\n"
                    + "attribute vec4 aPosition;\n"
                    + "attribute vec4 aTextureCoord;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "void main() {\n"
                    + "    gl_Position = uMVPMatrix * aPosition;\n"
                    + "    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n"
                    + "}\n";

            return vertexShaderCode;
        }
    }
    
    public static class FragmentShader {
        private final int mIndex;
        private final static ArrayList<String> mShaderCodes = new ArrayList<String>();

        public FragmentShader(int index) {
            mIndex = index;
        }
        
        private static void initFragmentShaders() {
            mShaderCodes.add(color());
            mShaderCodes.add(texture());
            mShaderCodes.add(texture2D());
            mShaderCodes.add(textureBW());
            mShaderCodes.add(textureChromaKey());
            mShaderCodes.add(textureChromaKeyYUV());
            mShaderCodes.add(textureChromaKeyBlend());
        }
        
        public int getIndex() {
            return mIndex;
        }

        public String getCode() {
            return mShaderCodes.get(mIndex);
        }
        
        private static String color() {
            final String fragmentShaderCode
                = "precision mediump float;"
                + "uniform vec4 vColor;"
                + "void main() {"
                + "  gl_FragColor = vColor;"
                + "}";
            
            return fragmentShaderCode;
        }
        
        private static String texture() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
                    + "}\n";
            return fragmentShaderCode;
        }
        
        private static String texture2D() {
            final String fragmentShaderCode
                    = "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform sampler2D sTexture;\n"
                    + "void main() {\n"
                    + "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
                    + "}\n";
            return fragmentShaderCode;
        }
        
        private static String textureBW() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "    vec4 Ca = texture2D(sTexture, vTextureCoord); \n"
                    + "    float lum = 0.2126 * Ca.r + 0.7152 * Ca.g + 0.0722 * Ca.b; \n"
                    + "    float alpha = 1.0; \n"
                    + "    gl_FragColor = vec4(lum, lum, lum, alpha);\n"
                    + "}\n";
            return fragmentShaderCode;
        }
        
        private static String textureChromaKey() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform vec4 uKey;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "    vec4 Ca = texture2D(sTexture, vTextureCoord); \n"
                    + "float alpha = 1.0; \n"
                    + "float threshold = uKey.a; \n"
                    + "float redDiff = uKey.r - Ca.r; \n"
                    + "float greenDif = uKey.g - Ca.g; \n"
                    + "float blueDiff = uKey.b - Ca.b; \n"
                    + "if(abs(redDiff) < threshold && abs(greenDif) < threshold && abs(blueDiff) < threshold) \n"
                    + "alpha = 0.0; \n"
                    + "  gl_FragColor = vec4(Ca.r, Ca.g, Ca.b, alpha);\n"
                    + "}";
            return fragmentShaderCode;
        }
        
        private static String textureChromaKeyYUV() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform vec4 uKey;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "    vec4 Ca = texture2D(sTexture, vTextureCoord); \n"
                    + "float yDiff = 0.299 * (Ca.r - uKey.r) + 0.587 * (Ca.g - uKey.g) + 0.114 * (Ca.b - uKey.b); \n"
                    + "float uDiff = -0.1471 * (Ca.r - uKey.r) - 0.28886 * (Ca.g - uKey.g) + 0.436 * (Ca.b - uKey.b); \n"
                    + "float vDiff = 0.615 * (Ca.r - uKey.r) - 0.51499 * (Ca.g - uKey.g) - 0.10001 * (Ca.b - uKey.b); \n"
                    + "float alpha = 1.0; \n"
                    + "float threshold = uKey.a; \n"
                    + "if(abs(yDiff) < 0.2 && abs(uDiff) < 0.15 && abs(vDiff) < 0.15) \n"
                    + "alpha = 0.0; \n"
                    + "  gl_FragColor = vec4(Ca.r, Ca.g, Ca.b, alpha);\n"
                    + "}";
            return fragmentShaderCode;
        }
        
        private static String textureChromaKeyBlend2() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform vec4 uKey;\n"
                    + "uniform samplerExternalOES sTexture1;\n"
                    + "uniform samplerExternalOES sTexture2;\n"
                    + "void main() {\n"
                    + "    vec4 Ca = texture2D(sTexture1, vTextureCoord); \n"
                    + "    vec4 Cb = texture2D(sTexture2, vTextureCoord); \n"
                    + "float yDiff = 0.299 * (Ca.r - uKey.r) + 0.587 * (Ca.g - uKey.g) + 0.114 * (Ca.b - uKey.b); \n"
                    + "float uDiff = -0.1471 * (Ca.r - uKey.r) - 0.28886 * (Ca.g - uKey.g) + 0.436 * (Ca.b - uKey.b); \n"
                    + "float vDiff = 0.615 * (Ca.r - uKey.r) - 0.51499 * (Ca.g - uKey.g) - 0.10001 * (Ca.b - uKey.b); \n"
                    + "float alpha = 1.0; \n"
                    + "float threshold = uKey.a; \n"
                    + "if(abs(yDiff) < 0.2 && abs(uDiff) < 0.15 && abs(vDiff) < 0.15) \n"
                    + "alpha = 0.0; \n"
                    + "float r = Ca.r * alpha + (1.0 - alpha) * Cb.r; \n"
                    + "float g = Ca.g * alpha + (1.0 - alpha) * Cb.g; \n"
                    + "float b = Ca.b * alpha + (1.0 - alpha) * Cb.b; \n"
                    + "  gl_FragColor = vec4(r, g, b, 1.0);\n"
                    + "}";
            return fragmentShaderCode;
        }
        
        private static String textureChromaKeyBlend() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform int uNumOfKeys;\n"
                    + "uniform vec3 uKeys[10];\n"
                    + "uniform vec3 uThresholds[10];\n"
                    + "uniform samplerExternalOES sTexture1;\n"
                    + "uniform samplerExternalOES sTexture2;\n"
                    + "void isCloseToKey(in vec4 Ca, in int index, out bool isClose) {\n"
                    + "     float yDiff = 0.299 * (Ca.r - uKeys[index].x) + 0.587 * (Ca.g - uKeys[index].y) + 0.114 * (Ca.b - uKeys[index].z); \n"
                    + "     float uDiff = -0.1471 * (Ca.r - uKeys[index].x) - 0.28886 * (Ca.g - uKeys[index].y) + 0.436 * (Ca.b - uKeys[index].z); \n"
                    + "     float vDiff = 0.615 * (Ca.r - uKeys[index].x) - 0.51499 * (Ca.g - uKeys[index].y) - 0.10001 * (Ca.b - uKeys[index].z); \n"
                    + "     if(abs(yDiff) < uThresholds[index].x && abs(uDiff) < uThresholds[index].y && abs(vDiff) < uThresholds[index].z) \n"
                    + "         isClose = true;\n"
                    + "     else\n"
                    + "         isClose = false;\n"
                    + "}\n"
                    + "void main() {\n"
                    + "     vec4 Ca = texture2D(sTexture1, vTextureCoord); \n"
                    + "     vec4 Cb = texture2D(sTexture2, vTextureCoord); \n"
                    + "     float alpha = 1.0; \n"
                    + "     for(int i = 0; i < uNumOfKeys; i++) {\n"
                    + "         bool isClose = false;\n"
                    + "         isCloseToKey(Ca, i, isClose);\n"
                    + "         if(isClose) {\n"
                    + "             alpha = 0.0;\n"
                    + "             break;\n"
                    + "         }\n"
                    + "     }\n"
                    + "     float r = Ca.r * alpha + (1.0 - alpha) * Cb.r; \n"
                    + "     float g = Ca.g * alpha + (1.0 - alpha) * Cb.g; \n"
                    + "     float b = Ca.b * alpha + (1.0 - alpha) * Cb.b; \n"
                    + "     gl_FragColor = vec4(r, g, b, 1.0);\n"
                    + "}";
            return fragmentShaderCode;
        }
    }
}
