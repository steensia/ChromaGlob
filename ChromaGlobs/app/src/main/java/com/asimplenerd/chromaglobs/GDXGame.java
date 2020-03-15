package com.asimplenerd.chromaglobs;

import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GDXGame extends Game {
    @Override
    public void create(){
        Log.d("GDXGame", "Creation Successful!");
    }

    @Override
    public void render(){
        //Drawing method
        Gdx.gl.glClearColor(1.00f, 0, 1.0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    @Override
    public void pause(){
        System.out.println("App paused.");

    }

    @Override
    public void resume(){
        System.out.println("App resumed");
    }
}
