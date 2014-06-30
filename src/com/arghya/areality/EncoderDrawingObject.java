/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

/**
 *
 * @author sur
 */
public class EncoderDrawingObject {
    private Square mSquare;
    private float[] mMVPMatrix;
    private float[] mSTMatrix;
    private long mTimeStamp;
    private int mTextureID;
    
    public EncoderDrawingObject(Square square, float[] mVPMatrix, float[] sTMatrix, long ts, int tId) {
        mSquare = square;
        mMVPMatrix = mVPMatrix;
        mSTMatrix = sTMatrix;
        mTimeStamp = ts;
        mTextureID = tId;
    }
    
    public long getTimeStamp() {
        return mTimeStamp;
    }
    
    public void draw() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);
        mSquare.draw(mMVPMatrix, mSTMatrix);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    }
}
