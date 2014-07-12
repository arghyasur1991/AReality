package com.arghya.areality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    
    private GLCameraSurfaceView glSurfaceView;
    
    Screenshot mScreenshot;
    private boolean mRecordingEnabled;      // controls button state
    private int mWidth;
    private int mHeight;
    
    private final static TextureMovieEncoder sVideoEncoder = new TextureMovieEncoder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSize();
        
        glSurfaceView = new GLCameraSurfaceView(this);
        
        RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(
                (int)(0.8 * mWidth), (int) (0.8 * mHeight));
        //previewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        
        mScreenshot = new Screenshot(glSurfaceView);
        
        setContentView(R.layout.main);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.MainFrame);
        
        layout.addView(glSurfaceView, 0, previewParams);
        
        setButtonOnClick(R.id.CaptureScreen,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mScreenshot.capture();
                    }
                });
        
        setButtonOnClick(R.id.ToggleRecording_button,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickToggleRecording();
                    }
                });
    }
    
    private void getSize() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mWidth = size.x;
        mHeight = size.y;
    }
    
    /**
     * onClick handler for "record" button.
     */
    public void clickToggleRecording() {
        mRecordingEnabled = !mRecordingEnabled;
        
        glSurfaceView.changeRecordingState(mRecordingEnabled);
        updateControls();
    }
    
    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
        Button toggleRelease = (Button) findViewById(R.id.ToggleRecording_button);
        toggleRelease.setActivated(mRecordingEnabled); 
    }
    
    public TextureMovieEncoder getEncoder() {
        return sVideoEncoder;
    }
    
    private void setButtonOnClick(int buttonId, View.OnClickListener onClickListener) {
        Button button = (Button) findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }
    
    synchronized public void requestRender() {
        glSurfaceView.requestRender();
    }

    @Override
    public void onPause() {
        glSurfaceView.release();
        System.exit(0);
    }
}