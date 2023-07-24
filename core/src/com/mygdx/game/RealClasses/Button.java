package com.mygdx.game.RealClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.Main;

public class Button extends Rectangle {
    private float textX;
    private float textY;
    private boolean withText = true;

    private BitmapFont font;

    private String text;
    private final Texture picture;

    public Button(float x, float y, float sizeX, float sizeY, Texture picture, String text, int textColor, int textSize){
        super(x, y, sizeX, sizeY);
        font = new BitmapFont();
        Main.parameter.color = new Color(textColor);
        Main.parameter.size = textSize;
        font = Main.generator.generateFont(Main.parameter);
        this.text = text;
        this.picture = picture;
        Main.gl.setText(font, text);
        this.textX = (int) Main.gl.width;
        this.textY = (int) Main.gl.height;
    }

    public Button(float x, float y, float sizeX, float sizeY, Texture picture){
        super(x, y, sizeX, sizeY);
        withText = false;
        this.picture = picture;
    }

    public void draw() {
        Main.batch.draw(picture, x, y, sizeX, sizeY);
        if (withText) {
            this.font.draw(Main.batch, this.text, this.x + (float)(sizeX - textX) / 2, y + (float)(sizeY + textY) / 2);
        }
    }

    public void changeText(String newText){
        this.text = newText;
        Main.gl.setText(font, text);
        this.textX = (int) Main.gl.width;
        this.textY = (int) Main.gl.height;
    }

    public void placeCenter() {
        this.x = (Main.scrX - this.sizeX) / 2;
    }

    public boolean isTouched() {
        if(super.isInside(Gdx.input.getX(), Main.scrY - Gdx.input.getY())){
            Main.clickSound.play(Main.soundVolume * Main.soundOn);
            return true;
        }
        return false;
    }

    public boolean isTouched(boolean withSound){
        return super.isInside(Gdx.input.getX(), Main.scrY - Gdx.input.getY());
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }
}
