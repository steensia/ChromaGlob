package com.asimplenerd.chromaglobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity implements LoginFields.OnFragmentInteractionListener{
    Fragment frag;
    Player user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Request to start in landscape mode.
        if(findViewById(R.id.login_fragment_layout) != null){
            if(savedInstanceState != null){
                return;
            }
            LoginFields loginFields = new LoginFields();
            getSupportFragmentManager().beginTransaction().add(R.id.login_fragment_layout, loginFields).commit();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("Fragment", "Got uri: " + uri.toString());
    }

    public void swapToNewFragment(Fragment newFrag, boolean shouldStore){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(shouldStore){
            ft.addToBackStack(null);
        }
        ft.replace(R.id.login_fragment_layout, newFrag).commit();
    }

    public void launchGameActivity(){
        Log.d("ActivityChange", "Swapping to libGDX enabled activity");
        Intent gameLauncher = new Intent(this, ChromaGlobsGame.class);
        startActivity(gameLauncher);
    }

    public void setPlayer(Player p){
        user = p;
    }

}
