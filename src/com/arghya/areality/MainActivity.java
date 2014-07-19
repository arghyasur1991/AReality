package com.arghya.areality;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
    private MediaChooser mMediaChooser;
    private TextureMovieEncoder sVideoEncoder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.MainFrame);
        
        Utilities.mActivity = this;
        
        sVideoEncoder = new TextureMovieEncoder();
        
        mCameraModeController = new CameraModeController(this);
        mGLSurfaceView = mCameraModeController.getGLView();
        layout.addView(mGLSurfaceView, 0);
        
        mToggleEditMode = new ToggleEditMode(this);
        mModeSelection = new ModeSelection(this);
        mMediaChooser = new MediaChooser(this);
        
        GridView listview = (GridView) findViewById(R.id.KeyList);
        listview.setAdapter(mGLSurfaceView.getColorListAdapter());
        
        Button clear = (Button) findViewById(R.id.ClearColorsButton);
        DrawableLayeredButton db = new DrawableLayeredButton(this, R.drawable.clear, false);
        clear.setBackground(db.getDrawable());
        
        Utilities.setButtonOnClick(R.id.ClearColorsButton,
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
    
    synchronized public void requestRender() {
        mGLSurfaceView.requestRender();
    }

    @Override
    public void onDestroy() {
        mGLSurfaceView.release();
        super.onDestroy();
    }

    public void setSelectMode(int mode) {
        mGLSurfaceView.setSelectMode(mode);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MediaChooser.CHOOSE_MEDIA_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String filePath = data.toUri(0);
                Uri uri = Uri.parse(filePath);
                
                String path = MediaChooser.getPath(this, uri);
                mGLSurfaceView.setMedia(path);
            }
        }
    }
}