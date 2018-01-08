package com.example.rasmus.myhangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Rasmus-Laptop on 06/11/2017.
 */

public class GameFragment extends Fragment {

    private String[] words;
    private Random rand;
    private String randWord;
    private String randCategory;
    private LinearLayout wordLayout;
    private TextView[] selectedWord;
    private TextView category_word;
    private TextView score_text;
    private GridView letters;
    private ImageView[] parts;
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
    private ArrayList<String> drWordList = new ArrayList<>();
    private SharedPreferences mySharedPreferences;

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
        parts = new ImageView[numParts];
        parts[0] = (ImageView) v.findViewById(R.id.mistake1);
        parts[1] = (ImageView) v.findViewById(R.id.mistake2);
        parts[2] = (ImageView) v.findViewById(R.id.mistake3);
        parts[3] = (ImageView) v.findViewById(R.id.mistake4);
        parts[4] = (ImageView) v.findViewById(R.id.mistake5);
        parts[5] = (ImageView) v.findViewById(R.id.mistake6);

        fileHandler = new FileHandler(container);
        NetworkHandler n = new NetworkHandler();

        mySharedPreferences = container.getContext().getSharedPreferences("WORDLIST", Activity.MODE_PRIVATE);

        if(drWordList.isEmpty()){
            drWordList = getWords(n);

        }

        playRound();

        return v;
    }

    private String getRandWord() {

        ArrayList<String> usedWords = new ArrayList();

        if (drWordList.size() != 0) {
            String[] returnedWords = new String[drWordList.size()];
            for (int i = 0; i < drWordList.size(); i++) {
                returnedWords[i] = drWordList.get(i).toUpperCase();
            }
            words = returnedWords;
        } else {
            words = res.getStringArray(R.array.wordlist);
        }

        boolean check = false;
        while (!check) {
           if(mySharedPreferences.getString("0",null) == null){
               randWord = words[rand.nextInt(words.length)];
           } else {
               randWord = mySharedPreferences.getString(rand.nextInt()+"", null);
           }
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
            parts[i].setVisibility(View.INVISIBLE);
        }

        category_word.setText(randCategory);
        selectedWord = new TextView[randWord.length()];
        wordLayout.removeAllViews();
        for (int i = 0; i < randWord.length(); i++) {
            selectedWord[i] = new TextView(container.getContext());
            selectedWord[i].setText("" + randWord.charAt(i));
            selectedWord[i].setTextColor(Color.parseColor("#FFFFFF"));
            selectedWord[i].setBackgroundResource(R.drawable.blank);
            //add to layout
            wordLayout.addView(selectedWord[i]);
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
                selectedWord[k].setTextColor(Color.BLACK);
            }
        }

        if (correct) {
            //correct guess
            if (numCorr == randWord.length()) {
                //user has won
                // Disable Buttons
                disableBtns();

                checkHighScore(score + 10);
                Bundle b = new Bundle();
                b.putString("word",randWord);
                b.putInt("score",score);
                winLoseScreen(new WinningFragment(), b);

            }
        } else if (currPart < numParts) {
            //some guesses left
            //
            parts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        } else {
            //user has lost
            disableBtns();
            tries--;
            Bundle b = new Bundle();
            b.putString("word",randWord);
            b.putInt("score",score);

            winLoseScreen(new LosingFragment(),b);

            if (tries > 0) {
                playRound();
            } else {
                checkHighScore(score);
                reset();
                playRound();
            }

        }
    }

    public void disableBtns() {
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }

    public ArrayList getWords(NetworkHandler n) {

        Thread thread = new Thread(() -> {
            try {
                drWordList = n.hentOrdFraDr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            thread.start();
            thread.join();

        } catch (Exception e) {

        }
        return drWordList;
    }


    public boolean checkHighScore(int score) {

        int[] scoreList = fileHandler.readHighscores();
        int temp;
        Arrays.sort(scoreList);

        if (scoreList[scoreList.length - 1] < score) {
            int[] newScoreList = new int[scoreList.length + 1];
            for (int i = 0; i < scoreList.length; i++) {
                newScoreList[i] = scoreList[i];
            }
            newScoreList[newScoreList.length - 1] = score;

            for (int i = 0; i < newScoreList.length / 2; i++) {
                temp = newScoreList[i];
                newScoreList[i] = newScoreList[newScoreList.length - 1 - i];
                newScoreList[newScoreList.length - 1 - i] = temp;
            }

            fileHandler.writeHighscore(newScoreList);
            return true;
        } else {
            return false;
        }

    }

    public void reset() {
        randWord = "";
        randCategory = "";
        score = 0;
        tries = 3;
    }

    public void winLoseScreen(Fragment fragment, Bundle b) {

        fragment.setArguments(b);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("d")
                .commit();
    }

}
