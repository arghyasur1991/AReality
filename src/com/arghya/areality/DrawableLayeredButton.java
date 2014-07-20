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
    private static final int mGreenButtonResource = R.drawable.common_green_button;
    private final StateListDrawable mDrawable;
    
    public DrawableLayeredButton(Context context, int topLayer, boolean hasBorder) {
        Drawable topLayerDrawable = context.getResources().getDrawable(topLayer);
        Drawable greenButtonDrawable = context.getResources().getDrawable(mGreenButtonResource);
        
        InsetDrawable topLayerInset = new InsetDrawable(topLayerDrawable, Utilities.convertToPx(8));
        InsetDrawable greenButtonInset = new InsetDrawable(greenButtonDrawable, Utilities.convertToPx(5));

        int width = Utilities.convertToPx(40);
        int height = Utilities.convertToPx(40);

        ShapeDrawable border = new ShapeDrawable(new OvalShape());
        border.getPaint().setColor(0xffabab25);
        border.setBounds(0, 0, width, height);
        
        Drawable[] nonActivatedlayers = new Drawable[2];
        nonActivatedlayers[0] = greenButtonInset;
        nonActivatedlayers[1] = topLayerInset;
        
        LayerDrawable nonActivatedLayer = new LayerDrawable(nonActivatedlayers);
        
        Drawable[] activatedlayers = new Drawable[3];
        activatedlayers[0] = border;
        activatedlayers[1] = greenButtonInset;
        activatedlayers[2] = topLayerInset;

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
