package com.mygdx.game.SubwayClasses;

import static com.mygdx.game.Main.pppX;
import static com.mygdx.game.Main.pppY;
import static com.mygdx.game.Main.scrX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.RealClasses.PictureBox;

import java.util.ArrayList;

public class Player extends PictureBox {
    public boolean exist;
    int pos;
    int reload;

    public Player(float x, int pos, float sizeX, float sizeY, String path) {
        super(x, pos * pppY * 20 + pppY * 7, sizeX, sizeY, path);
        this.pos = pos;
        exist = true;
    }

    public void update(ArrayList<Trash> trashes) {
        for (Trash trash : trashes) {
            if (new Rectangle(trash.getX(), trash.getY(), trash.getSizeX(), trash.getSizeY()).overlaps(new Rectangle(x, y, sizeX, sizeY))) {
                exist = false;
            }
        }
        if(Gdx.input.getX() < scrX / 2 && pos >0 && Gdx.input.justTouched()){
            pos -= 1;
            setCoordinates(getX(), pos * pppY * 25 + pppY * 7);
            reload = 0;
        } else if (Gdx.input.getX() > scrX / 2 && pos <3 && Gdx.input.justTouched()) {
            pos += 1;
            setCoordinates(getX(), pos * pppY * 25 + pppY * 7);
            reload = 0;
        }
        draw();
    }

    public boolean isExist() {
        return exist;
    }
}
