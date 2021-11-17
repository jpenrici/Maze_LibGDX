package com.mazegdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MazeGdxGame extends Game {

    BitmapFont font;
    SpriteBatch batch;
    Texture screenImage;
    OrthographicCamera camera;

    Map<String, String> message;
    float screenWidth;
    float screenHeight;

    @Override
    public void create() {

        font = new BitmapFont();
        batch = new SpriteBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        message = new HashMap<>();
        message.put("menuStart", "Tap anywhere to begin!");
        message.put("wait", "The game will restart in a moment!");
        if (Locale.getDefault().getLanguage().equals("pt")) {
            message.put("menuStart", "Toque na tela para começar!");
            message.put("wait", "O jogo reiniciará em instantes!");
        }

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        screenImage.dispose();
    }
}
