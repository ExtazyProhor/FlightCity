package com.mygdx.game.SubwayClasses;

import static com.mygdx.game.Main.scrX;

import com.mygdx.game.RealClasses.PictureBox;

public class Road extends PictureBox{
    float speed;
    public Road(float x, float y, float sizeX, float sizeY, String path, float speed){
        super(x, y, sizeX, sizeY, path);
        this.speed = speed;
    }

    public void update(float ms){
        speed = ms;
        x -= speed;
        draw();
        if(x + sizeX < 0)
        {
            x += scrX * 2;
        }
    }
}
