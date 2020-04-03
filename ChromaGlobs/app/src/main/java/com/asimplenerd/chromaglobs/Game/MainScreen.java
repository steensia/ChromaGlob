package com.asimplenerd.chromaglobs.Game;

import android.util.Log;

import com.asimplenerd.chromaglobs.GDXGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// part of tutorial Full libgdx Game Tutorial
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-flgt-home/
public class MainScreen implements Screen {
    private GDXGame parent;
    private Stage stage;
    private float screenW, screenH;
    private Texture board, player, opponent;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    public MainScreen(GDXGame game){
        parent = game;

        screenW = Gdx.graphics.getWidth();
        screenH = Gdx.graphics.getHeight();

        player = new Texture(Gdx.files.internal("Player.png"));
        opponent = new Texture(Gdx.files.internal("Opponent.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenW, screenH);
        batch = new SpriteBatch();

        setUpButtons();
    }

    public void setUpButtons(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        table.right();
        table.pad(50);
        TextButton quit = new TextButton("Quit", GDXGame.gameSkin);
        TextButton settings = new TextButton("Settings", GDXGame.gameSkin);
        table.add(quit).padBottom(20).fillX().uniform();
        table.row();
        table.add(settings).fillX().uniform();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 128.0f/255.0f, 239.0f/255.0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);     // draws the color onto the

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(opponent, 0, screenH - opponent.getHeight());
        batch.draw(player, 0, 0);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        batch.dispose();
        stage.dispose();
    }
}