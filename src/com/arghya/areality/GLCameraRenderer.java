/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arghya.areality;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @author sur
 */
public class GLCameraRenderer implements GLSurfaceView.Renderer {
    ArrayList<Integer> mTextureList;
    MainActivity mDelegate;
    
    private static final int RECORDING_OFF = 0;
    private static final int RECORDING_ON = 1;
    private static final int RECORDING_RESUMED = 2;
    
    private Square mSquare;
    private Square mSquare2;
    
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mSTMatrix = new float[16];

    float[] mKey = {0, 0, 0, 0};
    
    private final CameraSurface mCameraSurface;
    private final VideoSurface mVideoSurface;
    
    private final TextureMovieEncoder mVideoEncoder;
    private boolean mRecordingEnabled;
    private int mRecordingStatus;
    private int mFrameCount;
    
    private File mOutputFile;

    public GLCameraRenderer(MainActivity _delegate) {
        mDelegate = _delegate;
        mTextureList = new ArrayList<Integer>();
        mCameraSurface = new CameraSurface(mDelegate);
        mVideoSurface = new VideoSurface(mDelegate);
        mVideoEncoder = mDelegate.getEncoder();
        
        mRecordingStatus = -1;
        mRecordingEnabled = false;
        mFrameCount = -1;
        
        mOutputFile = new File(Environment.getExternalStorageDirectory(), "camera-test.mp4");
    }

    public void onSurfaceCreated(GL10 unused, javax.microedition.khronos.egl.EGLConfig config) {
        
        // We're starting up or coming back.  Either way we've got a new EGLContext that will
        // need to be shared with the video encoder, so figure out if a recording is already
        // in progress.
        mRecordingEnabled = mVideoEncoder.isRecording();
        if (mRecordingEnabled) {
            mRecordingStatus = RECORDING_RESUMED;
        } else {
            mRecordingStatus = RECORDING_OFF;
        }
        
        createExternalTexture();
        createExternalTexture();
        
        String fileName = Environment.getExternalStorageDirectory() + File.separator + "Frozen.mp4";

        mVideoSurface.setMedia(fileName);
        
        Shaders chromaKeyShader = new Shaders(this, Shaders.VertexShader.texture(), Shaders.FragmentShader.textureChromaKeyYUV());
        chromaKeyShader.setTextureIndex(0);
        mSquare = new Square(chromaKeyShader);
        
        Shaders textureShader = new Shaders(this, Shaders.VertexShader.texture(), Shaders.FragmentShader.texture());
        textureShader.setTextureIndex(1);
        mSquare2 = new Square(textureShader);
        
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        mCameraSurface.start(mTextureList.get(0));
        mVideoSurface.start(mTextureList.get(1));
    }
    
    /**
     * Notifies the renderer that we want to stop or start recording.
     */
    public void changeRecordingState(boolean isRecording) {
        mRecordingEnabled = isRecording;
    }

    public float[] getKey() {
        return mKey;
    }
    
    public void setKey(Point p)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(4);
        bb.order(ByteOrder.nativeOrder());
        bb.position(0);
        GLES20.glReadPixels(p.x, p.y , 1, 1, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, bb);
        byte b[] = new byte[4];
        bb.get(b);
        mKey[0] = (float)(bb.get(0) & 0xFF)/ 255.0f;
        mKey[1] = (float)(bb.get(1) & 0xFF)/ 255.0f;
        mKey[2] = (float)(bb.get(2) & 0xFF)/ 255.0f;
    }

    synchronized public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        mCameraSurface.updateSurface(mSTMatrix);
        
        float[] mtx = new float[16];
        mVideoSurface.updateSurface(mtx);
        
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw square
        mSquare2.draw(mMVPMatrix, mtx);
        mSquare.draw(mMVPMatrix, mSTMatrix);
        
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        
        if (mRecordingEnabled) {
            switch (mRecordingStatus) {
                case RECORDING_OFF:
                    //Log.d(TAG, "START recording");
                    // start recording
                    mVideoEncoder.startRecording(new TextureMovieEncoder.EncoderConfig(
                            mOutputFile, 640, 480, 1000000, EGL14.eglGetCurrentContext()));
                    mRecordingStatus = RECORDING_ON;
                    break;
                case RECORDING_RESUMED:
                    //Log.d(TAG, "RESUME recording");
                    mVideoEncoder.updateSharedContext(EGL14.eglGetCurrentContext());
                    mRecordingStatus = RECORDING_ON;
                    break;
                case RECORDING_ON:
                    // yay
                    break;
                default:
                    throw new RuntimeException("unknown status " + mRecordingStatus);
            }
        } else {
            switch (mRecordingStatus) {
                case RECORDING_ON:
                case RECORDING_RESUMED:
                    // stop recording
                    //Log.d(TAG, "STOP recording");
                    mVideoEncoder.stopRecording();
                    mRecordingStatus = RECORDING_OFF;
                    break;
                case RECORDING_OFF:
                    // yay
                    break;
                default:
                    throw new RuntimeException("unknown status " + mRecordingStatus);
            }
        }
        // Set the video encoder's texture name.  We only need to do this once, but in the
        // current implementation it has to happen after the video encoder is started, so
        // we just do it here.
        //
        // TODO: be less lame.
        //mVideoEncoder.setTextureId(mTextureList.get(0));

        // Tell the video encoder thread that a new frame is available.
        // This will be ignored if we're not actually recording.
        
        EncoderDrawingObject object = new EncoderDrawingObject(mSquare,
                mMVPMatrix, mSTMatrix, mCameraSurface.getTimeStamp(), mTextureList.get(0));
        
        mVideoEncoder.frameAvailable(object);
        
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) {       //Prevent A Divide By Zero By
            height = 1;         //Making Height Equal One
        }
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
    
    private void createTexture2D() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);

        // Retrieve our image from resources.
        int id = mDelegate.getResources().getIdentifier("drawable/ic_launcher", null, mDelegate.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mDelegate.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + mTextureList.size());
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
        
        mTextureList.add(texture[0]);
    }

    private void createExternalTexture() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + mTextureList.size());
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        mTextureList.add(texture[0]);
    }

    public void release() {
        mCameraSurface.release();
        mVideoSurface.release();
    }
}
