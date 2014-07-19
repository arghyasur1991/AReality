/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.graphics.SurfaceTexture;

/**
 *
 * @author sur
 */
public abstract class MediaSurface {
    protected SurfaceTexture mSurfaceTexture;
    protected int mTexture;
    
    public abstract void setMedia(String filePath);

    public void start(int texture) {
        mTexture = texture;
        mSurfaceTexture = new SurfaceTexture(mTexture);
    }
    
    public final void stop() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
        }
    }

    public final long getTimeStamp() {
        return mSurfaceTexture.getTimestamp();
    }

    public final void updateSurface() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
        }
    }

    public void release() {
        stop();
    }
}
