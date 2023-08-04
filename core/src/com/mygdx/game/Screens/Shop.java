package com.mygdx.game.Screens;

import static com.mygdx.game.Main.*;
import static com.mygdx.game.Screens.City.*;

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

    //houses:
    Button[] housesShop;

    //menu:
    TextBox shopText;
    TextBox housesText;
    TextBox territoryText;
    TextBox coinsText;

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
        for (int i = 0; i < ShopInfo.quantityCoins; i++) {
            coinsPurchase[i] = new Button((15 + (i%3) * 25) * pppX, (30 - 25 * (i / 3)) * pppX, 20 * pppX, 20 * pppX,
                    new Texture("city/shop/orangeWindow.png"));////////
            coinsPrices[i] = new TextBox(0, 0, Integer.toString(ShopInfo.sapphires[i]), 0xffffffff, (int)(3 * pppY));
            coinsPrices[i].positionToRight((30 + (i%3) * 25) * pppX);
            coinsPrices[i].positionToMiddleY((32.5f - 25 * (i / 3)) * pppX);
        }
        sapphire = new PictureBox(0, 0, pppX * 3, pppX * 3, "general/sapphire.png");

        //houses:
        housesShop = new Button[ShopInfo.quantityHouses];
        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            housesShop[i] = new Button((15 + (i%3) * 25) * pppX, (30 - 25 * (i / 3)) * pppX, 20 * pppX, 20 * pppX,
                    houses[i][0]);////////
        }

        //text:
        parameter.borderWidth = pppY/2;
        shopText = new TextBox(scrX/2, 85 * pppY, Languages.shop[selectedLanguage], 0xf192f7ff, (int)(15 * pppY));
        parameter.borderWidth = 3;
        housesText = new TextBox(pppX * 25, 0, Languages.houses[selectedLanguage], 0xffffffff, (int)(3 * pppY));
        housesText.positionToDown(pppY * 28);
        territoryText = new TextBox(pppX * 50, 0, Languages.territory[selectedLanguage], 0xffffffff, (int)(3 * pppY));
        territoryText.positionToDown(pppY * 28);
        coinsText = new TextBox(pppX * 75, 0, Languages.coins[selectedLanguage], 0xffffffff, (int)(3 * pppY));
        coinsText.positionToDown(pppY * 28);

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
        housesText.draw();
        territoryButton.draw();
        territoryText.draw();
        coinsButton.draw();
        coinsText.draw();

        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                game.setScreen(game.city);
            } else if (housesButton.isTouched()){
                state = ShopState.HOUSES;
            } else if (territoryButton.isTouched()){
                state = ShopState.TERRITORY;
            } else if (coinsButton.isTouched()){
                state = ShopState.COINS;
            }
        }
    }

    void showHouses(){
        for (int i = 0; i < ShopInfo.quantityHouses; i++) {
            housesShop[i].draw();
        }
        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                state = ShopState.MENU;
            }else{
                for (int i = 0; i < ShopInfo.quantityHouses; i++) {
                    if(housesShop[i].isTouched()){

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
            sapphire.draw((31 + (i%3) * 25) * pppX, (31 - 25 * (i / 3)) * pppX);
            coinsPrices[i].draw();
        }
        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                state = ShopState.MENU;
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
