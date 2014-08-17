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
public class ColorButtonDrawable {
    private final StateListDrawable mDrawable;
    
    public ColorButtonDrawable(Context context, int[] color) {
        GradientDrawable colorRect = new GradientDrawable();
        
        colorRect.setShape(GradientDrawable.RECTANGLE);
        colorRect.setColor(Color.rgb(color[0], color[1], color[2]));
        
        colorRect.setCornerRadius(Utilities.convertToPx(5));
        colorRect.setStroke(Utilities.convertToPx(1), Color.BLACK);

        int width = Utilities.convertToPx(30);
        int height = Utilities.convertToPx(30);
        
        colorRect.setSize(width, height);
        
        InsetDrawable colorRectInset = new InsetDrawable(colorRect, Utilities.convertToPx(2));
        
        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.RECTANGLE);
        border.setColor(Color.WHITE);

        border.setCornerRadius(Utilities.convertToPx(5));
        border.setStroke(Utilities.convertToPx(1), Color.BLACK);

        border.setSize(width, height);
        
        Drawable[] activatedlayers = new Drawable[2];
        activatedlayers[0] = border;
        activatedlayers[1] = colorRectInset;

        LayerDrawable activatedLayer = new LayerDrawable(activatedlayers);
        
        mDrawable = new StateListDrawable();
        mDrawable.addState(new int[]{android.R.attr.state_activated},
                activatedLayer);
        
        mDrawable.addState(new int[]{},
                colorRectInset);
        
    }
    
    public Drawable getDrawable() {
        return mDrawable;
    }
}
