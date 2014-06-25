/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

/**
 *
 * @author sur
 */
public class BackgroundVideoView extends VideoView {

    long lastPausedTime = 0; // The time of the last pause (milliseconds)
    long totalPausedTime = 0; // The total time paused (milliseconds)
    Uri uri;
    
    public BackgroundVideoView(Context context) {
        super(context);
    }
    
    @Override
    public void start() {
        if (lastPausedTime != 0) {
            totalPausedTime += System.currentTimeMillis() - lastPausedTime;
        }
        super.start();
    }

    public long getTotalTimeMillis() {
        return totalPausedTime;
    }
    
    @Override
    public void setVideoURI(Uri uri) {
        super.setVideoURI(uri);
        this.uri = uri;
    }
    
    public String getPath() {
        return uri.getPath();
    }
}
