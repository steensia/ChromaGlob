package com.asimplenerd.chromaglobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.net.URI;

public class MainActivity extends FragmentActivity implements LoginFields.OnFragmentInteractionListener{
    Fragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Request to start in landscape mode.
        frag = new LoginFields();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("Fragment", "Got uri: " + uri.toString());
    }
}
