package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;

public class Shop implements Screen {
    Main game;
    String path;

    PictureBox backGround;

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

        //pictures:
        backGround = new PictureBox(0, 0, Main.scrX, Main.scrY, path + "brickWall.png");

        //text:
        Main.parameter.borderWidth = Main.pppY/2;
        shopText = new TextBox(Main.scrX/2, 85 * Main.pppY, Languages.shop[Main.selectedLanguage], 0xf192f7ff, (int)(15 * Main.pppY));
        Main.parameter.borderWidth = 3;
        housesText = new TextBox(Main.pppX * 25, 0, Languages.houses[Main.selectedLanguage], 0xffffffff, (int)(3 * Main.pppY));
        housesText.positionToDown(Main.pppY * 28);
        territoryText = new TextBox(Main.pppX * 50, 0, Languages.territory[Main.selectedLanguage], 0xffffffff, (int)(3 * Main.pppY));
        territoryText.positionToDown(Main.pppY * 28);
        coinsText = new TextBox(Main.pppX * 75, 0, Languages.coins[Main.selectedLanguage], 0xffffffff, (int)(3 * Main.pppY));
        coinsText.positionToDown(Main.pppY * 28);

        //buttons:
        housesButton = new Button(Main.pppX * 15, Main.pppY * 25, Main.pppX * 20, Main.pppX * 20, new Texture(path + "housesButton.png"));
        territoryButton = new Button(Main.pppX * 40, Main.pppY * 25, Main.pppX * 20, Main.pppX * 20, new Texture(path + "orangeWindow.png"));
        coinsButton = new Button(Main.pppX * 65, Main.pppY * 25, Main.pppX * 20, Main.pppX * 20, new Texture(path + "coinsButton.png"));
    }

    @Override
    public void render(float delta) {
        Main.batch.begin();
        backGround.draw();
        game.startMenu.buttonExit.draw();
        float moneyBGX = Math.min(Main.coinText.getX(), Main.sapphireText.getX()) - 3 * Main.pppY;
        Main.batch.draw(game.city.moneyBackGround, moneyBGX, 81 * Main.pppY, 57 * Main.pppY, 19 * Main.pppY);
        Main.showMoney();
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

            } else if (territoryButton.isTouched()){

            } else if (coinsButton.isTouched()){

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
        backGround.dispose();
        housesButton.dispose();
        territoryButton.dispose();
        coinsButton.dispose();
        shopText.dispose();
    }
}
