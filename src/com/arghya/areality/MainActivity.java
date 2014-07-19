package com.arghya.areality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    
    private GLCameraSurfaceView mGlSurfaceView;
    
    Screenshot mScreenshot;
    private boolean mRecordingEnabled;      // controls button state
    private int mWidth;
    private int mHeight;
    
    private ModeSelection mModeSelection;
    
    private final static TextureMovieEncoder sVideoEncoder = new TextureMovieEncoder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSize();
        
        mGlSurfaceView = new GLCameraSurfaceView(this);
        
        mScreenshot = new Screenshot(mGlSurfaceView);
        
        setContentView(R.layout.main);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.MainFrame);
        
        layout.addView(mGlSurfaceView, 0);
        final Button captureButton = (Button) findViewById(R.id.CaptureScreenButton);
        final Button recordButton = (Button) findViewById(R.id.ToggleRecordingButton);
        
        GridView listview = (GridView) findViewById(R.id.KeyList);
        listview.setAdapter(mGlSurfaceView.getColorListAdapter());
        
        Switch toggle = (Switch) findViewById(R.id.ToggleCameraMode);
        
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    captureButton.setVisibility(View.VISIBLE);
                    recordButton.setVisibility(View.GONE);
                    
                } else {
                    captureButton.setVisibility(View.GONE);
                    recordButton.setVisibility(View.VISIBLE);
                }
            }
        });
        
        setButtonOnClick(R.id.CaptureScreenButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //mScreenshot.capture();
                    }
                });
        
        setButtonOnClick(R.id.ToggleRecordingButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //clickToggleRecording();
                    }
                });
        
        setButtonOnClick(R.id.ToggleEditModeButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Button editToggle = (Button) findViewById(R.id.ToggleEditModeButton);
                        editToggle.setActivated(!editToggle.isActivated());
                    }
                });
        
        setButtonOnClick(R.id.ChooseMediaButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String fileName = Environment.getExternalStorageDirectory() + File.separator + "Frozen.mp4";
                        mGlSurfaceView.setMedia(fileName);
                    }
                });
        
        Button clear = (Button) findViewById(R.id.ClearColorsButton);
        DrawableLayeredButton db = new DrawableLayeredButton(this, R.drawable.clear, false);
        clear.setBackground(db.getDrawable());
        
        setButtonOnClick(R.id.ClearColorsButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mGlSurfaceView.clearKeyList();
                    }
                });
        
        mModeSelection = new ModeSelection(this);
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
        
        mGlSurfaceView.changeRecordingState(mRecordingEnabled);
        updateControls();
    }
    
    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
        Button toggleRelease = (Button) findViewById(R.id.ToggleRecordingButton);
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
        mGlSurfaceView.requestRender();
    }

    @Override
    public void onPause() {
        mGlSurfaceView.release();
        System.exit(0);
    }

    public void setSelectMode(int mode) {
        mGlSurfaceView.setSelectMode(mode);
    }
}