package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

import java.util.Random;

public class PlaneGame implements Screen {
    Main game;

    Texture planeTexture;
    float angle = 0;


    public PlaneGame(Main game) {
        this.game = game;
        planeTexture = new Texture("settings/flagBelorussian.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        Main.batch.begin();

        Main.batch.draw(planeTexture, 200, 200, 300, 150, 600, 300, 1, 1, angle, 0, 0,
                planeTexture.getWidth(), planeTexture.getHeight(), false, false);

        angle++;

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