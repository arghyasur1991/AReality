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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
    private TransparentColorController mTCController;
    
    private GridView mGridView;
    private SeekBar mToleranceBar;

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
        
        mGridView = (GridView) findViewById(R.id.KeyList);
        
        mToleranceBar = (SeekBar) findViewById(R.id.tolerance);
        
        mTCController = mGLSurfaceView.getTCController();
        ColorListAdapter adapter = mTCController.getListAdapter();
        adapter.setOnSelectionChangedListener(new ColorListAdapter.SelectionChangedListener() {

            public void onSelectionChanged(int index) {
                if(mToleranceBar.getVisibility() == View.GONE)
                    mToleranceBar.setVisibility(View.VISIBLE);
                mToleranceBar.setProgress((int)(mTCController.getTolerance(index) * 100));
            }
        });
        
        adapter.setOnColorRemovedListener(new ColorListAdapter.ColorRemovedListener() {

            public void onColorRemoved(int index) {
                mTCController.removeColor(index);
                if(mTCController.getKeys().isEmpty())
                    mToleranceBar.setVisibility(View.GONE);
            }
        });
        
        mToleranceBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                mTCController.setTolerance(progressChanged);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                mTCController.setTolerance(progressChanged);
            }
        });
        
        mGridView.setAdapter(adapter);
        
        Button clear = (Button) findViewById(R.id.ClearColorsButton);
        DrawableLayeredButton db = new DrawableLayeredButton(this, R.drawable.clear, false);
        clear.setBackground(db.getDrawable());
        
        Utilities.setButtonOnClick(R.id.ClearColorsButton,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mTCController.removeAll();
                        mToleranceBar.setVisibility(View.GONE);
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
    
    public void resetSelectMode() {
        mModeSelection.selectMode(TransparentColorController.NO_SELECT_MODE);
        setSelectMode(TransparentColorController.NO_SELECT_MODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MediaChooser.CHOOSE_MEDIA_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String filePath = data.toUri(0);
                int i = filePath.indexOf("image");
                int type = MediaSurfaceController.IMAGE;
                if(i == -1)
                    type = MediaSurfaceController.VIDEO;
                Uri uri = Uri.parse(filePath);
                
                String path = MediaChooser.getPath(this, uri);
                mGLSurfaceView.setMedia(path, type);
            }
        }
    }
}