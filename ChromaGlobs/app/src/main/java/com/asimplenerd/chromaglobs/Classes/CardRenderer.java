package com.asimplenerd.chromaglobs.Classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CardRenderer extends View {

    Card card;

    Paint cardBackground;
    Paint textPaint;

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
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), cardBackground);
        if(card != null) {
            c.drawText(card.getAvailableMana() + "", 30, 58, textPaint);
            c.drawText(card.getCardName(), c.getWidth()/2 - 58, 58, textPaint);
            c.drawText("LV: " + card.getCardLevel(), c.getWidth() - 132, 58, textPaint);
            c.drawText("HP: " + card.getCardHealth(), c.getWidth() - 132, 108, textPaint);
            c.drawBitmap(card.getCardImage(getContext()), c.getWidth() / 2 - 92, 132, textPaint);
            c.drawLine(10, 300, c.getWidth() - 10, 300, textPaint);
            c.drawText("Attack: " , 30, 340, textPaint);
            c.drawText(card.getAttackPower() + "", c.getWidth() - 90, 340, textPaint);
            c.drawLine(10, 360, c.getWidth() - 10, 360, textPaint);
            c.drawText("Special: ", 30, 400, textPaint);
            c.drawText(card.getSpecialAttackPower() + "", c.getWidth() - 90, 400, textPaint);
            c.drawLine(10, 420, c.getWidth() - 10, 420, textPaint);
            c.drawText("Defense: ", 30, 460, textPaint);
            c.drawText(card.getDefenseRating() + "", c.getWidth() - 90, 460, textPaint);
            c.drawLine(10, 480, c.getWidth() - 10, 480, textPaint);
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
        textPaint.setTextSize(38);
    }

    public void setCard(Card c){
        Log.d("CardRenderer", "new card set!");
        card = c;
        cardBackground.setColor(getCardColor().toArgb());
        invalidate();
    }

}
