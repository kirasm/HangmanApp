package com.example.rasmus.myhangman;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Rasmus on 08/01/2018.
 */

public class DrLoading extends Activity {

    NetworkHandler n;
    ArrayList<String> drWordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        startHeavyProcessing();
        n = new NetworkHandler();

    }

    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            //some heavy processing resulting in a Data String
                try {
                        try {
                            drWordList = n.hentOrdFraDr();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                } catch (Exception e) {
                    Thread.interrupted();
                }
            return drWordList;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            Intent i = new Intent(DrLoading.this, MainActivity.class);
            i.putExtra("data", result);
            startActivity(i);
            finish();
        }



        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
