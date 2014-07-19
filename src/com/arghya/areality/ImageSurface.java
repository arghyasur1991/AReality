/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Surface;

/**
 *
 * @author sur
 */
public class ImageSurface extends MediaSurface{
    private Surface mSurface;
    private Bitmap mBitmap;
    
    public void setMedia(String filePath){
        mBitmap = BitmapFactory.decodeFile(filePath);
    }
    
    @Override
    public void start(int texture) {
        super.start(texture);
        
        int width = 1920;
        int height = 1080;
        mSurfaceTexture.setDefaultBufferSize(width, height);
        
        mSurface = new Surface(mSurfaceTexture);
        
        Canvas canvas = mSurface.lockCanvas(null);
        Rect src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        Rect dest = new Rect(0, 0, width, height);
        canvas.drawBitmap(mBitmap, src, dest, null);
        
        mSurface.unlockCanvasAndPost(canvas);
    }
    
    @Override
    public void release() {
        if(mBitmap != null)
            mBitmap.recycle();
        
        super.release();
    }
}
