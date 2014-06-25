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
    private ImageSurfaceView imageSurfaceView;
    private BackgroundVideoView videoView;
    private SurfaceTexture surface;
    GLCameraRenderer renderer;
    public static boolean capture;
    public static int width;
    public static int height;
    MediaMetadataRetriever mediaMetadataRetriever ;
    MediaController myMediaController;
    

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
        imageSurfaceView = new ImageSurfaceView(this);
        videoView = new BackgroundVideoView(this);

        String fileName = Environment.getExternalStorageDirectory() + File.separator + "Frozen.mp4";
        videoView.setVideoURI(Uri.parse(fileName));
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(fileName);
        
        myMediaController = new MediaController(this);
        videoView.setMediaController(myMediaController);
        
        videoView.start();

        
        renderer = glSurfaceView.getRenderer();
        
        setContentView(R.layout.main);
        
        FrameLayout layout = (FrameLayout) findViewById(R.id.mainFrame);
        //layout.addView(imageSurfaceView);
        layout.addView(videoView);
        layout.addView(glSurfaceView);
        
        RelativeLayout newLayout = new RelativeLayout(this);
        Button b = new Button(this);
        b.setText("click");
        setButtonOnClick(b,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(!capture)
                            capture = true;
                        
                        /*try {
                            takeScreen(renderer.capture());
                            
                            //int currentPosition = videoView.getCurrentPosition(); //in millisecond
                            
                            Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(currentPosition * 1000); //unit in microsecond
                            
                            if (bmFrame != null)
                            {
                            try {
                            takeScreen(bmFrame);
                            } catch (IOException ex) {
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                    }
                });
        
        newLayout.addView(b);
        
        layout.addView(newLayout);
    }
    
    private void setButtonOnClick(Button button, View.OnClickListener onClickListener) {
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }
    
    public static Bitmap loadBitmapFromView(Context context, View v) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        v.measure(MeasureSpec.makeMeasureSpec(dm.widthPixels, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(dm.heightPixels, MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }
    
    public static void takeScreen(Bitmap bitmap) throws IOException {
        String mPath = Environment.getExternalStorageDirectory() + File.separator + "screen_" + System.currentTimeMillis() + ".png";
        File imageFile = new File(mPath);
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fout.close();
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
        videoView.stopPlayback();
        System.exit(0);
    
    }
}