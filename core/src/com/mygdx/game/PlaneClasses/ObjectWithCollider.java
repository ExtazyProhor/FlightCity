package com.mygdx.game.PlaneClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.RealClasses.Rectangle;
import static com.mygdx.game.Main.*;

public class ObjectWithCollider extends Rectangle {
    public Collider[] colliders;
    public Texture texture;

    public int getTextureSizeX(){return this.texture.getWidth();}

    public int getTextureSizeY(){return this.texture.getHeight();}

    public void setSize(int x, int y){
        this.sizeX = x;
        this.sizeY = y;
    }

    public ObjectWithCollider(int x, int y, int sizeX, int sizeY, int numberOfColliders, String fileName) {
        super(x, y, sizeX, sizeY);
        this.colliders = new Collider[numberOfColliders];
        this.texture = new Texture(fileName);
    }

    public ObjectWithCollider(int x, int y, int sizeX, int sizeY, int numberOfColliders){
        super(x, y, sizeX, sizeY);
        this.colliders = new Collider[numberOfColliders];
    }

    public void draw() {
        batch.draw(texture, x, y, sizeX, sizeY);
    }
}
