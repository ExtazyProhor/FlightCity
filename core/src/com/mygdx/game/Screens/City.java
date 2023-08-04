package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.CityClasses.Building;
import com.mygdx.game.CityClasses.ShopState;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;

public class City implements Screen {
    Main game;
    String path = "city/";
    float screenDelta;

    // window
    PictureBox windowBackGround;
    boolean windowIsOpened = false;
    int touchedBuilding;
    Button cancelButton;
    Texture moneyBackGround;

    Button sellButton;
    TextBox sellText;

    Button upgradeButton;
    TextBox upgradeText;
    PictureBox nonUpgradeButton;
    TextBox nonUpgradeText;

    PictureBox backGround;
    Button shopButton;
    Building[] buildings;

    public City(Main game) {
        this.game = game;
        screenDelta = Main.scrX - Main.scrY;

        // pictures
        backGround = new PictureBox((Main.scrX - 2 * Main.scrY) / 2, 0, 2 * Main.scrY, Main.scrY, path + "backGround.png");
        windowBackGround = new PictureBox(screenDelta / 2, Main.scrY / 4, Main.scrY, Main.scrY / 2, path + "window.png");
        moneyBackGround = new Texture(path + "moneyBG.png");

        // buttons
        cancelButton = new Button(screenDelta / 2 + Main.pppY * 82, Main.pppY * 57, 15 * Main.pppY, 15 * Main.pppY,
                new Texture("buttons/xButton.png"));
        shopButton = new Button(3 * Main.pppY, 64 * Main.pppY, 15 * Main.pppY, 15 * Main.pppY,
                new Texture("buttons/shop.png"));
        sellButton = new Button(screenDelta / 2 + 6 * Main.pppY, 28 * Main.pppY, 40 * Main.pppY, 16 * Main.pppY,
                new Texture("buttons/red button.png"), Languages.sell[Main.selectedLanguage] + "\n\n", 0xffffffff, (int) (4 * Main.pppY));
        upgradeButton = new Button(screenDelta / 2 + 54 * Main.pppY, 28 * Main.pppY, 40 * Main.pppY, 16 * Main.pppY,
                new Texture("buttons/blue button.png"), Languages.upgrade[Main.selectedLanguage] + "\n\n", 0xffffffff, (int) (4 * Main.pppY));
        nonUpgradeButton = new PictureBox(screenDelta / 2 + 54 * Main.pppY, 28 * Main.pppY, 40 * Main.pppY, 16 * Main.pppY, "buttons/grey button.png");

        // text
        sellText = new TextBox(screenDelta / 2 + 26 * Main.pppY, 36 * Main.pppY, "", 0xffff00ff, (int) (4 * Main.pppY));
        upgradeText = new TextBox(screenDelta / 2 + 74 * Main.pppY, 36 * Main.pppY, "", 0xffff00ff, (int) (4 * Main.pppY));
        nonUpgradeText = new TextBox(screenDelta / 2 + 74 * Main.pppY, 40 * Main.pppY, "", 0xffffffff, (int) (3 * Main.pppY));

        // buildings
        buildings = new Building[20];
        for (int i = 0; i < buildings.length; ++i) {
            buildings[i] = new Building(screenDelta / 2 + Main.scrY / 36 + (i % 5) * 5 * Main.scrY / 24,
                    29 * Main.scrY / 36 - (float) (i / 5) * Main.scrY / 4);
            buildings[i].spawn(0);///////////////////
        }
        buildings[0].upgrade();//////////////////////////////
        buildings[0].upgrade();//////////////////////////////
        buildings[0].upgrade();//////////////////////////////
        buildings[0].upgrade();//////////////////////////////
    }


    @Override
    public void render(float delta) {
        Main.batch.begin();
        backGround.draw();
        float moneyBGX = Math.min(Main.coinText.getX(), Main.sapphireText.getX()) - 3 * Main.pppY;
        Main.batch.draw(moneyBackGround, moneyBGX, 81 * Main.pppY, 57 * Main.pppY, 19 * Main.pppY);
        for (Building building : buildings) {
            if (building.isExist()) building.draw();
        }
        Main.showMoney();

        if (windowIsOpened) {
            windowBackGround.draw();
            buildings[touchedBuilding].showWindow();
            cancelButton.draw();
            sellButton.draw();
            sellText.draw();
            if(!buildings[touchedBuilding].isMaxLevel()){
                upgradeButton.draw();
                upgradeText.draw();
            }else{
                nonUpgradeButton.draw();
                nonUpgradeText.draw();
            }
            if (Gdx.input.justTouched()) {
                if (cancelButton.isTouched()) {
                    windowIsOpened = false;
                }
                else if(sellButton.isTouched()){

                }
                else if(!buildings[touchedBuilding].isMaxLevel() && upgradeButton.isTouched() && Main.money >= buildings[touchedBuilding].getUpgradeCost()){

                }
            }
        } else {
            game.startMenu.buttonExit.draw();
            shopButton.draw();
            if (Gdx.input.justTouched()) {
                if (game.startMenu.buttonExit.isTouched()) {
                    game.setScreen(game.startMenu);
                } else if (shopButton.isTouched()) {
                    game.setScreen(game.shop);
                } else {
                    for (int i = 0; i < buildings.length; ++i) {
                        if (!buildings[i].isExist()) {
                            continue;
                        }
                        if (buildings[i].isTouched(Gdx.input.getX(), Main.scrY - Gdx.input.getY())) {
                            windowIsOpened = true;
                            touchedBuilding = i;
                            sellText.changeText(Integer.toString(buildings[touchedBuilding].saleIncome()));
                            nonUpgradeText.changeText(Languages.maxLevel[Main.selectedLanguage]);
                            if(!buildings[touchedBuilding].isMaxLevel()) {
                                upgradeText.changeText(Integer.toString(buildings[touchedBuilding].getUpgradeCost()));
                                if(Main.money < buildings[touchedBuilding].getUpgradeCost()) upgradeText.setColor(1, 0, 0);
                                else upgradeText.setColor(1, 1, 0);
                            }
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
        windowBackGround.dispose();
        cancelButton.dispose();
        sellButton.dispose();
        upgradeButton.dispose();
        sellText.dispose();
        upgradeText.dispose();
        nonUpgradeButton.dispose();
        nonUpgradeText.dispose();
        moneyBackGround.dispose();

        backGround.dispose();
        shopButton.dispose();
    }
}
