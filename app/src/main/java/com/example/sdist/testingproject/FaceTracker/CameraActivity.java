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
import android.graphics.Matrix;
import android.graphics.Paint;
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

import com.example.sdist.testingproject.FaceTracker.camera.CameraSourcePreview;
import com.example.sdist.testingproject.FaceTracker.camera.GraphicOverlay;
import com.example.sdist.testingproject.FaceTracker.facedetection.FaceGraphic;
import com.example.sdist.testingproject.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "FaceTracker";
    private CameraSource mCameraSource = null;
    private int cameraFacing;
    private int[] filters = new int[] { R.drawable.dog, R.drawable.cat };
    private int i = 0;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    Bitmap filter;
    ImageButton switchCamera;
    ImageButton takePicture;

    private GraphicOverlay mOverlay;
    private FaceGraphic mFaceGraphic;

    LinearLayout cameraLayout;
    ImageView preview;
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

        switchCamera = (ImageButton) findViewById(R.id.btnSwitchCam);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        filter = BitmapFactory.decodeResource(getResources(), filters[0]);
        cameraFacing = CameraSource.CAMERA_FACING_BACK;
        takePicture = (ImageButton) findViewById(R.id.btnCapture);
        cameraLayout = (LinearLayout) findViewById(R.id.cameraLayout);
        preview = (ImageView) findViewById(R.id.imgViewPreview);
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

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraSource.takePicture(null, new CameraSource.PictureCallback(){
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        Resources r = getResources();
                        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 512, r.getDisplayMetrics());
                        cameraLayout.getLayoutParams().height = 0;
                        preview.getLayoutParams().height = (int) px;

                        Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Matrix matrix = new Matrix();

                        if (cameraFacing == CameraSource.CAMERA_FACING_FRONT) {
                            matrix.postRotate(-90f);
                            matrix.preScale(1, -1);
                        }

                        else if (cameraFacing == CameraSource.CAMERA_FACING_BACK) {
                            matrix.postRotate(90f);
                        }
                        picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), matrix, false);
                        picture = picture.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(picture);

                        try {
                            filter = Bitmap.createScaledBitmap(filter, (int) (mFaceGraphic.width * 2.5), (int) (mFaceGraphic.height * 2.5), false);
                            canvas.drawBitmap(filter, mFaceGraphic.posX * 3, mFaceGraphic.posY * 3, new Paint());
                        }

                        catch (Exception ex) { }

                        ((ImageView) findViewById(R.id.imgViewPreview)).setImageBitmap(picture);
                        mCameraSource.stop();
//                        picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), matrix, false);
//                        File file = getOutputMediaFile();
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                        FileOutputStream fos = null;
//                        try {
//                            fos = new FileOutputStream(file.getAbsoluteFile());
//                            picture.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                            byte[] imgBytes = bos.toByteArray();
//                            fos.write(imgBytes);
//                            fos.close();
//                            picture.recycle();
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
//                        intent.putExtra("imageFileName", file.getPath());
//                        intent.putExtra("posX", mFaceGraphic.posX);
//                        intent.putExtra("posY", mFaceGraphic.posY);
//                        intent.putExtra("height", mFaceGraphic.height);
//                        intent.putExtra("width", mFaceGraphic.width);
//                        intent.putExtra("filterId", filters[i]);
//                        MainActivity.this.startActivity(intent);
                    }
                });
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
                + "/Temp");

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
}