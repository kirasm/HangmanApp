package com.example.rasmus.myhangman;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Rasmus-Laptop on 06/11/2017.
 */

public class LeaderboardFragment extends Fragment {

    ArrayAdapter<String> aa;
    ListView list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.leaderboard_fragment, container, false);

        final FileHandler fileHandler = new FileHandler(container);

        Button reset = (Button) v.findViewById(R.id.reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fileHandler.resetScores();
                list.setAdapter(aa);
            }
        });

        list = (ListView) v.findViewById(R.id.leaderboard_listview);
        int[] scoreList = fileHandler.readHighscores();
        if(scoreList == null){
            fileHandler.resetScores();
            scoreList = fileHandler.readHighscores();
        }

        String[] stringArray = new String[scoreList.length];
        String[] stringArrayPresentable = new String[scoreList.length];
        for(int i = 0; i < scoreList.length;i++){
            stringArray[i] = scoreList[i] + "";
        }
        int i = 0;
        for (String score : stringArray) {
            stringArrayPresentable[i] = i+1 + ":   " + score;
            i++;
        }
        aa = new ArrayAdapter(container.getContext(), R.layout.list_item, R.id.text, stringArrayPresentable);
        list.setAdapter(aa);

        return v;
    }
}
