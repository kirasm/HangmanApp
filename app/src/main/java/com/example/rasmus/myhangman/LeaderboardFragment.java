package com.example.rasmus.myhangman;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Rasmus-Laptop on 06/11/2017.
 */

public class LeaderboardFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.leaderboard_fragment, container, false);

        ListView list = (ListView) v.findViewById(R.id.leaderboard_listview);
        final String[] stringArray = getResources().getStringArray(R.array.highscores);
        ArrayList<String> stringArrayList = new ArrayList<>(Arrays.asList(stringArray));
        Collections.sort(stringArrayList);
        Collections.reverse(stringArrayList);
        String[] sortedArray = stringArrayList.toArray(new String[0]);
        String[] sortedPresentableArray = new String[sortedArray.length];
        int i = 0;
        for (String score : sortedArray) {
            sortedPresentableArray[i] = i+1 + ":   " + score;
            i++;
        }
        final ArrayAdapter<String> aa = new ArrayAdapter(container.getContext(), R.layout.list_item, R.id.text, sortedPresentableArray);
        list.setAdapter(aa);

        return v;
    }
}
