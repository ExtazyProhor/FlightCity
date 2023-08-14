package com.mygdx.game.PlaneClasses;

import java.util.Random;

public class Barrier {
    int colliderLevel;
    int barrierId;

    static Random random = new Random();
    public static int levels = 4;
    public static int id = 7;

    public Barrier(){
        setRandomValues(2);
    }

    public Barrier(int lastLevel){
        setRandomValues(lastLevel);
    }

    public void setRandomValues(int lastLevel){
        if(lastLevel == 0) this.colliderLevel = random.nextInt(levels - 1);
        else if (lastLevel == levels - 1) this.colliderLevel = random.nextInt(levels - 1) + 1;
        else this.colliderLevel = random.nextInt(levels);
        this.barrierId = random.nextInt(id);
    }

    public void setValues(int id, int level){
        this.barrierId = id;
        this.colliderLevel = level;
    }

    public int getColliderLevel() {
        return this.colliderLevel;
    }

    public int getBarrierId() {
        return barrierId;
    }
}
