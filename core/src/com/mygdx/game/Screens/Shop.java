package com.mygdx.game.Screens;

import static com.mygdx.game.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.CityClasses.ShopInfo;
import com.mygdx.game.CityClasses.ShopState;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;

public class Shop implements Screen {
    Main game;
    String path;
    ShopState state;

    PictureBox backGround;

    //coins:
    Button[] coinsPurchase;
    PictureBox sapphire;
    TextBox[] coinsPrices;
    TextBox[] coinsQuantities;

    //houses:
    Button[] housesShop;

    //menu:
    TextBox shopText;
    TextBox housesTextShop;
    TextBox territoryTextShop;
    TextBox coinsTextShop;

    Button housesButton;
    Button territoryButton;
    Button coinsButton;

    public Shop(Main game){
        this.game = game;
        path = "city/shop/";
        state = ShopState.MENU;

        //pictures:
        backGround = new PictureBox(0, 0, scrX, scrY, path + "brickWall.png");

        //coins:
        coinsPurchase = new Button[ShopInfo.quantityCoins];
        coinsPrices = new TextBox[ShopInfo.quantityCoins];
        coinsQuantities = new TextBox[ShopInfo.quantityCoins];
        for (int i = 0; i < ShopInfo.quantityCoins; i++) {
            float cornerX = (18 + (i%3) * 22);
            float cornerY = (24 - 22 * (i / 3));
            coinsPurchase[i] = new Button(cornerX * pppX, cornerY * pppX, 20 * pppX, 20 * pppX,
                    new Texture(path + "coins-"+i+".png"));
            coinsPrices[i] = new TextBox(0, 0, divisionDigits(ShopInfo.sapphiresInShop[i]), 0xffffffff, (int)(3 * pppY));
            coinsPrices[i].positionToRight((cornerX + 15) * pppX);
            coinsPrices[i].positionToMiddleY((cornerY + 2.5f) * pppX);
            coinsQuantities[i] = new TextBox((cornerX + 10) * pppX, 0, divisionDigits(ShopInfo.coinsInShop[i]), 0xffff00ff, (int)(5 * pppY));
            coinsQuantities[i].positionToDown((cornerY + 5) * pppX);
        }
        sapphire = new PictureBox(0, 0, pppX * 3, pppX * 3, "general/sapphire.png");

        //houses:
        housesShop = new Button[ShopInfo.quantityHouses];
        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            housesShop[i] = new Button((20 + (i%3) * 24) * pppX, (23 - 20 * (i / 3)) * pppX, 12 * pppX, 5 * pppX,
                    new Texture(path + "buyHouseButton.png"), divisionDigits(ShopInfo.cost[i]), 0xffff00ff, (int)(4 * pppY));
        }

        //text:
        parameter.borderWidth = pppY/2;
        shopText = new TextBox(scrX/2, 85 * pppY, Languages.shop[selectedLanguage], 0xf192f7ff, (int)(15 * pppY));
        parameter.borderWidth = 3;
        housesTextShop = new TextBox(pppX * 25, 0, Languages.houses[selectedLanguage], 0xffffffff, (int)(3 * pppY));
        housesTextShop.positionToDown(pppY * 28);
        territoryTextShop = new TextBox(pppX * 50, 0, Languages.territory[selectedLanguage], 0xffffffff, (int)(3 * pppY));
        territoryTextShop.positionToDown(pppY * 28);
        coinsTextShop = new TextBox(pppX * 75, 0, Languages.coins[selectedLanguage], 0xffffffff, (int)(3 * pppY));
        coinsTextShop.positionToDown(pppY * 28);

        //buttons:
        housesButton = new Button(pppX * 15, pppY * 25, pppX * 20, pppX * 20, new Texture(path + "housesButton.png"));
        territoryButton = new Button(pppX * 40, pppY * 25, pppX * 20, pppX * 20, new Texture(path + "orangeWindow.png"));
        coinsButton = new Button(pppX * 65, pppY * 25, pppX * 20, pppX * 20, new Texture(path + "coinsButton.png"));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        backGround.draw();
        game.startMenu.buttonExit.draw();
        float moneyBGX = Math.min(coinText.getX(), sapphireText.getX()) - 3 * pppY;
        batch.draw(game.city.moneyBackGround, moneyBGX, 81 * pppY, 57 * pppY, 19 * pppY);
        showMoney();

        switch (state){
            case MENU:
                showMenu();
                break;
            case HOUSES:
                showHouses();
                break;
            case TERRITORY:
                showTerritory();
                break;
            case COINS:
                showCoins();
                break;
        }

        batch.end();
    }

    void showMenu(){
        shopText.draw();
        housesButton.draw();
        housesTextShop.draw();
        territoryButton.draw();
        territoryTextShop.draw();
        coinsButton.draw();
        coinsTextShop.draw();

        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                game.setScreen(game.city);
            } else if (housesButton.isTouched()){
                updateShop();
                state = ShopState.HOUSES;
            } else if (territoryButton.isTouched()){
                updateShop();
                state = ShopState.TERRITORY;
            } else if (coinsButton.isTouched()){
                updateShop();
                state = ShopState.COINS;
            }
        }
    }

    void showHouses(){
        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            batch.draw(City.houses[i][0], (20 + (i%3) * 24) * pppX, (28 - 20 * (i / 3)) * pppX, 12 * pppX, 12 * pppX);
            housesShop[i].draw();
        }
        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                state = ShopState.MENU;
            }else{
                for (int i = 0; i < ShopInfo.quantityHouses; i++) {
                    if(housesShop[i].isTouched(false)){
                        sellSound.play(soundVolume * soundOn);

                        break;
                    }
                }
            }
        }
    }

    void showTerritory(){
        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                state = ShopState.MENU;
            }
        }
    }

    void showCoins(){
        for(int i = 0; i < ShopInfo.quantityCoins; ++i){
            coinsPurchase[i].draw();
            sapphire.draw((34 + (i%3) * 22) * pppX, (25 - 22 * (i / 3)) * pppX);
            coinsPrices[i].draw();
            coinsQuantities[i].draw();
        }
        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                state = ShopState.MENU;
            } else {
                for (int i = 0; i < ShopInfo.quantityCoins; i++) {
                    if(coinsPurchase[i].isTouched(false) && sapphires >= ShopInfo.sapphiresInShop[i]){
                        sellSound.play(soundVolume * soundOn);
                        money += ShopInfo.coinsInShop[i];
                        sapphires -= ShopInfo.sapphiresInShop[i];
                        savePrefs();
                        updateShop();
                        break;
                    }
                }
            }
        }
    }

    void updateShop(){
        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            if(money < ShopInfo.cost[i]) housesShop[i].setColor(1, 0, 0);
            else housesShop[i].setColor(1, 1, 0);
        }
        for(int i = 0; i < ShopInfo.quantityCoins; ++i){
            if(sapphires < ShopInfo.sapphiresInShop[i]) coinsPrices[i].setColor(1, 0, 0);
            else coinsPrices[i].setColor(1, 1, 1);
        }
        updateMoney();
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
        backGround.dispose();
        housesButton.dispose();
        territoryButton.dispose();
        coinsButton.dispose();
        shopText.dispose();

        for (int i = 0; i < ShopInfo.quantityCoins; i++) {
            coinsPurchase[i].dispose();
            coinsPrices[i].dispose();
        }
        sapphire.dispose();
        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            housesShop[i].dispose();
        }
    }
}
