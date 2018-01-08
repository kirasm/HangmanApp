package com.example.rasmus.myhangman;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends BaseAdapter {
    private LayoutInflater letterInf;

    private ArrayList wordList;
    private Context c;
    private SharedPreferences mySharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    public WordAdapter(Context c, ArrayList wordList) {
        this.c = c;
        this.wordList = wordList;
        letterInf = LayoutInflater.from(c);

        mySharedPreferences = c.getSharedPreferences("WORDLIST", Activity.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
    }

    @Override
    public int getCount() {

        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        // Auto-generated method stub
        return wordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //create a button for the letter at this position in the alphabet
        Button letterBtn;
        LayoutInflater f = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = f.inflate(R.layout.word_item, null);
        CheckBox checkBox = convertView.findViewById(R.id.word_added);
        TextView textView = (TextView) convertView.findViewById(R.id.text2);

        textView.setText(wordList.get(position).toString());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    boolean isChecked = ((CheckBox)v).isChecked();
                        editor.putString(wordList.get(position).toString(), null);
                    }
                });

        return convertView;

    }


}
