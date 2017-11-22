package com.example.rasmus.myhangman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class LetterAdapter extends BaseAdapter {
    private String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","Æ","Ø","Å"};
    private LayoutInflater letterInf;
    private GameFragment g;

    public LetterAdapter(Context c, GameFragment g) {
        this.g = g;
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
        letterBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                g.letterTry(v);
            }
        });
        return letterBtn;

    }


}
