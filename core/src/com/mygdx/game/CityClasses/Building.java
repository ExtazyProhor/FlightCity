package com.mygdx.game.CityClasses;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Rectangle;

public class Building extends Rectangle {
    boolean exist;
    int id;
    int level;
    public Building(float x, float y){
        super(x, y, Main.scrY / 9, Main.scrY / 9);
        exist = false;
    }
    public void draw(){
        Main.batch.draw(Main.house, this.x, this.y, this.sizeX, this.sizeY);
    }
    public void delete(){
        exist = false;
    }
    public void spawn(int id){
        exist = true;
        this.id = id;
        this.level = 0;
    }
    public void upgrade(){
        this.level++;
    }

    public boolean isExist() {
        return exist;
    }
}
