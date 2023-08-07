package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.TextBox;
import com.mygdx.game.Screens.City;
import com.mygdx.game.Screens.PlaneGame;
import com.mygdx.game.Screens.Settings;
import com.mygdx.game.Screens.Shop;
import com.mygdx.game.Screens.StartMenu;

public class Main extends Game {
	public static SpriteBatch batch;
	public static Sound clickSound;
	public static Sound sellSound;
	public static Sound upgradeSound;
	public static Sound errorSound;

	public static Preferences prefs;
	public static Preferences cityPrefs;

	public static FreeTypeFontGenerator generator;
	public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	public static GlyphLayout gl;

	public static float scrX;
	public static float scrY;
	public static float pppX; // pixels per percent width
	public static float pppY; // pixels per percent height
	//     16:10      4:3       3:2       5:3       16:9
	//     144:90    120:90    135:90    150:90    160:90
	//     1.33 - 1.77

	//saves:
	public static int selectedLanguage = 0;
	public static float soundVolume = 1f;
	public static float musicVolume = 1f;
	public static int soundOn = 1;
	public static int musicOn = 1;

	//game saves:
	public static long money = 0;
	public static long sapphires = 0;

	//money:
	public static PictureBox coinPicture;
	public static PictureBox sapphirePicture;
	public static TextBox coinText;
	public static TextBox sapphireText;

	public StartMenu startMenu;
	public Settings settings;
	public City city;
	public PlaneGame planeGame;
	public Shop shop;
	
	@Override
	public void create () {
		scrX = Gdx.graphics.getWidth();
		scrY = Gdx.graphics.getHeight();
		pppX = scrX/100;
		pppY = scrY/100;

		prefs = Gdx.app.getPreferences("FlightCityMainSaves");
		cityPrefs = Gdx.app.getPreferences("CitySaves");
		loadPrefs();

		initializationFont();
		batch = new SpriteBatch();
		batch.enableBlending();
		clickSound = Gdx.audio.newSound(Gdx.files.internal("general/click.mp3"));
		sellSound = Gdx.audio.newSound(Gdx.files.internal("city/sounds/sellSound.mp3"));
		upgradeSound = Gdx.audio.newSound(Gdx.files.internal("city/sounds/upgradeSound.mp3"));
		errorSound = Gdx.audio.newSound(Gdx.files.internal("city/sounds/errorSound.mp3"));

		coinPicture = new PictureBox(scrX - 8 * pppY, 92 * pppY, 5 * pppY, 5 * pppY, "general/coin.png");
		sapphirePicture = new PictureBox(scrX - 8 * pppY, 84 * pppY, 5 * pppY, 5 * pppY, "general/sapphire.png");
		coinText = new TextBox(0, 0, divisionDigits(money), 0xffffffff, (int) (3 * pppY));
		coinText.positionToRight(scrX - 10 * pppY);
		coinText.positionToMiddleY(94.5f * pppY);
		sapphireText = new TextBox(0, 0, divisionDigits(sapphires), 0xffffffff, (int) (3 * pppY));
		sapphireText.positionToRight(scrX - 10 * pppY);
		sapphireText.positionToMiddleY(86.5f * pppY);

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

		city.dispose();
		planeGame.dispose();
		settings.dispose();
		shop.dispose();
		startMenu.dispose();

		coinPicture.dispose();
		coinText.dispose();
		sapphirePicture.dispose();
		sapphireText.dispose();
	}

	public static void savePrefs(){
		prefs.putInteger("selectedLanguage", selectedLanguage);
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putFloat("musicVolume", musicVolume);
		prefs.putInteger("soundOn", soundOn);
		prefs.putInteger("musicOn", musicOn);

		prefs.putLong("money", money);
		prefs.putLong("sapphires", sapphires);

		prefs.flush();
	}

	public void loadPrefs(){
		soundVolume = prefs.getFloat("soundVolume", 1f);
		musicVolume = prefs.getFloat("musicVolume", 1f);
		soundOn = prefs.getInteger("soundOn", 1);
		musicOn = prefs.getInteger("musicOn", 1);
		selectedLanguage = prefs.getInteger("selectedLanguage", 0);

		money = prefs.getLong("money", 0);
		sapphires = prefs.getLong("sapphires", 0);
	}

	public void saveCityPrefs(int houseIndex){
		if (houseIndex < 0 || houseIndex >= city.buildings.length) return;
		cityPrefs.putBoolean("building-" + houseIndex + "-isExist", city.buildings[houseIndex].isExist());
		cityPrefs.putInteger("building-" + houseIndex + "-id", city.buildings[houseIndex].getId());
		cityPrefs.putInteger("building-" + houseIndex + "-level", city.buildings[houseIndex].getLevel());

		cityPrefs.flush();
	}

	void RESETPREFS(){
		prefs.clear();
		prefs.flush();
		cityPrefs.clear();
		cityPrefs.flush();
	}

	public static float textureAspectRatio(Texture texture, boolean toHeight){
		if(toHeight) return (float) texture.getWidth() / texture.getHeight();
		return (float) texture.getHeight() / texture.getWidth();
	}

	public static void showMoney(){
		coinPicture.draw();
		coinText.draw();
		sapphirePicture.draw();
		sapphireText.draw();
	}

	public static void updateMoney(){
		coinText.changeText(divisionDigits(money));
		sapphireText.changeText(divisionDigits(sapphires));
	}

	public static String divisionDigits(long value){
		String number;
		if(value >= 1000000000000L) number = Long.toString(value/1000000000);
		else if(value >= 1000000000) number = Long.toString(value/1000000);
		else number = Long.toString(value);
		String newNumber = "";
		if(number.length() % 3 != 0){
			newNumber += number.substring(0, number.length() % 3);
			number = number.substring(number.length() % 3);
			if(number.length() > 0) newNumber += '.';
		}
		while(number.length() != 0){
			newNumber += number.substring(0, 3);
			number = number.substring(3);
			if(number.length() > 0) newNumber += '.';
		}
		if(value >= 1000000000000L) newNumber += " B";
		else if(value >= 1000000000) newNumber += " M";
		return newNumber;
	}
}
