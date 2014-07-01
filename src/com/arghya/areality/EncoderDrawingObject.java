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
    private final ArrayList<Square> mSquareList;
    private final float[] mMVPMatrix;
    private final float[] mSTMatrix;
    private final long mTimeStamp;
    private final ArrayList<Integer> mTextureList;
    
    public EncoderDrawingObject(ArrayList<Square> squareList, 
            ArrayList<Integer> textureList, float[] mVPMatrix, float[] sTMatrix, long ts) {
        mSquareList = squareList;
        mMVPMatrix = mVPMatrix;
        mSTMatrix = sTMatrix;
        mTimeStamp = ts;
        mTextureList = textureList;
    }
    
    public long getTimeStamp() {
        return mTimeStamp;
    }
    
    public void draw() {
        for(int i = mTextureList.size() - 1; i >= 0; i--) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureList.get(i));
        }
        
        for (int i = mSquareList.size() - 1; i >= 0; i--) {
            mSquareList.get(i).draw(mMVPMatrix, mSTMatrix);
        }
        
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    }
}
