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
    public static final int MAX_KEYS = 10;
    
    private final ArrayList<float[]> mColorList;
    private final ArrayList<float[]> mTolerances;
    private int mMode;
    
    public TransparentColorController() {
        mColorList = new ArrayList<float[]>();
        mTolerances = new ArrayList<float[]>();
        mMode = NO_SELECT_MODE;
    }
    
    public int getMode() {
        return mMode;
    }
    
    public void setMode(int mode) {
        mMode = mode;
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
    
    public ArrayList<float[]> getTolerances() {
        return mTolerances;
    }
    
    private void addColor(float[] color) {
        if(validateColor(color)) {
            mColorList.add(color);
            
            float[] tolerance = {0.2f, 0.15f, 0.15f};
            mTolerances.add(tolerance);
        }
    }
    
    private void addColor(int[] color) {
        float[] colorF = convertIntToFloat(color);
        if (validateColor(colorF)) {
            mColorList.add(colorF);

            float[] tolerance = {0.2f, 0.15f, 0.15f};
            mTolerances.add(tolerance);
        }
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
            float[] tolerance = {0.2f, 0.15f, 0.15f};
            mTolerances.add(tolerance);
        }
    }
    
    public void removeColor(int index) {
        mColorList.remove(index);
        mTolerances.remove(index);
    }
    
    public void removeAll() {
        mColorList.clear();
        mTolerances.clear();
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
