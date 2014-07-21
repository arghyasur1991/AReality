/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.*;

/**
 *
 * @author sur
 */
public class CameraControlsDrawable {
    private static final int mCameraButtonResource = R.drawable.camera_black;
    
    private final StateListDrawable mDrawable;
    private final Drawable mCameraDrawable;
    
    public CameraControlsDrawable(Context context) {
        mDrawable = new StateListDrawable();
        
        int[] outerColors = {Color.rgb(220, 220, 220), Color.rgb(230, 230, 230)};
        GradientDrawable outerRect = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, outerColors);
        outerRect.setShape(GradientDrawable.RECTANGLE);
        
        outerRect.setCornerRadius(Utilities.convertToPx(15));
        outerRect.setStroke(Utilities.convertToPx(1), Color.rgb(150, 150, 150));
        
        int[] innerColorsNormal = {Color.rgb(170, 170, 170), Color.rgb(210, 210, 210)};
        
        InsetDrawable innerRectNormalInset = getInnerRect(innerColorsNormal);
        
        mCameraDrawable = context.getResources().getDrawable(mCameraButtonResource);
        InsetDrawable cameraInset = new InsetDrawable(mCameraDrawable, Utilities.convertToPx(9),
                Utilities.convertToPx(35), Utilities.convertToPx(9), Utilities.convertToPx(35));
        
        LayerDrawable normalStateLayer = getLayerDrawable(outerRect, innerRectNormalInset, cameraInset);
        
        int[] innerColorsPressed = {Color.rgb(100, 100, 100), Color.rgb(150, 150, 150)};
        InsetDrawable innerRectPressedInset = getInnerRect(innerColorsPressed);
        LayerDrawable pressedStateLayer = getLayerDrawable(outerRect, innerRectPressedInset, cameraInset);
        
        mDrawable.addState(new int[]{android.R.attr.state_pressed},
                pressedStateLayer);
        
        mDrawable.addState(new int[]{},
                normalStateLayer);
    }
    
    public Drawable getDrawable() {
        //mCameraDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        return mDrawable;
    }
    
    private InsetDrawable getInnerRect(int[] colors) {
        GradientDrawable innerRect = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        innerRect.setShape(GradientDrawable.RECTANGLE);

        innerRect.setCornerRadius(Utilities.convertToPx(11));
        innerRect.setStroke(Utilities.convertToPx(1), Color.BLACK);

        InsetDrawable innerRectInset = new InsetDrawable(innerRect, Utilities.convertToPx(3));
        return innerRectInset;
    }
    
    private LayerDrawable getLayerDrawable(Drawable outerRect, Drawable innerRect, Drawable camera) {
        Drawable[] normalStateLayers = new Drawable[3];
        normalStateLayers[0] = outerRect;
        normalStateLayers[1] = innerRect;
        normalStateLayers[2] = camera;

        LayerDrawable layer = new LayerDrawable(normalStateLayers);
        
        return layer;
    }
}
