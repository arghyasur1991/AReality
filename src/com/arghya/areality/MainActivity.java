package com.arghya.areality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity implements SurfaceTexture.OnFrameAvailableListener {

    private Camera mCamera;
    
    private GLCameraSurfaceView glSurfaceView;
    //private ImageSurfaceView imageSurfaceView;
    private BackgroundVideoView videoView;
    
    private SurfaceTexture surface;
    private GLCameraRenderer renderer;
    
    private int width;
    private int height;
    
    Screenshot mScreenshot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        glSurfaceView = new GLCameraSurfaceView(this);
        //imageSurfaceView = new ImageSurfaceView(this);
        videoView = new BackgroundVideoView(this);

        String fileName = Environment.getExternalStorageDirectory() + File.separator + "Frozen.mp4";
        videoView.setVideoURI(Uri.parse(fileName));
        
        mScreenshot = new Screenshot(this, glSurfaceView, videoView);
        videoView.start();

        renderer = glSurfaceView.getRenderer();
        
        setContentView(R.layout.main);
        
        FrameLayout layout = (FrameLayout) findViewById(R.id.mainFrame);
        
        //layout.addView(imageSurfaceView);
        layout.addView(videoView);
        layout.addView(glSurfaceView);
        
        RelativeLayout newLayout = (RelativeLayout) findViewById(R.id.UILayout);
        Button capture = (Button) findViewById(R.id.CaptureScreen);
        setButtonOnClick(capture,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mScreenshot.capture();
                    }
                });
        
        layout.bringChildToFront(newLayout);
    }
    
    private void setButtonOnClick(Button button, View.OnClickListener onClickListener) {
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }
    
    public void startCamera(int texture) {
        surface = new SurfaceTexture(texture);
        surface.setOnFrameAvailableListener(this);
        renderer.setSurface(surface);

        mCamera = getCameraInstance();

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            //Log.w("MainActivity", "CAM LAUNCH FAILED");
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    /**
     * A safe way to get an instance of the Camera object.
     */
    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        glSurfaceView.requestRender();
    }

    @Override
    public void onPause() {
        if(mCamera != null)
        {
            mCamera.stopPreview();
            mCamera.release();
        }
        videoView.stopPlayback();
        System.exit(0);
    
    }
}