package com.mygdx.game.Screens;

import static com.mygdx.game.Main.pppX;
import static com.mygdx.game.Main.pppY;
import static com.mygdx.game.Main.scrX;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;
import com.mygdx.game.SubwayClasses.Player;
import com.mygdx.game.SubwayClasses.Trash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SubwayGame implements Screen {
    float ms;
    Random r = new Random();
    Main game;
    ArrayList<Trash> trashes = new ArrayList<>();
    Iterator<Trash> i;
    Player player;
    Rectangle playerhitbox;
    int framecount;
    int framelimit;

    public SubwayGame(Main game) {
        this.game = game;
        player = new Player(pppX * 15, 0, pppX * 10, pppY * 10, "subwaygame/car1.png");
        framelimit = (int) (20 * Math.random()) + 70;
        framecount = 0;
        ms = 0.4f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(player.isExist()) {
            ScreenUtils.clear(0, 0, 0, 1);
            i = trashes.iterator();
            Main.batch.begin();
            while (i.hasNext()) {
                Trash trash = i.next();
                if (!trash.isExist()) {
                    trash.dispose();
                    i.remove();
                } else {
                    trash.update();
                }
            }
            player.update(trashes);
            if (framecount == framelimit) {
                trashes.add(new Trash(scrX, pppY * r.nextInt(4) * 20 + pppY * 10, pppX * 8, pppY * 16, "subwaygame/stone1.png", pppX * ms));
                framecount = 0;
                framelimit = (int) ((20 * Math.random()) + 170 - (ms - 0.4f) * 100);
                if (ms < 1.5) {
                    ms += 0.005;
                }
            }
            framecount += 1;
            Main.batch.end();
        }
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
