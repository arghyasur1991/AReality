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
public class RecordControlsDrawable {
    private static final int mPauseButtonResource = R.drawable.pause;
    
    private final StateListDrawable mDrawable;
    private final Drawable mButtonDrawable;
    
    public RecordControlsDrawable(Context context, int control) {
        mDrawable = new StateListDrawable();
        
        int[] outerColors = {Color.rgb(220, 220, 220), Color.rgb(230, 230, 230)};
        GradientDrawable outerCircle = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, outerColors);
        outerCircle.setShape(GradientDrawable.OVAL);
        
        outerCircle.setStroke(Utilities.convertToPx(1), Color.rgb(150, 150, 150));
        
        int[] innerColorsNormal = {Color.rgb(170, 170, 170), Color.rgb(210, 210, 210)};
        
        InsetDrawable innerCircleNormalInset = getInnerCircle(innerColorsNormal);
        if(control == 0) {
            mButtonDrawable = context.getResources().getDrawable(mPauseButtonResource);
        }
        else {
            GradientDrawable dr = new GradientDrawable();
            dr.setShape(GradientDrawable.RECTANGLE);
            dr.setColor(Color.BLACK);
            mButtonDrawable = dr;
        }
        
        InsetDrawable controlInset = new InsetDrawable(mButtonDrawable,
                                                Utilities.convertToPx(10),
                                                Utilities.convertToPx(10),
                                                Utilities.convertToPx(10),
                                                Utilities.convertToPx(10));
        
        LayerDrawable normalStateLayer = getLayerDrawable(outerCircle, innerCircleNormalInset, controlInset);
        
        int[] innerColorsPressed = {Color.rgb(100, 100, 100), Color.rgb(150, 150, 150)};
        InsetDrawable innerCirclePressedInset = getInnerCircle(innerColorsPressed);
        LayerDrawable pressedStateLayer = getLayerDrawable(outerCircle, innerCirclePressedInset, controlInset);
        
        if(control == 0) {
            GradientDrawable dr = new GradientDrawable();
            dr.setShape(GradientDrawable.OVAL);
            dr.setColor(Color.RED);
            dr.setStroke(Utilities.convertToPx(1), Color.BLACK);
            
            int inset = 12;
            InsetDrawable playInset = new InsetDrawable(dr,
                    Utilities.convertToPx(inset),
                    Utilities.convertToPx(inset),
                    Utilities.convertToPx(inset),
                    Utilities.convertToPx(inset));

            int[] innerColorsActivatedPressed = {Color.rgb(100, 100, 100), Color.rgb(150, 150, 150)};
            InsetDrawable innerCircleActivatedPressedInset = getInnerCircle(innerColorsActivatedPressed);
            LayerDrawable activatedPressedStateLayer = getLayerDrawable(outerCircle, innerCircleActivatedPressedInset, playInset);
            mDrawable.addState(new int[]{android.R.attr.state_activated, android.R.attr.state_pressed},
                    activatedPressedStateLayer);

            LayerDrawable activatedStateLayer = getLayerDrawable(outerCircle, innerCircleNormalInset, playInset);
            mDrawable.addState(new int[]{android.R.attr.state_activated},
                    activatedStateLayer);
        }
        
        
        mDrawable.addState(new int[]{android.R.attr.state_pressed},
                pressedStateLayer);
        
        mDrawable.addState(new int[]{},
                normalStateLayer);
    }
    
    public Drawable getDrawable() {
        return mDrawable;
    }
    
    private LayerDrawable getTriangle() {
        return null;
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
