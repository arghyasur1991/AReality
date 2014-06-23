package com.arghya.areality;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.io.IOException;

public class MainActivity extends Activity implements SurfaceTexture.OnFrameAvailableListener {

    private Camera mCamera;
    private GLCameraSurfaceView glSurfaceView;
    private SurfaceTexture surface;
    GLCameraRenderer renderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLCameraSurfaceView(this);
        renderer = glSurfaceView.getRenderer();
        
        setContentView(R.layout.main);
        
        FrameLayout layout = (FrameLayout) findViewById(R.id.mainFrame);
        layout.addView(glSurfaceView);
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
    
    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
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
        System.exit(0);
    
    }
}