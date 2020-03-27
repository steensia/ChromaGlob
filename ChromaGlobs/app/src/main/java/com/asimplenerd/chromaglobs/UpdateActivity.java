package com.asimplenerd.chromaglobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.asimplenerd.chromaglobs.Classes.Card;
import com.asimplenerd.chromaglobs.Classes.GlobType;
import com.asimplenerd.chromaglobs.Classes.Rarity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //Request to start in landscape mode.
    }

    @Override
    public void onStart(){
        super.onStart();
        updateApplication();
    }

    private void updateApplication(){
        //TODO
        Log.d("AppUpdate", "Update started");
        //Create the images dir if it does not exist.
        File imgDir = new File(getBaseContext().getFilesDir(), "images/");
        if(!imgDir.exists()){
            imgDir.mkdir();
        }
        checkForApplicationContentUpdate();
        launchMainActivity();
    }

    private void checkForApplicationContentUpdate(){
        final String localVersion = getLocalAppVersion();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //Check db version.
        db.getReference("AppInfo").child("version").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Check that the version matches the current app version
                    Log.d("ServerAppVersion", dataSnapshot.getValue().toString());
                    if(!localVersion.equals(dataSnapshot.getValue())){
                        Log.d("AppUpdate", "Application version mismatch. Updating to version " + dataSnapshot.getValue().toString());
                        Updater updater = new Updater();
                        updater.doInBackground(0);
                    }
                    else{
                        Log.d("AppUpdate", "Application is up to date. No update needed. Starting MainActivity");
                        launchMainActivity();
                    }
                }
                else{
                    showConnectionError(); //The server version info is missing... Something bad happened!
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("AppUpdate", "Application update failed. App closing!");
                showConnectionError();
            }
        });
    }

    private String getLocalAppVersion(){
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream(new File(getResources().getString(R.string.update_file)));
            XmlReader xmlReader = new XmlReader();
            String appName = xmlReader.parse(fileIn).getAttribute("appName");
            String appVersion = xmlReader.parse(fileIn).getAttribute("appVersion");
            if(!appName.equals(getResources().getString(R.string.app_name))){
                return "-2.0"; //app mismatch? TODO
            }
            return appVersion;
        }catch (FileNotFoundException e){
            return "-1.0"; //There is no version file, so this is the base installed app. update time.
        }
    }

    private void showConnectionError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.connection_error);
        builder.setMessage(R.string.connection_error_message);
        builder.setPositiveButton(R.string.positive_button_default, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Application should now close.
                finish();
            }
        });
        builder.show();
    }

    private void launchMainActivity(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }



    class Updater extends AsyncTask<Integer, Integer, Boolean> {

        ArrayList<Card> allCards = new ArrayList<>();

        @Override
        protected Boolean doInBackground(Integer... integers) {
            allCards = fetchCards();
            return true;
        }

        @Override
        public void onProgressUpdate(Integer... update){
            int updatePercentage = update[0];
            if(updatePercentage == 100){
                launchMainActivity();
            }
            else{
                ProgressBar pb = findViewById(R.id.progressBar);
                if(updatePercentage == 0)
                    pb.setVisibility(View.VISIBLE);
                pb.setProgress(updatePercentage);
            }

        }

        private ArrayList<Card> fetchCards(){
            final ArrayList<Card> cardList = new ArrayList<>();
            Log.d("AppUpdate", "Downloading cards...");
            FirebaseDatabase.getInstance().getReference("CardList").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Log.d("AppUpdate", "Found card list in remote DB");
                        Object cardMap = dataSnapshot.getValue();
                        Log.d("AppUpdate", "Retrieved map of card objects");
                        Log.d("AppUpdate", "Retrieval type is " + cardMap);
                        addCards(cardList, cardMap);
                        Log.d("AppUpdate", "All new cards added to cardlist");
                    }
                    else{
                        showConnectionError();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showConnectionError();
                }
            });
            return cardList;
        }

        private void addCards(ArrayList<Card> dest, Object cardList){
            ArrayList<HashMap<String, Object>> cardsList = ((ArrayList<HashMap<String, Object>>) cardList);
            Log.d("CardListType", cardsList.getClass().getName());
            //Iterate through each card extracting info
            for(int i = 0; i < cardsList.size(); i++){
                Card c = mapToCard(cardsList.get(i), i);
                dest.add(c);
                publishProgress((100/(i + 1)));
            }
        }

        private Card mapToCard(HashMap<String, Object> map, int id){
            String name = map.get("Name").toString();
            GlobType type = getGlobType(map.get("Type").toString());
            int health = Integer.parseInt(map.get("Health").toString());
            int ap = Integer.parseInt(map.get("AP").toString());
            int dp = Integer.parseInt(map.get("DP").toString());
            Rarity rarity = getGlobRarity(map.get("Rarity").toString());
            int cardId = id;
            Card c = new Card(name, type, health, ap, dp, rarity, cardId);
            Log.d("CardCreation", "Card created: " + c.getCardName() + " with id: " + c.getId());
            //Download the image associated with the card
            downloadCardImage(map.get("imgSrc").toString() + ".png");
            return c;
        }

        private GlobType getGlobType(String typeString){
            switch(typeString.toLowerCase()){
                case "air":
                    return GlobType.Air;
                case "earth":
                    return GlobType.Earth;
                case "fire":
                    return GlobType.Fire;
                case "water":
                    return GlobType.Water;
                case "light":
                    return GlobType.Light;
                case "dark":
                    return GlobType.Dark;
                default:
                    return GlobType.Unknown;
            }
        }

        private Rarity getGlobRarity(String rarity){
            switch(rarity.toLowerCase()){
                case "common":
                    return Rarity.Common;
                case "uncommon":
                    return Rarity.Uncommon;
                case "rare":
                    return Rarity.Rare;
                case "arcane":
                    return Rarity.Arcane;
                case "unique":
                    return Rarity.Unique;
                case "legendary":
                    return Rarity.Legendary;
                case "chroma":
                    return Rarity.Chroma;
            }
            return Rarity.Common;
        }

        private void downloadCardImage(String imageLoc){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Log.d("ImageFetch", "Retrieving image from " + getResources().getString(R.string.card_image_url) + imageLoc);
            File image = new File(getBaseContext().getFilesDir(), "images/" + imageLoc);
            if(!image.exists()){
                try {
                    image.createNewFile();
                }catch(IOException e){
                    Log.e("ImageFetch", "Unable to create files! Error: " + e.getLocalizedMessage());
                    return;
                }
            }
            //Fetch the image file
            storage.getReference(getResources().getString(R.string.card_image_url) + imageLoc).getFile(image);
        }
    }

}