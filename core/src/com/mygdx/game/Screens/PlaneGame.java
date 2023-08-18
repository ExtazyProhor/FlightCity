package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import static com.mygdx.game.Main.*;
import static com.mygdx.game.PlaneClasses.Collision.*;

import com.mygdx.game.PlaneClasses.Barrier;
import com.mygdx.game.PlaneClasses.Coin;
import com.mygdx.game.PlaneClasses.Line;
import com.mygdx.game.PlaneClasses.Point;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;
import com.mygdx.game.RealClasses.TextBox;

import java.util.Random;

public class PlaneGame implements Screen {
    //general:
    Main game;
    String path = "planeGame/";
    PlaneState state = PlaneState.FLYING;

    //select:
    int selectedPlane;
    int selectedBackGround;
    PictureBox plane;
    Texture backGround;

    //game:
    int score = 0;
    int localMoney = 0;
    int bestScore;

    //UI:
    TextBox scoreLabel;
    TextBox localMoneyLabel;
    TextBox bestScoreLabel;
    PictureBox scoreTexture;
    PictureBox localMoneyTexture;
    PictureBox bestScoreTexture;

    TextBox newBestScore;

    Texture blackBuildings;
    float blackBuildingsX = 0;
    Texture[] explosionFX;
    final float timeForExplosion = 0.8f;
    float time = 0;
    Sound[] explosionSound;
    Random rand;

    Texture[][] barriersTextures;
    Barrier[] barriers;
    Coin[] coins;
    public static Sound coinCollectSound;
    public static Texture[] coinsTextures;
    float barriersX;

    //plane info:
    float planeAspectRatio = 48f/19f;
    float angle = 0;

    final float maxAngle = 35;
    final float minAngle = -30;
    final float angleSpeed = 75;
    final float speed = 48;

    Button pauseButton;
    Button resumeButton;
    Button exitButton;
    Button restartButton;

