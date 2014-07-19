/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 *
 * @author sur
 */
public class ToggleEditMode {
    public static final int NO_EDIT_MODE = 0;
    public static final int EDIT_MODE = 1;
    
    private int mMode;
    private final RelativeLayout mEditLayout;
    private final Button mToggleEditModeButton;
    
    private final MainActivity mActivity;
    
    public ToggleEditMode(MainActivity activity) {
        mActivity = activity;
        mEditLayout = (RelativeLayout) mActivity.findViewById(R.id.EditFrame);
        mMode = NO_EDIT_MODE;
        mToggleEditModeButton = (Button) mActivity.findViewById(R.id.ToggleEditModeButton);

        mActivity.setButtonOnClick(R.id.ToggleEditModeButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mToggleEditModeButton.setActivated(!mToggleEditModeButton.isActivated());
                        if(mToggleEditModeButton.isActivated()) {
                            mEditLayout.setVisibility(View.VISIBLE);
                            mMode = EDIT_MODE;
                        }
                        else {
                            mEditLayout.setVisibility(View.GONE);
                            mMode = NO_EDIT_MODE;
                        }
                    }
                });
    }
    
    
    
}
