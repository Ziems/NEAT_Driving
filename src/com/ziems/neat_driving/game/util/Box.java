package com.ziems.neat_driving.game.util;

/**
 * Created by noahziems on 5/7/16.
 */
public class Box {

    public float x;
    public float y;
    public float width;
    public float height;

    public Box(){

    }

    public Box(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getEndX(){
        return x + width;
    }

    public float getEndY(){
        return y + height;
    }

    public float getHalfWidth(){
        return width/2;
    }

    public float getHalfHeight(){
        return height/2;
    }

    public float getCenterX(){
        return x + getHalfWidth();
    }

    public float getCenterY(){
        return y + getHalfHeight();
    }

    public boolean hitTest(Box b){
        return b.getEndX() >= x && b.getEndY() >= y && getEndX() >= b.x && getEndY() >= b.y;
    }
}
