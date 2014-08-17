/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import java.util.ArrayList;

/**
 *
 * @author sur
 */
public class ColorListAdapter extends ArrayAdapter<float[]>{
    private final Context mContext;
    private final ArrayList<float[]> mKeyList;
    private int mSelectedColorIndex = -1;
    public ColorListAdapter(Context context, int resource, ArrayList<float[]> keys) {
        super(context, resource, keys);
        mContext = context;
        mKeyList = keys;
    }

    public int getSelectedIndex() {
        return mSelectedColorIndex;
    }
    
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.color_list, parent, false);
        
        final Button colorButton = (Button) v.findViewById(R.id.colorButton);
        float[] color = mKeyList.get(position);
        int[] colorInt = new int[3];
        
        colorInt[0] = Math.round(color[0] * 255);
        colorInt[1] = Math.round(color[1] * 255);
        colorInt[2] = Math.round(color[2] * 255);
        
        colorButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mSelectedColorIndex = position;
                for(int i = 0; i < mKeyList.size(); i++) {
                    if(i != mSelectedColorIndex) {
                        View v1 = ((GridView) parent).getChildAt(i);
                        Button cb = (Button) v1.findViewById(R.id.colorButton);
                        cb.setActivated(false);
                    }
                }
                colorButton.setActivated(true);
            }
        });
        
        colorButton.setBackground(new ColorButtonDrawable(mContext, colorInt).getDrawable());
        if(position == mKeyList.size() - 1) {
            colorButton.setActivated(true);
            mSelectedColorIndex = position;
        }
        return v;
    }
    
}
