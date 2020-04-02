package com.asimplenerd.chromaglobs.CollectionActivityMap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.asimplenerd.chromaglobs.Classes.Card;
import com.asimplenerd.chromaglobs.Classes.CardRenderer;
import com.asimplenerd.chromaglobs.Classes.Deck;
import com.asimplenerd.chromaglobs.Classes.GlobType;
import com.asimplenerd.chromaglobs.Classes.Rarity;
import com.asimplenerd.chromaglobs.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    ArrayList<Deck> userDecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Request to start in landscape mode.
        setupCardDisplay();
        setupTestDeck();
    }

    private void setupCardDisplay() {
        ViewGroup viewRoot = (ViewGroup) findViewById(R.id.frame_holder).findViewById(R.id.card_displays);
        for(int i = 0; i < viewRoot.getChildCount(); ++i){
            CardRenderer renderer = (CardRenderer)((ViewGroup)viewRoot.getChildAt(i)).getChildAt(0);
            renderer.setCard(new Card("Test", i % 4 == 0 ? GlobType.Fire : i % 3 == 0 ? GlobType.Dark : i % 2 == 0 ? GlobType.Air : GlobType.Light, 10, 10, 10, Rarity.Chroma, 23 + i));
        }
    }

    private void setupTestDeck(){
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("Angry", GlobType.Dark, 10, 10, 10, Rarity.Common, 0));
        cards.add(new Card("Spoopy", GlobType.Fire, 4, 12, 43, Rarity.Rare, 32));
        Deck testDeck = new Deck(cards);
        testDeck.writeToXml(getApplicationContext().getFilesDir() + "/decks");
    }
}
