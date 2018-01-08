package com.example.rasmus.myhangman;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Rasmus-Laptop on 06/11/2017.
 */

public class WordlistFragment extends Fragment {

    ArrayAdapter<String> aa;
    ListView list;
    private ArrayList<String> drWordList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.wordlist, container, false);

        NetworkHandler n = new NetworkHandler();

        Thread thread = new Thread(() -> {
            try {
                drWordList = n.hentOrdFraDr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();

        }catch (Exception e){

        }

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), R.layout.word_item, drWordList);
        list = (ListView) v.findViewById(R.id.wordlist_listview);
        list.setAdapter(new WordAdapter(container.getContext(), drWordList));
        String[] returnedWords = new String[drWordList.size()];

        for(int i = 0; i < drWordList.size(); i++){
            returnedWords[i] = drWordList.get(i).toUpperCase();
        }

        aa = new ArrayAdapter(container.getContext(), R.layout.word_item, R.id.text2, returnedWords);
       //list.setAdapter(aa);

        return v;
    }
}
