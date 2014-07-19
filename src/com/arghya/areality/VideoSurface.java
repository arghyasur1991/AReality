/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.media.MediaPlayer;
import android.view.Surface;
import java.io.IOException;

/**
 *
 * @author sur
 */
public class VideoSurface extends MediaSurface{
    private final MediaPlayer mMediaPlayer;
    
    public VideoSurface() {
        mMediaPlayer = new MediaPlayer();
    }
    
    public void setMedia(String filePath) {
        mMediaPlayer.reset();
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
    
    @Override
    public void start(int texture) {
        super.start(texture);

        Surface surface = new Surface(mSurfaceTexture);
        mMediaPlayer.setSurface(surface);
        surface.release();

        try {
            mMediaPlayer.prepare();
        } catch (IOException t) {
            //Log.e(TAG, "media player prepare failed");
        }
        
        mMediaPlayer.start();
    }
    
    @Override
    public void release() {
        if(mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        
        super.release();
    }
    
}
