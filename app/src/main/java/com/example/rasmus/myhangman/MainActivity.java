package com.example.rasmus.myhangman;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        Button game_btn = (Button) findViewById(R.id.game_btn);
        Button leaderboard_btn = (Button) findViewById(R.id.leaderboard_btn);

        game_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragment = new GameFragment();
                changeFragment();
            }
        });

        leaderboard_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragment = new LeaderboardFragment();
                changeFragment();
            }
        });

    }

    public void changeFragment() {

        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

}
