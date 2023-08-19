package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.PlaneClasses.Collision;
import com.mygdx.game.PlaneClasses.Point;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;
import com.mygdx.game.RealClasses.TextBox;

import static com.mygdx.game.Main.*;

import java.util.Date;

public class Inventory implements Screen {
    Main game;
    InventoryState state = InventoryState.PLANES;
    String path = "inventory/";
    float startX;
    float boxSize;
    float space;

    PictureBox backGround;
    PictureBox planks;
    Texture itemBox;
    Texture selectionTick;
    TextBox itemsCount;

    Button itemsButton;
    Button planesButton;
    Button backGroundsButton;
    Texture buttonFrame;

    Button chooseButton;

    // inventory:
    int[] backGrounds = new int[10];
    int[] planes = new int[7];

    // textures:
    Texture[] backGroundsTextures;
    Texture[] planesTextures;

    public Inventory(Main game) {
        this.game = game;
        startX = game.city.screenDelta / 2;
        boxSize = 23 * pppY;
        space = pppY;

        float backGroundSizeX = textureAspectRatio(new Texture(path + "backGround.png"), true) * scrY;
        backGround = new PictureBox((scrX - backGroundSizeX) / 2, 0, backGroundSizeX, scrY, path + "backGround.png");
        planks = new PictureBox(startX, 0, scrY, scrY, path + "planks.png");
        itemBox = new Texture(path + "item box.png");
        selectionTick = new Texture(path + "selection tick.png");
        itemsCount = new TextBox(0, 0, "", 0xffffffff, (int)(5 * pppY));

        chooseButton = new Button(startX + 55 * pppY, 4 * pppY, 40 * pppY, 16 * pppY, new Texture("buttons/blue button.png"),
                Languages.choose[selectedLanguage], 0xffffffff, (int)(5 * pppY));

        itemsButton = new Button((scrX + scrY) / 2 + pppY, 84 * pppY, 15 * pppY, 15 * pppY, new Texture(path + "item icon.png"));
        planesButton = new Button((scrX + scrY) / 2 + pppY, 68 * pppY, 15 * pppY, 15 * pppY, new Texture(path + "plane icon.png"));
        backGroundsButton = new Button((scrX + scrY) / 2 + pppY, 52 * pppY, 15 * pppY, 15 * pppY, new Texture(path + "backGround icon.png"));
        buttonFrame = new Texture(path + "frame.png");

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
        itemsButton.draw();
        planesButton.draw();
        backGroundsButton.draw();

        switch (state){
            case ITEMS:
                batch.draw(buttonFrame, itemsButton.getX(), itemsButton.getY(), 15 * pppY, 15 * pppY);

                break;
            case PLANES:
                batch.draw(buttonFrame, planesButton.getX(), planesButton.getY(), 15 * pppY, 15 * pppY);
                for(int i = 0; i < planesTextures.length; ++i){
                    batch.draw(itemBox, startX + 3 * pppY + (i % 4) * (boxSize + space), 98 * pppY - (float)(i / 4 + 1) * (boxSize + space), boxSize, boxSize);
                    batch.draw(planesTextures[i], startX + 4 * pppY + (i % 4) * (boxSize + space),
                            108 * pppY - (float)(i / 4 + 1) * (boxSize + space), boxSize - 2 * pppY, (boxSize - 2 * pppY) / game.planeGame.planeAspectRatio);
                    if(i == 0) continue;
                    itemsCount.positionToDown(scrY - (float)(i / 4 + 1) * (boxSize + space));
                    itemsCount.positionToRight(startX + (i % 4 + 1) * (boxSize + space));
                    if(planes[i] == 0) itemsCount.setColor(1, 0, 0);
                    else itemsCount.setColor(1, 1, 1);
                    itemsCount.changeText(divisionDigits(planes[i]));
                    itemsCount.draw();
                }
                batch.draw(selectionTick, startX + 5 * pppY + (game.planeGame.selectedPlane % 4) * (boxSize + space),
                        scrY - (float)(game.planeGame.selectedPlane / 4 + 1) * (boxSize + space), 4 * pppY, 4 * pppY);

                break;
            case BACK_GROUNDS:
                batch.draw(buttonFrame, backGroundsButton.getX(), backGroundsButton.getY(), 15 * pppY, 15 * pppY);
                for(int i = 0; i < backGroundsTextures.length; ++i){
                    batch.draw(itemBox, startX + 3 * pppY + (i % 4) * (boxSize + space), 98 * pppY - (float)(i / 4 + 1) * (boxSize + space), boxSize, boxSize);
                    batch.draw(backGroundsTextures[i], startX + 4 * pppY + (i % 4) * (boxSize + space),
                            106 * pppY - (float)(i / 4 + 1) * (boxSize + space), boxSize - 2 * pppY, boxSize / 2 - pppY);
                    if(i == 0) continue;
                    itemsCount.positionToDown(scrY - (float)(i / 4 + 1) * (boxSize + space));
                    itemsCount.positionToRight(startX + (i % 4 + 1) * (boxSize + space));
                    if(backGrounds[i] == 0) itemsCount.setColor(1, 0, 0);
                    else itemsCount.setColor(1, 1, 1);
                    itemsCount.changeText(divisionDigits(backGrounds[i]));
                    itemsCount.draw();
                }
                batch.draw(selectionTick, startX + 5 * pppY + (game.planeGame.selectedBackGround % 4) * (boxSize + space),
                        scrY - (float)(game.planeGame.selectedBackGround / 4 + 1) * (boxSize + space), 4 * pppY, 4 * pppY);

                break;
        }

        touchChecking();

        batch.end();
    }

