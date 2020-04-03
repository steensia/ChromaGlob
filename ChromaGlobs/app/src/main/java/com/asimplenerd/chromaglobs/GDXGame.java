package com.asimplenerd.chromaglobs;

import android.util.Log;

import com.asimplenerd.chromaglobs.Game.EndScreen;
import com.asimplenerd.chromaglobs.Game.LoadingScreen;
import com.asimplenerd.chromaglobs.Game.MainScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


// Orchestrator of the game
public class GDXGame extends Game {

    static public Skin gameSkin;

    private LoadingScreen loadingScreen;
    private MainScreen mainScreen;
    private EndScreen endScreen;

    public final static int MAIN = 0;
    public final static int END = 1;

    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        this.setScreen(new MainScreen(this));
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
}