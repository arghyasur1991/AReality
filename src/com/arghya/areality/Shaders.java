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
        
        public static String projection() {
            final String vertexShaderCode =
                    // This matrix member variable provides a hook to manipulate
                    // the coordinates of the objects that use this vertex shader

                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                // The matrix must be included as a modifier of gl_Position.
                // Note that the uMVPMatrix factor *must be first* in order
                // for the matrix multiplication product to be correct.
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}";

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
                    + "precision mediump float;"
                    + "varying vec2 textureCoordinate;                            \n"
                    + "uniform samplerExternalOES s_texture;               \n"
                    + "void main() {"
                    + "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n"
                    + "}";
            return fragmentShaderCode;
        }
        
        public static String textureBW() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;"
                    + "varying vec2 textureCoordinate;                            \n"
                    + "uniform samplerExternalOES s_texture;               \n"
                    + "void main() {"
                    + "vec4 Ca = texture2D(s_texture, textureCoordinate); \n"
                    + "float lum = 0.2126 * Ca.r + 0.7152 * Ca.g + 0.0722 * Ca.b; \n"
                    + "float alpha = 0.5; \n"
                    + "  gl_FragColor = vec4(lum, lum, lum, alpha);\n"
                    + "}";
            return fragmentShaderCode;
        }
        
        public static String textureChromaKey() {
            final String fragmentShaderCode
                    = "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;"
                    + "varying vec2 textureCoordinate;                            \n"
                    + "uniform samplerExternalOES s_texture;               \n"
                    + "uniform vec4 key;               \n"
                    + "void main() {"
                    + "vec4 Ca = texture2D(s_texture, textureCoordinate); \n"
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
                    + "precision mediump float;"
                    + "varying vec2 textureCoordinate;                            \n"
                    + "uniform samplerExternalOES s_texture;               \n"
                    + "uniform vec4 key;               \n"
                    + "void main() {"
                    + "vec4 Ca = texture2D(s_texture, textureCoordinate); \n"
                    + "float yDiff = 0.299 * (Ca.r - key.r) + 0.587 * (Ca.g - key.g) + 0.114 * (Ca.b - key.b); \n"
                    + "float uDiff = -0.1471 * (Ca.r - key.r) - 0.28886 * (Ca.g - key.g) + 0.436 * (Ca.b - key.b); \n"
                    + "float vDiff = 0.615 * (Ca.r - key.r) - 0.51499 * (Ca.g - key.g) - 0.10001 * (Ca.b - key.b); \n"
                    + "float alpha = 1.0; \n"
                    + "float threshold = key.a; \n"
                    + "if(abs(yDiff) < 0.2 && abs(uDiff) < 0.15 && abs(vDiff) < 0.15) \n"
                    + "alpha = 0.0; \n"
                    + "  gl_FragColor = vec4(Ca.r, Ca.g, Ca.b, alpha);\n"
                    + "}";
            return fragmentShaderCode;
        }
    }
}
