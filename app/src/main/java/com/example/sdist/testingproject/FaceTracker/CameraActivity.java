package com.example.sdist.testingproject.FaceTracker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sdist.testingproject.FaceTracker.camera.CameraSourcePreview;
import com.example.sdist.testingproject.FaceTracker.camera.GraphicOverlay;
import com.example.sdist.testingproject.FaceTracker.facedetection.FaceGraphic;
import com.example.sdist.testingproject.R;
import com.example.sdist.testingproject.Set_Configurations;
import com.example.sdist.testingproject.Set_WebServices;
import com.example.sdist.testingproject.friendlist;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "FaceTracker";
    private CameraSource mCameraSource = null;
    public static int cameraFacing;
    private int[] filters = new int[] { R.drawable.dog, R.drawable.cat };
    private int i = 0;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private GraphicOverlay mOverlay;
    private FaceGraphic mFaceGraphic;

    Bitmap temp;
    Bitmap picture;
    Bitmap filter;
    ImageButton switchCamera;
    ImageButton takePicture;
    LinearLayout cameraLayout;
    ImageView preview;

    ImageButton btnBack;
    ImageButton btnBack2;
    ImageButton btnSend;
    ImageButton btnSave;

    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    /**
     * Initializes the UI and initiates the creation of a face detector.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraFacing = CameraSource.CAMERA_FACING_BACK;
        switchCamera = (ImageButton) findViewById(R.id.btnSwitchCam);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        filter = BitmapFactory.decodeResource(getResources(), filters[0]);
        takePicture = (ImageButton) findViewById(R.id.btnCapture);
        cameraLayout = (LinearLayout) findViewById(R.id.cameraLayout);
        preview = (ImageView) findViewById(R.id.imgViewPreview);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack2 = (ImageButton) findViewById(R.id.btnBack2);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnSave = (ImageButton) findViewById(R.id.btnSave);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraSource.takePicture(null, new CameraSource.PictureCallback(){
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        mCameraSource.stop();
                        Resources r = getResources();
                        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 512, r.getDisplayMetrics());
                        cameraLayout.getLayoutParams().height = 0;
                        preview.getLayoutParams().height = (int) px;

                        picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Matrix matrix = new Matrix();

                        //ERICK
                        if (cameraFacing == CameraSource.CAMERA_FACING_FRONT) {
                            matrix.postRotate(-90f);
                            matrix.preScale(1, -1);
                        }

                        else if (cameraFacing == CameraSource.CAMERA_FACING_BACK) {
                            matrix.postRotate(90f);
                        }

                        //RJ
//                        if (cameraFacing == CameraSource.CAMERA_FACING_FRONT) {
//                            matrix.preScale(-1, 1);
//                        }

                        picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), matrix, false);
                        picture = picture.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(picture);

                        try {
                            if (cameraFacing == CameraSource.CAMERA_FACING_FRONT) {
                                filter = Bitmap.createScaledBitmap(filter, (int) (mFaceGraphic.width * 2.5), (int) (mFaceGraphic.height * 2.5), false);
                                canvas.drawBitmap(filter, mFaceGraphic.posX * 3, mFaceGraphic.posY  * 3, new Paint()); //ERICK
                                //canvas.drawBitmap(filter, (int) (mFaceGraphic.posX * 2), (int) (mFaceGraphic.posY  * 2), new Paint()); //RJ
                            }
                        }

                        catch (Exception ex) { }

                        temp = picture.copy(Bitmap.Config.ARGB_8888, true);
                        preview.setImageBitmap(picture);


                    }
                });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Send().execute();
            }
        });

        mPreview.setOnTouchListener(new OnSwipeTouchListener(CameraActivity.this) {

            public void onSwipeRight() {
                if (i == 0) {
                    filter = BitmapFactory.decodeResource(getResources(), filters[1]);
                    i = 1;
                }

                else if (i == 1) {
                    filter = BitmapFactory.decodeResource(getResources(), filters[0]);
                    i--;
                }
                mCameraSource.release();
                createCameraSource(cameraFacing);
                startCameraSource();
            }
            public void onSwipeLeft() {
                if (i <= 0 && i != filters.length) {
                    i++;
                }

                else if (i == filters.length) {
                    i = 0;
                }

                filter = BitmapFactory.decodeResource(getResources(), filters[i]);
                mCameraSource.release();
                createCameraSource(cameraFacing);
                startCameraSource();
            }
        });

        preview.setOnTouchListener(new OnSwipeTouchListener(CameraActivity.this) {

            Paint paint = new Paint();
            String txt;
            public void onSwipeRight() {
                if (preview.getHeight() > 0){
                    txt = "TEMP";
                    Canvas canvas = new Canvas(picture);
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(200f);
                    canvas.drawBitmap(temp, 0, 0, new Paint());
                    canvas.drawText(txt, (picture.getWidth()-paint.getTextSize()*Math.abs(txt.length()/1.8f))/2, picture.getHeight()/1.5f, paint);
                    preview.setImageBitmap(picture);
                }
            }
            public void onSwipeLeft() {
                if (preview.getHeight() > 0){
                    txt = "LOCATION";
                    Canvas canvas = new Canvas(picture);
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(200f);
                    canvas.drawBitmap(temp, 0, 0, new Paint());
                    canvas.drawText(txt, (picture.getWidth()-paint.getTextSize()*Math.abs(txt.length()/1.8f))/2, picture.getHeight()/1.5f, paint);
                    preview.setImageBitmap(picture);
                }
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraSource.release();
                if (cameraFacing == CameraSource.CAMERA_FACING_BACK) {
                    switchCamera.setImageResource(R.mipmap.switch_to_front_cam);
                    cameraFacing = CameraSource.CAMERA_FACING_FRONT;
                    createCameraSource(cameraFacing);
                }

                else if (cameraFacing == CameraSource.CAMERA_FACING_FRONT) {
                    switchCamera.setImageResource(R.mipmap.switch_to_rear_cam);
                    cameraFacing = CameraSource.CAMERA_FACING_BACK;
                    createCameraSource(cameraFacing);
                }

                startCameraSource();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = getOutputMediaFile();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    picture.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.close();
                    Toast.makeText(CameraActivity.this, "Saved!", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(cameraFacing);
        } else {
            requestCameraPermission();
        }
    }

    @Override
    public void onBackPressed() {
        if(cameraLayout.getHeight() == 0) {
            cameraLayout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            preview.setImageDrawable(null);
            startCameraSource();
        }

        else {
            finish();
        }
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Images");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private void createCameraSource(int cameraView) {
        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(cameraView)
                .setRequestedFps(30.0f)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Log.d("RESUME", "On Resume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("STOP", "On Stop");
        mPreview.stop();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "On Destroy");
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource(cameraFacing);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {
        //private GraphicOverlay mOverlay;
        //private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
            mFaceGraphic.setFilter(filter);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.clear();
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mFaceGraphic.width = 0;
            mFaceGraphic.height = 0;
            mFaceGraphic.posX = 0;
            mFaceGraphic.posY = 0;
            mOverlay.clear();
            mOverlay.remove(mFaceGraphic);
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }

    public class Send extends AsyncTask<Void, Void, Void>
    {
        @Override
        public Void doInBackground(Void... params)
        {
            try{

                String stringToPass = "{\"file\" : \"%s\", \"userId\" : { \"userId\" : %s}, \"timestamp\" : \"2009-09-17T00:00:00+08:00\", \"recipient\" : 2}";

                Set_WebServices.postJsonObject(Set_Configurations.User_Friends + Set_Configurations.userId, String.format(stringToPass, picture.toString(), "" + 1));

            }catch(Exception e)
            {
                Toast.makeText(CameraActivity.this, "Testing", Toast.LENGTH_SHORT).show();
            }
        }


    }
}