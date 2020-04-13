package com.asimplenerd.chromaglobs.ProfileActivityMap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asimplenerd.chromaglobs.CollectionActivityMap.CollectionActivity;
import com.asimplenerd.chromaglobs.MainActivity;
import com.asimplenerd.chromaglobs.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.findViewById(R.id.decksButton).setOnClickListener(this);
        view.findViewById(R.id.battlesButton).setOnClickListener(this);
        view.findViewById(R.id.friendsButton).setOnClickListener(this);
        view.findViewById(R.id.collectionButton).setOnClickListener(this);
        view.findViewById(R.id.logoutButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.decksButton:
                setupDecks();
                break;
            case R.id.battlesButton:
                setupBattles();
                break;
            case R.id.friendsButton:
                setupFriends();
                break;
            case R.id.collectionButton:
                setupCollection();
                break;
            default:
                Log.d("OnClick", "not handled for item: " + v.getId());
                break;
        }
    }

    private void setupCollection() {
        ((MainActivity) getActivity()).startCollectionsActivity();
    }
    private void setupFriends() {
        ((MainActivity) getActivity()).swapToNewFragment(new FriendsFragment(), true);
    }

    private void setupBattles() {
        ((MainActivity) getActivity()).swapToNewFragment(new BattlesFragment(), true);
    }

    private void setupDecks() {
        ((MainActivity) getActivity()).startDecksActivity();
    }
}
