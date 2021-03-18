package com.badlogic.androidgames.pongy2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// 5 - Let's extend surfaceview and add the necessary variables
public class Game extends SurfaceView implements Runnable {

    private final boolean isDebug = true;

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

    private int score;
    private int lives;

    // 7-Let's set up thread structure
    private Thread mGameThread = null;
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    // 8- let's coding constructor
    public Game(Context context, int x, int y) {
    super(context);

    mScreenX = x;
    mScreenY = y;

    mFontSize = mScreenX / 20;
    mFontMargin = mScreenX / 75;

    mHolder = getHolder();
    mPaint = new Paint();

    mBall = new Ball(mScreenX);
    mPaddle = new Paddle(mScreenX, mScreenY);

    NewGame();
    }

    // 9 -newgame
    private void NewGame() {
        mBall.reset(mScreenX, mScreenY);

        score = 0;
        lives = 3;
    }

    // 10- 0verride the run method after implements runnable
    @Override
    public void run() {
        while (mPlaying) {
            long frameStartTime = System.currentTimeMillis();
            if(!mPaused) {
                update();
                detectCollisions();
            }
            draw();

            long thisTimeFrame = System.currentTimeMillis() - frameStartTime;

            if(thisTimeFrame > 0) {
                mFPS = MILLIS_IN_SECOND / thisTimeFrame;
            }
        }
    }

    // 11 - update method
    private void update() {
        // update the bat and  the ball
        mBall.update(mFPS);
        mPaddle.update(mFPS);
    }

    // 12 - detectCollision
    private void detectCollisions() {
        // Has the bat hit the ball?

        // Has the ball hit the edge of the screen

        // Bottom

        // Top

        // Left

        // Right
    }

    // 13 - draw method
    void draw() {
        if(mHolder.getSurface().isValid()) {
            mCanvas = mHolder.lockCanvas();

            mCanvas.drawColor(Color.argb(255,45,67,34));
            mPaint.setColor(Color.argb(255,255,255,255));

            // draw paddle, ball and brick
            mCanvas.drawRect(mBall.getmRectf(), mPaint);
            mCanvas.drawRect(mPaddle.getRect(), mPaint);


            mPaint.setTextSize(mFontSize);

            // draw the hud
            mCanvas.drawText("Score: "+score + "Lives: "+lives, mFontMargin,mFontSize,mPaint);
            if(isDebug) {
                printDebuggingText();
            }

            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
            mPaused = false;

            if(event.getX() > mScreenX / 2) {
                mPaddle.setMovementState(mPaddle.RIGHT);
            } else {
                mPaddle.setMovementState(mPaddle.LEFT);
            }
            break;

            case MotionEvent.ACTION_UP:
                mPaddle.setMovementState(mPaddle.STOPPED);
                break;
        }
        return true;
    }

    // 14-
    private void printDebuggingText(){
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS ,
                10, debugStart + debugSize, mPaint);

    }

    // 15-
    public void pause() {
        mPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error","Joining Thread");
        }
    }

    // 16-
    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }
}
