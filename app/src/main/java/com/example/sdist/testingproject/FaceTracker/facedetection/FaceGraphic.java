package com.example.sdist.testingproject.FaceTracker.facedetection;

/**
 * Created by ezeki on 08/09/2017.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.sdist.testingproject.FaceTracker.CameraActivity;
import com.example.sdist.testingproject.FaceTracker.camera.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
public class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private Bitmap filter;
    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;

    public FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    public void setId(int id) {
        mFaceId = id;
    }

    public void setFilter(Bitmap filter) {
        this.filter = filter;
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    public void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */

    public int height = 0, width = 0;
    public float posX = 0f, posY = 0f;

    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null || CameraActivity.cameraFacing == CameraSource.CAMERA_FACING_BACK) {
            return;
        }

        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);


        //ERICK
//        width = (int) ((filter.getWidth() + face.getWidth()) * 0.63f);
//        height = (int) ((filter.getHeight() + face.getHeight() + 60) * 0.63f);

        //RJ
        width = (int) ((filter.getWidth() + face.getWidth()) * 0.65f);
        height = (int) ((filter.getHeight() + face.getHeight() + 60) * 0.65f);

        //ANNE
        width = (int) ((filter.getWidth() + face.getWidth()) * 0.75f);
        height = (int) ((filter.getHeight() + face.getHeight() + 60) * 0.75f);

        posX = (x - filter.getWidth()/2);

        posY = y - (filter.getHeight()/2) + 80; //ERICK
//        posY = y - (filter.getHeight()/2) + 120; //RJ

        filter = filter.copy(Bitmap.Config.ARGB_8888, true);
        filter = Bitmap.createScaledBitmap(filter, width, height, false);
        canvas.drawBitmap(filter, posX,  posY, mIdPaint);

    }
}