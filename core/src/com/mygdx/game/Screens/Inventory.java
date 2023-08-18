package com.mygdx.game.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;
import static com.mygdx.game.Main.*;

import java.util.Date;

public class Inventory implements Screen {
    Main game;

    public Inventory(Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        batch.begin();

        batch.end();
    }

    @Override
    public void dispose() {

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

    enum InventoryState{
        MENU,
        PLANES,

    }
}
