package com.badlogic.androidgames.pongy2d;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.Nullable;

// 1- Let's extend the activity class and call oncreate. Then let's create our game, paddel and ball classes.
public class PongyActivity extends Activity {

    private Game mGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2- Let's do the basic setups
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mGame = new Game(this, size.x, size.y);
        setContentView(mGame);
    }

   // 3- Let's override resume and pauses methods
    @Override
    protected void onResume() {
        super.onResume();

        mGame.resume();
    }

    // 4- and let's move to the Game class
    @Override
    protected void onPause() {
        super.onPause();

        mGame.pause();
    }
}
