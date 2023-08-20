package com.mygdx.game.Screens;

import static com.mygdx.game.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
    Button planeButton;
    Button inventoryButton;
    Button rouletteButton;

    public Building[] buildings;
    public Texture[] houseLevelBars;
    public Texture moneyNotice;
    public static Texture[][] houses;
    int territoryLevel;

    // window
    PictureBox blackout;
    PictureBox windowBackGround;
    int touchedBuilding;
    Button cancelButton;
    Texture moneyBackGround;
    TextBox houseType;
    TextBox houseLevel;
    TextBox timeLeft;
    TextBox incomePerMinute;

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
    PictureBox movingArrow;
    PictureBox freePlace;
    Button confirmButton;
    Button noBuyButton;
    float deltaTouchX;
    float deltaTouchY;
    float touchX;
    float touchY;
    float buttonsSize;

    public City(Main game) {
        this.game = game;
        screenDelta = scrX - scrY;
        state = CityState.DEFAULT;
        territoryLevel = cityPrefs.getInteger("territoryLevel", 0);

        deltaTouchX = 29 * pppY;
        deltaTouchY = 5 * pppY;
        buttonsSize = 12 * pppY;

        // pictures
        backGround = new PictureBox((scrX - 2 * scrY) / 2, 0, 2 * scrY, scrY, path + "backGround.png");
        windowBackGround = new PictureBox(screenDelta / 2, scrY / 4, scrY, scrY / 2, path + "window.png");
        moneyBackGround = new Texture(path + "moneyBG.png");
        blackout = new PictureBox(0, 0, scrX, scrY, path + "blackout.png");
        moneyNotice = new Texture(path + "moneyNotice.png");

        movingArrow = new PictureBox(5 * pppY, -3 * pppY, 32 * pppY, 16 * pppY, path + "movingArrow.png");
        freePlace = new PictureBox(0, 0, scrY / 9, scrY / 9, path + "freePlace.png");

        // buttons
        shopButton = new Button(3 * pppY, 64 * pppY, 15 * pppY, 15 * pppY,
                new Texture("buttons/shop.png"));
        planeButton = new Button(scrX - 18 * pppY, 63 * pppY, 15 * pppY, 15 * pppY,
                new Texture("buttons/planeButton.png"));
        inventoryButton = new Button(3 * pppY, 46 * pppY, 15 * pppY, 15 * pppY,
                new Texture("buttons/inventoryButton.png"));
        rouletteButton = new Button(3 * pppY, 28 * pppY, 15 * pppY, 15 * pppY,
                new Texture("buttons/caseButton.png"));

        cancelButton = new Button(screenDelta / 2 + pppY * 82, pppY * 57, 15 * pppY, 15 * pppY,
                new Texture("buttons/xButton.png"));
        sellButton = new Button(screenDelta / 2 + 6 * pppY, 28 * pppY, 40 * pppY, 16 * pppY,
                new Texture("buttons/red button.png"), Languages.sell[selectedLanguage] + "\n\n", 0xffffffff, (int) (4 * pppY));
        upgradeButton = new Button(screenDelta / 2 + 54 * pppY, 28 * pppY, 40 * pppY, 16 * pppY,
                new Texture("buttons/blue button.png"), Languages.upgrade[selectedLanguage] + "\n\n", 0xffffffff, (int) (4 * pppY));
        nonUpgradeButton = new PictureBox(screenDelta / 2 + 54 * pppY, 28 * pppY, 40 * pppY, 16 * pppY, "buttons/grey button.png");
        confirmButton = new Button(0, 0, buttonsSize, buttonsSize, new Texture("buttons/tickButton.png"));
        noBuyButton = new Button(0, 0, buttonsSize, buttonsSize, new Texture("buttons/xButton.png"));

        // text
        sellText = new TextBox(screenDelta / 2 + 26 * pppY, 36 * pppY, "", 0xffff00ff, (int) (4 * pppY));
        upgradeText = new TextBox(screenDelta / 2 + 74 * pppY, 36 * pppY, "", 0xffff00ff, (int) (4 * pppY));
        nonUpgradeText = new TextBox(screenDelta / 2 + 74 * pppY, 40 * pppY, "", 0xffffffff, (int) (3 * pppY));

        houseType = new TextBox((screenDelta + scrY)/2, 72 * pppY, "", 0x1e9affff, (int) (7 * pppY));
        houseLevel = new TextBox((screenDelta + scrY)/2, 0, "", 0xffffffff, (int) (4 * pppY));
        houseLevel.positionToDown(62 * pppY);
        timeLeft = new TextBox(screenDelta / 2 + 13 * pppY, 0, "", 0xffffffff, (int) (4 * pppY));
        timeLeft.positionToMiddleY(48 * pppY);
        incomePerMinute = new TextBox(scrX/2, scrY/2, "", 0xffffffff, (int) (4 * pppY));

        // buildings
        buildings = new Building[20];
        for (int i = 0; i < buildings.length; ++i) {
            buildings[i] = new Building(screenDelta / 2 + scrY / 36 + (i % 5) * 5 * scrY / 24,
                    29 * scrY / 36 - (float) (i / 5) * scrY / 4);
            if(cityPrefs.getBoolean("building-" + i + "-isExist", false)) buildings[i].spawn(
                    cityPrefs.getInteger("building-" + i + "-id", 0),
                    cityPrefs.getInteger("building-" + i + "-level", 0),
                    cityPrefs.getLong("building-" + i + "-time", System.currentTimeMillis()));
        }

        houseLevelBars = new Texture[ShopInfo.maxLevel + 1];
        for(int i = 0; i < houseLevelBars.length; ++i){
            houseLevelBars[i] = new Texture(path + "level-" + i + ".png");
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
        planeButton.draw();
        inventoryButton.draw();
        rouletteButton.draw();

        for (Building building : buildings) {
            if (building.isExist()) {
                batch.draw(houseLevelBars[building.getLevel()], building.getX(),
                        building.getY() + building.getSizeY() + pppY,
                        building.getSizeX(), building.getSizeY() / 10);
                if(building.isReady()) batch.draw(moneyNotice, building.getX() + building.getSizeY()/4,
                        building.getY() + 13.75f * pppY, building.getSizeX()/2, building.getSizeY()/2);
            }
        }

        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                game.setScreen(game.startMenu);
            } else if (shopButton.isTouched()) {
                game.setScreen(game.shop);
            } else if (planeButton.isTouched()){
                cityMusic.stop();
                Random random = new Random();
                musicIndex = random.nextInt(planeMusic.length);
                planeMusic[musicIndex].play();

                if(game.planeGame.backGround != null) game.planeGame.backGround.dispose();
                game.planeGame.backGround = new Texture("planeGame/backGrounds/BG" + game.planeGame.selectedBackGround + ".png");
                if(game.planeGame.plane != null) game.planeGame.plane.dispose();
                game.planeGame.plane = new PictureBox(20 * pppY, 44 * pppY, 12 * pppY * game.planeGame.planeAspectRatio, 12 * pppY,
                        "planeGame/planes/plane-" + game.planeGame.selectedPlane + ".png");
                game.setScreen(game.planeGame);
            } else if (inventoryButton.isTouched()){
                game.setScreen(game.inventory);
            }else if (rouletteButton.isTouched()){
                game.roulette.spinCostText.setColor(1, 1, 1);
                switch (Roulette.currencyType[game.roulette.rouletteIndex]){
                    case "coin":
                        if(money < Roulette.spinCost[game.roulette.rouletteIndex]) game.roulette.spinCostText.setColor(1, 0, 0);
                        break;
                    case "sapphire":
                        if(sapphires < Roulette.spinCost[game.roulette.rouletteIndex]) game.roulette.spinCostText.setColor(1, 0, 0);
                        break;
                }
                game.setScreen(game.roulette);
            }else {
                for (int i = 0; i < buildings.length; ++i) {
                    if (!buildings[i].isExist()) continue;
                    if (buildings[i].isTouched()) {
                        if(buildings[i].isReady()) buildings[i].getMoney(i);
                        else{
                            state = CityState.WINDOW;
                            touchedBuilding = i;
                            sellText.changeText(divisionDigits(buildings[touchedBuilding].saleIncome()));
                            nonUpgradeText.changeText(Languages.maxLevel[selectedLanguage]);
                            houseType.changeText(Languages.buildingTypes[buildings[touchedBuilding].getId()][selectedLanguage]);
                            houseLevel.changeText(Languages.level[selectedLanguage] + " " + buildings[touchedBuilding].getLevel());
                            int moneyPerMinute = (int)
                                    (ShopInfo.incomeFromHouses[buildings[touchedBuilding].getId()][buildings[touchedBuilding].getLevel()]
                                            * 60 / ShopInfo.houseTimer[buildings[touchedBuilding].getLevel()]);
                            incomePerMinute.changeText(divisionDigits(moneyPerMinute) + "$/" +
                                    Languages.minute[selectedLanguage]);

                            if(!buildings[touchedBuilding].isMaxLevel()) {
                                upgradeText.changeText(divisionDigits(buildings[touchedBuilding].getUpgradeCost()));
                                if(money < buildings[touchedBuilding].getUpgradeCost()) upgradeText.setColor(1, 0, 0);
                                else upgradeText.setColor(1, 1, 0);
                            }
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
        batch.draw(City.houses[buildings[touchedBuilding].getId()][buildings[touchedBuilding].getLevel()],
                (scrX - scrY) / 2 + pppY * 3, pppY*52, pppY*20, pppY*20);
        cancelButton.draw();
        sellButton.draw();
        sellText.draw();
        houseType.draw();
        houseLevel.draw();
        timeLeft.changeText(buildings[touchedBuilding].getTime());
        timeLeft.draw();
        incomePerMinute.draw();

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
                money += buildings[touchedBuilding].saleIncome();
                buildings[touchedBuilding].sell();
                game.saveCityPrefs(touchedBuilding);
                savePrefs();
                updateMoney();
                state = CityState.DEFAULT;
            }
            else if(upgradeButton.isTouched(false)){
                if (!buildings[touchedBuilding].isMaxLevel() && money >= buildings[touchedBuilding].getUpgradeCost()){
                    upgradeSound.play(soundVolume * soundOn);
                    money -= buildings[touchedBuilding].getUpgradeCost();
                    buildings[touchedBuilding].upgrade();
                    houseLevel.changeText(Languages.level[selectedLanguage] + " " + buildings[touchedBuilding].getLevel());
                    game.saveCityPrefs(touchedBuilding);
                    savePrefs();
                    updateMoney();
                    sellText.changeText(divisionDigits(buildings[touchedBuilding].saleIncome()));
                    int moneyPerMinute = (int)
                            (ShopInfo.incomeFromHouses[buildings[touchedBuilding].getId()][buildings[touchedBuilding].getLevel()]
                                    * 60 / ShopInfo.houseTimer[buildings[touchedBuilding].getLevel()]);
                    incomePerMinute.changeText(divisionDigits(moneyPerMinute) + "$/" +
                            Languages.minute[selectedLanguage]);
                    if(!buildings[touchedBuilding].isMaxLevel()) {
                        upgradeText.changeText(divisionDigits(buildings[touchedBuilding].getUpgradeCost()));
                        if(money < buildings[touchedBuilding].getUpgradeCost()) upgradeText.setColor(1, 0, 0);
                        else upgradeText.setColor(1, 1, 0);
                    }
                }else errorSound.play(soundVolume * soundOn);
            }
        }
    }

    public void modeInstallation(){
        touchX = Gdx.input.getX() - deltaTouchX;
        touchY = scrY - Gdx.input.getY() - deltaTouchY;

        for(int i = 0; i < buildings.length; ++i){
            if((i % 5) < ShopInfo.freeHousesPlace[territoryLevel][0] &&
                    (i / 5) < ShopInfo.freeHousesPlace[territoryLevel][1] &&
                    !buildings[i].isExist() && i != selectedPlaceIndex){
                freePlace.draw(buildings[i].getX(), buildings[i].getY());
            }
        }
        if(!(Gdx.input.isTouched() && isTouchHouse)) movingArrow.draw(movingArrow.getX() + buildings[selectedPlaceIndex].getX(),
                    movingArrow.getY() + buildings[selectedPlaceIndex].getY());

        if(Gdx.input.isTouched() && isTouchHouse) batch.setColor(1, 1, 1, 0.7f);
        batch.draw(houses[purchasedHouse][0],
                buildings[selectedPlaceIndex].getX(), buildings[selectedPlaceIndex].getY(),
                buildings[selectedPlaceIndex].getSizeX(), buildings[selectedPlaceIndex].getSizeY());
        batch.setColor(1, 1, 1, 1);

        if(Gdx.input.justTouched()){
            if(buildings[selectedPlaceIndex].getX() + 21 * pppY < Gdx.input.getX() &&
                    Gdx.input.getX() < buildings[selectedPlaceIndex].getX() + 37 * pppY &&
                    buildings[selectedPlaceIndex].getY() - 3 * pppY < scrY - Gdx.input.getY() &&
                    scrY - Gdx.input.getY() < buildings[selectedPlaceIndex].getY() + 13 * pppY) isTouchHouse = true;
            else if(confirmButton.isTouched()){
                buildings[selectedPlaceIndex].spawn(purchasedHouse, 0, 0);
                buildings[selectedPlaceIndex].setTime(selectedPlaceIndex);
                game.saveCityPrefs(selectedPlaceIndex);
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
            movingArrow.draw(movingArrow.getX() + touchX, movingArrow.getY() + touchY);
            int x = (int)((Gdx.input.getX() - (screenDelta/2 - 3 * scrY/144) /**/ - deltaTouchX + scrY/18) / (5 * scrY / 24));
            int y = (int)((Gdx.input.getY() /**/ - deltaTouchY + scrY/18) / (scrY/4));
            if(x < 0) x = 0;
            else if(x > ShopInfo.freeHousesPlace[territoryLevel][0] - 1) x = ShopInfo.freeHousesPlace[territoryLevel][0] - 1;
            if(y < 0) y = 0;
            else if(y > ShopInfo.freeHousesPlace[territoryLevel][1] - 1) y = ShopInfo.freeHousesPlace[territoryLevel][1] - 1;
            if(!buildings[5 * y + x].isExist()) selectedPlaceIndex = 5 * y + x;
            batch.draw(houses[purchasedHouse][0], touchX, touchY, scrY/9, scrY/9);
        }else {
            isTouchHouse = false;
            float thisY;
            if (selectedPlaceIndex < buildings.length/2) thisY = buildings[selectedPlaceIndex].getY() - buttonsSize;
            else thisY = buildings[selectedPlaceIndex].getY() + scrY/9;

            confirmButton.setX(buildings[selectedPlaceIndex].getX() + scrY/9);
            confirmButton.setY(thisY);
            confirmButton.draw();

            noBuyButton.setX(buildings[selectedPlaceIndex].getX() - buttonsSize);
            noBuyButton.setY(thisY);
            noBuyButton.draw();
        }
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
        blackout.dispose();

        backGround.dispose();
        shopButton.dispose();
        planeButton.dispose();
        inventoryButton.dispose();
        rouletteButton.dispose();

        movingArrow.dispose();
        freePlace.dispose();
        confirmButton.dispose();
        noBuyButton.dispose();

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