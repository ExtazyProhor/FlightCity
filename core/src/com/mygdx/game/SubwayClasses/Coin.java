package com.mygdx.game.SubwayClasses;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;

public class Coin extends Rectangle {
    float speed;
    boolean exist;
    public Coin(float x, float y, float sizeX, float sizeY, float speed){
        super(x, y, sizeX, sizeY);
        this.speed = speed;
        exist = true;
    }

    public void update(float ms){
        speed = ms;
        x -= speed;
        Main.coinPicture.draw(x, y, sizeX, sizeY);
        if(x + sizeX < 0)
        {
            exist = false;
        }
    }

    public boolean isExist(){
        return exist;
    }
}
