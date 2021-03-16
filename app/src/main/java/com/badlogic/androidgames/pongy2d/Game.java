package com.badlogic.androidgames.pongy2d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// 5 - Let's extend surfaceview and add the necessary variables
public class Game extends SurfaceView {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    private long mFPS;
    private final int MILLIS_IN_SECOND = 1000;

    private int mScreenX;
    private int mScreenY;
    private int mFontSize;
    private int mFontMargin;

    // 6- Let's add objects of other classes
    private Paddle mPaddle;
    private Ball mBall;

    private int score = 0;
    private int lives = 3;

    // 7-Let's set up thread structure
    private Thread mGameThread = null;
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    // 8- constructor will be installed
    // 9- startnewgame will be written

}
