package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;

import static com.mygdx.game.Main.*;

import java.util.Date;

public class Inventory implements Screen {
    Main game;
    InventoryState state = InventoryState.ITEMS;
    String path = "inventory/";
    PictureBox backGround;
    PictureBox planks;

    Button chooseButton;

    // inventory:
    int[] backGrounds = new int[10];
    int[] planes = new int[7];

    // textures:
    Texture[] backGroundsTextures;
    Texture[] planesTextures;

    public Inventory(Main game) {
        this.game = game;

        float backGroundSizeX = textureAspectRatio(new Texture(path + "backGround.png"), true) * scrY;
        backGround = new PictureBox((scrX - backGroundSizeX) / 2, 0, backGroundSizeX, scrY, path + "backGround.png");
        planks = new PictureBox(game.city.screenDelta / 2, 0, scrY, scrY, path + "planks.png");
        chooseButton = new Button(game.city.screenDelta / 2 + 55 * pppY, 4 * pppY, 40 * pppY, 16 * pppY, new Texture("buttons/blue button.png"),
                Languages.choose[selectedLanguage], 0xffffffff, (int)(5 * pppY));

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
        backGround.draw();
        planks.draw();
        game.startMenu.buttonExit.draw();

        if(Gdx.input.justTouched()){
            if(game.startMenu.buttonExit.isTouched()){
                game.setScreen(game.city);
            }
        }

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
        backGround.dispose();
        planks.dispose();

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
