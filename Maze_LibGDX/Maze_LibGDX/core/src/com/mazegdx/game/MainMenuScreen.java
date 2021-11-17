package com.mazegdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final MazeGdxGame game;

    public MainMenuScreen(final MazeGdxGame game) {
        this.game = game;
        this.game.screenImage = new Texture(Gdx.files.internal("bkg_start.png"));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.screenImage, 0, 0, game.screenWidth, game.screenHeight);
        game.font.draw(game.batch, game.message.get("menuStart"), game.screenWidth / 3, 50);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MazeScreen(game));
            dispose();
        }
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
}
