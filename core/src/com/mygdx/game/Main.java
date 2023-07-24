package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Screens.City;
import com.mygdx.game.Screens.PlaneGame;
import com.mygdx.game.Screens.Settings;
import com.mygdx.game.Screens.Shop;
import com.mygdx.game.Screens.StartMenu;

public class Main extends Game {
	public static Texture house;

	public static SpriteBatch batch;
	public static Sound clickSound;
	public static OrthographicCamera camera;

	public static Preferences prefs;
	public static Preferences language;

	public static FreeTypeFontGenerator generator;
	public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	public static GlyphLayout gl;

	public static float scrX, scrY;
	public static float pppX; // pixels per percent width
	public static float pppY; // pixels per percent height
	//     16:10      4:3       3:2       5:3       16:9
	//     144:90    120:90    135:90    150:90    160:90
	//     1.33 - 1.77

	//saves:
	public static String selectedLanguage = "eng_";
	public static float soundVolume = 1f;
	public static float musicVolume = 1f;
	public static int soundOn = 1;
	public static int musicOn = 1;

	//game saves:
	public static int money = 0;

	public StartMenu startMenu;
	public Settings settings;
	public City city;
	public PlaneGame planeGame;
	public Shop shop;
	
	@Override
	public void create () {
		house = new Texture("city/Cafe.png");
		scrX = Gdx.graphics.getWidth();
		scrY = Gdx.graphics.getHeight();
		pppX = (float)scrX/100;
		pppY = (float)scrY/100;

		prefs = Gdx.app.getPreferences("FlightCityInventory");
		language = Gdx.app.getPreferences("language");
		loadPrefs();
		//saveText();

		initializationFont();
		batch = new SpriteBatch();
		clickSound = Gdx.audio.newSound(Gdx.files.internal("general/click.mp3"));
		camera = new OrthographicCamera(scrX, scrY);
		camera.position.set(scrX/2, scrY/2, 0);
		camera.update();

		startMenu = new StartMenu(this);
		settings = new Settings(this);
		city = new City(this);
		planeGame = new PlaneGame(this);
		shop = new Shop(this);

		setScreen(startMenu);
	}

	void initializationFont(){
		generator = new FreeTypeFontGenerator(Gdx.files.internal("general/joystix.otf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
				"абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" +
				"абвгддждзеёжзійклмнопрстуўфхцчшыьэюяАБВГДДЖДЗЕЁЖЗІЙКЛМНОПРСТУЎФХЦЧШЫЬЭЮЯ" +
				"0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter.borderWidth = 3;
		parameter.borderColor = Color.valueOf("000000ff");
		gl = new GlyphLayout();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		generator.dispose();
	}

	public void savePrefs(){
		prefs.putString("selectedLanguage", selectedLanguage);
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putFloat("musicVolume", musicVolume);
		prefs.putInteger("soundOn", soundOn);
		prefs.putInteger("musicOn", musicOn);

		prefs.putInteger("money", money);
		prefs.flush();
	}

	public void loadPrefs(){
		soundVolume = prefs.getFloat("soundVolume", 1f);
		musicVolume = prefs.getFloat("musicVolume", 1f);
		soundOn = prefs.getInteger("soundOn", 1);
		musicOn = prefs.getInteger("musicOn", 1);
		selectedLanguage = prefs.getString("selectedLanguage", "eng_");

		money = prefs.getInteger("money", 0);
	}

	void RESETPREFS(){
		prefs.putString("selectedLanguage", "eng_");
		prefs.putFloat("soundVolume", 1f);
		prefs.putFloat("musicVolume", 1f);
		prefs.putInteger("soundOn", 1);
		prefs.putInteger("musicOn", 1);

		prefs.putInteger("money", 0);
		prefs.flush();
	}

	public void saveText(){
		// english - eng_
		// russian - rus_
		// belorussian - bel_
		language.putString("eng_play", "play");
		language.putString("rus_play", "играть");
		language.putString("bel_play", "гуляць");

		language.putString("eng_settings", "settings");
		language.putString("rus_settings", "настройки");
		language.putString("bel_settings", "наладкі");

		language.putString("eng_sound", "sounds");
		language.putString("rus_sound", "звуки");
		language.putString("bel_sound", "гукі");

		language.putString("eng_music", "music");
		language.putString("rus_music", "музыка");
		language.putString("bel_music", "музыка");

		language.putString("eng_language", "language");
		language.putString("rus_language", "язык");
		language.putString("bel_language", "мова");

		language.putString("eng_save", "save");
		language.putString("rus_save", "сохранить");
		language.putString("bel_save", "захаваць");
/*
		language.putString("eng_", "");
		language.putString("rus_", "");
		language.putString("bel_", "");
*/
		language.flush();
	}

	public static float textureAspectRatio(Texture texture, boolean toHeight){
		if(toHeight) return (float) texture.getWidth() / texture.getHeight();
		return (float) texture.getHeight() / texture.getWidth();
	}
}