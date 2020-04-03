package com.asimplenerd.chromaglobs;

import android.util.Log;

import com.asimplenerd.chromaglobs.Game.EndScreen;
import com.asimplenerd.chromaglobs.Game.LoadingScreen;
import com.asimplenerd.chromaglobs.Game.MainScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


// Orchestrator of the game
public class GDXGame extends Game {

    static public Skin gameSkin;

    private LoadingScreen loadingScreen;
    private MainScreen mainScreen;
    private EndScreen endScreen;

    private float screenW, screenH;

    public final static int MAIN = 0;
    public final static int END = 1;

    public static float getScreenWidth(){  return Gdx.graphics.getWidth(); }
    public static float getScreenHeight(){  return Gdx.graphics.getHeight(); }

    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.internal("skin/rainbow/rainbow-ui.json"));
        this.setScreen(new MainScreen(this));
        Gdx.input.setCatchKey(Input.Keys.BACK, true); //handle the back arrow press
        screenW = Gdx.graphics.getWidth();
        screenH = Gdx.graphics.getHeight();
    }

    public void render(){
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);     // draws the color onto the

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void changeScreen(int screen){
        switch(screen){
            case MAIN:
                if(mainScreen == null)
                    mainScreen = new MainScreen(this);
                this.setScreen(mainScreen);
                Log.d("GDXGame: ", "changeScreen entered");
            case END:
                if(endScreen == null)
                    endScreen = new EndScreen(this);
                this.setScreen(endScreen);
        }
    }

    public void quit(){
        this.dispose();
        mainScreen.dispose();
        endScreen.dispose();
        Gdx.app.exit();
    }
}