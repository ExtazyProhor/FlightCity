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
    RouletteState state = RouletteState.NONE;

    int rouletteIndex = 0;
    Button nextButton;
    Button previousButton;
    final int rouletteQuantity = 2;
    PictureBox[] roulette;
    PictureBox pick;
    Button spinButton;
    TextBox spinCostText;

    TextBox prizeText;
    float planeSizeX;

    float angle = 0;
    int prizeIndex;
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

        nextButton = new Button(scrX - 40 * pppY, 10 * pppY, 20 * pppY, 20 * pppY, new Texture(path + "arrow.png"));
        previousButton = new Button(20 * pppY, 10 * pppY, 20 * pppY, 20 * pppY, new Texture(path + "arrow.png"));

        roulette = new PictureBox[rouletteQuantity];
        for(int i = 0; i < rouletteQuantity; ++i){
            roulette[i] = new PictureBox(scrX / 2 - 25 * pppY, 35 * pppY, scrY / 2, scrY / 2, path + "roulette " + i + ".png");
        }
        pick = new PictureBox(scrX / 2 + 22 * pppY, 58 * pppY, 8 * pppY, 4 * pppY, path + "pick.png");
        spinButton = new Button(scrX/2 - 25 * pppY, 10 * pppY, 50 * pppY, 20 * pppY, new Texture("buttons/button 5-2.png"),
                Languages.spin[selectedLanguage] + "\n\n", 0xffffffff, (int)(5 * pppY));
        spinCostText = new TextBox(scrX / 2, 0, divisionDigits(spinCost[rouletteIndex]), 0xffffffff, (int)(5 * pppY));
        spinCostText.positionToMiddleY(18 * pppY);

        prizeText = new TextBox(scrX/2, scrY/2 + pppX * 11.5f, "", 0xffffffff, (int)(5 * pppY));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        backGround.draw();
        float moneyBGX = Math.min(coinText.getX(), sapphireText.getX()) - 3 * pppY;
        batch.draw(game.city.moneyBackGround, moneyBGX, 81 * pppY, 57 * pppY, 19 * pppY);
        showMoney();
        game.startMenu.buttonExit.draw();

        roulette[rouletteIndex].draw(angle);
        pick.draw();

        switch (state){
            case NONE:

                if(rouletteIndex > 0){
                    previousButton.draw(0, true, false);
                    if(Gdx.input.justTouched()){
                        if(previousButton.isTouched()) {
                            rouletteIndex--;
                            spinCostText.changeText(divisionDigits(spinCost[rouletteIndex]));
                            spinCostText.setColor(1, 1, 1);
                            switch (currencyType[rouletteIndex]){
                                case "coin":
                                    if(money < spinCost[rouletteIndex]) spinCostText.setColor(1, 0, 0);
                                    break;
                                case "sapphire":
                                    if(sapphires < spinCost[rouletteIndex]) spinCostText.setColor(1, 0, 0);
                                    break;
                            }
                        }
                    }
                }
                if(rouletteIndex < rouletteQuantity - 1){
                    nextButton.draw();
                    if(Gdx.input.justTouched()){
                        if(nextButton.isTouched()) {
                            rouletteIndex++;
                            spinCostText.changeText(divisionDigits(spinCost[rouletteIndex]));
                        }
                        spinCostText.setColor(1, 1, 1);
                        switch (currencyType[rouletteIndex]){
                            case "coin":
                                if(money < spinCost[rouletteIndex]) spinCostText.setColor(1, 0, 0);
                                break;
                            case "sapphire":
                                if(sapphires < spinCost[rouletteIndex]) spinCostText.setColor(1, 0, 0);
                                break;
                        }
                    }
                }

                spinButton.draw();
                spinCostText.draw();
                switch (currencyType[rouletteIndex]){
                    case "coin":
                        coinPicture.draw(scrX/2 + 15 * pppY, 15 * pppY);
                        break;
                    case "sapphire":
                        sapphirePicture.draw(scrX/2 + 15 * pppY, 15 * pppY);
                        break;
                }
                angle -= staticSpeed * Gdx.graphics.getDeltaTime();

                break;
            case ROLLING:
                if((int)angle / 24 != (int)(angle + speed * Gdx.graphics.getDeltaTime()) / 24) tickSound.play(soundVolume * soundOn);
                angle += speed * Gdx.graphics.getDeltaTime();
                speed -= deltaSpeed * Gdx.graphics.getDeltaTime();
                prizeIndex = ((int)angle % 360) / 24;
                if (speed <= 0) state = RouletteState.GET_PRIZE;

                break;
            case GET_PRIZE:
                getPrize();
                state = RouletteState.WINDOW;

                break;
            case WINDOW:
                game.city.blackout.draw();
                game.city.windowBackGround.draw();
                game.city.cancelButton.draw();
                prizeText.draw();
                drawPrize();
                break;
        }

        batch.end();
        touchChecking();
    }

    void touchChecking(){
        if(!Gdx.input.justTouched()) return;

        switch (state){
            case NONE:
                if(game.startMenu.buttonExit.isTouched()){
                    game.setScreen(game.city);
                    rouletteIndex = 0;
                    spinCostText.changeText(divisionDigits(spinCost[rouletteIndex]));
                }else if(spinButton.isTouched(false)){
                    switch (currencyType[rouletteIndex]){
                        case "coin":
                            if(money < spinCost[rouletteIndex]) {
                                errorSound.play(soundVolume * soundOn);
                            } else{
                                money -= spinCost[rouletteIndex];
                                updateMoney();
                                if(money < spinCost[rouletteIndex]) game.roulette.spinCostText.setColor(1, 0, 0);
                                else game.roulette.spinCostText.setColor(1, 1, 1);
                                state = RouletteState.ROLLING;
                                speed = startingSpeed;
                                angle = random.nextInt(360);
                            }
                            break;
                        case "sapphire":
                            if(sapphires < spinCost[rouletteIndex]) {
                                errorSound.play(soundVolume * soundOn);
                            } else{
                                sapphires -= spinCost[rouletteIndex];
                                updateMoney();
                                if(sapphires < spinCost[rouletteIndex]) game.roulette.spinCostText.setColor(1, 0, 0);
                                else game.roulette.spinCostText.setColor(1, 1, 1);
                                state = RouletteState.ROLLING;
                                speed = startingSpeed;
                                angle = random.nextInt(360);
                            }
                            break;
                    }
                }
                break;
            case WINDOW:
                if(game.city.cancelButton.isTouched()) state = RouletteState.NONE;
                break;
            default:
                break;
        }
    }

    void getPrize(){
        upgradeSound.play(soundVolume * soundOn);
        switch (rouletteIndex){
            case 0:
                switch (prizeIndex){
                    case 0:
                    case 10:
                        money += 5000;
                        prizeText.changeText("+ 5.000");
                        break;
                    case 1:
                        sapphires += 10;
                        prizeText.changeText("+ 10");
                        break;
                    case 2:
                    case 12:
                        money += 1000;
                        prizeText.changeText("+ 1.000");
                        break;
                    case 3:
                        prizeText.changeText("+ 50");
                        sapphires += 50;
                        break;
                    case 4:
                        prizeText.changeText("+ 500");
                        money += 500;
                        break;
                    case 5:
                        prizeText.changeText("+ 15");
                        sapphires += 15;
                        break;
                    case 6:
                        prizeText.changeText("+ 10.000");
                        money += 10000;
                        break;
                    case 7:
                        prizeText.changeText("");
                        break;
                    case 8:
                        prizeText.changeText("+ 200");
                        money += 200;
                        break;
                    case 9:
                        prizeText.changeText("+ 100");
                        sapphires += 100;
                        break;
                    case 11:
                        prizeText.changeText("+ 5");
                        sapphires += 5;
                        break;
                    case 13:
                        prizeText.changeText("+ 25");
                        sapphires += 25;
                        break;
                    case 14:
                        prizeText.changeText("+ 100");
                        money += 100;
                        break;
                }
                break;
            case 1:
                prizeText.changeText("");

                if(prizeIndex < 9){
                    game.inventory.backGrounds[prizeIndex + 1]++;
                } else{
                    game.inventory.planes[prizeIndex - 8]++;
                    planeSizeX = game.planeGame.planeAspectRatio * 30 * pppY;
                }

                break;
        }
        game.inventory.saveInventory();
        updateMoney();
        savePrefs();
        spinCostText.setColor(1, 1, 1);
        switch (currencyType[rouletteIndex]){
            case "coin":
                if(money < spinCost[rouletteIndex]) spinCostText.setColor(1, 0, 0);
                break;
            case "sapphire":
                if(sapphires < spinCost[rouletteIndex]) spinCostText.setColor(1, 0, 0);
                break;
        }
    }

    void drawPrize(){
        switch (rouletteIndex){
            case 0:
                switch (prizeIndex){
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                    case 8:
                    case 10:
                    case 12:
                    case 14:
                        coinPicture.draw(scrX / 2 - 15 * pppY, pppY * 30, 30 * pppY, 30 * pppY);
                        break;
                    case 1:
                    case 3:
                    case 5:
                    case 9:
                    case 11:
                    case 13:
                        sapphirePicture.draw(scrX / 2 - 15 * pppY, pppY * 30, 30 * pppY, 30 * pppY);
                        break;
                    case 7:

                        break;
                }
                break;
            case 1:
                if(prizeIndex < 9){
                    batch.draw(game.inventory.backGroundsTextures[prizeIndex + 1], scrX/2 - 30 * pppY, 35 * pppY, 60 * pppY, 30 * pppY);
                } else{
                    batch.draw(game.inventory.planesTextures[prizeIndex - 8], scrX/2 - planeSizeX/2, 35 * pppY, planeSizeX, 30 * pppY);
                }
                break;
        }
    }

    @Override
    public void dispose() {
        backGround.dispose();

        nextButton.dispose();
        previousButton.dispose();
        for(int i = 0; i < roulette.length; ++i){
            roulette[i].dispose();
        }
        pick.dispose();
        spinButton.dispose();
        spinCostText.dispose();

        prizeText.dispose();

        tickSound.dispose();
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

    public static final int[] spinCost = {
            2000,
            1000
    };
    public static final String[] currencyType = {
            "coin",
            "sapphire"
    };

    enum RouletteState {
        NONE,
        ROLLING,
        WINDOW,
        GET_PRIZE
    }
}
