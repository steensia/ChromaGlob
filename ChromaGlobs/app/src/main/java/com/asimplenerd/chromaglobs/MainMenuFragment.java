package com.asimplenerd.chromaglobs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asimplenerd.chromaglobs.Classes.Card;
import com.asimplenerd.chromaglobs.Classes.DatabaseManagerKt;
import com.asimplenerd.chromaglobs.Classes.Player;
import com.asimplenerd.chromaglobs.SettingsActivityMap.SettingsFragment;
import com.asimplenerd.chromaglobs.ShopActivityMap.ShopFragment;
import com.asimplenerd.chromaglobs.TradeActivityMap.TradeSetupFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean usernamePromptShown = false;

    private OnFragmentInteractionListener mListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        view.findViewById(R.id.playButton).setOnClickListener(this);
        view.findViewById(R.id.tradeButton).setOnClickListener(this);
        view.findViewById(R.id.collectionButton).setOnClickListener(this);
        view.findViewById(R.id.missionsButton).setOnClickListener(this);
        view.findViewById(R.id.shopButton).setOnClickListener(this);
        view.findViewById(R.id.settingsButton).setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserInfo();
        DatabaseManagerKt.updatePlayersMissions(((MainActivity) getActivity()).user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playButton:
                ((MainActivity) getActivity()).launchGameActivity();
                break;
            case R.id.tradeButton:
                // Move to the trade fragment, and prepare for a trade
                setupTrade();
                break;
            case R.id.settingsButton:
                // Move to the settings fragment
                setupSettings();
                break;
            case R.id.missionsButton:
                setupMissions();
                break;
            case R.id.collectionButton:
                setupCollection();
            case R.id.shopButton:
                setupShop();
            default:
                Log.d("OnClick", "not handled for item: " + v.getId());
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupTrade() {
        MainActivity mainActivity = (MainActivity) getActivity();
        TradeSetupFragment tradeFrag = new TradeSetupFragment();
        Bundle ownedCardBundle = new Bundle();
        ownedCardBundle.putParcelableArrayList("ownedCards", mainActivity.getOwnedCards());
        tradeFrag.setArguments(ownedCardBundle);
        mainActivity.swapToNewFragment(tradeFrag, true);
    }

    private void setupSettings() {
        ((MainActivity) getActivity()).swapToNewFragment(new SettingsFragment(), true);
    }

    private void setupMissions() {
        ((MainActivity) getActivity()).swapToNewFragment(new Missions(), true);
    }

    private void setupShop() {
        ((MainActivity) getActivity()).swapToNewFragment(new ShopFragment(), true);
    }

    private void setupCollection() {
        ((MainActivity) getActivity()).startCollectionsActivity();
    }

    private void getUserInfo() {
        usernamePromptShown = true;
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        db.getReference().child("Users").child(user.getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            final MainActivity mainActivity = (MainActivity) getActivity();
            private boolean promptShown = false;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.getValue() == null || dataSnapshot.getValue() == "") && !promptShown) {
                    Log.d("UserInfo", "No user");
                    promptShown = true;
                    //user is brand new. Create its data
                    DatabaseReference info = db.getReference().child("Users");
                    mainActivity.askForNewUsername(dataSnapshot);
                    Player p = new Player(((MainActivity) getActivity()).user.username, user.getUid(), new ArrayList<Card>());
                    Map playerList = new HashMap<>();
                    playerList.put(user.getUid(), p);
                    info.updateChildren(playerList);
                    Log.d("DBMod", "Added UID Field to Users db");
                } else {
                    Log.d("DBMod", "User already setup on UID");
                    Log.d("DBMod", "Username was: " + dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void updateUserLogin() {

        DatabaseManagerKt.updatePlayerLogin(((MainActivity) getActivity()).user);

    }
}
