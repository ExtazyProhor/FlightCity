package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.game.Main;

public class Shop implements Screen {
    Main game;


    public Shop(Main game){
        this.game = game;
    }

    @Override
    public void render(float delta) {


        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                game.setScreen(game.city);
            }
        }

        Main.batch.begin();
        game.startMenu.buttonExit.draw();

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
