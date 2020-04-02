package com.asimplenerd.chromaglobs;

import android.util.Log;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GDXGame extends Game {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private Texture board;

    private float screenW, screenH, gameW, gameH;

    @Override
    public void create(){
        Log.d("GDXGame", "Creation Successful!");

        screenW = Gdx.graphics.getWidth();         // width is 1080
        screenH = Gdx.graphics.getHeight();          // height is 2028
        // the game screen has a different width and height: basically inverted of the screen
        gameH = screenW;
        gameW = screenH;

        // load images
        board = new Texture(Gdx.files.internal("MatchBoard.png"));

        // create the camera and SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenW, screenH);
        batch = new SpriteBatch();

        shape = new ShapeRenderer();
    }

    @Override
    public void render(){
        //Drawing method
        // sets the background to a light gray color
        Gdx.gl.glClearColor(239.0f/255.0f, 239.0f/255.0f, 239.0f/255.0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);     // draws the color onto the screen

        camera.update();

        //TextureRegion tr = new TextureRegion(board);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(board, 0, 0);
        batch.end();

        shape.setProjectionMatrix(camera.combined);

//        drawCenterLine();
/**        ReCoordinate rc = new ReCoordinate();

        shape.begin(ShapeType.Line);
        shape.setColor(Color.BLACK);
        Gdx.gl20.glLineWidth(2);

        rc = offset(0, gameH/2, gameW, gameH/2, 0, 0);
        shape.line(rc.x, rc.y, rc.x2, rc.y2);

        rc = offset(0, 0, 0, 0, 250, 350);
        shape.rect(rc.x, rc.y, rc.w, rc.h);
        shape.end();*/

    }

    @Override
    public void dispose(){
        shape.dispose();
        batch.dispose();
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
        Gdx.gl20.glLineWidth(2);
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

        rc.w = height * -1;
        rc.h = width * -1;

        return rc;
    }
}