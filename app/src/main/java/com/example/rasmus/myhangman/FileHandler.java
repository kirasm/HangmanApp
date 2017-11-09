package com.example.rasmus.myhangman;

import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Rasmus on 09/11/2017.
 */

public class FileHandler {

    ViewGroup container;

    public FileHandler(ViewGroup container){
        this.container = container;
    }

    public int[] readHighscores() {

        File path = container.getContext().getFilesDir();
        File file = new File(path, "highscores.json");
        FileInputStream inStream = null;

        int length = (int) file.length();

        byte[] bytes = new byte[length];
        try {
            inStream = new FileInputStream(file);
            inStream.read(bytes);

        } catch (Exception e) {

        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception ignore) {
                    // Nothing to do
                }
            }
        }

        String contents = new String(bytes);

        try {

            JSONObject inData = new JSONObject(contents);
            JSONArray inScores = inData.getJSONArray("scores");

            int[] stuff = new int[inScores.length()];
            for (int i = 0; i < inScores.length(); i++) {
                stuff[i] = inScores.getInt(i);
            }

            return stuff;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void writeHighscore(int[] arrayList) {

        File path = container.getContext().getFilesDir();
        File file = new File(path, "highscores.json");

        int[] scoreArray = new int[arrayList.length];
        int i = 0;
        for (int e : arrayList) {
            scoreArray[i++] = e;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("scores", new JSONArray(scoreArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        byte[] data = object.toString().getBytes();

        FileOutputStream outStream = null;
        try {

            outStream = new FileOutputStream(file);
            outStream.write(data);

        } catch (Exception e) {

        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (Exception ignore) {

                }
            }
        }

    }

    public void resetScores(){
        int[] oldArray = {0,0,0,0,0,0,0,0,0,0};
        writeHighscore(oldArray);
    }
}
