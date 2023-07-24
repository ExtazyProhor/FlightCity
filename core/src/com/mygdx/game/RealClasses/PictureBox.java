package com.mygdx.game.RealClasses;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Main;

public class PictureBox extends Rectangle {
    private final Texture picture;
    public PictureBox(float x, float y, float sizeX, float sizeY, String path){
        super(x, y, sizeX, sizeY);
        picture = new Texture(path);
    }
    public void draw(){
        Main.batch.draw(picture, x, y, sizeX, sizeY);
    }
    public void draw(float x, float y){
        Main.batch.draw(picture, x, y, sizeX, sizeY);
    }
    public void draw(float x, float y, float sizeX, float sizeY, int rX, int rY, int rSizeX, int rSizeY,
                     boolean flipX, boolean flipY){
        Main.batch.draw(picture, x, y, sizeX, sizeY, rX, rY, rSizeX, rSizeY, flipX, flipY);
    }
}
