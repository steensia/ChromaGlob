package com.asimplenerd.chromaglobs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asimplenerd.chromaglobs.Classes.Daily;
import com.asimplenerd.chromaglobs.Classes.DatabaseManagerKt;
import com.asimplenerd.chromaglobs.Classes.MissionType;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dailies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dailies extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Daily daily;

    public Dailies() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dalies.
     */
    // TODO: Rename and change types and number of parameters
    public static Dailies newInstance(String param1, String param2) {
        Dailies fragment = new Dailies();
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
        View v = inflater.inflate(R.layout.fragment_dalies, container, false);

        //Create daily for this display if one does not exist.
        int id = ((MainActivity) getActivity()).user.nextMissionID();
        Log.d("mission id", id+" ");

        Random rand = new Random();
        if(rand.nextBoolean())
            daily = new Daily(false, "desc", false, MissionType.Gold, id, (TextView) v.findViewById(R.id.missionDesc));
        else
            daily = new Daily(false, "desc", false, MissionType.Card, id, (TextView) v.findViewById(R.id.missionDesc));

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();

        setupDaily();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.rewardButton){
            Log.d("ClaimingReward", "User has started claiming a reward");
            daily.claimReward(((MainActivity)getActivity()).user);
        }
    }

    private void setupDaily() {
        DatabaseManagerKt.updatePlayersMissions(((MainActivity)getActivity()).user);

        TextView status = getView().findViewById(R.id.missionStatus);
        TextView desc = getView().findViewById(R.id.missionDesc);
        TextView type = getView().findViewById(R.id.missionType);
        status.setText(daily.getComplete() ? "Complete" : "Incomplete");
        status.setTextColor(daily.getComplete() ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
        desc.setText(daily.getDescription());
        type.setText(daily.getMissionRewardType());

        //Setup button based on completion status
        getView().findViewById(R.id.rewardButton).setEnabled(daily.getComplete());
        getView().findViewById(R.id.rewardButton).setOnClickListener(this);

        //Have daily commit to file
        daily.updateXmlFile();
    }

    public void updateDescription() {
        TextView desc = getView().findViewById(R.id.missionDesc);
        desc.setText(daily.getDescription());
    }

    private void checkDailyStatus(){
        if(!daily.getComplete()){
            DatabaseManagerKt.checkMissionCompletionStatus(((MainActivity) getActivity()).user, daily);

        }
        //Enable the button if a daily is not claimed and is complete.
        getView().findViewById(R.id.rewardButton).setEnabled(daily.getComplete() && (!daily.getClaimed()));
    }
}