    public void touchChecking(){
        if(!Gdx.input.justTouched()) return;

        if(game.startMenu.buttonExit.isTouched()){
            state = InventoryState.PLANES;
            game.setScreen(game.city);
        } else if(itemsButton.isTouched()){
            state = InventoryState.ITEMS;
        } else if(planesButton.isTouched()){
            state = InventoryState.PLANES;
        } else if (backGroundsButton.isTouched()) {
            state = InventoryState.BACK_GROUNDS;
        }

        float x = Gdx.input.getX();
        float y = scrY - Gdx.input.getY();
        switch (state){
            case ITEMS:
                break;
            case PLANES:
                for (int i = 0; i < planesTextures.length; ++i){
                    if(Collision.isCollision(
                            new Rectangle(startX + 3 * pppY + (i % 4) * (boxSize + space),98 * pppY - (float)(i / 4 + 1) * (boxSize + space), boxSize, boxSize),
                            new Point(x, y))){
                        if(planes[i] == 0) errorSound.play(soundVolume * soundOn);
                        else if (game.planeGame.selectedPlane != i){
                            clickSound.play(soundVolume * soundOn);
                            game.planeGame.selectedPlane = i;
                            game.planeGame.saveSelection();
                        }
                    }
                }
                break;
            case BACK_GROUNDS:
                for (int i = 0; i < backGroundsTextures.length; ++i){
                    if(Collision.isCollision(
                            new Rectangle(startX + 3 * pppY + (i % 4) * (boxSize + space),98 * pppY - (float)(i / 4 + 1) * (boxSize + space), boxSize, boxSize),
                            new Point(x, y))){
                        if(backGrounds[i] == 0) errorSound.play(soundVolume * soundOn);
                        else if (game.planeGame.selectedBackGround != i){
                            clickSound.play(soundVolume * soundOn);
                            game.planeGame.selectedBackGround = i;
                            game.planeGame.saveSelection();
                        }
                    }
                }
                break;
        }
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
        itemBox.dispose();
        selectionTick.dispose();
        itemsCount.dispose();

        itemsButton.dispose();
        planesButton.dispose();
        backGroundsButton.dispose();
        buttonFrame.dispose();

        chooseButton.dispose();

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
