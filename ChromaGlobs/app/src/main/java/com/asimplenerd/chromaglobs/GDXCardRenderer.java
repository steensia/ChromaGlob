package com.asimplenerd.chromaglobs;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.asimplenerd.chromaglobs.Classes.Card;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GDXCardRenderer extends Game {

    public Card shownCard;
    boolean cardSelected = false;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    int width, height;

    @Override
    public void create() {
        Log.d("CardRender", "Created successfully");
        Log.d("CardViewDims", Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    //Note that the coordinates are like that of quadrant 1 on a two dimensional graph.
    @Override
    public void render(){
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if(!cardSelected){
            font.setColor(Color.RED);
            font.getData().setScale(4.0f);
            font.draw(batch, "Select a Card!", 150, height/2 + 50);
        }
        batch.end();
        if(cardSelected){
            displayCard();
        }
    }

    private void displayCard(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(0, 0, width, height);
        shapeRenderer.end();
    }

    public void updateCard(Card c){
        if(c == null)
            return;
        shownCard = c;
        cardSelected = true;
    }

}
