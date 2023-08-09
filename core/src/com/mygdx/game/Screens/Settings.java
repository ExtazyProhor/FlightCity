package com.mygdx.game.Screens;

import static com.mygdx.game.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Languages;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;

public class Settings implements Screen {
    Main game;
    final String path = "settings/";

    int localSelectedLanguage;
    float localSoundVolume;
    float localMusicVolume;
    int localSoundOn;
    int localMusicOn;

    float positionY = 0;
    float touchPosition;
    float touchPlace;
    boolean touchToSwipe = false;
    boolean touchSound = false;
    boolean touchMusic = false;

    TextBox languageLabel;
    TextBox soundsLabel;
    TextBox musicLabel;
    TextBox englishLabel;
    TextBox russianLabel;
    TextBox belorussianLabel;

    PictureBox settingsBackGround;
    PictureBox frame;
    PictureBox tick;
    PictureBox emptyString;
    PictureBox fullString;

    Button saveButton;

    Button flagEngland;
    Button flagRussian;
    Button flagBelorussian;

    Button musicPoint;
    Button soundPoint;
    Button musicBar;
    Button soundBar;

    public Settings(Main game) {
        this.game = game;

        // labels
        languageLabel = new TextBox(scrX / 2, pppY * 97, Languages.language[selectedLanguage],
                0xffffffff, (int) (5 * pppY));
        englishLabel = new TextBox(29 * pppX, 87 * pppY - 9 * pppX, "english",
                0xffffffff, (int) (3 * pppY));
        russianLabel = new TextBox(50 * pppX, 87 * pppY - 9 * pppX, "русский",
                0xffffffff, (int) (3 * pppY));
        belorussianLabel = new TextBox(71 * pppX, 87 * pppY - 9 * pppX, "беларуская",
                0xffffffff, (int) (3 * pppY));
        soundsLabel = new TextBox(scrX/2, 80 * pppY - 9 * pppX, Languages.sound[selectedLanguage],
                0xffffffff, (int) (5 * pppY));
        musicLabel = new TextBox(scrX/2, 60 * pppY - 9 * pppX, Languages.music[selectedLanguage],
                0xffffffff, (int) (5 * pppY));

        // pictures
        settingsBackGround = new PictureBox(19 * pppX, 0, 62 * pppX, scrY,
                path + "settingsBackGround.png");
        frame = new PictureBox(0, 0, 18 * pppX, 9 * pppX,
                path + "frame.png");
        tick = new PictureBox(23 * pppX, 45 * pppY - 9 * pppX, 5 * pppX, 5 * pppX,
                path + "tick.png");
        emptyString = new PictureBox(31 * pppX, 45 * pppY - 7 * pppX, 46 * pppX, pppX,
                path + "emptyString.png");
        fullString = new PictureBox(31 * pppX, 45 * pppY - 7 * pppX, 46 * pppX, pppX,
                path + "fullString.png");

        // buttons
        saveButton = new Button(81.5f * pppX, 97 * pppY - 10 * pppX, 18 * pppX, 10 * pppX,
                new Texture("buttons/button 3-2.png"), Languages.save[selectedLanguage],
                0xffffffff, (int) (3 * pppY));
        flagEngland = new Button(20 * pppX, 90 * pppY - 9 * pppX, 18 * pppX, 9 * pppX,
                new Texture(path + "flagEngland.png"));
        flagRussian = new Button(41 * pppX, 90 * pppY - 9 * pppX, 18 * pppX, 9 * pppX,
                new Texture(path + "flagRussian.png"));
        flagBelorussian = new Button(62 * pppX, 90 * pppY - 9 * pppX, 18 * pppX, 9 * pppX,
                new Texture(path + "flagBelorussian.png"));

        soundBar = new Button(23 * pppX, 65 * pppY - 9 * pppX, 5 * pppX, 5 * pppX,
                new Texture(path + "bar.png"));
        musicBar = new Button(23 * pppX, 45 * pppY - 9 * pppX, 5 * pppX, 5 * pppX,
                new Texture(path + "bar.png"));

        soundPoint = new Button(30 * pppX, 45 * pppY - 8 * pppX, 3* pppX, 3 * pppX,
                new Texture(path + "point.png"));
        musicPoint = new Button(30 * pppX, 65 * pppY - 8 * pppX, 3* pppX, 3 * pppX,
                new Texture(path + "point.png"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        batch.draw(game.startMenu.menuBackGround, game.startMenu.backGroundCoordinateX, 0,
                game.startMenu.backGroundWidth, scrY);
        settingsBackGround.draw();
        game.startMenu.buttonExit.draw();
        saveButton.draw();

        showLanguages();
        showSoundsAndMusic();

        batch.end();

        swipeRender();
        soundRender();
        musicRender();
        buttonsCheck();
    }

    void showLanguages() {
        languageLabel.draw(languageLabel.getX(), languageLabel.getY() + positionY);
        flagEngland.draw();
        flagRussian.draw();
        flagBelorussian.draw();

        switch (localSelectedLanguage) {
            case 0:
                frame.draw(flagEngland.getX(), flagEngland.getY());
                break;
            case 1:
                frame.draw(flagRussian.getX(), flagRussian.getY());
                break;
            case 2:
                frame.draw(flagBelorussian.getX(), flagBelorussian.getY());
                break;
        }

        if (localSelectedLanguage==0) englishLabel.setColor(1, 1, 0);
        else englishLabel.setColor(1, 1, 1);
        if (localSelectedLanguage==1) russianLabel.setColor(1, 1, 0);
        else russianLabel.setColor(1, 1, 1);
        if (localSelectedLanguage==2) belorussianLabel.setColor(1, 1, 0);
        else belorussianLabel.setColor(1, 1, 1);

        englishLabel.draw(englishLabel.getX(), englishLabel.getY() + positionY);
        russianLabel.draw(russianLabel.getX(), russianLabel.getY() + positionY);
        belorussianLabel.draw(belorussianLabel.getX(), belorussianLabel.getY() + positionY);
    }

    void showSoundsAndMusic(){
        soundPoint.setX(30 * pppX + 46 * pppX * localSoundVolume);
        soundPoint.setY(65 * pppY - 8 * pppX + positionY);
        musicPoint.setX(30 * pppX + 46 * pppX * localMusicVolume);
        musicPoint.setY(45 * pppY - 8 * pppX + positionY);

        soundsLabel.draw(soundsLabel.getX(), soundsLabel.getY() + positionY);
        musicLabel.draw(musicLabel.getX(), musicLabel.getY() + positionY);

        soundBar.draw();
        musicBar.draw();
        if (localSoundOn == 1) {
            tick.draw(tick.getX(), tick.getY() + 20 * pppY + positionY);
        }
        if (localMusicOn == 1) {
            tick.draw(tick.getX(), tick.getY() + positionY);
        }

        emptyString.draw(emptyString.getX(), emptyString.getY() + positionY);
        emptyString.draw(emptyString.getX(), emptyString.getY() + 20* pppY + positionY);
        fullString.draw(fullString.getX(), fullString.getY() + positionY, fullString.getSizeX() * localMusicVolume,
                fullString.getSizeY(), 0, 0, (int)(localMusicVolume * 300), 6, false, false);
        fullString.draw(fullString.getX(), fullString.getY() + 20* pppY+ positionY, fullString.getSizeX() * localSoundVolume,
                fullString.getSizeY(), 0, 0, (int)(localSoundVolume * 300), 6, false, false);

        soundPoint.draw();
        musicPoint.draw();
    }

    void swipeRender(){
        if (Gdx.input.justTouched() && (Gdx.input.getX() < pppX * 20 || Gdx.input.getX() > pppX * 80)) {
            touchPlace = Gdx.input.getY();
            touchPosition = positionY;
            touchToSwipe = true;
        }
        if (Gdx.input.isTouched() && touchToSwipe) {
            positionY = touchPosition - Gdx.input.getY() + touchPlace;
            if (positionY < 0) positionY = 0;

            flagEngland.setY(90 * pppY - 9 * pppX + positionY);
            flagRussian.setY(90 * pppY - 9 * pppX + positionY);
            flagBelorussian.setY(90 * pppY - 9 * pppX + positionY);
            soundBar.setY(65 * pppY - 9 * pppX + positionY);
            musicBar.setY(45 * pppY - 9 * pppX + positionY);
        } else {
            touchToSwipe = false;
        }
    }

    void soundRender(){
        if(Gdx.input.justTouched() && Gdx.input.getX() > 30 * pppX && Gdx.input.getX() < 78 * pppX
                && scrY - Gdx.input.getY() > 65 * pppY - 9 * pppX + positionY
                && scrY - Gdx.input.getY() < 65 * pppY - 4 * pppX + positionY){
            touchSound = true;
        }
        if(Gdx.input.isTouched() && touchSound){
            localSoundVolume = ((Gdx.input.getX() - 1.5f * pppX) * 100f / scrX - 30f) / 46f;
            if(localSoundVolume < 0) localSoundVolume = 0;
            if(localSoundVolume > 1) localSoundVolume = 1;
        }else{
            touchSound = false;
        }
    }

    void musicRender(){
        if(Gdx.input.justTouched() && Gdx.input.getX() > 30 * pppX && Gdx.input.getX() < 78 * pppX
                && scrY - Gdx.input.getY() > 45 * pppY - 9 * pppX + positionY
                && scrY - Gdx.input.getY() < 45 * pppY - 4 * pppX + positionY){
            touchMusic = true;
        }
        if(Gdx.input.isTouched() && touchMusic){
            localMusicVolume = ((Gdx.input.getX() - 1.5f * pppX) * 100f / scrX - 30f) / 46f;
            if(localMusicVolume < 0) localMusicVolume = 0;
            if(localMusicVolume > 1) localMusicVolume = 1;
        }else{
            touchMusic = false;
        }
    }

    void buttonsCheck(){
        if (Gdx.input.justTouched()) {
            if (game.startMenu.buttonExit.isTouched()) {
                positionY = 0;
                touchToSwipe = false;
                flagEngland.setY(90 * pppY - 9 * pppX);
                flagRussian.setY(90 * pppY - 9 * pppX);
                flagBelorussian.setY(90 * pppY - 9 * pppX);
                soundBar.setY(65 * pppY - 9 * pppX);
                musicBar.setY(45 * pppY - 9 * pppX);
                game.startMenu.buttonPlay.changeText(Languages.play[selectedLanguage]);
                game.startMenu.buttonSettings.changeText(Languages.settings[selectedLanguage]);
                game.setScreen(game.startMenu);
            } else if (flagEngland.isTouched()) {
                localSelectedLanguage = 0;
            } else if (flagRussian.isTouched()) {
                localSelectedLanguage = 1;
            } else if (flagBelorussian.isTouched()) {
                localSelectedLanguage = 2;
            } else if (saveButton.isTouched(false)) {
                clickSound.play(localSoundVolume * localSoundOn);
                selectedLanguage = localSelectedLanguage;
                soundVolume = localSoundVolume;
                musicVolume = localMusicVolume;
                soundOn = localSoundOn;
                musicOn = localMusicOn;

                savePrefs();

                languageLabel.changeText(Languages.language[selectedLanguage]);
                soundsLabel.changeText(Languages.sound[selectedLanguage]);
                musicLabel.changeText(Languages.music[selectedLanguage]);
                saveButton.changeText(Languages.save[selectedLanguage]);

                game.city.sellButton.changeText(Languages.sell[selectedLanguage] + "\n\n");
                game.city.upgradeButton.changeText(Languages.upgrade[selectedLanguage] + "\n\n");
                game.city.nonUpgradeText.changeText(Languages.maxLevel[selectedLanguage]);

                game.shop.shopText.changeText(Languages.shop[selectedLanguage]);
                game.shop.housesTextShop.changeText(Languages.houses[selectedLanguage]);
                game.shop.territoryTextShop.changeText(Languages.territory[selectedLanguage]);
                game.shop.coinsTextShop.changeText(Languages.coins[selectedLanguage]);
                game.shop.maxLevel.changeText(Languages.maxLevel[selectedLanguage]);

            } else if (soundBar.isTouched()) {
                localSoundOn = Math.abs(localSoundOn - 1);
            } else if (musicBar.isTouched()) {
                localMusicOn = Math.abs(localMusicOn - 1);
            }
        }
    }

    @Override
    public void dispose() {
        game.dispose();

        languageLabel.dispose();
        soundsLabel.dispose();
        musicLabel.dispose();
        englishLabel.dispose();
        russianLabel.dispose();
        belorussianLabel.dispose();

        settingsBackGround.dispose();
        frame.dispose();
        tick.dispose();
        emptyString.dispose();
        fullString.dispose();

        saveButton.dispose();

        flagEngland.dispose();
        flagRussian.dispose();
        flagBelorussian.dispose();

        musicPoint.dispose();
        soundPoint.dispose();
        musicBar.dispose();
        soundBar.dispose();
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