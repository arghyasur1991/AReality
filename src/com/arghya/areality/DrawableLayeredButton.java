/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.drawable.*;
import android.graphics.drawable.shapes.OvalShape;

/**
 *
 * @author sur
 */
public class DrawableLayeredButton {
    private static final int green_button_resource = R.drawable.common_green_button;
    private final StateListDrawable mDrawable;
    
    public DrawableLayeredButton(Context context, int topLayer, boolean hasBorder) {
        Drawable top_layer = context.getResources().getDrawable(topLayer);
        Drawable green_button = context.getResources().getDrawable(green_button_resource);
        
        InsetDrawable top_layer_inset = new InsetDrawable(top_layer, Utilities.convertToPx(8));
        InsetDrawable green_button_inset = new InsetDrawable(green_button, Utilities.convertToPx(5));

        int width = Utilities.convertToPx(40);
        int height = Utilities.convertToPx(40);

        ShapeDrawable border = new ShapeDrawable(new OvalShape());
        border.getPaint().setColor(0xffabab25);
        border.setBounds(0, 0, width, height);
        
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
}
