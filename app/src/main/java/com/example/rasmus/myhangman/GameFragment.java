package com.example.rasmus.myhangman;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Rasmus-Laptop on 06/11/2017.
 */

public class GameFragment extends Fragment {

    private String[] words;
    private String[] categories;
    private Random rand;
    private String randWord;
    private String randCategory;
    private LinearLayout wordLayout;
    private TextView[] letterViews;
    private TextView category_word;
    private TextView score_text;
    private GridView letters;
    private ImageView[] bodyParts;
    private int numParts = 6;
    private int currPart;
    private int numCorr;
    private int score;
    private int tries;
    private LetterAdapter ltrAdapt;
    private Resources res;
    private View v;
    private ViewGroup container;
    private FileHandler fileHandler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_game, container, false);
        this.container = container;

        rand = new Random();
        reset();

        res = v.getResources();
        wordLayout = (LinearLayout) v.findViewById(R.id.word);
        letters = (GridView) v.findViewById(R.id.letters);
        score_text = (TextView) v.findViewById(R.id.score_text);
        category_word = (TextView) v.findViewById(R.id.category);
        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView) v.findViewById(R.id.mistake1);
        bodyParts[1] = (ImageView) v.findViewById(R.id.mistake2);
        bodyParts[2] = (ImageView) v.findViewById(R.id.mistake3);
        bodyParts[3] = (ImageView) v.findViewById(R.id.mistake4);
        bodyParts[4] = (ImageView) v.findViewById(R.id.mistake5);
        bodyParts[5] = (ImageView) v.findViewById(R.id.mistake6);

        playRound();

        fileHandler = new FileHandler(container);


        return v;
    }

    private String getRandWord() {

        ArrayList<String> usedWords = new ArrayList();
        ArrayList<String> usedCategories = new ArrayList<>();

        categories = res.getStringArray(R.array.categories);

        Boolean check = false;
        while (!check) {
            randCategory = categories[rand.nextInt(categories.length)];

            if (!usedCategories.contains(randCategory)) {
                usedCategories.add(randCategory);
                check = true;
            }
        }

        words = res.getStringArray(res.getIdentifier(randCategory, "array", container.getContext().getPackageName()));

        check = false;
        while (!check) {
            randWord = words[rand.nextInt(words.length)];

            if (!usedWords.contains(randWord)) {
                usedWords.add(randWord);
                check = true;
            }

        }
        return randWord;

    }

    private void playRound() {

        numCorr = 0;
        currPart = 0;

        score_text.setText(String.valueOf(score));
        String randWord = getRandWord();

        for (int i = 0; i < numParts; i++) {
            bodyParts[i].setVisibility(View.INVISIBLE);
        }


        category_word.setText(randCategory);
        letterViews = new TextView[randWord.length()];
        wordLayout.removeAllViews();
        for (int i = 0; i < randWord.length(); i++) {
            letterViews[i] = new TextView(container.getContext());
            letterViews[i].setText("" + randWord.charAt(i));
            letterViews[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            letterViews[i].setGravity(Gravity.CENTER);
            letterViews[i].setTextColor(Color.parseColor("#FFFFFF"));
            letterViews[i].setBackgroundResource(R.drawable.blank_letter);
            //add to layout
            wordLayout.addView(letterViews[i]);

        }

        ltrAdapt = new LetterAdapter(container.getContext(), this);
        letters.setAdapter(ltrAdapt);
    }

    public void letterTry(View view) {
        //user has pressed a letter to guess
        String ltr = ((TextView) view).getText().toString();
        char letterChar = ltr.charAt(0);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_tried);

        boolean correct = false;
        for (int k = 0; k < randWord.length(); k++) {
            if (randWord.charAt(k) == letterChar) {
                correct = true;
                numCorr++;
                letterViews[k].setTextColor(Color.BLACK);
            }
        }

        if (correct) {
            //correct guess
            if (numCorr == randWord.length()) {
                //user has won
                // Disable Buttons
                disableBtns();

                // Display Alert Dialog
                AlertDialog.Builder popupWin = new AlertDialog.Builder(container.getContext());
                popupWin.setTitle("There you go!");
                popupWin.setMessage("You win!\n\nAnswer:\n\n" + randWord + "\n\nScore: " + score + "               " + "Tries left: " + tries);
                popupWin.setPositiveButton("Next word",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                score = score + 10;
                                playRound();
                            }
                        });

                popupWin.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //   finish();
                            }
                        });

                popupWin.show();
            }
        } else if (currPart < numParts) {
            //some guesses left
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        } else {
            //user has lost
            disableBtns();
            tries--;
            // Display Alert Dialog
            AlertDialog.Builder popupLose = new AlertDialog.Builder(container.getContext());
            popupLose.setTitle("Not this time");
            popupLose.setMessage("You lose!\n\nAnswer:\n\n" + randWord + "\n\nScore: " + score + "               " + "Tries left: " + tries);
            popupLose.setPositiveButton("Try again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (tries > 0) {
                                playRound();
                            } else {
                                checkHighScore(score);
                                reset();
                                playRound();
                            }
                        }
                    });

            popupLose.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //finish();
                        }
                    });

            popupLose.show();


        }
    }

    public void disableBtns() {
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }

    public boolean checkHighScore(int score) {

        Log.i("Other test", fileHandler.readHighscores().toString());
        int[] scoreList = fileHandler.readHighscores();

        int temp;



        Arrays.sort(scoreList);


        if(scoreList[scoreList.length-1] < score){
            int[] newScoreList = new int[scoreList.length+1];
            for (int i = 0; i < scoreList.length; i++) {
                newScoreList[i] = scoreList[i];
            }
            newScoreList[newScoreList.length-1] = score;

            for (int i = 0; i < newScoreList.length/2; i++)
            {
                temp = newScoreList[i];

                newScoreList[i] = newScoreList[newScoreList.length-1-i];

                newScoreList[newScoreList.length-1-i] = temp;
            }

            for(int a : newScoreList){
                Log.i("After reverse", a+"");
            }

            fileHandler.writeHighscore(newScoreList);
            displayHighscore(newScoreList);
            return true;
        } else {
            return false;
        }

    }

    public void displayHighscore(int[] scoreList){



    }



    public void reset() {
        randWord = "";
        randCategory = "";
        score = 0;
        tries = 3;
    }


}
