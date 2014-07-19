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
public class MediaSurfaceController {
    public static final int IMAGE = 0;
    public static final int VIDEO = 1;
    
    private final MediaSurface[] mMediaSurfaces;
    private int mCurrentMediaType;
    
    public MediaSurfaceController() {
        mMediaSurfaces = new MediaSurface[2];
        mMediaSurfaces[0] = new ImageSurface();
        mMediaSurfaces[1] = new VideoSurface();
        
        mCurrentMediaType = IMAGE;
    }
    
    public void setMedia(String filePath, int type) {
        mMediaSurfaces[1 - type].stop();
        mMediaSurfaces[type].setMedia(filePath);
        mCurrentMediaType = type;
    }
    
    public void start(int texture) {
        mMediaSurfaces[mCurrentMediaType].start(texture);
    }

    public long getTimeStamp() {
        return mMediaSurfaces[mCurrentMediaType].getTimeStamp();
    }

    public void updateSurface() {
        mMediaSurfaces[mCurrentMediaType].updateSurface();
    }

    public void release() {
        mMediaSurfaces[0].release();
        mMediaSurfaces[1].release();
    }
}
