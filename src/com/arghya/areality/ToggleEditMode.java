/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
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
    
    private final AnimationSet mToEditAnimation;
    private final AnimationSet mToNoEditAnimation;
    
    public ToggleEditMode(MainActivity activity) {
        mActivity = activity;
        mEditLayout = (RelativeLayout) mActivity.findViewById(R.id.EditFrame);
        mMode = EDIT_MODE;
        mToggleEditModeButton = (Button) mActivity.findViewById(R.id.ToggleEditModeButton);
        mToggleEditModeButton.setActivated(true);
        
        mToEditAnimation = new AnimationSet(false);
        Animation transAnimation1 = new TranslateAnimation(-Utilities.convertToPx(300), 0, 0, 0);
        Animation alphaAnimation1 = new AlphaAnimation(0, 1);

        mToEditAnimation.addAnimation(alphaAnimation1);
        mToEditAnimation.addAnimation(transAnimation1);
        mToEditAnimation.setDuration(500);
        
        mToNoEditAnimation = new AnimationSet(false);
        Animation transAnimation2 = new TranslateAnimation(0, -Utilities.convertToPx(300), 0, 0);
        Animation alphaAnimation2 = new AlphaAnimation(1, 0);

        mToNoEditAnimation.addAnimation(alphaAnimation2);
        mToNoEditAnimation.addAnimation(transAnimation2);
        mToNoEditAnimation.setDuration(500);
        
        Animation.AnimationListener animListener = new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                mToggleEditModeButton.setActivated(!mToggleEditModeButton.isActivated());
                mActivity.resetSelectMode();
            }

            public void onAnimationRepeat(Animation animation) {
            }
        };
        
        mToEditAnimation.setAnimationListener(animListener);
        mToNoEditAnimation.setAnimationListener(animListener);
                
        Utilities.setButtonOnClick(R.id.ToggleEditModeButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!mToggleEditModeButton.isActivated()) {
                            mEditLayout.setVisibility(View.VISIBLE);
                            mEditLayout.startAnimation(mToEditAnimation);
                            mMode = EDIT_MODE;
                        }
                        else {
                            mEditLayout.startAnimation(mToNoEditAnimation);
                            mEditLayout.setVisibility(View.GONE);
                            mMode = NO_EDIT_MODE;
                        }
                    }
                });
    }
    
    
    
}
