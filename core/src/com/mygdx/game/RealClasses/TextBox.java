package com.mygdx.game.RealClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.Main;

public class TextBox extends Rectangle {
    BitmapFont font;
    String text;

    public TextBox(float centerX, float y, String text, int textColor, int textSize) {
        font = new BitmapFont();
        Main.parameter.color = new Color(textColor);
        Main.parameter.size = textSize;
        font = Main.generator.generateFont(Main.parameter);
        this.text = text;
        Main.gl.setText(font, text);
        this.sizeX = (int) Main.gl.width;
        this.sizeY = (int) Main.gl.height;
        this.x = centerX - this.sizeX/2;
        this.y = y;
    }

    public void draw() {
        this.font.draw(Main.batch, this.text, x, y);
    }

    public void draw(float x, float y) {
        this.font.draw(Main.batch, this.text, x, y);
    }

    public void changeText(String newText){
        this.x += sizeX/2;
        this.text = newText;
        Main.gl.setText(font, text);
        this.sizeX = (int) Main.gl.width;
        this.sizeY = (int) Main.gl.height;
        this.x -= sizeX/2;
    }

    public void setColor(float r, float g, float b){
        this.font.setColor(r, g, b, 1);
    }
}
