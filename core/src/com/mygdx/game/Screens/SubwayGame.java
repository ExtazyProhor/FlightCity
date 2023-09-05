package com.mygdx.game.Screens;

import static com.mygdx.game.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;
import com.mygdx.game.SubwayClasses.Coin;
import com.mygdx.game.SubwayClasses.Player;
import com.mygdx.game.SubwayClasses.Road;
import com.mygdx.game.SubwayClasses.Trash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class SubwayGame implements Screen {
    float ms;
    Random r = new Random();
    Main game;
    ArrayList<Trash> trashes = new ArrayList<>();
    ArrayList<Trash> trashesToAdd = new ArrayList<>();
    Iterator<Trash> trashIterator;
    ArrayList<Coin> coins = new ArrayList<>();
    Iterator<Coin> coinIterator;
    ArrayList<Road> roads = new ArrayList<>();
    Player player;
    Rectangle playerhitbox;
    Button pauseButton;
    Button resumeButton;
    Button restartButton;
    Button exitButton;
    TextBox scoreLabel;
    TextBox localMoneyLabel;
    TextBox bestScoreLabel;
    TextBox newBestScore;
    PictureBox scoreTexture;
    PictureBox localMoneyTexture;
    PictureBox bestScoreTexture;
    Sound[] explosionSound;
    Sound coinCollectSound;
    Texture[] explosionFX;
    final float timeForExplosion = 0.8f;
    float time = 0;
    String path = "planeGame/";
    CarState state = CarState.DRIVING;

    int rCoin;
    int rTrash;
    int rTrashLast;
    boolean isTrashSpawn;
    boolean incoin; // Находится ли камень в монете
    boolean intrash; // Находится ли монета в камне
    int framecount;
    int framelimit;
    int score = 0;
    int localMoney = 0;
    int bestScore;
    int selectedCar;

    public SubwayGame(Main game) {
        this.game = game;
        framelimit = (int) (20 * Math.random()) + 70;
        framecount = 0;
        ms = 0.4f;
        player = new Player(pppX * 15, 0, pppX * 10, pppY * 10, "subwaygame/car1.png");
        for (int i = 0; i < 4; i++) {
            roads.add(new Road(0, i * pppY * 25 + 5 * pppY, scrX, pppY * 14, "subwaygame/road.png", ms * pppX));
            roads.add(new Road(scrX, i * pppY * 25 + 5 * pppY, scrX, pppY * 14, "subwaygame/road.png", ms * pppX));
        }
        selectedCar = prefs.getInteger("selectedCar", 0);
        bestScore = prefs.getInteger("bestScoreCar", 0);
        //UI:
        float uiSizeY = 4.5f * pppX;
        scoreLabel = new TextBox(scrX - 6.5f * uiSizeY, 0, "0", 0xffffffff, (int)(4 * pppY));
        localMoneyLabel = new TextBox(scrX - 10.5f * uiSizeY, 0, "0", 0xffffffff, (int)(4 * pppY));
        bestScoreLabel = new TextBox(scrX - 2.5f * uiSizeY, 0, divisionDigits(bestScore), 0xffffffff, (int)(4 * pppY));
        scoreLabel.positionToMiddleY(scrY - uiSizeY / 2);
        localMoneyLabel.positionToMiddleY(scrY - uiSizeY / 2);
        bestScoreLabel.positionToMiddleY(scrY - uiSizeY / 2);
        scoreTexture = new PictureBox(scrX - 8 * uiSizeY, scrY - uiSizeY, 4 * uiSizeY, uiSizeY, path + "score.png");
        localMoneyTexture = new PictureBox(scrX - 12 * uiSizeY, scrY - uiSizeY, 4 * uiSizeY, uiSizeY, path + "local money.png");
        bestScoreTexture = new PictureBox(scrX - 4 * uiSizeY, scrY - uiSizeY, 4 * uiSizeY, uiSizeY, path + "best score.png");

        parameter.borderWidth = (int)(pppY * 0.7);
        newBestScore = new TextBox(scrX / 2, 80 * pppY, Languages.newBestScore[selectedLanguage], 0xffffffff, (int)(10 * pppY));
        parameter.borderWidth = 3;

        // buttons:
        pauseButton = new Button(3 * pppY, 82 * pppY, 15 * pppY, 15 * pppY, new Texture("buttons/pauseButton.png"));
        resumeButton = new Button(0, 65 * pppY, 50 * pppY, 20 * pppY, new Texture("buttons/button 5-2.png"),
                Languages.resume[selectedLanguage], 0xffffffff, (int) (4 * pppY));
        resumeButton.placeCenter();
        restartButton = new Button(0, 40 * pppY, 50 * pppY, 20 * pppY, new Texture("buttons/button 5-2.png"),
                Languages.restart[selectedLanguage], 0xffffffff, (int) (4 * pppY));
        restartButton.placeCenter();
        exitButton = new Button(0, 15 * pppY, 50 * pppY, 20 * pppY, new Texture("buttons/button 5-2.png"),
                Languages.exit[selectedLanguage], 0xffffffff, (int) (4 * pppY));
        exitButton.placeCenter();

        coinCollectSound = Gdx.audio.newSound(Gdx.files.internal(path + "coins/coin collect sound.mp3"));

        explosionFX = new Texture[17];
        for(int i = 0; i < explosionFX.length; ++i){
            explosionFX[i] = new Texture(path + "explosion/a" + i + ".png");
        }
        explosionSound = new Sound[3];
        for(int i = 0; i < explosionSound.length; ++i){
            explosionSound[i] = Gdx.audio.newSound(Gdx.files.internal(path + "explosion/boom-" + i + ".mp3"));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(!planeMusic[musicIndex].isPlaying()){
            musicIndex = (musicIndex + 1) % planeMusic.length;
            planeMusic[musicIndex].play();
        }
        batch.begin();
        switch (state){
            case DRIVING:
                ScreenUtils.clear(0.22f, 0.6f, 0.18f, 1);
                isTrashSpawn = true;
                trashIterator = trashes.iterator();
                coinIterator = coins.iterator();
                for (Road road : roads) {
                    road.update(ms * pppX);
                }
                while (trashIterator.hasNext()) {
                    Trash trash = trashIterator.next();
                    if (trash.getX() > scrX / 2) {
                        isTrashSpawn = false;
                    }
                    if (!trash.isExist()) {
                        trashIterator.remove();
                    } else {
                        trash.update(ms * pppX);
                    }
                }
                playerhitbox = new Rectangle(player.getX(), player.getY(), player.getSizeX(), player.getSizeY());
                while (coinIterator.hasNext()) {
                    Coin coin = coinIterator.next();
                    if (!coin.isExist()) {
                        coinIterator.remove();
                    } else if (new Rectangle(coin.getX(), coin.getY(), coin.getSizeX(), coin.getSizeY()).overlaps(playerhitbox)) {
                        coinIterator.remove();
                        localMoney += 1;
                        Main.money += 1;
                        savePrefs();
                        updateMoney();
                        localMoneyLabel.changeText(divisionDigits(localMoney));
                    } else {
                        coin.update(ms * pppX);
                    }
                }
                player.update(trashes);
                if (isTrashSpawn) {
                    incoin = false;
                    if (trashesToAdd.size() < 1) {
                        rTrash = r.nextInt(6);
                        if (rTrashLast != rTrash) {
                            if (rTrash == 0) {
                                trashesToAdd.addAll(Arrays.asList(new Trash(scrX * 1.1f, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.45f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.35f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.7f, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.7f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms)));
                            } else if (rTrash == 1) {
                                trashesToAdd.addAll(Arrays.asList(new Trash(scrX, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.25f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.5f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.75f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.25f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.5f, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms)));
                            } else if (rTrash == 2) {
                                trashesToAdd.addAll(Arrays.asList(new Trash(scrX, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.25f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.5f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.75f, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.25f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.5f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms)));
                            } else if (rTrash == 3) {
                                trashesToAdd.addAll(Arrays.asList(new Trash(scrX, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.4f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.4f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.4f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.8f, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.8f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.8f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms)));
                            } else if (rTrash == 4) {
                                trashesToAdd.addAll(Arrays.asList(new Trash(scrX, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.4f, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.4f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.4f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.8f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.8f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.8f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms)));
                            } else if (rTrash == 5) {
                                trashesToAdd.addAll(Arrays.asList(new Trash(scrX, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.45f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.45f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.9f, pppY * 0 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 1.9f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.35f, pppY * 3 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.35f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.8f, pppY * 1 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms), new Trash(scrX * 2.8f, pppY * 2 * 25 + pppY * 5, pppY * 20, pppY * 20,  pppX * ms)));

                            }
                        }
                    }
                    for (Trash trash : trashesToAdd) {
                        for (Coin coin : coins) {
                            if (new Rectangle(trash.getX(), trash.getY(), trash.getSizeX(), trash.getSizeY()).overlaps(new Rectangle(coin.getX(), coin.getY(), coin.getSizeX(), coin.getSizeY()))) {
                                incoin = true;
                                break;
                            }
                        }
                    }
                    if (!incoin) {
                        trashes.addAll(trashesToAdd);
                        trashesToAdd.clear();
                        rTrashLast = rTrash;
                        score += 1;
                        scoreLabel.changeText(divisionDigits(score));
                        if(score > bestScore){
                            bestScoreLabel.changeText(divisionDigits(score));
                        }
                    }
                }
                if (framecount == framelimit) {
                    intrash = true;
                    while (intrash) {
                        intrash = false;
                        rCoin = r.nextInt(4);
                        for (Trash trash : trashes) {
                            if (new Rectangle(trash.getX(), trash.getY(), trash.getSizeX(), trash.getSizeY()).overlaps(new Rectangle(scrX, pppY * rCoin * 20 + pppY * 10, pppY * 10, pppY * 10))) {
                                intrash = true;
                            }
                        }
                    }
                    coins.add(new Coin(scrX, pppY * rCoin * 25 + pppY * 7, pppY * 10, pppY * 10, pppX * ms));
                    framecount = 0;
                    //framelimit = (int) ((20 * Math.random()) + 101 - (ms - 0.4f) * 100);
                    /*if (ms < 1.4) {
                        ms += 0.005;
                    }*/
                }
                framecount += 1;
                pauseButton.draw();
                localMoneyTexture.draw();
                localMoneyLabel.draw();
                scoreTexture.draw();
                scoreLabel.draw();
                bestScoreTexture.draw();
                bestScoreLabel.draw();
                if(!player.isExist()){
                    if(score > bestScore) {
                        prefs.putInteger("bestScoreCar", score);
                        prefs.flush();
                    }
                    state = CarState.EXPLOSION;
                }
                break;
            case PAUSE:
                resumeButton.draw();
                restartButton.draw();
                exitButton.draw();
                break;
            case AFTER_DEATH:
                restartButton.draw();
                exitButton.draw();
                if(score > bestScore) {
                    newBestScore.draw();
                }
                break;
            case EXPLOSION:
                batch.draw(explosionFX[(int)(time * explosionFX.length / timeForExplosion)],
                        player.getX() + player.getSizeX()/2 - 10 * pppY,
                        player.getY() + player.getSizeY()/2 - 10 * pppY, pppY * 20, pppY * 20);
                time += Gdx.graphics.getDeltaTime();
                if(time >= timeForExplosion){
                    time = 0;
                    state = CarState.AFTER_DEATH;
                }
                break;
        }
        touchChecking();
        Main.batch.end();
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
        player.dispose();
        for (Texture fx : explosionFX) {
            fx.dispose();
        }
        for (Sound sound : explosionSound) {
            sound.dispose();
        }
        pauseButton.dispose();
        resumeButton.dispose();
        exitButton.dispose();
        restartButton.dispose();

        scoreLabel.dispose();
        localMoneyLabel.dispose();
        bestScoreLabel.dispose();
        scoreTexture.dispose();
        localMoneyTexture.dispose();
        bestScoreTexture.dispose();
        newBestScore.dispose();
        coinCollectSound.dispose();
    }
    public void touchChecking(){
        if(!Gdx.input.justTouched()) return;
        switch (state){
            case DRIVING:
                if(pauseButton.isTouched()) state = CarState.PAUSE;
                break;
            case PAUSE:
                if(resumeButton.isTouched()) state = CarState.DRIVING;
            case AFTER_DEATH:
                if(restartButton.isTouched()){
                    reset();
                }else if(exitButton.isTouched()){
                    reset();
                    planeMusic[musicIndex].stop();
                    cityMusic.play();
                    game.setScreen(game.city);
                }
                break;
            case EXPLOSION:
                break;
        }
    }
    public void reset(){
        state = CarState.DRIVING;
        framelimit = (int) (20 * Math.random()) + 70;
        framecount = 0;
        ms = 0.4f;
        player.exist = true;
        score = 0;
        localMoney = 0;
        scoreLabel.changeText("0");
        localMoneyLabel.changeText("0");
        bestScore = prefs.getInteger("bestScoreCar", 0);
        bestScoreLabel.changeText(divisionDigits(bestScore));
        trashes.clear();
        trashesToAdd.clear();
        coins.clear();
        for (Road road: roads) {
            road.dispose();
        }
        roads.clear();
        for (int i = 0; i < 4; i++) {
            roads.add(new Road(0, i * pppY * 25 + 5 * pppY, scrX, pppY * 14, "subwaygame/road.png", ms * pppX));
            roads.add(new Road(scrX, i * pppY * 25 + 5 * pppY, scrX, pppY * 14, "subwaygame/road.png", ms * pppX));
        }
    }
    enum CarState {
        DRIVING,
        EXPLOSION,
        PAUSE,
        AFTER_DEATH
    }
}
