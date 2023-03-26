package com.example.individualassignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.highlightgame.R;
import com.google.android.material.color.utilities.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private List<Score> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreList = new ArrayList<>();
        readScoresFromPrefs();
        sortScores();

        TextView noScoresTextView = findViewById(R.id.no_scores_text_view);
        ListView scoreListView = findViewById(R.id.score_list_view);

        if (scoreList.isEmpty()) {
            noScoresTextView.setVisibility(View.VISIBLE);
            scoreListView.setVisibility(View.GONE);
        } else {
            noScoresTextView.setVisibility(View.GONE);
            scoreListView.setVisibility(View.VISIBLE);

            ScoreListAdapter adapter = new ScoreListAdapter(this, scoreList);
            scoreListView.setAdapter(adapter);
        }
    }

    public void saveScore(View view) {
        EditText nameEditText = findViewById(R.id.name_edit_text);
        String name = nameEditText.getText().toString().trim();
        int score = getIntent().getIntExtra("score", 0);

        if (name.isEmpty()) {
            nameEditText.setError(getString(R.string.name_required_error));
            return;
        }

        Score newScore = new Score(name, score);
        scoreList.add(newScore);
        saveScoresToPrefs();

        finish();
    }

    private void readScoresFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        int numScores = prefs.getInt(getString(R.string.num_scores_key), 0);

        for (int i = 0; i < numScores; i++) {
            String name = prefs.getString(getString(R.string.name_key, i), "");
            int score = prefs.getInt(getString(R.string.score_key, i), 0);

            Score s = new Score(name, score);
            scoreList.add(s);
        }
    }

    private void saveScoresToPrefs() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Sort scores
        sortScores();

        // Write scores to prefs
        int numScores = Math.min(scoreList.size(), 25);
        editor.putInt(getString(R.string.num_scores_key), numScores);
        for (int i = 0; i < numScores; i++) {
            editor.putString(getString(R.string.name_key, i), scoreList.get(i).getName());
            editor.putInt(getString(R.string.score_key, i), scoreList.get(i).getScore());
        }

        editor.apply();
    }

    private void sortScores() {
        Collections.sort(scoreList, new Comparator<Score>() {
            @Override
            public int compare(Score s1, Score s2) {
                return Integer.compare(s2.getScore(), s1.getScore());
            }
        });
    }
}