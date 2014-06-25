/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.MediaController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author sur
 */
public class Screenshot {
    private Bitmap mCameraPreviewBmp;
    private Bitmap mVideoViewBmp;
    
    private final MediaMetadataRetriever mMediaMetadataRetriever;
    private final MediaController mMediaController;
    
    private final GLCameraSurfaceView mCameraView;
    private final BackgroundVideoView mVideoView;
    
    private final Bitmap mResultBmp;
    private final MainActivity mContext;
    private final Canvas mCanvas;
    
    private final int width;
    private final int height;
    
    private String mSaveLocation;
    
    public Screenshot(Context context, GLCameraSurfaceView cameraView, BackgroundVideoView videoView) {
        mContext = (MainActivity)context;
        mCameraView = cameraView;
        mVideoView = videoView;
        
        mSaveLocation = "Screens";
        
        mMediaMetadataRetriever = new MediaMetadataRetriever();
        mMediaMetadataRetriever.setDataSource(mVideoView.getPath());

        mMediaController = new MediaController(mContext);
        mVideoView.setMediaController(mMediaController);
        
        width = mContext.getWidth();
        height = mContext.getHeight();
        
        mResultBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        
        mCanvas = new Canvas(mResultBmp);
    }
    
    public void capture() {
        captureVideoFrame();
        mCameraView.capture(this);
    }
    
    private void captureVideoFrame() {
        int currentPosition = mVideoView.getCurrentPosition(); //in millisecond

        mVideoViewBmp = mMediaMetadataRetriever.getFrameAtTime(currentPosition * 1000); //unit in microsecond
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setCameraBitmap(Bitmap bmp) {
        mCameraPreviewBmp = bmp;
        writeScreenShot();
    }
    
    public void setSaveLocation(String newLocation) {
        mSaveLocation = newLocation;
    }
    
    private void writeScreenShot() {
        if (mVideoViewBmp != null) {
            Rect src = new Rect(0, 0, mVideoViewBmp.getWidth(), mVideoViewBmp.getHeight());
            Rect dest = new Rect(0, 0, width, height);
            mCanvas.drawBitmap(mVideoViewBmp, src, dest, null);
        }

        mCanvas.drawBitmap(mCameraPreviewBmp, 0, 0, null);

        try {
            writeBitmapToFile(mResultBmp);
        } catch (IOException ex) {
            //Logger.getLogger(Screenshot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Bitmap loadBitmapFromView(Context context, View v) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }

    private void writeBitmapToFile(Bitmap bitmap) throws IOException {
        String mPath = Environment.getExternalStorageDirectory() + File.separator + 
                        mSaveLocation + File.separator +"screen_" + System.currentTimeMillis() + ".png";
        File imageFile = new File(mPath);
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            if(fout != null)
                fout.close();
        }
    }
}
