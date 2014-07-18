/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;

/**
 *
 * @author sur
 */
public class ColorListAdapter extends ArrayAdapter<float[]>{
    private final Context mContext;
    private final ArrayList<float[]> mKeyList;
    public ColorListAdapter(Context context, int resource, ArrayList<float[]> keys) {
        super(context, resource, keys);
        mContext = context;
        mKeyList = keys;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.color_list, parent, false);
        
        Button colorButton = (Button) v.findViewById(R.id.colorButton);
        float[] color = mKeyList.get(position);
        int r = Math.round(color[0] * 255);
        int g = Math.round(color[1] * 255);
        int b = Math.round(color[2] * 255);
        
        GradientDrawable drawable = (GradientDrawable) colorButton.getBackground();
        drawable.setColor(Color.rgb(r, g, b));

        return v;
    }
    
}
