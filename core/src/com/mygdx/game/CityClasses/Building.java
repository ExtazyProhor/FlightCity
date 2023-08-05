package com.mygdx.game.CityClasses;

import static com.mygdx.game.CityClasses.ShopInfo.*;
import static com.mygdx.game.Main.*;

import com.mygdx.game.RealClasses.Rectangle;
import com.mygdx.game.Screens.City;

public class Building extends Rectangle {
    private boolean exist;
    private int id;
    private int level;

    public Building(float x, float y) {
        super(x, y, scrY / 9, scrY / 9);
        exist = false;
    }

    public void draw() {
        batch.draw(City.houses[this.id][this.level], this.x, this.y, this.sizeX, this.sizeY);
    }

    public void spawn(int id) {
        exist = true;
        this.id = id;
        this.level = 0;
    }

    public void sell() {
        exist = false;
    }

    public void upgrade() {
        this.level++;
    }

    public boolean isExist() {
        return exist;
    }

    public int saleIncome() {
        int income = cost[this.id];
        for (int i = 0; i < this.level; ++i) {
            income += upgradeCost[this.id][i];
        }
        return income / 2;
    }

    public int getUpgradeCost(){
        return upgradeCost[id][level];
    }

    public boolean isMaxLevel(){
        return (level == maxLevel);
    }

    public boolean isTouched(float x, float y){
        return super.isInside(x, y);
    }

    public void showWindow(){
        batch.draw(City.houses[this.id][this.level], (scrX - scrY) / 2 + pppY * 3, pppY*52, pppY*20, pppY*20);
    }
}
