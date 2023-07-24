package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.CityClasses.Building;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;

public class City implements Screen {
    Main game;
    String path = "city/";
    float screenDelta;

    int[][] upgradeCost = {
            {100, 500, 2000, 10000},
            {150, 700, 3000, 15000},
            {300, 1500, 6000, 30000}
    };

    // window
    PictureBox windowBackGround;
    boolean windowIsOpened = false;
    int touchedBuilding;
    Button cancelButton;

    PictureBox backGround;
    Button shopButton;
    Building[] buildings;

    public City(Main game) {
        this.game = game;
        screenDelta = Main.scrX - Main.scrY;
        backGround = new PictureBox((Main.scrX - 2 * Main.scrY) / 2, 0, 2 * Main.scrY, Main.scrY, path + "backGround.png");
        windowBackGround = new PictureBox(screenDelta/2, Main.scrY/4, Main.scrY, Main.scrY/2, path + "window.png");
        cancelButton = new Button(screenDelta/2 + Main.pppY*82, Main.pppY * 57, 15 * Main.pppY, 15 * Main.pppY,
                new Texture("buttons/xButton.png"));

        shopButton = new Button(Main.scrX - 18 * Main.pppY, 82 * Main.pppY, 15 * Main.pppY, 15 * Main.pppY,
                new Texture("buttons/shop.png"));

        buildings = new Building[20];
        for (int i = 0; i < buildings.length; ++i) {
            buildings[i] = new Building(screenDelta / 2 + Main.scrY / 36 + (i % 5) * 5 * Main.scrY / 24,
                    Main.scrY / 18 + (float) (i / 5) * Main.scrY / 4);
            buildings[i].spawn(0);///////////////////
        }

    }


    @Override
    public void render(float delta) {
        Main.batch.begin();
        backGround.draw();
        for (Building building : buildings) {
            if (building.isExist()) building.draw();
        }

        if(windowIsOpened){
            windowBackGround.draw();
            Main.batch.draw(Main.house, windowBackGround.getX() + Main.pppY * 3, Main.pppY*52,
                    Main.pppY*20, Main.pppY*20);
            cancelButton.draw();
            if(Gdx.input.justTouched()){
                if(cancelButton.isTouched()){
                    windowIsOpened = false;
                }
            }
        }else{
            game.startMenu.buttonExit.draw();
            shopButton.draw();
            if (Gdx.input.justTouched()) {
                if (game.startMenu.buttonExit.isTouched()) {
                    game.setScreen(game.startMenu);
                }
                else if(shopButton.isTouched()){
                    game.setScreen(game.shop);
                }
                else{
                    for (int i = 0; i < buildings.length; ++i) {
                        if(!buildings[i].isExist()){
                            continue;
                        }
                        if(buildings[i].isInside(Gdx.input.getX(), Main.scrY - Gdx.input.getY())){
                            windowIsOpened = true;
                            touchedBuilding = i;
                            break;
                        }
                    }
                }
            }
        }

        Main.batch.end();
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

    @Override
    public void dispose() {
    }
}
