package com.mygdx.game.SubwayClasses;

import com.mygdx.game.RealClasses.PictureBox;

public class Coin extends PictureBox{
    float speed;
    boolean exist;
    public Coin(float x, float y, float sizeX, float sizeY, String path, float speed){
        super(x, y, sizeX, sizeY, path);
        this.speed = speed;
        exist = true;
    }

    public void update(){
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
