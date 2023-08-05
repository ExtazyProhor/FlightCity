package com.mygdx.game.Screens;

import static com.mygdx.game.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.CityClasses.Building;
import com.mygdx.game.CityClasses.ShopInfo;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;

import java.util.Random;

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
    public static Texture[][] houses;

    public City(Main game) {
        this.game = game;
        screenDelta = scrX - scrY;

        // pictures
        backGround = new PictureBox((scrX - 2 * scrY) / 2, 0, 2 * scrY, scrY, path + "backGround.png");
        windowBackGround = new PictureBox(screenDelta / 2, scrY / 4, scrY, scrY / 2, path + "window.png");
        moneyBackGround = new Texture(path + "moneyBG.png");

        // buttons
        cancelButton = new Button(screenDelta / 2 + pppY * 82, pppY * 57, 15 * pppY, 15 * pppY,
                new Texture("buttons/xButton.png"));
        shopButton = new Button(3 * pppY, 64 * pppY, 15 * pppY, 15 * pppY,
                new Texture("buttons/shop.png"));
        sellButton = new Button(screenDelta / 2 + 6 * pppY, 28 * pppY, 40 * pppY, 16 * pppY,
                new Texture("buttons/red button.png"), Languages.sell[selectedLanguage] + "\n\n", 0xffffffff, (int) (4 * pppY));
        upgradeButton = new Button(screenDelta / 2 + 54 * pppY, 28 * pppY, 40 * pppY, 16 * pppY,
                new Texture("buttons/blue button.png"), Languages.upgrade[selectedLanguage] + "\n\n", 0xffffffff, (int) (4 * pppY));
        nonUpgradeButton = new PictureBox(screenDelta / 2 + 54 * pppY, 28 * pppY, 40 * pppY, 16 * pppY, "buttons/grey button.png");

        // text
        sellText = new TextBox(screenDelta / 2 + 26 * pppY, 36 * pppY, "", 0xffff00ff, (int) (4 * pppY));
        upgradeText = new TextBox(screenDelta / 2 + 74 * pppY, 36 * pppY, "", 0xffff00ff, (int) (4 * pppY));
        nonUpgradeText = new TextBox(screenDelta / 2 + 74 * pppY, 40 * pppY, "", 0xffffffff, (int) (3 * pppY));

        // buildings
        buildings = new Building[20];
        Random rand = new Random();
        for (int i = 0; i < buildings.length; ++i) {
            buildings[i] = new Building(screenDelta / 2 + scrY / 36 + (i % 5) * 5 * scrY / 24,
                    29 * scrY / 36 - (float) (i / 5) * scrY / 4);
            if(rand.nextInt()%3 != 0) buildings[i].spawn(rand.nextInt(ShopInfo.quantityHouses));/////////////////////////////////////
        }

        houses = new Texture[ShopInfo.quantityHouses][ShopInfo.maxLevel+1];
        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            for (int j = 0; j < ShopInfo.maxLevel+1; j++) {
                houses[i][j] = new Texture("city/houses/house-id-" + i + ".png");////////////////////////
                //houses[i][j] = new Texture("city/houses/house-id-" + i + "-level-" + j + ".png");
            }
        }
    }


    @Override
    public void render(float delta) {
        batch.begin();
        backGround.draw();
        float moneyBGX = Math.min(coinText.getX(), sapphireText.getX()) - 3 * pppY;
        batch.draw(moneyBackGround, moneyBGX, 81 * pppY, 57 * pppY, 19 * pppY);
        for (Building building : buildings) {
            if (building.isExist()) building.draw();
        }
        showMoney();

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
                else if(sellButton.isTouched(false)){
                    sellSound.play(soundVolume * soundOn);
                    buildings[touchedBuilding].sell();
                    money += buildings[touchedBuilding].saleIncome();
                    savePrefs();
                    updateMoney();
                    windowIsOpened = false;
                }
                else if(!buildings[touchedBuilding].isMaxLevel() && upgradeButton.isTouched(false) && money >= buildings[touchedBuilding].getUpgradeCost()){
                    upgradeSound.play(soundVolume * soundOn);
                    money -= buildings[touchedBuilding].getUpgradeCost();
                    buildings[touchedBuilding].upgrade();
                    savePrefs();
                    updateMoney();
                    sellText.changeText(divisionDigits(buildings[touchedBuilding].saleIncome()));
                    if(!buildings[touchedBuilding].isMaxLevel()) {
                        upgradeText.changeText(divisionDigits(buildings[touchedBuilding].getUpgradeCost()));
                        if(money < buildings[touchedBuilding].getUpgradeCost()) upgradeText.setColor(1, 0, 0);
                        else upgradeText.setColor(1, 1, 0);
                    }
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
                        if (buildings[i].isTouched(Gdx.input.getX(), scrY - Gdx.input.getY())) {
                            windowIsOpened = true;
                            touchedBuilding = i;
                            sellText.changeText(divisionDigits(buildings[touchedBuilding].saleIncome()));
                            nonUpgradeText.changeText(Languages.maxLevel[selectedLanguage]);
                            if(!buildings[touchedBuilding].isMaxLevel()) {
                                upgradeText.changeText(divisionDigits(buildings[touchedBuilding].getUpgradeCost()));
                                if(money < buildings[touchedBuilding].getUpgradeCost()) upgradeText.setColor(1, 0, 0);
                                else upgradeText.setColor(1, 1, 0);
                            }
                            break;
                        }
                    }
                }
            }
        }

        batch.end();
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

        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            for (int j = 0; j < ShopInfo.maxLevel+1; j++) {
                houses[i][j].dispose();
            }
        }
    }
}
