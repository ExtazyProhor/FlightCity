package com.mygdx.game.SubwayClasses;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;

public class Trash extends Rectangle {
    float speed;
    boolean exist;
    public Trash(float x, float y, float sizeX, float sizeY, float speed){
        super(x, y, sizeX, sizeY);
        this.speed = speed;
        exist = true;
    }

    public static Texture texture = new Texture("subwaygame/stone1.png");

    public void update(float ms){
        speed = ms;
        x -= speed;
        Main.batch.draw(texture, x, y ,sizeX, sizeY);
        if(x + sizeX < 0)
        {
            exist = false;
        }
    }
    public boolean isExist(){
        return exist;
    }
}
