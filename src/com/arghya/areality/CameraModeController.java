/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

/**
 *
 * @author sur
 */
public class CameraModeController {
    public static final int CAPTURE_MODE = 0;
    public static final int RECORDING_MODE = 1;
    
    private final GLCameraSurfaceView mGLSurfaceView;
    private final Screenshot mScreenshot;
    private boolean mRecordingEnabled;
    private final MainActivity mActivity;
    
    private int mMode;
    
    private final Button mCaptureButton;
    private final Button mStartRecordingButton;
    
    private final LinearLayout mRecordingButtonGroup;
    private final Button mPauseRecordingButton;
    private final Button mStopRecordingButton;
    
    private final Switch mToggleSwitch;
    
    public CameraModeController(MainActivity activity) {
        mActivity = activity;
        
        mGLSurfaceView = new GLCameraSurfaceView(mActivity);
        mScreenshot = new Screenshot(mGLSurfaceView);
        mMode = CAPTURE_MODE;
        
        mToggleSwitch = (Switch) mActivity.findViewById(R.id.ToggleCameraMode);
        mToggleSwitch.setThumbDrawable(new ThumbDrawable(mActivity).getDrawable());
        //mToggleSwitch.setTrackDrawable(new RecordGroupDrawable(mActivity).getDrawable());
        
        mCaptureButton = (Button) mActivity.findViewById(R.id.CaptureScreenButton);
        mStartRecordingButton = (Button) mActivity.findViewById(R.id.StartRecordingButton);
        
        mRecordingButtonGroup = (LinearLayout) mActivity.findViewById(R.id.RecordingButtonGroup);
        mPauseRecordingButton = (Button) mActivity.findViewById(R.id.PauseRecordingButton);
        mStopRecordingButton = (Button) mActivity.findViewById(R.id.StopRecordingButton);
        
        mCaptureButton.setBackground(new CameraControlsDrawable(mActivity, CAPTURE_MODE).getDrawable());
        mStartRecordingButton.setBackground(new CameraControlsDrawable(mActivity, RECORDING_MODE).getDrawable());
        
        mRecordingButtonGroup.setBackground(new RecordGroupDrawable(mActivity).getDrawable());
        mPauseRecordingButton.setBackground(new RecordControlsDrawable(mActivity, 0).getDrawable());
        mStopRecordingButton.setBackground(new RecordControlsDrawable(mActivity, 1).getDrawable());
        
        mToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mMode = CAPTURE_MODE;
                    if(mRecordingEnabled) {
                        clickToggleRecording();
                    }
                    mStartRecordingButton.setVisibility(View.GONE);
                    mCaptureButton.setVisibility(View.VISIBLE);

                } else {
                    mMode = RECORDING_MODE;
                    mCaptureButton.setVisibility(View.GONE);
                    mRecordingButtonGroup.setVisibility(View.GONE);
                    mStartRecordingButton.setVisibility(View.VISIBLE);
                }
            }
        });
        
        Utilities.setButtonOnClick(R.id.CaptureScreenButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //mScreenshot.capture();
                    }
                });

        Utilities.setButtonOnClick(R.id.StartRecordingButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickToggleRecording();
                    }
                });
        
        Utilities.setButtonOnClick(R.id.PauseRecordingButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mPauseRecordingButton.setActivated(!mPauseRecordingButton.isActivated());
                        //mGLSurfaceView.togglePauseRecording(mPauseRecordingButton.isActivated());
                    }
                });
        
        Utilities.setButtonOnClick(R.id.StopRecordingButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickToggleRecording();
                    }
                });
    }
    
    public int getMode() {
        return mMode;
    }
    
    public GLCameraSurfaceView getGLView() {
        return mGLSurfaceView;
    }
    
    /**
     * onClick handler for "record" button.
     */
    public void clickToggleRecording() {
        mRecordingEnabled = !mRecordingEnabled;

        //mGLSurfaceView.changeRecordingState(mRecordingEnabled);
        updateControls();
    }
    
    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
        if(mRecordingEnabled) {
            mStartRecordingButton.setVisibility(View.GONE);
            mRecordingButtonGroup.setVisibility(View.VISIBLE);
        }
        else {
            mStartRecordingButton.setVisibility(View.VISIBLE);
            mRecordingButtonGroup.setVisibility(View.GONE);
        }
    }
}
