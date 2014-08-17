/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.content.Context;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
    private SelectionChangedListener mSelectionChangedListener;
    
    public ColorListAdapter(Context context, int resource, ArrayList<float[]> keys) {
        super(context, resource, keys);
        mContext = context;
        mKeyList = keys;
    }

    public int getSelectedIndex() {
        return mSelectedColorIndex;
    }
    
    public void setOnSelectionChangedListener(SelectionChangedListener listener) {
        mSelectionChangedListener = listener;
    }
    
    public interface SelectionChangedListener {
        public void onSelectionChanged(int index);
    }
    
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.color_list, parent, false);
        
        final Button colorButton = (Button) layout.findViewById(R.id.colorButton);
        float[] color = mKeyList.get(position);
        int[] colorInt = new int[3];
        
        colorInt[0] = Math.round(color[0] * 255);
        colorInt[1] = Math.round(color[1] * 255);
        colorInt[2] = Math.round(color[2] * 255);
        
        final GestureDetector detector = new GestureDetector(mContext, new GestureDetector.OnGestureListener() {

            public boolean onDown(MotionEvent e) {
                return true;
            }

            public void onShowPress(MotionEvent e) {
            }

            public boolean onSingleTapUp(MotionEvent e) {
                View v1 = ((GridView) parent).getChildAt(mSelectedColorIndex);
                Button cb = (Button) v1.findViewById(R.id.colorButton);
                cb.setActivated(false);
                
                mSelectedColorIndex = position;
                colorButton.setActivated(true);
                
                if(mSelectionChangedListener != null)
                    mSelectionChangedListener.onSelectionChanged(mSelectedColorIndex);
                return true;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            public void onLongPress(MotionEvent e) {
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                mKeyList.remove(position);
                mSelectedColorIndex = mKeyList.size() - 1;
                notifyDataSetChanged();
                return true;
            }
        });
        
        colorButton.setOnTouchListener(new View.OnTouchListener() {
            
            public boolean onTouch(View v, MotionEvent me) {
                detector.onTouchEvent(me);
                return true;
            }
        });
        
        colorButton.setBackground(new ColorButtonDrawable(mContext, colorInt).getDrawable());
        if(position == mKeyList.size() - 1) {
            colorButton.setActivated(true);
            mSelectedColorIndex = position;
            if (mSelectionChangedListener != null) {
                mSelectionChangedListener.onSelectionChanged(mSelectedColorIndex);
            }
        }
        return layout;
    }
    
}
