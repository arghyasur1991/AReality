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

public class MainActivity extends Activity {
    
    private GLCameraSurfaceView glSurfaceView;
    
    Screenshot mScreenshot;
    private boolean mRecordingEnabled;      // controls button state
    
    private final static TextureMovieEncoder sVideoEncoder = new TextureMovieEncoder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLCameraSurfaceView(this);
        
        mScreenshot = new Screenshot(glSurfaceView);
        
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
    
    /**
     * onClick handler for "record" button.
     */
    public void clickToggleRecording(@SuppressWarnings("unused") View unused) {
        mRecordingEnabled = !mRecordingEnabled;
        
        glSurfaceView.changeRecordingState(mRecordingEnabled);
        updateControls();
    }

//    /**
//     * onClick handler for "rebind" checkbox.
//     */
//    public void clickRebindCheckbox(View unused) {
//        CheckBox cb = (CheckBox) findViewById(R.id.rebindHack_checkbox);
//        TextureRender.sWorkAroundContextProblem = cb.isChecked();
//    }
    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
        Button toggleRelease = (Button) findViewById(R.id.toggleRecording_button);
        String record = mRecordingEnabled
                ? "stop recording" : "record";
        toggleRelease.setText(record);

        //CheckBox cb = (CheckBox) findViewById(R.id.rebindHack_checkbox);
        //cb.setChecked(TextureRender.sWorkAroundContextProblem);
    }
    
    public TextureMovieEncoder getEncoder() {
        return sVideoEncoder;
    }
    
    private void setButtonOnClick(Button button, View.OnClickListener onClickListener) {
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