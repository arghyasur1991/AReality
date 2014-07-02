/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import java.util.ArrayList;

/**
 *
 * @author sur
 */
public class EncoderDrawingObject {
    private final Square mSquare;
    private final ArrayList<Integer> mTextureList;
    private float[] mMVPMatrix;
    private float[] mSTMatrix;
    private long mTimeStamp;
    
    public EncoderDrawingObject(Square square, ArrayList<Integer> textureList) {
        mSquare = square;
        mTextureList = textureList;
    }
    
    synchronized void setData(float[] mVPMatrix, float[] sTMatrix, long ts) {
        mMVPMatrix = mVPMatrix.clone();
        mSTMatrix = sTMatrix.clone();
        mTimeStamp = ts;
    }
    
    public long getTimeStamp() {
        return mTimeStamp;
    }
    
    public void draw() {
        for(int i = 0; i < mTextureList.size(); i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureList.get(i));
        }
        
        mSquare.draw(mMVPMatrix, mSTMatrix);
        
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    }
}
