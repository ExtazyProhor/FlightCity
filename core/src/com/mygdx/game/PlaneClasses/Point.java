package com.mygdx.game.PlaneClasses;

import com.mygdx.game.RealClasses.Rectangle;

public class Point {
    public float x;
    public float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Point(Rectangle rectangle){
        this.x = rectangle.getX() + rectangle.getSizeX() / 2;
        this.y = rectangle.getY() + rectangle.getSizeY() / 2;
    }
}