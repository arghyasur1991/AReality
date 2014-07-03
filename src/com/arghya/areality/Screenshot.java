/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
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
    private Bitmap mGLBitmap;
    
    private final GLCameraSurfaceView mGlSurfaceView;
    
    private String mSaveLocation;
    
    public Screenshot(GLCameraSurfaceView surfaceView) {
        mGlSurfaceView = surfaceView;
        
        mSaveLocation = "Screens";
    }
    
    public void capture() {
        mGlSurfaceView.capture(this);
    }
    
    public void setCameraBitmap(Bitmap bmp) {
        mGLBitmap = bmp;
        writeBitmapToFile(mGLBitmap);
    }
    
    public void setSaveLocation(String newLocation) {
        mSaveLocation = newLocation;
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

    private void writeBitmapToFile(Bitmap bitmap){
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
            if(fout != null) {
                try {
                    fout.close();
                } catch (IOException ex) {
                    //Logger.getLogger(Screenshot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
