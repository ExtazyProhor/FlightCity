package com.mygdx.game.PlaneClasses;

import static com.mygdx.game.Main.*;
import com.mygdx.game.RealClasses.Rectangle;
import com.mygdx.game.Screens.PlaneGame;

public class Coin extends Rectangle {
    public int count;
    public boolean isCollect;

    public Coin(float x, float y){
        super(x, y, 10 * pppY, 10 * pppY);
        isCollect = false;
    }

    public boolean collide(Rectangle player, float angle){
        if(isCollect) return false;
        if(Collision.isCollision(this, player, angle)){
            PlaneGame.coinCollectSound.play(soundVolume * soundOn);
            isCollect = true;
            return true;
        }
        return false;
    }

    public void draw(){
        if(!isCollect) batch.draw(PlaneGame.coinsTextures[count], x, y, sizeX, sizeY);
    }
}
