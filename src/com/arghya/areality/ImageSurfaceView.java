/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sur
 */
public class ImageSurfaceView extends SurfaceView{

    private final Bitmap bmp;
    private final Rect src;
    private final Rect dest;
    private final MainActivity mContext;
    private final SurfaceHolder holder;

    public ImageSurfaceView(Context context) {
        super(context);
        mContext = (MainActivity) context;
        holder = getHolder();
        
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        
        src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        dest = new Rect(0, 0, mContext.width, mContext.height);
        
        final ImageSurfaceView view = this;
        
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas(null);
                onDraw(c);
                holder.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bmp, src, dest , null);
        
    }
}