    public PlaneGame(Main game) {
        this.game = game;
        barriersX = scrX;
        selectedPlane = prefs.getInteger("selectedPlane", 0);
        selectedBackGround = prefs.getInteger("selectedBackGround", 0);
        bestScore = prefs.getInteger("bestScore", 0);

        rand = new Random();

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

        //game:
        blackBuildings = new Texture(path + "blackBuildings.png");
        barriersTextures = new Texture[Barrier.id][Barrier.levels];
        for(int i = 0; i < Barrier.id; i++){
            for (int j = 0; j < Barrier.levels; j++) {
                barriersTextures[i][j] = new Texture(path + "barriers/barrier-" + i + "-" + j + ".png");
            }
        }
        barriers = new Barrier[4];
        coins = new Coin[4];
        barriers[0] = new Barrier();
        for(int i = 0; i < barriers.length; ++i){
            coins[i] = new Coin(barriersX + 61.66f * pppY + scrY * (i - 1), rand.nextFloat(30 * pppY) + 47 * pppY);
            coins[i].count = getMoneyCount();
            if(i == 0) continue;
            coins[i].isCollect = rand.nextInt(3) == 0;
            barriers[i] = new Barrier(barriers[i - 1].getBarrierId());
        }
        coins[0].isCollect = true;
        coinCollectSound = Gdx.audio.newSound(Gdx.files.internal(path + "coins/coin collect sound.mp3"));
        coinsTextures = new Texture[10];
        for (int i = 0; i < coinsTextures.length; i++) {
            coinsTextures[i] = new Texture(path + "coins/coin " + (i + 1) + ".png");
        }

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
    public void render(float delta) {
        batch.begin();
        float backGroundSizeX = textureAspectRatio(backGround, true) * scrY;
        batch.draw(backGround, (scrX - backGroundSizeX) / 2, 0,
                backGroundSizeX, scrY);
        batch.draw(blackBuildings, blackBuildingsX, 0, scrX,
                scrX * blackBuildings.getHeight() / blackBuildings.getWidth());
        batch.draw(blackBuildings, blackBuildingsX + scrX, 0, scrX,
                scrX * blackBuildings.getHeight() / blackBuildings.getWidth());

        for(int i = 0; i < barriers.length; i++){
            batch.draw(barriersTextures[barriers[i].getBarrierId()][barriers[i].getColliderLevel()],
                    barriersX + i * scrY, 0, scrY/3, scrY);
            coins[i].draw();
        }

        switch (state){
            case FLYING:
                blackBuildingsX -= 180 * Gdx.graphics.getDeltaTime();
                if (blackBuildingsX < -scrX) blackBuildingsX += scrX;

                float lastPosition = barriersX;
                barriersX -= speed * 0.65f * pppY * Gdx.graphics.getDeltaTime();
                for(int i = 0; i < barriers.length; i++){
                    coins[i].setCoordinates(barriersX + 61.66f * pppY + scrY * (i - 1), coins[i].getY());
                }

                if(barriersX + 25 * pppY < plane.getX() && lastPosition + 25 * pppY > plane.getX() ||
                        barriersX + 125 * pppY < plane.getX() && lastPosition + 125 * pppY > plane.getX()) {
                    score++;
                    scoreLabel.changeText(divisionDigits(score));
                    if(score > bestScore){
                        bestScoreLabel.changeText(divisionDigits(score));
                    }
                }
                if(barriersX < - scrY / 2){
                    barriersX += scrY;
                    for(int i = 0; i < 3; i++){
                        barriers[i].setValues(barriers[i + 1].getBarrierId(), barriers[i + 1].getColliderLevel());
                        coins[i].setCoordinates(coins[i + 1].getX(), coins[i + 1].getY());
                        coins[i].isCollect = coins[i + 1].isCollect;
                        coins[i].count = coins[i + 1].count;
                    }
                    barriers[3].setRandomValues(barriers[2].getBarrierId());
                    coins[3].setCoordinates(barriersX + 361.66f * pppY, rand.nextFloat(30 * pppY) + 47 * pppY);
                    coins[3].isCollect = rand.nextInt(3) == 0;
                    coins[3].count = getMoneyCount();
                }

                plane.draw(angle);
                movingPlane();
                pauseButton.draw();

                localMoneyTexture.draw();
                localMoneyLabel.draw();
                scoreTexture.draw();
                scoreLabel.draw();
                bestScoreTexture.draw();
                bestScoreLabel.draw();
                break;
            case PAUSE:
                resumeButton.draw();
                restartButton.draw();
                exitButton.draw();
                break;
            case AFTER_DEATH:
                restartButton.draw();
                exitButton.draw();
                if(score > bestScore) newBestScore.draw();
                break;
            case EXPLOSION:
                batch.draw(explosionFX[(int)(time * explosionFX.length / timeForExplosion)],
                        plane.getX() + plane.getSizeX()/2 - 10 * pppY,
                        plane.getY() + plane.getSizeY()/2 - 10 * pppY, pppY * 20, pppY * 20);
                time += Gdx.graphics.getDeltaTime();
                if(time >= timeForExplosion){
                    time = 0;
                    state = PlaneState.AFTER_DEATH;
                }
                break;
        }
        touchChecking();

        batch.end();
    }

    public int getMoneyCount(){
        int moneyCount = (score + 3) / 10;
        if(moneyCount >= 8) moneyCount = 7;
        return rand.nextInt(3) + moneyCount;
    }

    public void movingPlane(){
        if (Gdx.input.isTouched() && angle < maxAngle) {
            angle += angleSpeed * Gdx.graphics.getDeltaTime();
        }
        if ((!Gdx.input.isTouched()) && angle > minAngle) {
            angle -= angleSpeed * Gdx.graphics.getDeltaTime() / 1.7f;
        }
        plane.addCoordinates(0, (float) Math.sin(Math.toRadians(angle)) * speed * pppY * Gdx.graphics.getDeltaTime());

        Rectangle planeCollider = new Rectangle(plane.getX(), plane.getY() + plane.getSizeY() / 3,
                plane.getSizeX(), plane.getSizeY() / 3);

        float barrier0Y = 10 * pppY * (barriers[0].getColliderLevel() + 1);
        float barrier1Y = 10 * pppY * (barriers[1].getColliderLevel() + 1);

        if(isCollision(planeCollider,
                new Rectangle(barriersX, 0, scrY / 3, scrY * 5 / 6 - barrier0Y), angle)) death();
        else if(isCollision(planeCollider,
                new Rectangle(barriersX + scrY, 0, scrY / 3, scrY * 5 / 6 - barrier1Y), angle)) death();
        else if(isCollision(planeCollider,
                new Rectangle(barriersX, scrY - barrier0Y,scrY / 3, barrier0Y), angle)) death();
        else if(isCollision(planeCollider,
                new Rectangle(barriersX + scrY, scrY - barrier1Y, scrY / 3, barrier1Y), angle)) death();
        else if(isCollision(planeCollider,
                new Line(new Point(0, 0), new Point(scrX, 0)), angle)) death();
        else if(isCollision(planeCollider,
                new Line(new Point(0, scrY), new Point(scrX, scrY)), angle)) death();

        for(int i = 0; i < coins.length; i++){
            if(coins[i].collide(new Rectangle(plane.getX(), plane.getY() + plane.getSizeY() / 5,
                    plane.getSizeX(), plane.getSizeY() * 3 / 5), angle)) {
                localMoney += (coins[i].count + 1);
                money += (coins[i].count + 1);
                savePrefs();
                updateMoney();
                localMoneyLabel.changeText(divisionDigits(localMoney));
            }
        }
    }

    public void touchChecking(){
        if(!Gdx.input.justTouched()) return;
        switch (state){
            case FLYING:
                if(pauseButton.isTouched()) state = PlaneState.PAUSE;
                break;
            case PAUSE:
                if(resumeButton.isTouched()) state = PlaneState.FLYING;
            case AFTER_DEATH:
                if(restartButton.isTouched()){
                    reset();
                }else if(exitButton.isTouched()){
                    reset();
                    game.setScreen(game.city);
                }
                break;
            case EXPLOSION:
                break;
        }
    }

    public void saveSelection(){
        prefs.putInteger("selectedPlane", selectedPlane);
        prefs.putInteger("selectedBackGround", selectedBackGround);
        prefs.flush();
    }

    public void death(){
        explosionSound[rand.nextInt(explosionSound.length)].play(soundVolume * soundOn);
        if(score > bestScore) {
            prefs.putInteger("bestScore", score);
            prefs.flush();
        }
        state = PlaneState.EXPLOSION;
    }

    public void reset(){
        state = PlaneState.FLYING;
        plane.setCoordinates(20 * pppY, 44 * pppY);
        blackBuildingsX = 0;

        barriers[0].setRandomValues();
        for(int i = 1; i < 4; ++i){
            barriers[i].setRandomValues(barriers[i - 1].getBarrierId());
        }
        for(int i = 0; i < barriers.length; ++i){
            coins[i].setCoordinates(barriersX + 61.66f * pppY + scrY * (i - 1), rand.nextFloat(30 * pppY) + 47 * pppY);
            if(i == 0) continue;
            coins[i].isCollect = rand.nextInt(3) == 0;
        }
        coins[0].isCollect = true;

        barriersX = scrX;
        angle = 0;

        score = 0;
        localMoney = 0;
        scoreLabel.changeText("0");
        localMoneyLabel.changeText("0");
        bestScore = prefs.getInteger("bestScore", 0);
        bestScoreLabel.changeText(divisionDigits(bestScore));
    }

    @Override
    public void dispose() {
        plane.dispose();
        backGround.dispose();

        blackBuildings.dispose();
        for(int i = 0; i < explosionFX.length; ++i){
            explosionFX[i].dispose();
        }
        for(int i = 0; i < explosionSound.length; ++i){
            explosionSound[i].dispose();
        }

        for(int i = 0; i < Barrier.id; i++){
            for (int j = 0; j < Barrier.levels; j++) {
                barriersTextures[i][j].dispose();
            }
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
        for (int i = 0; i < coinsTextures.length; i++) {
            coinsTextures[i].dispose();
        }
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

    enum PlaneState {
        FLYING,
        EXPLOSION,
        PAUSE,
        AFTER_DEATH
    }
}