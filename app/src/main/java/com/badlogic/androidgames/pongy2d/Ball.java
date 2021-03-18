package com.badlogic.androidgames.pongy2d;

import android.graphics.RectF;

public class Ball {

    //21- add variables
    private RectF mRectf;
    private float mXvelocity;
    private float mYvelocity;
    private float ballWidth;
    private float ballHeight;

    //22-constructoe
    Ball (int screenX) {
        ballWidth = screenX / 100;
        ballHeight = screenX / 100;

        mRectf = new RectF();
    }

    RectF getmRectf() {
        return mRectf;
    }

    void update(long fps) {
        mRectf.left = mRectf.left + (mXvelocity / fps);
        mRectf.top = mRectf.top + (mYvelocity / fps);
        mRectf.right = mRectf.left + ballWidth;
        mRectf.bottom = mRectf.top + ballHeight;
    }

    //23 - methods
    void reverseYvelocity() {
        mYvelocity = -mYvelocity;
    }

    void reverseXvelocity() {
        mXvelocity = -mXvelocity;
    }

    void reset(int x, int y) {
        mRectf.left = x / 2;
        mRectf.top = 0;
        mRectf.right = x/2 + ballWidth;
        mRectf.bottom = ballHeight;

        mXvelocity = (y/3);
        mYvelocity = -(y/3);
    }

    void increaseVelocity() {
        mXvelocity = mXvelocity * 1.2f;
        mYvelocity = mYvelocity * 1.2f;
    }

    void PaddleBounce(RectF paddlePosition) {
        float PaddleCenter = paddlePosition.left + (paddlePosition.width()/2);
        float ballCenter = mRectf.left + (ballWidth / 2);
        float intersect = (PaddleCenter - ballCenter);

        if(intersect < 0) {
            mXvelocity = Math.abs(mXvelocity);
        } else {
            mXvelocity = -Math.abs(mXvelocity);
        }
        reverseYvelocity();
    }
}
