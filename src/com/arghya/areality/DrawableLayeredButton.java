/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.drawable.*;

/**
 *
 * @author sur
 */
public class DrawableLayeredButton {
    private static final int green_button_resource = R.drawable.common_green_button;
    private final StateListDrawable mDrawable;
    private final Context mContext;
    
    public DrawableLayeredButton(Context context, int topLayer, boolean hasBorder) {
        mContext = context;
        Drawable top_layer = context.getResources().getDrawable(topLayer);
        Drawable green_button = context.getResources().getDrawable(green_button_resource);
        
        InsetDrawable top_layer_inset = new InsetDrawable(top_layer, convertToPx(8));
        InsetDrawable green_button_inset = new InsetDrawable(green_button, convertToPx(5));
        
        Drawable border = context.getResources().getDrawable(R.drawable.ring_border);
        
        Drawable[] nonActivatedlayers = new Drawable[2];
        nonActivatedlayers[0] = green_button_inset;
        nonActivatedlayers[1] = top_layer_inset;
        
        LayerDrawable nonActivatedLayer = new LayerDrawable(nonActivatedlayers);
        
        Drawable[] activatedlayers = new Drawable[3];
        activatedlayers[0] = border;
        activatedlayers[1] = green_button_inset;
        activatedlayers[2] = top_layer_inset;

        LayerDrawable activatedLayer = new LayerDrawable(activatedlayers);
        
        mDrawable = new StateListDrawable();
        if(hasBorder) {
            mDrawable.addState(new int[]{android.R.attr.state_checked},
                    activatedLayer);
        }
        
        mDrawable.addState(new int[]{},
                nonActivatedLayer);
        
    }
    
    public Drawable getDrawable() {
        return mDrawable;
    }
    
    private int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = mContext.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }
}
