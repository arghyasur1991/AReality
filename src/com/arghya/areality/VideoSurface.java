/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import java.io.IOException;

/**
 *
 * @author sur
 */
public class VideoSurface implements SurfaceTexture.OnFrameAvailableListener {
    private final MediaPlayer mMediaPlayer;
    private SurfaceTexture mSurface;
    private int mTexture;
    private final MainActivity mContext;
    
    public VideoSurface(Context context) {
        mMediaPlayer = new MediaPlayer();
        mContext = (MainActivity)context;
    }
    
    public void setMedia(String filePath) {
        try {
            mMediaPlayer.setDataSource(filePath);
        } catch (IOException ex) {
            //Logger.getLogger(VideoSurface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            //Logger.getLogger(VideoSurface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            //Logger.getLogger(VideoSurface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            //Logger.getLogger(VideoSurface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void start(int texture) {
        mTexture = texture;
        mSurface = new SurfaceTexture(mTexture);
        mSurface.setOnFrameAvailableListener(this);

        Surface surface = new Surface(mSurface);
        mMediaPlayer.setSurface(surface);
        surface.release();

        try {
            mMediaPlayer.prepare();
        } catch (IOException t) {
            //Log.e(TAG, "media player prepare failed");
        }
        
        mMediaPlayer.start();
    }
    
    public long getTimeStamp() {
        return mSurface.getTimestamp();
    }
    
    public void updateSurface(float[] mSTMatrix) {
        mSurface.updateTexImage();
        mSurface.getTransformMatrix(mSTMatrix);
    }
    
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mContext.requestRender();
    }
    
    public void release() {
        mMediaPlayer.pause();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mSurface.release();
    }
    
}
