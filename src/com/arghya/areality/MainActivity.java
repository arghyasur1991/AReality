package com.arghya.areality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    
    private GLCameraSurfaceView mGLSurfaceView;
    private CameraModeController mCameraModeController;
    private ModeSelection mModeSelection;
    private ToggleEditMode mToggleEditMode;
    private TextureMovieEncoder sVideoEncoder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.MainFrame);
        
        sVideoEncoder = new TextureMovieEncoder();
        
        mCameraModeController = new CameraModeController(this);
        mGLSurfaceView = mCameraModeController.getGLView();
        layout.addView(mGLSurfaceView, 0);
        
        mToggleEditMode = new ToggleEditMode(this);
        mModeSelection = new ModeSelection(this);
        
        GridView listview = (GridView) findViewById(R.id.KeyList);
        listview.setAdapter(mGLSurfaceView.getColorListAdapter());
        
        setButtonOnClick(R.id.ChooseMediaButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String fileName = Environment.getExternalStorageDirectory() + File.separator + "Frozen.mp4";
                        mGLSurfaceView.setMedia(fileName);
                    }
                });
        
        Button clear = (Button) findViewById(R.id.ClearColorsButton);
        DrawableLayeredButton db = new DrawableLayeredButton(this, R.drawable.clear, false);
        clear.setBackground(db.getDrawable());
        
        setButtonOnClick(R.id.ClearColorsButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mGLSurfaceView.clearKeyList();
                    }
                });
    }
    
    public TextureMovieEncoder getEncoder() {
        return sVideoEncoder;
    }
    
    public void setButtonOnClick(int buttonId, View.OnClickListener onClickListener) {
        Button button = (Button) findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }
    
    synchronized public void requestRender() {
        mGLSurfaceView.requestRender();
    }

    @Override
    public void onPause() {
        mGLSurfaceView.release();
        System.exit(0);
    }

    public void setSelectMode(int mode) {
        mGLSurfaceView.setSelectMode(mode);
    }
}