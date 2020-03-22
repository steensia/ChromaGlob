package com.asimplenerd.chromaglobs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.asimplenerd.chromaglobs.Classes.Player;
import com.asimplenerd.chromaglobs.LoginActivityMap.LoginFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity implements LoginFragment.OnFragmentInteractionListener{
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
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.login_fragment_layout, loginFragment).commit();
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
        if(user.username.isEmpty()){

        }
    }

    public void askForNewUsername(final DataSnapshot dataSnapshot){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Welcome!");
        dialogBuilder.setMessage("Please enter your desired username. This is how you will appear to others online.");
        final EditText userField = new EditText(this);
        dialogBuilder.setView(userField);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Trying create username", userField.getText().toString());
                String user = userField.getText().toString();
                validateUsername(user, dataSnapshot);
                dialog.dismiss();
            }
        });
        dialogBuilder.show();
        Log.d("UsernameInput", "Should be shown");
    }

    class UsernameChecker implements ValueEventListener{
        boolean usernameTaken = false;

        public boolean getResult(){
            return usernameTaken;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Log.d("UsernameCreation", "Username Conflict!");
                usernameTaken = true;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            usernameTaken = true;
        }
    }

    private void addUserToDB(Player user){
        Log.d("UsernameUpdate", "Adding user to db tables");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //Update user selected display name
        Map<String, Object> usernameMap = new HashMap<>();
        usernameMap.put("username", user.username);
        db.getReference().child("Users").child(user.id).updateChildren(usernameMap);
        //Add this name to the usernames list to prevent duplicate usernames later.
        Map<String, Object> usernameTable = new HashMap<>();
        usernameTable.put(user.username, user.id);
        db.getReference().child("Players").updateChildren(usernameTable);
    }

    private void validateUsername(String username, DataSnapshot dataSnapshot){
        boolean nameNotTaken = false;
        //Check the players table to ensure that this username isn't already being used.
        if(!username.trim().isEmpty()) {
            Log.d("UsernameCreation", "Checking for existing user with name: " + username);
            UsernameChecker usernameChecker = new UsernameChecker();
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            db.getReference().child("Players").child(username).addListenerForSingleValueEvent(usernameChecker);
            if(!usernameChecker.getResult()) {
                Log.d("UsernameCreation", "No username found. Creating new username!");
                user.username = username;
                addUserToDB(user);
            }
            else{
                askForNewUsername(dataSnapshot);
            }
        }
        else
            askForNewUsername(dataSnapshot);
    }

}
