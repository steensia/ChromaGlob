package com.asimplenerd.chromaglobs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asimplenerd.chromaglobs.Classes.Daily;
import com.asimplenerd.chromaglobs.Classes.DatabaseManagerKt;
import com.asimplenerd.chromaglobs.Classes.MissionType;

import org.w3c.dom.Text;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dalies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dalies extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Daily daily;

    public Dalies() {
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
    public static Dalies newInstance(String param1, String param2) {
        Dalies fragment = new Dalies();
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
        return inflater.inflate(R.layout.fragment_dalies, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        //Create daily for this display if one does not exist.
        // TODO: Randomly pull a description from the DB
        Random rand = new Random();
        int id = rand.nextInt(7);

        daily = new Daily(false, "desc", false, MissionType.Gold, id, (TextView) getView().findViewById(R.id.missionDesc));
        DatabaseManagerKt.getMissionDesc(id, daily);

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
        TextView status = getView().findViewById(R.id.missionStatus);
        TextView desc = getView().findViewById(R.id.missionDesc);
        TextView type = getView().findViewById(R.id.missionType);
        status.setText(daily.getComplete() ? "Complete" : "Incomplete");
        status.setTextColor(daily.getComplete() ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
        desc.setText(daily.getDescription());
        type.setText(daily.getMissionRewardType());

        //Setup button based on completion status
        getView().findViewById(R.id.rewardButton).setEnabled(!daily.getComplete());
        getView().findViewById(R.id.rewardButton).setOnClickListener(this);
    }
}
