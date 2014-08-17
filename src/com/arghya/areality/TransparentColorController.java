/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;

import android.graphics.Point;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 *
 * @author sur
 */
public class TransparentColorController {
    public static final int NO_SELECT_MODE = 0;
    public static final int SELECT_COLOR_MODE = 1;
    public static final int ADD_COLOR_MODE = 2;
    public static final int MAX_KEYS = 5;
    
    public static final float SMOOTHING_FACTOR = 0.05f;
    
    private final MainActivity mActivity;
    
    private final ArrayList<float[]> mColorList;
    private final ArrayList<Float> mTolerances;
    private final ColorListAdapter mListAdapter;
    
    private final float mToleranceFactor = 0.4f;
    private int mMode;
    
    public TransparentColorController(MainActivity context) {
        mColorList = new ArrayList<float[]>();
        mTolerances = new ArrayList<Float>();
        mMode = NO_SELECT_MODE;
        
        mActivity = context;
        
        mListAdapter = new ColorListAdapter(context, R.layout.main, mColorList);
        
        /*
        float[] c = {1.0f, 0.0f, 0.0f};
        addColor(c);*/
    }
    
    public int getMode() {
        return mMode;
    }
    
    public void setMode(int mode) {
        mMode = mode;
    }
    
    public ColorListAdapter getListAdapter() {
        return mListAdapter;
    }
    
    public void handleTouchAtCoordinate(Point p) {
        if(mMode == NO_SELECT_MODE)
            return;
        
        if(mMode == ADD_COLOR_MODE && mColorList.size() == MAX_KEYS)
            return;
        
        ByteBuffer bb = ByteBuffer.allocateDirect(4);
        bb.order(ByteOrder.nativeOrder());
        bb.position(0);
        GLES20.glReadPixels(p.x, p.y, 1, 1, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, bb);
        byte b[] = new byte[4];
        bb.get(b);
        float[] key = new float[3];
        
        key[0] = (float) (bb.get(0) & 0xFF) / 255.0f;
        key[1] = (float) (bb.get(1) & 0xFF) / 255.0f;
        key[2] = (float) (bb.get(2) & 0xFF) / 255.0f;
        
        if(mMode == SELECT_COLOR_MODE)
            selectColor(key);
        else if(mMode == ADD_COLOR_MODE)
            addColor(key);
    }
    
    public ArrayList<float[]> getKeys() {
        return mColorList;
    }
    
    public ArrayList<Float> getTolerances() {
        return mTolerances;
    }
    
    private boolean isClose(float[] color, int keyIndex) {
        float[] key = mColorList.get(keyIndex);
        float tolerance = mTolerances.get(keyIndex);
        
        float yDiff = 0.299f * (color[0] - key[0]) + 0.587f * (color[1] - key[1]) + 0.114f * (color[2] - key[2]);
        float uDiff = -0.1471f * (color[0] - key[0]) - 0.28886f * (color[1] - key[1]) + 0.436f * (color[2] - key[2]);
        float vDiff = 0.615f * (color[0] - key[0]) - 0.51499f * (color[1] - key[1]) - 0.10001f * (color[2] - key[2]);

        return (Math.abs(yDiff) < (tolerance * 0.5 + SMOOTHING_FACTOR) &&
                Math.abs(uDiff) < (tolerance * 0.4 + SMOOTHING_FACTOR) &&
                Math.abs(vDiff) < (tolerance * 0.4 + SMOOTHING_FACTOR));

    }
    
    private void addColor(float[] color) {
        if(validateColor(color)) {
            for(int i = 0; i < mColorList.size(); i++) {
                if(isClose(color, i))
                    return;
            }
                
            mColorList.add(color);
            mTolerances.add(mToleranceFactor);
            
            notifyDataChangedAdapter();
        }
    }
    
    private void notifyDataChangedAdapter() {
        mActivity.runOnUiThread(new Runnable() {

            public void run() {
                if (mListAdapter != null) {
                    mListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    
    private void addColor(int[] color) {
        float[] colorF = convertIntToFloat(color);
        addColor(colorF);
    }
    
    private float[] convertIntToFloat(int[] color) {
        float[] colorF = new float[3];
        if (color.length == 3) {
            colorF[0] = (float) color[0] / 255.0f;
            colorF[1] = (float) color[1] / 255.0f;
            colorF[2] = (float) color[2] / 255.0f;
        }
        return colorF;
    }
    
    private void selectColor(float[] color) {
        if (validateColor(color)) {
            mColorList.clear();
            mColorList.add(color);
            
            mTolerances.clear();
            mTolerances.add(mToleranceFactor);

            notifyDataChangedAdapter();
        }
    }
    
    public void removeColor(int index) {
        mColorList.remove(index);
        mTolerances.remove(index);

        if (mListAdapter != null) {
            mListAdapter.notifyDataSetChanged();
        }
    }
    
    public void removeAll() {
        mColorList.clear();
        mTolerances.clear();

        notifyDataChangedAdapter();
    }
    
    private boolean validateColor(float[] color) {
        if (color.length == 3) {
            for (int i = 0; i < 2; i++) {
                if (color[i] < 0 || color[i] > 1) {
                    return false;
                }
            }
            return true;
        }
        else
            return false;
    }
}
