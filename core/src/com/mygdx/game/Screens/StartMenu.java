package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import static com.mygdx.game.Main.*;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.TextBox;

import java.util.Random;

public class StartMenu implements Screen {
    Main game;

    Texture menuBackGround;
    TextBox version;

    Texture[] carsTexture;
    Random rand;
    float carCoordinateX;
    float carSizeY;
    float carSpeed;
    int carIndex;
    boolean isMovingRight;

    Button buttonPlay;
    Button buttonExit;
    Button buttonSettings;

    float backGroundWidth;
    float backGroundCoordinateX;

    public StartMenu(Main game) {
        this.game = game;
        rand = new Random();
        carCoordinateX = pppX * 130;

        version = new TextBox(0, 98 * pppY, gameVersion, 0xffffffff, (int)(5 * pppY));
        version.positionToRight(scrX - 2 * pppY);

        initializationTexturesAndButtons();
        carsTexture = new Texture[6];
        for(int i = 0; i < carsTexture.length; i++){
            carsTexture[i] = new Texture("startMenu/cars/car" + i + ".png");
        }
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()) {
            if (buttonPlay.isTouched()) {
                cityMusic.play();
                game.setScreen(game.city);
            } else if (buttonSettings.isTouched()) {
                game.setScreen(game.settings);
                game.settings.touchPlace = Gdx.input.getY();
                game.settings.localSelectedLanguage = selectedLanguage;
                game.settings.localSoundVolume = soundVolume;
                game.settings.localMusicVolume = musicVolume;
                game.settings.localSoundOn = soundOn;
                game.settings.localMusicOn = musicOn;
            } else if (buttonExit.isTouched()) {
                Gdx.app.exit();
            }
        }

        batch.begin();

        batch.draw(menuBackGround, backGroundCoordinateX, 0, backGroundWidth, scrY);
        buttonPlay.draw();
        buttonSettings.draw();
        buttonExit.draw();

        version.draw();

        carsMove();

        batch.end();
    }

    void carsMove(){
        if(carCoordinateX > 120 * pppX || carCoordinateX < -30 * pppX){
            isMovingRight  = rand.nextBoolean();
            carIndex = rand.nextInt(carsTexture.length);
            carSizeY = textureAspectRatio(carsTexture[carIndex], false) * 10 * pppX;
            carSpeed = rand.nextInt((int)(pppX * 5)) + pppX * 15;
            if(isMovingRight) carCoordinateX = -20 * pppX;
            else carCoordinateX = 110 * pppX;
        }

        if(isMovingRight) carCoordinateX += carSpeed * Gdx.graphics.getDeltaTime();
        else carCoordinateX -= carSpeed * Gdx.graphics.getDeltaTime();

        batch.draw(carsTexture[carIndex], carCoordinateX, pppY * 4, pppX * 10, carSizeY,
                0, 0, carsTexture[carIndex].getWidth(), carsTexture[carIndex].getHeight(), isMovingRight, false);
    }

    void initializationTexturesAndButtons(){
        menuBackGround = new Texture("startMenu/cityBackGround.png");
        backGroundWidth = scrY * textureAspectRatio(menuBackGround, true);
        backGroundCoordinateX = (scrX - backGroundWidth)/2;

        buttonPlay = new Button(0, 63 * pppY, 50 * pppY,
                20 * pppY, new Texture("buttons/button 5-2.png"), Languages.play[selectedLanguage],
                0xffffffff, (int) (5 * pppY));
        buttonPlay.placeCenter();
        buttonSettings = new Button(0, 38 * pppY, 50 * pppY,
                20 * pppY, new Texture("buttons/button 5-2.png"), Languages.settings[selectedLanguage],
                0xffffffff, (int) (5 * pppY));
        buttonSettings.placeCenter();
        buttonExit = new Button(3 * pppY, 82 * pppY, 15 * pppY, 15 * pppY, new Texture("buttons/exitButton.png"));
    }

    @Override
    public void dispose() {
        menuBackGround.dispose();
        buttonPlay.dispose();
        buttonExit.dispose();
        buttonSettings.dispose();
        for (Texture texture : carsTexture) {
            texture.dispose();
        }
        version.dispose();
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
