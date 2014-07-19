/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author sur
 */
public class ModeSelection {
    
    private final RadioGroup mRadioGroup;
    private final ArrayList<RadioButton> mRadioButtons;

    private final ArrayList<Integer> mButtonIds;
    private final ArrayList<Integer> mButtonDrawableIds;
    
    private final MainActivity mContext;
    
    public ModeSelection(MainActivity context) {
        mContext = context;
        
        mRadioGroup = (RadioGroup) context.findViewById(R.id.ChangeSelectColorMode);
        mRadioButtons = new ArrayList<RadioButton>();
        mButtonIds = new ArrayList<Integer>(Arrays.asList(R.id.noSelect, R.id.selectColor, R.id.addColor));
        
        mButtonDrawableIds = new ArrayList<Integer>(Arrays.asList(R.drawable.camera, R.drawable.select, R.drawable.plus));
        
        
        for(int i = 0; i < 3; i++) {
            RadioButton rb = (RadioButton) mContext.findViewById(mButtonIds.get(i));
            DrawableLayeredButton db = new DrawableLayeredButton(mContext, mButtonDrawableIds.get(i), true);
            rb.setBackground(db.getDrawable());
            
            mRadioButtons.add(rb);
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                int mode = mButtonIds.indexOf(id);
                mContext.setSelectMode(mode);
            }
        });
    }
    
}
