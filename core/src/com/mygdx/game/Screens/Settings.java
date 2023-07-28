package com.mygdx.game.Screens;

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
        languageLabel = new TextBox(Main.scrX / 2, Main.pppY * 97,
                Main.language.getString(Main.selectedLanguage + "language"), 0xffffffff, (int) (5 * Main.pppY));
        englishLabel = new TextBox(29 * Main.pppX, 87 * Main.pppY - 9 * Main.pppX, "english", 0xffffffff, (int) (3 * Main.pppY));
        russianLabel = new TextBox(50 * Main.pppX, 87 * Main.pppY - 9 * Main.pppX, "русский", 0xffffffff, (int) (3 * Main.pppY));
        belorussianLabel = new TextBox(71 * Main.pppX, 87 * Main.pppY - 9 * Main.pppX, "беларуская", 0xffffffff,
                (int) (3 * Main.pppY));
        soundsLabel = new TextBox(Main.scrX/2, 80 * Main.pppY - 9 * Main.pppX,
                Main.language.getString(Main.selectedLanguage + "sound"), 0xffffffff, (int) (5 * Main.pppY));
        musicLabel = new TextBox(Main.scrX/2, 60 * Main.pppY - 9 * Main.pppX,
                Main.language.getString(Main.selectedLanguage + "music"), 0xffffffff, (int) (5 * Main.pppY));

        // pictures
        settingsBackGround = new PictureBox(19 * Main.pppX, 0, 62 * Main.pppX, Main.scrY, path + "settingsBackGround.png");
        frame = new PictureBox(0, 0, 18 * Main.pppX, 9 * Main.pppX, path + "frame.png");
        tick = new PictureBox(23 * Main.pppX, 45 * Main.pppY - 9 * Main.pppX, 5 * Main.pppX, 5 * Main.pppX, path + "tick.png");
        emptyString = new PictureBox(31 * Main.pppX, 45 * Main.pppY - 7 * Main.pppX, 46 * Main.pppX, Main.pppX,
                path + "emptyString.png");
        fullString = new PictureBox(31 * Main.pppX, 45 * Main.pppY - 7 * Main.pppX, 46 * Main.pppX, Main.pppX,
                path + "fullString.png");

        // buttons
        saveButton = new Button(81 * Main.pppX, 97 * Main.pppY - 10 * Main.pppX, 18 * Main.pppX, 10 * Main.pppX,
                new Texture("buttons/button 3-2.png"), Main.language.getString(Main.selectedLanguage + "save"),
                0xffffffff, (int) (3 * Main.pppY));
        flagEngland = new Button(20 * Main.pppX, 90 * Main.pppY - 9 * Main.pppX, 18 * Main.pppX, 9 * Main.pppX,
                new Texture(path + "flagEngland.png"));
        flagRussian = new Button(41 * Main.pppX, 90 * Main.pppY - 9 * Main.pppX, 18 * Main.pppX, 9 * Main.pppX,
                new Texture(path + "flagRussian.png"));
        flagBelorussian = new Button(62 * Main.pppX, 90 * Main.pppY - 9 * Main.pppX, 18 * Main.pppX, 9 * Main.pppX,
                new Texture(path + "flagBelorussian.png"));

        soundBar = new Button(23 * Main.pppX, 65 * Main.pppY - 9 * Main.pppX, 5 * Main.pppX, 5 * Main.pppX,
                new Texture(path + "bar.png"));
        musicBar = new Button(23 * Main.pppX, 45 * Main.pppY - 9 * Main.pppX, 5 * Main.pppX, 5 * Main.pppX,
                new Texture(path + "bar.png"));

        soundPoint = new Button(30 * Main.pppX, 45 * Main.pppY - 8 * Main.pppX, 3* Main.pppX, 3 * Main.pppX,
                new Texture(path + "point.png"));
        musicPoint = new Button(30 * Main.pppX, 65 * Main.pppY - 8 * Main.pppX, 3* Main.pppX, 3 * Main.pppX,
                new Texture(path + "point.png"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Main.batch.begin();

        Main.batch.draw(game.startMenu.menuBackGround, game.startMenu.backGroundCoordinateX, 0,
                game.startMenu.backGroundWidth, Main.scrY);
        settingsBackGround.draw();
        game.startMenu.buttonExit.draw();
        saveButton.draw();

        showLanguages();
        showSoundsAndMusic();

        Main.batch.end();

        swipeRender();
        soundRender();
        musicRender();
        buttonsCheck();
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

    @Override
    public void dispose() {
    }

    void showLanguages() {
        languageLabel.draw(languageLabel.getX(), languageLabel.getY() + positionY);
        flagEngland.draw();
        flagRussian.draw();
        flagBelorussian.draw();

        switch (localSelectedLanguage) {
            case "eng_":
                frame.draw(flagEngland.getX(), flagEngland.getY());
                break;
            case "rus_":
                frame.draw(flagRussian.getX(), flagRussian.getY());
                break;
            case "bel_":
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
        soundPoint.setX(30 * Main.pppX + 46 * Main.pppX * localSoundVolume);
        soundPoint.setY(65 * Main.pppY - 8 * Main.pppX + positionY);
        musicPoint.setX(30 * Main.pppX + 46 * Main.pppX * localMusicVolume);
        musicPoint.setY(45 * Main.pppY - 8 * Main.pppX + positionY);

        soundsLabel.draw(soundsLabel.getX(), soundsLabel.getY() + positionY);
        musicLabel.draw(musicLabel.getX(), musicLabel.getY() + positionY);

        soundBar.draw();
        musicBar.draw();
        if (localSoundOn == 1) {
            tick.draw(tick.getX(), tick.getY() + 20 * Main.pppY + positionY);
        }
        if (localMusicOn == 1) {
            tick.draw(tick.getX(), tick.getY() + positionY);
        }

        emptyString.draw(emptyString.getX(), emptyString.getY() + positionY);
        emptyString.draw(emptyString.getX(), emptyString.getY() + 20*Main.pppY + positionY);
        fullString.draw(fullString.getX(), fullString.getY() + positionY, fullString.getSizeX() * localMusicVolume,
                fullString.getSizeY(), 0, 0, (int)(localMusicVolume * 300), 6, false, false);
        fullString.draw(fullString.getX(), fullString.getY() + 20*Main.pppY+ positionY, fullString.getSizeX() * localSoundVolume,
                fullString.getSizeY(), 0, 0, (int)(localSoundVolume * 300), 6, false, false);

        soundPoint.draw();
        musicPoint.draw();
    }

    void swipeRender(){
        if (Gdx.input.justTouched() && (Gdx.input.getX() < Main.pppX * 20 || Gdx.input.getX() > Main.pppX * 80)) {
            touchPlace = Gdx.input.getY();
            touchPosition = positionY;
            touchToSwipe = true;
        }
        if (Gdx.input.isTouched() && touchToSwipe) {
            positionY = touchPosition - Gdx.input.getY() + touchPlace;
            if (positionY < 0) positionY = 0;

            flagEngland.setY(90 * Main.pppY - 9 * Main.pppX + positionY);
            flagRussian.setY(90 * Main.pppY - 9 * Main.pppX + positionY);
            flagBelorussian.setY(90 * Main.pppY - 9 * Main.pppX + positionY);
            soundBar.setY(65 * Main.pppY - 9 * Main.pppX + positionY);
            musicBar.setY(45 * Main.pppY - 9 * Main.pppX + positionY);
        } else {
            touchToSwipe = false;
        }
    }

    void soundRender(){
        if(Gdx.input.justTouched() && Gdx.input.getX() > 30 * Main.pppX && Gdx.input.getX() < 78 * Main.pppX
                && Main.scrY - Gdx.input.getY() > 65 * Main.pppY - 9 * Main.pppX + positionY
                && Main.scrY - Gdx.input.getY() < 65 * Main.pppY - 4 * Main.pppX + positionY){
            touchSound = true;
        }
        if(Gdx.input.isTouched() && touchSound){
            localSoundVolume = ((Gdx.input.getX() - 1.5f * Main.pppX) * 100f / Main.scrX - 30f) / 46f;
            if(localSoundVolume < 0) localSoundVolume = 0;
            if(localSoundVolume > 1) localSoundVolume = 1;
        }else{
            touchSound = false;
        }
    }

    void musicRender(){
        if(Gdx.input.justTouched() && Gdx.input.getX() > 30 * Main.pppX && Gdx.input.getX() < 78 * Main.pppX
                && Main.scrY - Gdx.input.getY() > 45 * Main.pppY - 9 * Main.pppX + positionY
                && Main.scrY - Gdx.input.getY() < 45 * Main.pppY - 4 * Main.pppX + positionY){
            touchMusic = true;
        }
        if(Gdx.input.isTouched() && touchMusic){
            localMusicVolume = ((Gdx.input.getX() - 1.5f * Main.pppX) * 100f / Main.scrX - 30f) / 46f;
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
                flagEngland.setY(90 * Main.pppY - 9 * Main.pppX);
                flagRussian.setY(90 * Main.pppY - 9 * Main.pppX);
                flagBelorussian.setY(90 * Main.pppY - 9 * Main.pppX);
                soundBar.setY(65 * Main.pppY - 9 * Main.pppX);
                musicBar.setY(45 * Main.pppY - 9 * Main.pppX);
                game.startMenu.buttonPlay.changeText(Languages.play[Main.selectedLanguage]);
                game.startMenu.buttonSettings.changeText(Languages.settings[Main.selectedLanguage]);
                game.setScreen(game.startMenu);
            } else if (flagEngland.isTouched()) {
                localSelectedLanguage = 0;
            } else if (flagRussian.isTouched()) {
                localSelectedLanguage = 1;
            } else if (flagBelorussian.isTouched()) {
                localSelectedLanguage = 2;
            } else if (saveButton.isTouched()) {
                Main.selectedLanguage = localSelectedLanguage;
                Main.soundVolume = localSoundVolume;
                Main.musicVolume = localMusicVolume;
                Main.soundOn = localSoundOn;
                Main.musicOn = localMusicOn;

                game.savePrefs();

                languageLabel.changeText(Languages.language[]);
                soundsLabel.changeText(Main.language.getString(Languages.sound);
                musicLabel.changeText(Main.language.getString(Languages.music);
                saveButton.changeText(Main.language.getString(Languages.save);

                languageLabel.changeText(Main.language.getString(Main.selectedLanguage + "language"));
                soundsLabel.changeText(Main.language.getString(Main.selectedLanguage + "sound"));
                musicLabel.changeText(Main.language.getString(Main.selectedLanguage + "music"));
                saveButton.changeText(Main.language.getString(Main.selectedLanguage + "save"));
            } else if (soundBar.isTouched()) {
                localSoundOn = Math.abs(localSoundOn - 1);
            } else if (musicBar.isTouched()) {
                localMusicOn = Math.abs(localMusicOn - 1);
            }
        }
    }
}