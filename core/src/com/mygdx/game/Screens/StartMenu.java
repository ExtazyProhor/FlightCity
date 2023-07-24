package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;

public class StartMenu implements Screen {
    Main game;

    Texture menuBackGround;
    Texture buttonTexture;
    Texture exitButtonTexture;
    //Texture[] carsTexture;
    //Texture[] planesTexture;

    Button buttonPlay;
    Button buttonExit;
    Button buttonSettings;

    //Random rand;
    float backGroundWidth;
    float backGroundCoordinateX;

    public StartMenu(Main game) {
        this.game = game;
        //rand = new Random();
        initializationTexturesAndButtons();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (Gdx.input.justTouched()) {
            if (buttonPlay.isTouched()) {
                game.setScreen(game.city);
            } else if (buttonSettings.isTouched()) {
                game.setScreen(game.settings);
                game.settings.touchPlace = Gdx.input.getY();
                game.settings.localSelectedLanguage = Main.selectedLanguage;
                game.settings.localSoundVolume = Main.soundVolume;
                game.settings.localMusicVolume = Main.musicVolume;
                game.settings.localSoundOn = Main.soundOn;
                game.settings.localMusicOn = Main.musicOn;
            } else if (buttonExit.isTouched()) {
                Gdx.app.exit();
            }
        }

        Main.camera.update();
        Main.batch.setProjectionMatrix(Main.camera.combined);
        Main.batch.begin();

        Main.batch.draw(menuBackGround, backGroundCoordinateX, 0, backGroundWidth, Main.scrY);
        buttonPlay.draw();
        buttonSettings.draw();
        buttonExit.draw();

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

    private void initializationTexturesAndButtons(){
        menuBackGround = new Texture("startMenu/cityBackGround.png");
        buttonTexture = new Texture("buttons/button 5-2.png");
        exitButtonTexture = new Texture("buttons/exitButton.png");
        backGroundWidth = Main.scrY * Main.textureAspectRatio(menuBackGround, true);
        backGroundCoordinateX = (Main.scrX - backGroundWidth)/2;

        buttonPlay = new Button(0, 63 * Main.pppY, Main.textureAspectRatio(buttonTexture, true) * 20 * Main.pppY,
                20 * Main.pppY, buttonTexture, Main.language.getString(Main.selectedLanguage + "play"),
                0xffffffff, (int) (5 * Main.pppY));
        buttonPlay.placeCenter();
        buttonSettings = new Button(0, 38 * Main.pppY, Main.textureAspectRatio(buttonTexture, true) * 20 * Main.pppY,
                20 * Main.pppY, buttonTexture, Main.language.getString(Main.selectedLanguage + "settings"),
                0xffffffff, (int) (5 * Main.pppY));
        buttonSettings.placeCenter();
        buttonExit = new Button(3 * Main.pppY, 82 * Main.pppY, 15 * Main.pppY, 15 * Main.pppY, exitButtonTexture);
    }

    @Override
    public void dispose() {
        menuBackGround.dispose();
        buttonTexture.dispose();
        exitButtonTexture.dispose();
/*        for (int i = 0; i < carsTexture.length; ++i) {
            carsTexture[i].dispose();
        }*/
    }
}
