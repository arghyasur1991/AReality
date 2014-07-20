/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
    private final Button mRecordingButton;
    
    private final Switch mToggleSwitch;
    
    public CameraModeController(MainActivity activity) {
        mActivity = activity;
        
        mGLSurfaceView = new GLCameraSurfaceView(mActivity);
        mScreenshot = new Screenshot(mGLSurfaceView);
        mMode = CAPTURE_MODE;
        
        mToggleSwitch = (Switch) mActivity.findViewById(R.id.ToggleCameraMode);
        
        mCaptureButton = (Button) mActivity.findViewById(R.id.CaptureScreenButton);
        mRecordingButton = (Button) mActivity.findViewById(R.id.ToggleRecordingButton);
        
        mCaptureButton.setBackground(new CameraControlsDrawable(mActivity).getDrawable());
        
        mToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mMode = CAPTURE_MODE;
                    mCaptureButton.setVisibility(View.VISIBLE);
                    mRecordingButton.setVisibility(View.GONE);

                } else {
                    mMode = RECORDING_MODE;
                    mCaptureButton.setVisibility(View.GONE);
                    mRecordingButton.setVisibility(View.VISIBLE);
                }
            }
        });
        
        Utilities.setButtonOnClick(R.id.CaptureScreenButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mScreenshot.capture();
                    }
                });

        Utilities.setButtonOnClick(R.id.ToggleRecordingButton,
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

        mGLSurfaceView.changeRecordingState(mRecordingEnabled);
        updateControls();
    }
    
    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {
        mRecordingButton.setActivated(mRecordingEnabled);
    }
}
