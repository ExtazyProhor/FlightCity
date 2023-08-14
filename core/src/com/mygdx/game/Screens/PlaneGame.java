package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import static com.mygdx.game.Main.*;

import com.mygdx.game.PlaneClasses.Barrier;
import com.mygdx.game.PlaneClasses.Line;
import com.mygdx.game.PlaneClasses.PlaneState;
import com.mygdx.game.PlaneClasses.Point;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;

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
    Texture blackBuildings;
    float blackBuildingsX = 0;
    Texture[] explosionFX;
    final float timeForExplosion = 0.8f;
    float time = 0;

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
            barriers[i] = new Barrier(barriers[i - 1].getColliderLevel());
        }

        explosionFX = new Texture[17];
        for(int i = 0; i < explosionFX.length; ++i){
            explosionFX[i] = new Texture(path + "explosion/a" + i + ".png");
        }
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
        if(rectanglesCollision(planeCollider,
                new Rectangle(barriersX, 0, scrY / 3, scrY * 5 / 6 - 10 * pppY * (barriers[0].getColliderLevel() + 1)),
                angle)) state = PlaneState.EXPLOSION;
        else if(rectanglesCollision(planeCollider,
                new Rectangle(barriersX + scrY, 0, scrY / 3, scrY * 5 / 6 - 10 * pppY * (barriers[1].getColliderLevel() + 1)),
                angle)) state = PlaneState.EXPLOSION;
        else if(rectanglesCollision(planeCollider,
                new Rectangle(barriersX + 2 * scrY, 0, scrY / 3, scrY * 5 / 6 - 10 * pppY * (barriers[2].getColliderLevel() + 1)),
                angle)) state = PlaneState.EXPLOSION;
    }

    public boolean rectanglesCollision(Rectangle a, Rectangle b, float angleA){
        angleA = (float)Math.toRadians(angleA);
        Point centerA = new Point(a);
        float diagonalA = (float) Math.sqrt(Math.pow(a.getSizeX(), 2) + Math.pow(a.getSizeY(), 2))  / 2;
        float originAngleA = (float)Math.atan(a.getSizeY() / a.getSizeX());

        Point a1 = new Point(centerA.x + (float)Math.cos(angleA + originAngleA) * diagonalA,
                centerA.y + (float)Math.sin(angleA + originAngleA) * diagonalA);
        Point a2 = new Point(centerA.x + (float)Math.cos(angleA - originAngleA) * diagonalA,
                centerA.y + (float)Math.sin(angleA - originAngleA) * diagonalA);
        Point a3 = new Point(centerA.x - (float)Math.cos(angleA + originAngleA) * diagonalA,
                centerA.y - (float)Math.sin(angleA + originAngleA) * diagonalA);
        Point a4 = new Point(centerA.x - (float)Math.cos(angleA - originAngleA) * diagonalA,
                centerA.y - (float)Math.sin(angleA - originAngleA) * diagonalA);

        Point b1 = new Point(b.getX() + b.getSizeX(), b.getY() + b.getSizeY());
        Point b2 = new Point(b.getX() + b.getSizeX(), b.getY());
        Point b3 = new Point(b.getX(), b.getY());
        Point b4 = new Point(b.getX(), b.getY() + b.getSizeY());

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            System.out.println("new Point(" + (int)a1.x + ", " + (int)a1.y + "),");
            System.out.println("new Point(" + (int)a2.x + ", " + (int)a2.y + "),");
            System.out.println("new Point(" + (int)a3.x + ", " + (int)a3.y + "),");
            System.out.println("new Point(" + (int)a4.x + ", " + (int)a4.y + ")");

            System.out.println("new Point(" + (int)b1.x + ", " + (int)b1.y + "),");
            System.out.println("new Point(" + (int)b2.x + ", " + (int)b2.y + "),");
            System.out.println("new Point(" + (int)b3.x + ", " + (int)b3.y + "),");
            System.out.println("new Point(" + (int)b4.x + ", " + (int)b4.y + ")");

            System.out.println();
        }

        Line[] linesA = {
                new Line(a1, a2),
                new Line(a2, a3),
                new Line(a3, a4),
                new Line(a4, a1)
        };
        Line[] linesB = {
                new Line(b1, b2),
                new Line(b2, b3),
                new Line(b3, b4),
                new Line(b4, b1)
        };

        for(int i = 0; i < 4; i++){
            for(int j  = 0; j < 4; j++){
                if (Line.linesCollision(linesA[i], linesB[j])) {
                    System.out.println("plane side " + i);
                    System.out.println("house side " + j);

                    System.out.println("new Point(" + (int)a1.x + ", " + (int)a1.y + "),");
                    System.out.println("new Point(" + (int)a2.x + ", " + (int)a2.y + "),");
                    System.out.println("new Point(" + (int)a3.x + ", " + (int)a3.y + "),");
                    System.out.println("new Point(" + (int)a4.x + ", " + (int)a4.y + ")");

                    System.out.println("new Point(" + (int)b1.x + ", " + (int)b1.y + "),");
                    System.out.println("new Point(" + (int)b2.x + ", " + (int)b2.y + "),");
                    System.out.println("new Point(" + (int)b3.x + ", " + (int)b3.y + "),");
                    System.out.println("new Point(" + (int)b4.x + ", " + (int)b4.y + ")");

                    System.out.println();
                    return true;
                }
            }
        }
        return false;
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
                    state = PlaneState.FLYING;
                }else if(exitButton.isTouched()){
                    state = PlaneState.FLYING;
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