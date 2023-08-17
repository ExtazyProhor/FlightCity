package com.mygdx.game.Screens;

import static com.mygdx.game.Main.pppX;
import static com.mygdx.game.Main.pppY;
import static com.mygdx.game.Main.scrX;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;
import com.mygdx.game.SubwayClasses.Coin;
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
    Iterator<Trash> trashIterator;
    ArrayList<Coin> coins = new ArrayList<>();
    Iterator<Coin> coinIterator;
    Player player;
    Rectangle playerhitbox;
    int randforcoin;
    boolean intrash;
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
            trashIterator = trashes.iterator();
            coinIterator = coins.iterator();
            Main.batch.begin();
            while (trashIterator.hasNext()) {
                Trash trash = trashIterator.next();
                if (!trash.isExist()) {
                    trash.dispose();
                    trashIterator.remove();
                } else {
                    trash.update();
                }
            }
            playerhitbox = new Rectangle(player.getX(), player.getY(), player.getSizeX(), player.getSizeY());
            while (coinIterator.hasNext()) {
                Coin coin = coinIterator.next();
                if (!coin.isExist()) {
                    coin.dispose();
                    coinIterator.remove();
                } else if (new Rectangle(coin.getX(), coin.getY(), coin.getSizeX(), coin.getSizeY()).overlaps(playerhitbox)){
                    coin.dispose();
                    coinIterator.remove();
                    Main.money += 1;
                    Main.savePrefs();
                } else{
                    coin.update();
                }
            }
            player.update(trashes);
            if (framecount == framelimit) {
                intrash = true;
                while (intrash) {
                    intrash = false;
                    r.nextInt(4);
                    for (Trash trash : trashes) {
                        if (new Rectangle(trash.getX(), trash.getY(), trash.getSizeX(), trash.getSizeY()).overlaps(new Rectangle(scrX, pppY * r.nextInt(4) * 20 + pppY * 10, pppY * 10, pppY * 10))) {
                            intrash = true;
                        }
                    }
                }
                coins.add(new Coin(scrX, pppY * r.nextInt(4) * 20 + pppY * 10, pppY * 10, pppY * 10, "general/coin.png", pppX * ms));
                framecount = 0;
                framelimit = (int) ((20 * Math.random()) + 101 - (ms - 0.4f) * 100);
                if (ms < 1.4) {
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
