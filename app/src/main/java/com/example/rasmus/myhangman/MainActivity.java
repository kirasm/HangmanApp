package com.example.rasmus.myhangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] words;
    private String[] categories;
    private Random rand;
    private String currWord;
    private String currCategory;
    private LinearLayout wordLayout;
    private TextView[] letterViews;
    private GridView letters;
    //body part images
    private ImageView[] bodyParts;
    //number of body parts
    private int numParts=6;
    //current part - will increment when wrong answers are chosen
    private int currPart;
    //number of characters in current word
    private int numChars;
    //number correctly guessed
    private int numCorr;
    private LetterAdapter ltrAdapt;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rand = new Random();
        currWord = "";
        currCategory = "";

        res = getResources();
        wordLayout = (LinearLayout) findViewById(R.id.word);
        letters = (GridView) findViewById(R.id.letters);
        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView) findViewById(R.id.mistake1);
        bodyParts[1] = (ImageView) findViewById(R.id.mistake2);
        bodyParts[2] = (ImageView) findViewById(R.id.mistake3);
        bodyParts[3] = (ImageView) findViewById(R.id.mistake4);
        bodyParts[4] = (ImageView) findViewById(R.id.mistake5);
        bodyParts[5] = (ImageView) findViewById(R.id.mistake6);

        playRound();
    }

    private void playRound() {

        //categories = res.getStringArray(R.array.categories);
        //currCategory = categories[rand.nextInt(categories.length)];
        words = res.getStringArray(R.array.brands);
        currWord = words[rand.nextInt(words.length)];
        numCorr = 0;

        for (int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }

        letterViews = new TextView[currWord.length()];
        wordLayout.removeAllViews();
        for (int c = 0; c < currWord.length(); c++) {
            letterViews[c] = new TextView(this);
            letterViews[c].setText("" + currWord.charAt(c));

            letterViews[c].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            letterViews[c].setGravity(Gravity.CENTER);
            letterViews[c].setTextColor(Color.WHITE);
            letterViews[c].setBackgroundResource(R.drawable.letter_background);
            //add to layout
            wordLayout.addView(letterViews[c]);
            ltrAdapt=new LetterAdapter(this);
            letters.setAdapter(ltrAdapt);

        }
    }

    public void letterTry(View view) {
        //user has pressed a letter to guess
        String ltr=((TextView)view).getText().toString();
        char letterChar = ltr.charAt(0);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_tried);

        boolean correct = false;
        for(int k = 0; k < currWord.length(); k++) {
            if (currWord.charAt(k)==letterChar){
                correct = true;
                numCorr++;
                letterViews[k].setTextColor(Color.BLACK);
            }
        }

        if (correct) {
            //correct guess
            if (numCorr == currWord.length()) {
                //user has won
                // Disable Buttons
                disableBtns();

                // Display Alert Dialog
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("Yay, well done!");
                winBuild.setMessage("You won!\n\nThe answer was:\n\n"+currWord);
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.playRound();
                            }
                        });

                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }});

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
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle("Oopsie");
            loseBuild.setMessage("You lose!\n\nThe answer was:\n\n"+currWord);
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.playRound();
                        }});

            loseBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }});

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
