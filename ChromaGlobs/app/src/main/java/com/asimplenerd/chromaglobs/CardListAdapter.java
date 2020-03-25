package com.asimplenerd.chromaglobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CardListAdapter extends ArrayAdapter<Card> {

    private static class ViewHolder{
        private static TextView itemView;
    }

    public CardListAdapter(Context c, int resourceID, ArrayList<Card> cards){
        super(c, resourceID, cards);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Card card = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.trade_card_display_layout, parent, false);
        }
        TextView cardInfoText = (TextView)convertView.findViewById(R.id.cardNameView);
        cardInfoText.setMaxHeight(20);
        cardInfoText.setText("Card Object");
        return convertView;
    }
}
