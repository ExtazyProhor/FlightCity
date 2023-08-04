package com.mygdx.game.CityClasses;

import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Rectangle;

public class Building extends Rectangle {
    private boolean exist;
    private int id;
    private int level;

    public Building(float x, float y) {
        super(x, y, Main.scrY / 9, Main.scrY / 9);
        exist = false;
    }

    public void draw() {
        Main.batch.draw(Main.house, this.x, this.y, this.sizeX, this.sizeY);
    }

    public void spawn(int id) {
        exist = true;
        this.id = id;
        this.level = 0;
    }

    public void delete() {
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
        Main.batch.draw(Main.house, (Main.scrX - Main.scrY) / 2 + Main.pppY * 3, Main.pppY*52,
                Main.pppY*20, Main.pppY*20);
    }

    private static final int maxLevel = 4;

    private static final int[] cost = {

            100, /* house */
            500, /* shop */
            800, /* restaurant */
            1000, /* bank */
            1200  /* hotel */
    };
    private static final int[][] upgradeCost = {

            {50, 200, 500, 1000}, /* house */
            {200, 700, 1500, 3500}, /* shop */
            {300, 1200, 2500, 5000}, /* restaurant */
            {600, 2000, 5000, 9000}, /* bank */
            {800, 2500, 7000, 12000} /* hotel */
    };
}
