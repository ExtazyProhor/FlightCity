package com.mygdx.game.SubwayClasses;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.RealClasses.PictureBox;

public class Trash extends PictureBox{
    float speed;
    boolean exist;
    public Trash(float x, float y, float sizeX, float sizeY, String path, float speed){
        super(x, y, sizeX, sizeY, path);
        this.speed = speed;
        exist = true;
    }

    public void update(float ms){
        speed = ms;
        x -= speed;
        draw();
        if(x + sizeX < 0)
        {
            exist = false;
        }
    }
    public boolean isExist(){
        return exist;
    }
}
