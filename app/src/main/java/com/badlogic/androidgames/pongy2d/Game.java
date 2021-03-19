package com.badlogic.androidgames.pongy2d;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

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

    // 23-audio variables
    private SoundPool soundPool;
    private int beepId = -1;
    private int boopId = -1;
    private int bopId = -1;
    private int missId = -1;

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

    // 24- Audio Spec. Initialized
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
    } else {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    // 25- Try catch for sound
    try {
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor descriptor;

        descriptor = assetManager.openFd("beep.ogg");
        beepId = soundPool.load(descriptor, 0);

        descriptor = assetManager.openFd("boop.ogg");
        boopId = soundPool.load(descriptor,0 );

        descriptor = assetManager.openFd("bop.ogg");
        bopId = soundPool.load(descriptor, 0);

        descriptor = assetManager.openFd("miss.ogg");
        missId = soundPool.load(descriptor, 0);

    } catch (IOException e) {
        Log.e("error","Ses Dosyaları Yüklenemedi");

    }

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

    // 12 - detectCollision //26-coding the method
    private void detectCollisions() {
        // Has the bat hit the ball?
        if(RectF.intersects(mPaddle.getRect(), mBall.getmRectf())) {
            mBall.PaddleBounce(mPaddle.getRect());
            mBall.increaseVelocity();
            score++;
            soundPool.play(beepId,1,1,0,0,1);
        }

        // Has the ball hit the edge of the screen

        // Bottomg
        if(mBall.getmRectf().bottom > mScreenY) {
            mBall.reverseYvelocity();
            lives--;
            soundPool.play(missId,1,1,0,0,1);
        }

        // Top
        if(mBall.getmRectf().top < 0) {
            mBall.reverseYvelocity();
            soundPool.play(boopId,1,1,0,0,1);
        }

        // Left
        if(mBall.getmRectf().left < 0) {
            mBall.reverseXvelocity();
            soundPool.play(bopId,1,1,0,0,1);
        }

        // Right
        if(mBall.getmRectf().right > mScreenX) {
            mBall.reverseXvelocity();
            soundPool.play(bopId, 1,1,0,0,1);
        }
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
