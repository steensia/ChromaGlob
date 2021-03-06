package com.asimplenerd.chromaglobs.Game;

import android.util.Log;

import com.asimplenerd.chromaglobs.GDXGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.exit;

// part of tutorial Full libgdx Game Tutorial
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-flgt-home/
public class MainScreen implements Screen {
    private GDXGame parent;
    private Stage stage;
    private float screenW, screenH;
    private Texture board, player, opponent;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private boolean quitDialogShown = false;

    public MainScreen(GDXGame game){
        parent = game;

        screenW = GDXGame.getScreenWidth();
        screenH = GDXGame.getScreenHeight();

        player = new Texture(Gdx.files.internal("Player.png"));
        opponent = new Texture(Gdx.files.internal("Opponent.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenW, screenH);
        batch = new SpriteBatch();

        setUpButtons();
    }

    private void setUpButtons(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new InputListener(){
           @Override
           public boolean keyDown(InputEvent event, int keyCode){
               if(keyCode == Input.Keys.BACK && !quitDialogShown)
                   quitDialog();
               return false;
           }
        });

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

        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!quitDialogShown)
                    quitDialog();
            }
        });
    }

    private void quitDialog(){
        quitDialogShown = true;
        Dialog dialog = new Dialog("Warning", GDXGame.gameSkin) {
            public void result(Object obj){
                System.out.println("result " + obj);
                if(obj instanceof Boolean && (Boolean)obj){
                    forfeitMatch();
                }
                else{
                    quitDialogShown = false;
                }
            }
        };
        Label text = new Label("Are you sure you want to quit?\n"
        + "If you quit now, this will count as an automatic forfeit for you.", GDXGame.gameSkin);
        text.setFontScale(2);
        text.setWrap(false);

        dialog.getContentTable().add(text).pad(40);

        dialog.button("Yes", true);
        dialog.button("No", false);
        dialog.key(Input.Keys.ENTER, true);
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);


        dialog.setName("quitDialog");
        dialog.show(stage);
        stage.addActor(dialog);

    }

    private void forfeitMatch(){
        //TODO place logic for forfeiting a match here
        try {
            Log.d("Forfeit", "Player has forfeited the match! Returning to main activity!");
            Gdx.app.exit();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
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
        board.dispose();
        player.dispose();
        opponent.dispose();
    }
}
