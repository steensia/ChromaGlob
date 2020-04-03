package com.asimplenerd.chromaglobs.Classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.asimplenerd.chromaglobs.R;

public class CardRenderer extends View {

    Card card;

    Paint cardBackground;
    Paint cardBorder;
    Paint textPaint;
    float xScale = 1, yScale = 1;
    float defaultWidth = 457.0f, defaultHeight = 530f;
    int defaultFontSize = 38;
    int defaultImageScaleX = 3, defaultImageScaleY = 3;
    Drawable rarityImage;
    Rarity rarity;

    public CardRenderer(Context context) {
        super(context);
        Log.d("CardRenderer", "Created!");
        init(null);
    }

    public CardRenderer(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        Log.d("CardRenderer", "Created with Attrs.");
        init(attributeSet);
    }

    public CardRenderer(Context context, AttributeSet attributeSet, int defStyleAttr){
        super(context, attributeSet, defStyleAttr);
        init(attributeSet);
    }

    public CardRenderer(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes){
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init(attributeSet);
    }

    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);
        drawCard(c);
    }

    private void drawCard(Canvas c){
        float xScale = c.getWidth() / defaultWidth;
        float yScale = c.getHeight() / defaultHeight;
        int imgXScale = (int) (defaultImageScaleX / xScale);
        int imgYScale = (int) (defaultImageScaleY / yScale);
        int rarityStartY = (int)(c.getHeight() - (45 * yScale));
        int rarityEndY = (int)(c.getHeight() - ( 15 * yScale));
        c.drawRoundRect((13 * xScale), (13 * yScale), c.getWidth() - (13 * xScale), c.getHeight() - (13 * yScale), (18 * xScale), (18 * yScale), cardBackground);
        //c.drawRect((13 * xScale), (13 * yScale), c.getWidth() - (13 * xScale), c.getHeight() - (13 * yScale), cardBackground);
        c.drawRoundRect((3 * xScale), (3 * yScale), c.getWidth() - (3 * xScale), c.getHeight() - (3 * yScale), (25 * xScale), (25 * yScale), cardBorder);
        Log.d("Canvas Height", c.getHeight() + "");
        if(card != null) {
            c.drawText(card.getAvailableMana() + "", (30 * xScale), (58 * yScale), textPaint);
            c.drawText(card.getCardName(), (c.getWidth()/2.0f) - (58 * xScale), (58 * yScale), textPaint);
            c.drawText("LV: " + card.getCardLevel(), (c.getWidth()) - (132 * xScale), (58 * yScale), textPaint);
            c.drawText("HP: " + card.getCardHealth(), (c.getWidth()) - (132 * xScale), (108 * yScale), textPaint);
            c.drawBitmap(card.getCardImage(getContext(), imgXScale, imgYScale), (c.getWidth() / 2.0f) - (92 * xScale), (132 * yScale), textPaint);
            c.drawLine((10 * xScale), (300 * yScale), (c.getWidth()) - (10 * xScale), (300 * yScale), textPaint);
            c.drawText("Attack: " , (30 * xScale), (340 * yScale), textPaint);
            c.drawText(card.getAttackPower() + "", c.getWidth() - (90 * xScale), (340 * yScale), textPaint);
            c.drawLine((10 * xScale), (360 * yScale), c.getWidth() - (10 * xScale), (360 * yScale), textPaint);
            c.drawText("Special: ", (30 * xScale), (400 * yScale), textPaint);
            c.drawText(card.getSpecialAttackPower() + "", c.getWidth() - (90 * xScale), (400 * yScale), textPaint);
            c.drawLine((10 * xScale), (420 * yScale), c.getWidth() - (10 * xScale), (420 * yScale), textPaint);
            c.drawText("Defense: ", (30 * xScale), (460 * yScale), textPaint);
            c.drawText(card.getDefenseRating() + "", c.getWidth() - (90 * xScale), (460 * yScale), textPaint);
            c.drawLine((10 * xScale), (480 * yScale), c.getWidth() - (10 * xScale), (480 * yScale), textPaint);
            rarityImage.setBounds((int)(c.getWidth() - (50 * xScale)), rarityStartY, (int)(c.getWidth() - (10 * xScale)), rarityEndY);
            rarityImage.draw(c);
            switch (rarity){
                case Uncommon:
                    rarityImage.setBounds((int)(c.getWidth() - (115 * xScale)), rarityStartY, (int)(c.getWidth() - (95 * xScale)), rarityEndY);
                    break;
                default:
                    break;
            }
        }
    }

    private Color getCardColor(){
        if(card == null)
            return Color.valueOf(0, 0, 0, 1.0f);
        switch(card.getCardType()){
            case Earth:
                return Color.valueOf(0.0f, 1.0f, 0);
            case Fire:
                return Color.valueOf(1.0f, 0, 0);
            case Air:
                return Color.valueOf(0.3f, 1.0f, 1.0f);
            case Water:
                return Color.valueOf(0.0f, 0.0f, 1.0f);
            case Light:
                return Color.valueOf(1.0f, 1.0f, 1.0f);
            case Dark:
                return Color.valueOf(0.3f, 0.3f, 0.3f);
        }
        return Color.valueOf(1.0f, 1.0f, 1.0f);
    }

    private void init(AttributeSet attrs){
        cardBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        cardBorder.setAntiAlias(true);
        cardBorder.setStyle(Paint.Style.STROKE);
        cardBorder.setStrokeWidth(3.0f);
        cardBorder.setColor(Color.BLACK);

        cardBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        cardBackground.setAntiAlias(true);
        cardBackground.setStyle(Paint.Style.FILL_AND_STROKE);
        cardBackground.setStrokeWidth(20);
        cardBackground.setColor(getCardColor().toArgb());

        //setup textPaint
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(2);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(defaultFontSize);

        //Load rarity bitmap
        rarityImage = getResources().getDrawable(R.drawable.level_indicator);
    }

    public void setCard(Card c){
        Log.d("CardRenderer", "new card set!");
        card = c;
        cardBackground.setColor(getCardColor().toArgb());
        rarityImage.mutate().setColorFilter(0xff001100, PorterDuff.Mode.MULTIPLY);
        rarity = c.getRarity();
        invalidate();
    }

    public void setScale(float xScale, float yScale){
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public void setResolution(int xRes, int yRes){
        Log.d("Setting Res", " new resolution is " + xRes + "x" + yRes);
        this.xScale = (float)xRes / defaultWidth;
        Log.d("XScale", "set to " + xScale);
        this.yScale = (float)yRes / defaultHeight;
        Log.d("YScale", "set to " + yScale);
        setFontSize();
    }

    private void setFontSize(){
        if(xScale > yScale){
            int newSize = (int) Math.floor(defaultFontSize * yScale);
            Log.d("Setting font size", "size set to " + newSize);
            textPaint.setTextSize(newSize);
        }
        else{
            int newSize = (int) Math.floor(defaultFontSize * xScale);
            Log.d("Setting font size", "size set to " + newSize);
            textPaint.setTextSize(newSize);
        }
    }

}
