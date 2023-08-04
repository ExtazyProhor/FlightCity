package com.mygdx.game.Screens;

import static com.mygdx.game.Main.pppX;
import static com.mygdx.game.Main.pppY;
import static com.mygdx.game.Main.scrX;
import static com.mygdx.game.Main.scrY;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSorter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;
import com.mygdx.game.SubwayClasses.Trash;

import java.util.ArrayList;
import java.util.Random;

public class SubwayGame implements Screen {
    Random r = new Random();
    Main game;
    ArrayList<Trash> trashes = new ArrayList<>();
    int framecount;
    int framelimit;
    public SubwayGame(Main game){
        this.game = game;
        framecount = 0;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        for (Trash trash:trashes) {
            if(trash.isExist()) {
                trash.update();
            }
            else{
                trash.dispose();
            }
        }
        if (framecount == framelimit){
            trashes.add(new Trash(scrX, pppY * r.nextInt(4) * 20 + pppY * 15, pppX * 4, pppY * 16, "subwaygame/stone1.png", pppX));
            framecount = 0;
            framelimit = (int)(10 * Math.random()) + 50;
        }
        framecount += 1;
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
