package com.asimplenerd.chromaglobs.TradeActivityMap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.asimplenerd.chromaglobs.CardDisplayFragment;
import com.asimplenerd.chromaglobs.Classes.CardListAdapter;
import com.asimplenerd.chromaglobs.R;
import com.asimplenerd.chromaglobs.Classes.Card;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TradeSetupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TradeSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TradeSetupFragment extends Fragment implements View.OnClickListener, AndroidFragmentApplication.Callbacks {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Card selectedCard;

    private ArrayList<Card> ownedCards = new ArrayList<>();
    private View cardListView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CardDisplayFragment cardDisplay = new CardDisplayFragment();

    public TradeSetupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TradeSetupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TradeSetupFragment newInstance(String param1, String param2) {
        TradeSetupFragment fragment = new TradeSetupFragment();
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
            ArrayList<Card> argCards = getArguments().getParcelableArrayList("ownedCards");
            if(argCards != null) {
                Log.d("TradeCard", "Cards are provided!");
                ownedCards = new ArrayList<>();
                ownedCards.addAll(argCards);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trade_setup, container, false);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart(){
        super.onStart();
        getView().findViewById(R.id.startTradeButton).setOnClickListener(this);
        populateListView();
    }

    @Override
    public void onStop(){
        super.onStop();
        ownedCards.clear();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.startTradeButton){
            beginNFCTrade();
        }
    }

    @Override
    public void exit() {
        //TODO
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

    private void setActiveCard(Card c){
        selectedCard = c;
        //TODO set image based on card info in firebase.
    }

    private void populateListView(){
        ListView listView = getView().findViewById(R.id.ownedCardList);
        final CardListAdapter cardListAdapter = new CardListAdapter(getContext(), R.id.ownedCardList, ownedCards);
        listView.setAdapter(cardListAdapter);
        cardListAdapter.addAll(ownedCards);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setActiveCard(cardListAdapter.getItem(position));

            }
        });
    }

    private void beginNFCTrade(){
        if(selectedCard == null)
            return;
        Toast.makeText(getContext(), "Starting trade using card: " + selectedCard.getCardName(), Toast.LENGTH_SHORT).show();
    }
}
