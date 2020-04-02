package com.asimplenerd.chromaglobs;

import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GDXGame extends Game {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shape;

    private float screenW, screenH, gameW, gameH;

    @Override
    public void create(){
        Log.d("GDXGame", "Creation Successful!");

        screenW = Gdx.graphics.getWidth();         // width is 1080
        screenH = Gdx.graphics.getHeight();          // height is 2028

        // the game screen has a different width and height: basically inverted of the screen
        gameH = screenW;
        gameW = screenH;

        // create the camera and SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenW, screenH);

        shape = new ShapeRenderer();
    }

    @Override
    public void render(){
        //Drawing method
        // sets the background to a light gray color
        Gdx.gl.glClearColor(239.0f/255.0f, 239.0f/255.0f, 239.0f/255.0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);     // draws the color onto the screen

        camera.update();

        shape.setProjectionMatrix(camera.combined);

        drawCenterLine();
    }

    @Override
    public void dispose(){
        batch.dispose();
        shape.dispose();
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

    // Helper method to draw a line at the center of the screen
    private void drawCenterLine(){
        ReCoordinate rc = offset(0, gameH/2, gameW, gameH/2, 0, 0);

        shape.begin(ShapeType.Line);
        shape.setColor(Color.BLACK);
        Gdx.gl20.glLineWidth(3);
        shape.line(rc.x, rc.y, rc.x2, rc.y2);
        shape.end();
    }

    // Helper method to relocate origin
    private ReCoordinate offset(float x, float y, float x2, float y2, float width, float height) {
        ReCoordinate rc = new ReCoordinate();
        rc.x = screenW - y;
        rc.y = screenH - x;

        rc.x2 = screenW - y2;
        rc.y2 = screenH - x2;

        rc.w = width * -1;
        rc.h = height * -1;

        return rc;
    }
}