package com.mygdx.game.CityClasses;

import static com.mygdx.game.CityClasses.ShopInfo.*;
import static com.mygdx.game.Main.*;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.RealClasses.Rectangle;
import com.mygdx.game.Screens.City;

public class Building extends Rectangle {
    private boolean exist;
    private int id;
    private int level;
    private long time;
    public static int housesQuantity = 0;

    public Building(float x, float y) {
        super(x, y, scrY / 9, scrY / 9);
        exist = false;
    }

    public void draw() {
        batch.draw(City.houses[this.id][this.level], this.x, this.y, this.sizeX, this.sizeY);
    }

    public void spawn(int id, int level, long time) {
        housesQuantity++;
        exist = true;
        this.id = id;
        this.level = level;
        this.time = time;
    }

    public void sell() {
        housesQuantity--;
        exist = false;
        id = 0;
        level = 0;
        time = 0;
    }

    public void upgrade() {
        this.level++;
    }

    public boolean isExist() {
        return exist;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int saleIncome() {
        int income = cost[this.id];
        for (int i = 0; i < this.level; ++i) {
            income += upgradeCost[this.id][i];
        }
        return income * 3 / 4;
    }

    public int getUpgradeCost(){
        return upgradeCost[id][level];
    }

    public boolean isMaxLevel(){
        return (level == maxLevel);
    }

    public boolean isTouched(){
        return super.isInside(Gdx.input.getX(), scrY - Gdx.input.getY());
    }

    public void setTime(int cityIndex) {
        this.time = System.currentTimeMillis();
        cityPrefs.putLong("building-" + cityIndex + "-time", this.time);
        cityPrefs.flush();
    }

    public String getTime() {
        if(this.isReady()) return "0:00";
        long secondsLong = (this.time + (long)houseTimer[this.level] * 1000 - System.currentTimeMillis())/1000;
        String minutes = Long.toString(secondsLong / 60);
        String seconds = Long.toString(secondsLong % 60);
        if(seconds.length() == 1) seconds = '0' + seconds;
        return minutes + ':' + seconds;
    }

    public void getMoney(int cityIndex){
        sellSound.play(soundVolume * soundOn);
        this.setTime(cityIndex);
        money += incomeFromHouses[this.id][this.level];
        savePrefs();
        updateMoney();
    }

    public boolean isReady() {
        return System.currentTimeMillis() >= this.time + (long)houseTimer[this.level] * 1000;
    }
}
