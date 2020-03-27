package com.asimplenerd.chromaglobs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asimplenerd.chromaglobs.Classes.Daily;
import com.asimplenerd.chromaglobs.Classes.MissionType;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dalies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dalies extends Fragment {
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
        //Create daily for this display if one does not exist.
        daily = new Daily(false, "doggo!!", false, MissionType.Gold);
        //Set daily button action based on mission reward type.

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
        setupDaily();
    }

    private void setupDaily() {
        TextView status = getView().findViewById(R.id.missionStatus);
        TextView desc = getView().findViewById(R.id.missionDesc);
        status.setText(daily.getComplete() ? "Complete" : "Incomplete");
        status.setTextColor(daily.getComplete() ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
        desc.setText(daily.getDescription());

        //Setup button based on completion status
        getView().findViewById(R.id.missionsButton).setEnabled(daily.getComplete());
    }
}
