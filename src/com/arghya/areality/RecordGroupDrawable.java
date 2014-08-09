/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;

/**
 *
 * @author sur
 */
public class RecordGroupDrawable {
    private final LayerDrawable mDrawable;

    public RecordGroupDrawable(Context context) {
        int[] outerColors = {Color.rgb(220, 220, 220), Color.rgb(230, 230, 230)};
        GradientDrawable outerRect = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, outerColors);
        outerRect.setShape(GradientDrawable.RECTANGLE);

        outerRect.setCornerRadius(Utilities.convertToPx(15));
        outerRect.setStroke(Utilities.convertToPx(1), Color.rgb(150, 150, 150));

        int[] innerColors = {Color.rgb(170, 170, 170), Color.rgb(210, 210, 210)};

        InsetDrawable innerRectInset = getInnerRect(innerColors);
        
        mDrawable = getLayerDrawable(outerRect, innerRectInset);
    }

    public Drawable getDrawable() {
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

    private LayerDrawable getLayerDrawable(Drawable outerRect, Drawable innerRect) {
        Drawable[] layers = new Drawable[2];
        layers[0] = outerRect;
        layers[1] = innerRect;

        LayerDrawable layer = new LayerDrawable(layers);

        return layer;
    }
}
