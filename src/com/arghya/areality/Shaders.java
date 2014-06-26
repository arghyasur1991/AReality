/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

/**
 *
 * @author sur
 */
public class Shaders {
    public static class VertexShader {
        public static String normal() {
            final String vertexShaderCode
                = "attribute vec4 position;"
                + "attribute vec2 inputTextureCoordinate;"
                + "varying vec2 textureCoordinate;"
                + "void main()"
                + "{"
                + "gl_Position = position;"
                + "textureCoordinate = inputTextureCoordinate;"
                + "}";
            
            return vertexShaderCode;
        }
        
        public static String texture() {
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
        public static String color() {
            final String fragmentShaderCode
                = "precision mediump float;"
                + "uniform vec4 vColor;"
                + "void main() {"
                + "  gl_FragColor = vColor;"
                + "}";
            
            return fragmentShaderCode;
        }
        
        public static String texture() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n" + // highp here doesn't seem to matter
                    "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
                    + "}\n";
            return fragmentShaderCode;
        }
        
        public static String textureBW() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + // highp here doesn't seem to matter
                    "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "    vec4 Ca = texture2D(sTexture, vTextureCoord); \n"
                    + "    float lum = 0.2126 * Ca.r + 0.7152 * Ca.g + 0.0722 * Ca.b; \n"
                    + "    float alpha = 0.5; \n"
                    + "    gl_FragColor = vec4(lum, lum, lum, alpha);\n"
                    + "}\n";
            return fragmentShaderCode;
        }
        
        public static String textureChromaKey() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + // highp here doesn't seem to matter
                    "varying vec2 vTextureCoord;\n"
                    + "uniform samplerExternalOES sTexture;\n"
                    + "void main() {\n"
                    + "    vec4 Ca = texture2D(sTexture, vTextureCoord); \n"
                    + "float alpha = 1.0; \n"
                    + "float threshold = key.a; \n"
                    + "float redDiff = key.r - Ca.r; \n"
                    + "float greenDif = key.g - Ca.g; \n"
                    + "float blueDiff = key.b - Ca.b; \n"
                    + "if(abs(redDiff) < threshold && abs(greenDif) < threshold && abs(blueDiff) < threshold) \n"
                    + "alpha = 0.0; \n"
                    + "  gl_FragColor = vec4(Ca.r, Ca.g, Ca.b, alpha);\n"
                    + "}";
            return fragmentShaderCode;
        }
        
        public static String textureChromaKeyYUV() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + // highp here doesn't seem to matter
                    "varying vec2 vTextureCoord;\n"
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
    }
}
