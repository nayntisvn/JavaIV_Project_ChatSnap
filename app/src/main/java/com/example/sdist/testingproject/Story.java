package com.example.sdist.testingproject;

import android.graphics.Bitmap;

/**
 * Created by Sdist on 9/13/2017.
 */

public class Story {

    private Bitmap storySnap;
    private String storyTime;

    public Bitmap getStorySnap() {
        return storySnap;
    }

    public void setStorySnap(Bitmap storySnap) {
        this.storySnap = storySnap;
    }

    public String getStoryTime() {
        return storyTime;
    }

    public void setStoryTime(String storyTime) {
        this.storyTime = storyTime;
    }
}
