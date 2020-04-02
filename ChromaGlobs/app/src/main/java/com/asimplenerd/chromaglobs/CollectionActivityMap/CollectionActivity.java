package com.asimplenerd.chromaglobs.CollectionActivityMap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.asimplenerd.chromaglobs.Classes.Card;
import com.asimplenerd.chromaglobs.Classes.CardRenderer;
import com.asimplenerd.chromaglobs.Classes.GlobType;
import com.asimplenerd.chromaglobs.Classes.Rarity;
import com.asimplenerd.chromaglobs.R;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Request to start in landscape mode.

        setupCardDisplay();
    }

    private void setupCardDisplay() {
        ViewGroup viewRoot = (ViewGroup) findViewById(R.id.activity_collection).getRootView();

        for(int i = 0; i < viewRoot.getChildCount() ; i++){
            if(viewRoot.getChildAt(i) instanceof FrameLayout){
                Log.d("FrameLayout", "Got a FrameLayout");
                FrameLayout frameLayout = (FrameLayout) viewRoot.getChildAt(i);
                CardRenderer cardRenderer = (CardRenderer) frameLayout.getChildAt(0);
                cardRenderer.setCard(new Card("A", GlobType.Water, 100, 10, 10, Rarity.Common, 0));
                cardRenderer.invalidate();
            }
            else{
                Log.d("Check Type", viewRoot.getChildAt(i).getClass().getCanonicalName());
            }
        }
    }
}
