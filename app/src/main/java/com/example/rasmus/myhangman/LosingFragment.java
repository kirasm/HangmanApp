package com.example.rasmus.myhangman;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rasmus-Laptop on 06/11/2017.
 */

public class LosingFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.losing_screen, container, false);

        savedInstanceState = this.getArguments();

        TextView message1 = v.findViewById(R.id.message1);
        TextView message2 = v.findViewById(R.id.message2);
        message1.setText("Too bad! The right word was: " + savedInstanceState.getString("word"));
        message2.setText("Your made a score of: " +  savedInstanceState.getInt("score"));
        Button back = v.findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }

            }
        });


        return v;
    }
}
