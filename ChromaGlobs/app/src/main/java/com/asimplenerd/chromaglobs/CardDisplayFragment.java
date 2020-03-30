package com.asimplenerd.chromaglobs;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.asimplenerd.chromaglobs.Classes.Card;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;


public class CardDisplayFragment extends AndroidFragmentApplication{

    // return the GLSurfaceView on which libgdx is drawing game stuff
    public GDXCardRenderer cardRenderer = new GDXCardRenderer();

    public CardDisplayFragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View app =  initializeForView(cardRenderer);
        return app;
    }

    public void setSelectedCard(Card c){
        cardRenderer.updateCard(c);
    }

}
