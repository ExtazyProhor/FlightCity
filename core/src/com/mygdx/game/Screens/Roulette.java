package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.CityClasses.ShopInfo;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import static com.mygdx.game.Main.*;

import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;

import java.util.Random;

public class Roulette implements Screen {
    Main game;
    String path = "roulette/";
    PictureBox backGround;
    boolean isSpinning = false;

    PictureBox roulette;
    PictureBox pick;
    Button spinButton;
    TextBox spinCostText;

    float angle = 0;
    Random random;
    Sound tickSound;

    final float startingSpeed = 350;
    final float staticSpeed = 30;
    final float deltaSpeed = 50;

    float speed;

    public Roulette(Main game) {
        this.game = game;
        float backGroundWidth = textureAspectRatio(new Texture(path + "backGround.png"), true) * scrY;
        backGround = new PictureBox(-(backGroundWidth - scrX) / 2, 0, backGroundWidth, scrY, path + "backGround.png");
        random = new Random();
        tickSound = Gdx.audio.newSound(Gdx.files.internal(path + "tick.mp3"));

        roulette = new PictureBox(scrX / 2 - 25 * pppY, 35 * pppY, scrY / 2, scrY / 2, path + "roulette.png");
        pick = new PictureBox(scrX / 2 + 22 * pppY, 58 * pppY, 8 * pppY, 4 * pppY, path + "pick.png");
        spinButton = new Button(scrX/2 - 25 * pppY, 10 * pppY, 50 * pppY, 20 * pppY, new Texture("buttons/button 5-2.png"),
                Languages.spin[selectedLanguage] + "\n\n", 0xffffffff, (int)(5 * pppY));
        spinCostText = new TextBox(scrX / 2, 0, divisionDigits(spinCost), 0xffffffff, (int)(5 * pppY));
        spinCostText.positionToMiddleY(18 * pppY);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        backGround.draw();
        float moneyBGX = Math.min(coinText.getX(), sapphireText.getX()) - 3 * pppY;
        batch.draw(game.city.moneyBackGround, moneyBGX, 81 * pppY, 57 * pppY, 19 * pppY);
        showMoney();
        game.startMenu.buttonExit.draw();

        roulette.draw(angle);
        pick.draw();

        if(isSpinning){
            if((int)angle / 24 != (int)(angle + speed * Gdx.graphics.getDeltaTime()) / 24) tickSound.play(soundVolume * soundOn);
            angle += speed * Gdx.graphics.getDeltaTime();
            speed -= deltaSpeed * Gdx.graphics.getDeltaTime();
            if (speed <= 0) isSpinning = false;
        }else{
            spinButton.draw();
            spinCostText.draw();
            coinPicture.draw(scrX/2 + 15 * pppY, 15 * pppY);
            angle -= staticSpeed * Gdx.graphics.getDeltaTime();
        }

        batch.end();
        touchChecking();
    }

    void touchChecking(){
        if(!Gdx.input.justTouched()) return;
        if(!isSpinning && game.startMenu.buttonExit.isTouched()){
            game.setScreen(game.city);
        } else if(!isSpinning && spinButton.isTouched(false)){
            if(money < spinCost) errorSound.play(soundVolume * soundOn);
            else{
                money -= spinCost;
                updateMoney();
                savePrefs();
                if(money < Roulette.spinCost) game.roulette.spinCostText.setColor(1, 0, 0);
                else game.roulette.spinCostText.setColor(1, 1, 1);
                isSpinning = true;
                speed = startingSpeed;
                angle = random.nextInt(360);
            }
        }
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

    public static final int spinCost = 1000;
}
