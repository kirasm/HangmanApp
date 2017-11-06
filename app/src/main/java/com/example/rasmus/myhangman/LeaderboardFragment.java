package com.example.rasmus.myhangman;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rasmus-Laptop on 06/11/2017.
 */

public class LeaderboardFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.leaderboard_fragment, container, false);

        return v;
    }
}
