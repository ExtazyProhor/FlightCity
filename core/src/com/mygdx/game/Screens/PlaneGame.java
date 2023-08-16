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
import com.mygdx.game.PlaneClasses.Line;
import com.mygdx.game.PlaneClasses.PlaneState;
import com.mygdx.game.PlaneClasses.Point;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;

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
    Texture blackBuildings;
    float blackBuildingsX = 0;
    Texture[] explosionFX;
    final float timeForExplosion = 0.8f;
    float time = 0;
    Sound explosionSound;

    Texture[][] barriersTextures;
    Barrier[] barriers;
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
        barriers[0] = new Barrier();
        for(int i = 1; i < 4; ++i){
            barriers[i] = new Barrier(barriers[i - 1].getBarrierId());
        }

        explosionFX = new Texture[17];
        for(int i = 0; i < explosionFX.length; ++i){
            explosionFX[i] = new Texture(path + "explosion/a" + i + ".png");
        }
        explosionSound = Gdx.audio.newSound(Gdx.files.internal(path + "explosion/boom1.mp3"));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(backGround, (scrX - backGround.getWidth()) / 2, 0,
                backGround.getWidth() * scrY / backGround.getHeight(), scrY);
        batch.draw(blackBuildings, blackBuildingsX, 0, scrX,
                scrX * blackBuildings.getHeight() / blackBuildings.getWidth());
        batch.draw(blackBuildings, blackBuildingsX + scrX, 0, scrX,
                scrX * blackBuildings.getHeight() / blackBuildings.getWidth());

        for(int i = 0; i < 4; i++){
            batch.draw(barriersTextures[barriers[i].getBarrierId()][barriers[i].getColliderLevel()],
                    barriersX + i * scrY, 0, scrY/3, scrY);
        }

        switch (state){
            case FLYING:
                blackBuildingsX -= 180 * Gdx.graphics.getDeltaTime();
                if (blackBuildingsX < -scrX) blackBuildingsX += scrX;

                barriersX -= speed * 0.65f * pppY * Gdx.graphics.getDeltaTime();
                if(barriersX < - scrY / 2){
                    barriersX += scrY;
                    for(int i = 0; i < 3; i++){
                        barriers[i].setValues(barriers[i + 1].getBarrierId(), barriers[i + 1].getColliderLevel());
                    }
                    barriers[3].setRandomValues(barriers[2].getColliderLevel());
                }

                plane.draw(angle);
                movingPlane();
                pauseButton.draw();
                break;
            case PAUSE:
                resumeButton.draw();
            case AFTER_DEATH:
                restartButton.draw();
                exitButton.draw();
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
        explosionSound.play(soundVolume * soundOn);
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

        barriersX = scrX;
        angle = 0;
    }

    @Override
    public void dispose() {
        plane.dispose();
        backGround.dispose();

        blackBuildings.dispose();
        for(int i = 0; i < explosionFX.length; ++i){
            explosionFX[i].dispose();
        }
        explosionSound.dispose();

        for(int i = 0; i < Barrier.id; i++){
            for (int j = 0; j < Barrier.levels; j++) {
                barriersTextures[i][j].dispose();
            }
        }

        pauseButton.dispose();
        resumeButton.dispose();
        exitButton.dispose();
        restartButton.dispose();
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