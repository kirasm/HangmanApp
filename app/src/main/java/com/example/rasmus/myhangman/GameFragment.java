package com.example.rasmus.myhangman;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
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
    private LetterAdapter ltrAdapt;
    private Resources res;
    private View v;
    private ViewGroup container;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_game, container, false);
        this.container = container;

        rand = new Random();
        randWord = "";
        randCategory = "";
        score = 0;

        res = v.getResources();
        wordLayout = (LinearLayout) v.findViewById(R.id.word);
        letters = (GridView) v.findViewById(R.id.letters);
        score_text = (TextView) v.findViewById(R.id.score_text);
        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView) v.findViewById(R.id.mistake1);
        bodyParts[1] = (ImageView) v.findViewById(R.id.mistake2);
        bodyParts[2] = (ImageView) v.findViewById(R.id.mistake3);
        bodyParts[3] = (ImageView) v.findViewById(R.id.mistake4);
        bodyParts[4] = (ImageView) v.findViewById(R.id.mistake5);
        bodyParts[5] = (ImageView) v.findViewById(R.id.mistake6);

        playRound();
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

        for (int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }
        category_word = (TextView) v.findViewById(R.id.category);
        category_word.setText(randCategory);
        letterViews = new TextView[randWord.length()];
        wordLayout.removeAllViews();
        for (int c = 0; c < randWord.length(); c++) {
            letterViews[c] = new TextView(container.getContext());
            letterViews[c].setText("" + randWord.charAt(c));
            letterViews[c].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            letterViews[c].setGravity(Gravity.CENTER);
            letterViews[c].setTextColor(Color.parseColor("#FFFFFF"));
            letterViews[c].setBackgroundResource(R.drawable.letter_background);
            //add to layout
            wordLayout.addView(letterViews[c]);
            ltrAdapt = new LetterAdapter(container.getContext(),this);
            letters.setAdapter(ltrAdapt);

        }
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
                AlertDialog.Builder winBuild = new AlertDialog.Builder(container.getContext());
                winBuild.setTitle("Yay, well done!");
                winBuild.setMessage("You won!\n\nThe answer was:\n\n" + randWord);
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                score = score + 10;
                                playRound();
                            }
                        });

                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                             //   finish();
                            }
                        });

                winBuild.show();
            }
        } else if (currPart < numParts) {
            //some guesses left
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        } else {
            //user has lost
            disableBtns();

            // Display Alert Dialog
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(container.getContext());
            loseBuild.setTitle("Oopsie");
            loseBuild.setMessage("You lose!\n\nThe answer was:\n\n" + randWord);
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            score = 0;
                            playRound();
                        }
                    });

            loseBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //finish();
                        }
                    });

            loseBuild.show();

        }
    }

    public void disableBtns() {
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }



}
