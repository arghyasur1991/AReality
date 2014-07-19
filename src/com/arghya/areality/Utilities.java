/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.view.View;
import android.widget.Button;

/**
 *
 * @author sur
 */
public class Utilities {
    public static MainActivity mActivity;
    
    public static int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = mActivity.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }
    
    public static void setButtonOnClick(int buttonId, View.OnClickListener onClickListener) {
        Button button = (Button) mActivity.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }
}
