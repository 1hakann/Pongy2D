package com.badlogic.androidgames.pongy2d;

import android.graphics.RectF;

public class Brick {

    private RectF mRect;

    private boolean isVisible;

    public Brick(int row, int column, int width, int height) {
        isVisible = true;
        int padding = 1;

        mRect = new RectF(column*width + padding, row*height + padding, column*width+width-padding,row*height+height-padding);
    }

    // diğer sınıflarda kullanılacak mı? cevabın evet ise get methodu ekle
    public RectF getmRect()
    {
        return this.mRect;
    }

    public void setIsvisible() {
        isVisible = false;
    }

    public boolean getVisibility() {
        return isVisible;
    }
}
