package com.mazegdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MazeScreen implements Screen {
    final MazeGdxGame game;

    private static final int MAX_COLLISIONS = 25;
    private static final int SIZE = 10;
    private static final int WAIT = 5;

    MazeMatrix mazeMatrix;
    boolean gameRunning;
    int collisions;
    long lastTime;

    Color colorFree;
    Color colorWall;
    Color colorBegin;
    Color colorEnd;
    Pixmap.Format pixmapFormat;

    Rectangle maze;
    Texture mazeTexture;
    int mazeRectWidth;
    int mazeRectHeight;
    int mazeEndX;
    int mazeEndY;

    Rectangle player;
    Texture playerTexture;
    boolean playerIsActive;
    int playerRectSize;
    int playerHalfSize;
    int playerPosX;
    int playerPosY;

    public MazeScreen(final MazeGdxGame game) {
        this.game = game;
        init();
    }

    private void init() {

        maze = new Rectangle();
        player = new Rectangle();
        playerIsActive = true;
        gameRunning = true;
        collisions = 0;
        lastTime = 0;

        mazeMatrix = new MazeMatrix(SIZE);
        mazeRectWidth = (int) game.screenWidth / mazeMatrix.getSize();
        mazeRectHeight = (int) game.screenHeight / mazeMatrix.getSize();
        playerRectSize = Math.min(mazeRectWidth, mazeRectHeight) / 2;
        playerHalfSize = playerRectSize / 2;

        pixmapFormat = Pixmap.Format.RGBA8888;
        Pixmap pixmapPlayer = new Pixmap(playerRectSize, playerRectSize, pixmapFormat);
        pixmapPlayer.setColor(rgb(255, 255, 128));
        pixmapPlayer.fillCircle(playerHalfSize, playerHalfSize, playerHalfSize);
        playerTexture = new Texture(pixmapPlayer);
        pixmapPlayer.dispose();

        int option = MathUtils.random(8);
        if (option % 3 == 0) {
            mazeMatrix.spiral();
        }
        switch (option) {
            case 1:
                mazeMatrix.reverse();
                break;
            case 2:
                mazeMatrix.flip();
                break;
            case 3:
                mazeMatrix.flip(true);
                break;
            case 4:
                mazeMatrix.reverse();
                mazeMatrix.flip();
                break;
            case 5:
                mazeMatrix.reverse();
                mazeMatrix.flip(true);
                break;
        }

        colorFree = new Color(rgb(192, 192, 192));
        colorWall = new Color(rgb(128, 0, 0));
        colorBegin = new Color(rgb(0, 128, 64));
        colorEnd = new Color(rgb(0, 128, 128));

        drawMaze();
    }

    private Color rgb(int r, int g, int b) {
        return new Color((float) r / 255, (float) g / 255, (float) b / 255, 1f);
    }

    private void drawMaze() {

        Pixmap pixmapMaze = new Pixmap((int) game.screenWidth, (int) game.screenHeight,
                pixmapFormat);

        for (int y = 0; y < mazeMatrix.getSize(); ++y) {
            for (int x = 0; x < mazeMatrix.getSize(); ++x) {
                Pixmap pixmap = new Pixmap(mazeRectWidth, mazeRectHeight, pixmapFormat);
                pixmap.setBlending(Pixmap.Blending.None);
                switch (mazeMatrix.getValue(y, x)) {
                    case MazeMatrix.WALL:
                        pixmap.setColor(colorWall);
                        break;
                    case MazeMatrix.FREE:
                        pixmap.setColor(colorFree);
                        break;
                    case MazeMatrix.BEGIN:
                        pixmap.setColor(colorBegin);
                        player.y = mazeRectHeight * y + playerHalfSize;
                        player.x = mazeRectWidth * x + playerHalfSize;
                        playerPosY = y;
                        playerPosX = x;
                        break;
                    case MazeMatrix.END:
                        pixmap.setColor(colorEnd);
                        mazeEndX = x;
                        mazeEndY = y;
                        break;
                }
                pixmap.fillRectangle(0, 0, mazeRectWidth, mazeRectHeight);
                pixmapMaze.drawPixmap(pixmap, x * mazeRectWidth,
                        (int) game.screenHeight - mazeRectHeight - y * mazeRectHeight);
                pixmap.dispose();
            }
        }

        mazeTexture = new Texture(pixmapMaze);
        pixmapMaze.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(colorWall);
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(mazeTexture, 0, 0);
        game.batch.draw(playerTexture, player.x, player.y);
        if (!gameRunning) {
            game.batch.draw(game.screenImage, 0, 0, game.screenWidth, game.screenHeight);
            game.font.draw(game.batch, game.message.get("wait"), game.screenWidth / 3, 50);
            if (TimeUtils.millis() - lastTime > WAIT * 1000) {
                init();
            }
        }
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);
            int y = (int) touchPos.y;
            int x = (int) touchPos.x;
            int mY = y / mazeRectHeight;
            int mX = x / mazeRectWidth;
            int value = mazeMatrix.getValue(mY, mX);
            double distance = Math.sqrt(Math.pow(mY - playerPosY, 2) + Math.pow(mX - playerPosX, 2));
            if (distance <= 1 && value != MazeMatrix.ERROR) {
                if (value == MazeMatrix.WALL) {
                    collisions++;
                    player.y = mazeRectHeight * playerPosY;
                    player.x = mazeRectWidth * playerPosX + playerHalfSize;
                    if (collisions > MAX_COLLISIONS) {
                        game.screenImage = new Texture(Gdx.files.internal("bkg_gameOver.png"));
                        lastTime = TimeUtils.millis();
                        gameRunning = false;
                    }
                }
                if (value == MazeMatrix.FREE || value == MazeMatrix.BEGIN) {
                    if (playerIsActive) {
                        player.y = y - playerHalfSize;
                        player.x = x - playerHalfSize;
                        playerPosY = mY;
                        playerPosX = mX;
                    }
                }
                if (value == MazeMatrix.END) {
                    game.screenImage = new Texture(Gdx.files.internal("bkg_winner.png"));
                    lastTime = TimeUtils.millis();
                    gameRunning = false;
                }
            }
        }

        if (player.x < player.width) {
            player.x = player.width;
        }
        if (player.y < player.height) {
            player.y = player.height;
        }
        if (player.x > (game.screenWidth - player.width)) {
            player.x = game.screenWidth - player.width;
        }
        if (player.y > (game.screenHeight - player.height)) {
            player.y = game.screenHeight - player.height;
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
        mazeTexture.dispose();
        playerTexture.dispose();
    }
}
