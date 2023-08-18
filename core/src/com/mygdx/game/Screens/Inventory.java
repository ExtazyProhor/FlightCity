package com.mygdx.game.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;
import static com.mygdx.game.Main.*;

import java.util.Date;

public class Inventory implements Screen {
    Main game;
    InventoryState state = InventoryState.ITEMS;

    // inventory:
    int[] backGrounds = new int[10];
    int[] planes = new int[7];

    // textures:
    Texture[] backGroundsTextures;
    Texture[] planesTextures;

    public Inventory(Main game) {
        this.game = game;

        backGroundsTextures = new Texture[backGrounds.length];
        planesTextures = new Texture[planes.length];

        backGrounds[0] = inventoryPrefs.getInteger("backGround-0", 1);
        for(int i = 0; i < backGrounds.length; ++i){
            backGroundsTextures[i] = new Texture("planeGame/backGrounds/BG" + i + ".png");
            if(i == 0) continue;
            backGrounds[i] = inventoryPrefs.getInteger("backGround-" + i, 0);
        }

        planes[0] = inventoryPrefs.getInteger("plane-0", 1);
        for(int i = 0; i < planes.length; ++i){
            planesTextures[i] = new Texture("planeGame/planes/plane-" + i + ".png");
            if(i == 0) continue;
            planes[i] = inventoryPrefs.getInteger("plane-" + i, 0);
        }
    }

    @Override
    public void render(float delta) {
        batch.begin();

        batch.end();
    }

    public void saveInventory(){
        for(int i = 0; i < backGrounds.length; ++i){
            inventoryPrefs.putInteger("backGround-" + i, backGrounds[i]);
        }
        for(int i = 0; i < planes.length; ++i){
            inventoryPrefs.putInteger("plane-" + i, planes[i]);
        }
        inventoryPrefs.flush();
    }

    @Override
    public void dispose() {
        for(int i = 0; i < backGrounds.length; ++i){
            backGroundsTextures[i].dispose();
        }
        for(int i = 0; i < planes.length; ++i){
            planesTextures[i].dispose();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    enum InventoryState{
        PLANES,
        BACK_GROUNDS,
        ITEMS
    }
}
