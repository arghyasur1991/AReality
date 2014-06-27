package com.arghya.areality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity{
    
    private GLCameraSurfaceView glSurfaceView;
    //private ImageSurfaceView imageSurfaceView;
    //private BackgroundVideoView videoView;
    
    private int width;
    private int height;
    
    Screenshot mScreenshot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        
        //testEncoder encoder = new testEncoder();
        //encoder.testEncodeVideoToMp4();

        glSurfaceView = new GLCameraSurfaceView(this);
        
        mScreenshot = new Screenshot(this, glSurfaceView);
        
        setContentView(R.layout.main);
        
        FrameLayout layout = (FrameLayout) findViewById(R.id.mainFrame);
        
        layout.addView(glSurfaceView);
        
        RelativeLayout newLayout = (RelativeLayout) findViewById(R.id.UILayout);
        Button capture = (Button) findViewById(R.id.CaptureScreen);
        setButtonOnClick(capture,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mScreenshot.capture();
                    }
                });
        
        layout.bringChildToFront(newLayout);
    }
    
    private void setButtonOnClick(Button button, View.OnClickListener onClickListener) {
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }
    
    synchronized public void requestRender() {
        glSurfaceView.requestRender();
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    @Override
    public void onPause() {
        glSurfaceView.release();
        System.exit(0);
    }
}