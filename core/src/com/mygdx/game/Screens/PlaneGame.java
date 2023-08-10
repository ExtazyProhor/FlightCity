package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;
import static com.mygdx.game.Main.*;

import com.mygdx.game.PlaneClasses.ObjectWithCollider;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;

import java.util.Random;

public class PlaneGame implements Screen {
    //general:
    Main game;
    boolean isPaused;

    //select:
    int selectedPlane;
    int selectedBackGround;
    Texture[] planeSkins;
    Texture[] backGroundSkins;

    //game:
    Texture blackBuildings;
    Texture[] explosionFX;

    Button pauseButton;
    Button resumeButton;
    Button exitButton;
    Button restartButton;

    PictureBox plane;
    ObjectWithCollider[] buildings;
    ObjectWithCollider ground;

    public PlaneGame(Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
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
}