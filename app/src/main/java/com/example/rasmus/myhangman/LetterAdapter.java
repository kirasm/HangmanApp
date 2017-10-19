package com.example.rasmus.myhangman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class LetterAdapter extends BaseAdapter {
    private String[] letters;
    private LayoutInflater letterInf;

    public LetterAdapter(Context c) {
        letters=new String[26];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = "" + (char)(i+'A');
        }
        //specify the context in which we want to inflate the layout
        // will be passed from the main activity
        letterInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {

        return letters.length;
    }

    @Override
    public Object getItem(int position) {
        // Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //create a button for the letter at this position in the alphabet
        Button letterBtn;
        if (convertView == null) {
            //inflate the button layout
            letterBtn = (Button)letterInf.inflate(R.layout.letter_btn, parent, false);
        } else {
            letterBtn = (Button) convertView;
        }
        //set the text to this letter
        letterBtn.setText(letters[position]);
        return letterBtn;

    }

}
