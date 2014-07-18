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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    
    private GLCameraSurfaceView mGlSurfaceView;
    private ColorListAdapter mColorListAdapter;
    
    Screenshot mScreenshot;
    private boolean mRecordingEnabled;      // controls button state
    private int mWidth;
    private int mHeight;
    
    private float mCurrentPreviewScale = 1.0f;
    
    private final static TextureMovieEncoder sVideoEncoder = new TextureMovieEncoder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSize();
        
        mGlSurfaceView = new GLCameraSurfaceView(this);
        
        RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(
                (int)(mCurrentPreviewScale * mWidth), (int) (mCurrentPreviewScale * mHeight));
        //previewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        
        mScreenshot = new Screenshot(mGlSurfaceView);
        
        setContentView(R.layout.main);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.MainFrame);
        
        layout.addView(mGlSurfaceView, 0, previewParams);
        final Button captureButton = (Button) findViewById(R.id.CaptureScreenButton);
        final Button recordButton = (Button) findViewById(R.id.ToggleRecordingButton);
        
        GridView listview = (GridView) findViewById(R.id.KeyList);
        
        mColorListAdapter = mGlSurfaceView.getColorListAdapter();
        listview.setAdapter(mColorListAdapter);
        
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
        
        setButtonOnClick(R.id.ClearColorsButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mGlSurfaceView.clearKeyList();
                    }
                });
        
        RadioGroup modeSelect = (RadioGroup) findViewById(R.id.ChangeSelectColorMode);
        
        modeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                int mode = TransparentColorController.NO_SELECT_MODE;
                if(id == R.id.selectColor)
                    mode = TransparentColorController.SELECT_COLOR_MODE;
                else if(id == R.id.addColor)
                    mode = TransparentColorController.ADD_COLOR_MODE;
                mGlSurfaceView.setSelectMode(mode);
            }
        });
    }
    
    public ColorListAdapter getColorListAdapter() {
        return mColorListAdapter;
    }
    
    private void getSize() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mWidth = size.x;
        mHeight = size.y;
    }
    
    public void togglePreviewSize() {
        if(mCurrentPreviewScale == 1.0f)
            mCurrentPreviewScale = 0.8f;
        else
            mCurrentPreviewScale = 1.0f;
        
        RelativeLayout.LayoutParams previewParams = new RelativeLayout.LayoutParams(
                (int) (mCurrentPreviewScale * mWidth), (int) (mCurrentPreviewScale * mHeight));
        
        mGlSurfaceView.setLayoutParams(previewParams);
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
}