package com.badlogic.androidgames.pongy2d;

import android.graphics.RectF;

public class Paddle {

    // 17 - ADD Variables.
    private RectF mRect;
    private float mLength;
    private float mXcoord;
    private float mPaddleSpeed;
    private int mScreenX;

    final int STOPPED = 0;
    final int LEFT = 1;
    final int RIGHT = 2;

    private int mPaddleMove = STOPPED;

    // 18- constructor
    Paddle(int sx, int sy) {
        mScreenX = sx;
        mLength = mScreenX / 8;
        float height = sy / 40;
        mXcoord = mScreenX / 2;
        float mYcoord = sy - height;

        mRect = new RectF(mXcoord,mYcoord,mXcoord+mLength,mYcoord+height);

        mPaddleSpeed = mScreenX;
    }

    // 19- getter and setter methods
    RectF getRect() {
        return mRect;
    }

    void setMovementState(int state) {
        mPaddleMove = state;
    }

    // 20 update methods
    void update(long fps) {
        if(mPaddleMove == LEFT) {
            mXcoord = mXcoord - mPaddleSpeed / fps;
        }

        if(mPaddleMove == RIGHT) {
            mXcoord = mXcoord + mPaddleSpeed / fps;
        }

        if(mXcoord < 0) {
            mXcoord = 0;
        }

        if(mXcoord + mLength > mScreenX) {
            mXcoord = mScreenX - mLength;
        }

        mRect.left = mXcoord;
        mRect.right = mXcoord + mLength;
    }
}