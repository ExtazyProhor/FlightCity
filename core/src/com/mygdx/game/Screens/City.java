package com.mygdx.game.Screens;

import static com.mygdx.game.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.CityClasses.Building;
import com.mygdx.game.CityClasses.CityState;
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
    CityState state;

    PictureBox backGround;
    Button shopButton;

    Building[] buildings;
    public static Texture[][] houses;
    int territoryLevel = 0;

    // window
    PictureBox blackout;
    PictureBox windowBackGround;
    int touchedBuilding;
    Button cancelButton;
    Texture moneyBackGround;

    Button sellButton;
    TextBox sellText;
    Button upgradeButton;
    TextBox upgradeText;
    PictureBox nonUpgradeButton;
    TextBox nonUpgradeText;

    // installation
    int selectedPlaceIndex;
    int purchasedHouse;
    boolean isTouchHouse = false;
    PictureBox movingArrows;
    PictureBox freePlace;
    Button confirmButton;
    Button noBuyButton;

    public City(Main game) {
        this.game = game;
        screenDelta = scrX - scrY;
        state = CityState.DEFAULT;

        // pictures
        backGround = new PictureBox((scrX - 2 * scrY) / 2, 0, 2 * scrY, scrY, path + "backGround.png");
        windowBackGround = new PictureBox(screenDelta / 2, scrY / 4, scrY, scrY / 2, path + "window.png");
        moneyBackGround = new Texture(path + "moneyBG.png");
        blackout = new PictureBox(0, 0, scrX, scrY, path + "blackout.png");

        movingArrows = new PictureBox(-2 * scrY / 45, -6.5f * pppY, 20 * pppY, 10 * pppY, path + "movingArrows.png");
        freePlace = new PictureBox(0, 0, scrY / 9, scrY / 9, path + "freePlace.png");

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
        confirmButton = new Button(0, 0, 7 * pppY, 7 * pppY, new Texture("buttons/tickButton.png"));
        noBuyButton = new Button(0, 0, 7 * pppY, 7 * pppY, new Texture("buttons/xButton.png"));

        // text
        sellText = new TextBox(screenDelta / 2 + 26 * pppY, 36 * pppY, "", 0xffff00ff, (int) (4 * pppY));
        upgradeText = new TextBox(screenDelta / 2 + 74 * pppY, 36 * pppY, "", 0xffff00ff, (int) (4 * pppY));
        nonUpgradeText = new TextBox(screenDelta / 2 + 74 * pppY, 40 * pppY, "", 0xffffffff, (int) (3 * pppY));

        // buildings
        buildings = new Building[20];
        for (int i = 0; i < buildings.length; ++i) {
            buildings[i] = new Building(screenDelta / 2 + scrY / 36 + (i % 5) * 5 * scrY / 24,
                    29 * scrY / 36 - (float) (i / 5) * scrY / 4);
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

        switch (state){
            case DEFAULT:
                modeDefault();
                break;
            case WINDOW:
                modeWindow();
                break;
            case INSTALLATION:
                modeInstallation();
                break;
        }

        batch.end();
    }

    public void modeDefault(){
        game.startMenu.buttonExit.draw();
        shopButton.draw();
        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                game.setScreen(game.startMenu);
            } else if (shopButton.isTouched()) {
                game.setScreen(game.shop);
            } else {
                for (int i = 0; i < buildings.length; ++i) {
                    if (!buildings[i].isExist()) continue;
                    if (buildings[i].isTouched()) {
                        state = CityState.WINDOW;
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

    public void modeWindow(){
        blackout.draw();
        windowBackGround.draw();
        buildings[touchedBuilding].showWindow();
        cancelButton.draw();
        sellButton.draw();
        sellText.draw();
        if(buildings[touchedBuilding].isMaxLevel()){
            nonUpgradeButton.draw();
            nonUpgradeText.draw();
        }else{
            upgradeButton.draw();
            upgradeText.draw();
        }
        if (Gdx.input.justTouched()) {
            if (cancelButton.isTouched()) state = CityState.DEFAULT;
            else if(sellButton.isTouched(false)){
                sellSound.play(soundVolume * soundOn);
                buildings[touchedBuilding].sell();
                money += buildings[touchedBuilding].saleIncome();
                savePrefs();
                updateMoney();
                state = CityState.DEFAULT;
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
    }

    public void modeInstallation(){
        for(int i = 0; i < buildings.length; ++i){
            if((i % 5) < ShopInfo.freeHousesPlace[territoryLevel][0] &&
                    (i / 5) < ShopInfo.freeHousesPlace[territoryLevel][1] &&
                    !buildings[i].isExist() && i != selectedPlaceIndex){
                freePlace.draw(buildings[i].getX(), buildings[i].getY());
            }
        }
        territoryLevel = 4;
        if(Gdx.input.isTouched() && isTouchHouse) batch.setColor(1, 1, 1, 0.7f);
        else movingArrows.draw(movingArrows.getX() + buildings[selectedPlaceIndex].getX(),
                movingArrows.getY() + buildings[selectedPlaceIndex].getY());
        batch.draw(houses[purchasedHouse][0],
                buildings[selectedPlaceIndex].getX(), buildings[selectedPlaceIndex].getY(),
                buildings[selectedPlaceIndex].getSizeX(), buildings[selectedPlaceIndex].getSizeY());
        batch.setColor(1, 1, 1, 1);

        if(Gdx.input.justTouched()){
            if(buildings[selectedPlaceIndex].isTouched()){
                isTouchHouse = true;
            }else if(confirmButton.isTouched()){
                buildings[selectedPlaceIndex].spawn(purchasedHouse);
                savePrefs();
                game.shop.updateShop();
                state = CityState.DEFAULT;
            }else if(noBuyButton.isTouched()){
                money += ShopInfo.cost[purchasedHouse];
                savePrefs();
                game.shop.updateShop();
                state = CityState.DEFAULT;
            }
        }
        if(Gdx.input.isTouched() && isTouchHouse){
            int x = (int)((Gdx.input.getX() - (screenDelta/2 - 3 * scrY/144)) / (5 * scrY / 24));
            int y = (int)(Gdx.input.getY() / (scrY/4));
            if(x < 0) x = 0;
            else if(x > ShopInfo.freeHousesPlace[territoryLevel][0] - 1) x = ShopInfo.freeHousesPlace[territoryLevel][0] - 1;
            if(y < 0) y = 0;
            else if(y > ShopInfo.freeHousesPlace[territoryLevel][1] - 1) y = ShopInfo.freeHousesPlace[territoryLevel][1] - 1;
            if(!buildings[5 * y + x].isExist()) selectedPlaceIndex = 5 * y + x;
            batch.draw(houses[purchasedHouse][0], Gdx.input.getX() - scrY/18, scrY - Gdx.input.getY() - scrY/18, scrY/9, scrY/9);
        }else {
            isTouchHouse = false;
            confirmButton.setCoordinates(buildings[selectedPlaceIndex].getX() + pppY * 12,
                    buildings[selectedPlaceIndex].getY() + pppY * 4);
            confirmButton.draw();
            noBuyButton.setCoordinates(buildings[selectedPlaceIndex].getX() - pppY * 8,
                    buildings[selectedPlaceIndex].getY() + pppY * 4);
            noBuyButton.draw();
        }
    }

    @Override
    public void dispose() {
        game.dispose();

        windowBackGround.dispose();
        cancelButton.dispose();
        sellButton.dispose();
        upgradeButton.dispose();
        sellText.dispose();
        upgradeText.dispose();
        nonUpgradeButton.dispose();
        nonUpgradeText.dispose();
        moneyBackGround.dispose();
        blackout.dispose();

        backGround.dispose();
        shopButton.dispose();

        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            for (int j = 0; j < ShopInfo.maxLevel+1; j++) {
                houses[i][j].dispose();
            }
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
}
