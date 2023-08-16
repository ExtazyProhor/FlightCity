package com.mygdx.game.PlaneClasses;

import java.util.Random;

public class Barrier {
    int colliderLevel;
    int barrierId;

    static Random random = new Random();
    public static int levels = 4;
    public static int id = 7;

    public Barrier(){
        setRandomValues();
    }

    public Barrier(int lastId){
        setRandomValues(lastId);
    }

    public void setRandomValues(int lastId){
        this.colliderLevel = random.nextInt(levels);
        this.barrierId = random.nextInt(id - 1);
        if(this.barrierId >= lastId) this.barrierId++;
    }

    public void setRandomValues(){
        this.colliderLevel = random.nextInt(levels);
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
