/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.*;

/**
 *
 * @author sur
 */
public class ThumbDrawable {
    
    private final StateListDrawable mDrawable;
    
    public ThumbDrawable(Context context) {
        mDrawable = new StateListDrawable();
        
        int[] outerColors = {Color.rgb(220, 220, 220), Color.rgb(230, 230, 230)};
        GradientDrawable outerCircle = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, outerColors);
        outerCircle.setShape(GradientDrawable.OVAL);
        
        //outerCircle.setSize(Utilities.convertToPx(40), Utilities.convertToPx(40));
        
        outerCircle.setStroke(Utilities.convertToPx(1), Color.rgb(150, 150, 150));
        
        int[] innerColorsNormal = {Color.rgb(170, 170, 170), Color.rgb(210, 210, 210)};
        
        InsetDrawable innerCircleNormalInset = getInnerCircle(innerColorsNormal);
        Drawable camera = context.getResources().getDrawable(R.drawable.camera_black);
        
        int inset = 6;
        InsetDrawable cameraInset = new InsetDrawable(camera,
                                                Utilities.convertToPx(inset));
        
        LayerDrawable normalStateLayer = getLayerDrawable(outerCircle, innerCircleNormalInset, cameraInset);
        
        int[] innerColorsPressed = {Color.rgb(100, 100, 100), Color.rgb(150, 150, 150)};
        InsetDrawable innerCirclePressedInset = getInnerCircle(innerColorsPressed);
        LayerDrawable pressedStateLayer = getLayerDrawable(outerCircle, innerCirclePressedInset, cameraInset);
        
        Drawable record = context.getResources().getDrawable(R.drawable.record);
        InsetDrawable recordInset = new InsetDrawable(record,
                Utilities.convertToPx(inset));

        int[] innerColorsActivatedPressed = {Color.rgb(100, 100, 100), Color.rgb(150, 150, 150)};
        InsetDrawable innerCircleActivatedPressedInset = getInnerCircle(innerColorsActivatedPressed);
        LayerDrawable activatedPressedStateLayer = getLayerDrawable(outerCircle, innerCircleActivatedPressedInset, recordInset);
        mDrawable.addState(new int[]{android.R.attr.state_checked, android.R.attr.state_pressed},
                activatedPressedStateLayer);

        LayerDrawable activatedStateLayer = getLayerDrawable(outerCircle, innerCircleNormalInset, recordInset);
        mDrawable.addState(new int[]{android.R.attr.state_checked},
                activatedStateLayer);
        
        
        mDrawable.addState(new int[]{android.R.attr.state_pressed},
                pressedStateLayer);
        
        mDrawable.addState(new int[]{},
                normalStateLayer);
    }
    
    public Drawable getDrawable() {
        return mDrawable;
    }
    
    private InsetDrawable getInnerCircle(int[] colors) {
        GradientDrawable innerRect = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        innerRect.setShape(GradientDrawable.OVAL);

        innerRect.setStroke(Utilities.convertToPx(1), Color.BLACK);

        InsetDrawable innerCircleInset = new InsetDrawable(innerRect, Utilities.convertToPx(3));
        return innerCircleInset;
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
