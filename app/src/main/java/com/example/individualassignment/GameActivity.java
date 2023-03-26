package com.example.individualassignment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // Views
    private LinearLayout layout;
    private TextView timerTextView;
    private TextView scoreTextView;
    private TextView levelTextView;

    // Game variables
    private int level;
    private int score;
    private int viewsPerLevel;
    private int currentViewIndex;
    private boolean gameRunning;

    // List of views
    private List<View> viewsList;
    private List<Score> scores;
    // Timer
    private CountDownTimer timer;
    private BreakIterator levelText;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get views
        layout = findViewById(R.id.layout);
        timerTextView = findViewById(R.id.timer_text_view);
        scoreTextView = findViewById(R.id.score_text_view);
        levelTextView = findViewById(R.id.level_text_view);

        // Initialize game variables
        level = 1;
        score = 0;
        viewsPerLevel = 4;
        currentViewIndex = -1;
        gameRunning = true;

        // Initialize views list
        viewsList = new ArrayList<>();

        // Initialize scores list
        scores = new ArrayList<>();

        // Set level and score text
        setLevelText();
        setScoreText();

        // Start game
        startGame();
    }

    /**
     * Starts the game.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startGame() {
        // Initialize views for first level
        initializeViews();

        // Start timer
        startTimer();

        // Set first view as highlighted
        highlightNextView();
    }

    /**
     * Initializes the views for the current level.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializeViews() {
        // Clear existing views
        layout.removeAllViews();

        // Create views for level
        for (int i = 0; i < viewsPerLevel; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getDrawable(R.drawable.circle_view));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleViewClick(view);
                }
            });
            viewsList.add(imageView);
            layout.addView(imageView);
        }

        // Shuffle views list
        Collections.shuffle(viewsList, new Random(System.currentTimeMillis()));
    }

    /**
     * Starts the timer for the current level.
     */
    private void startTimer() {
        // Create timer
        timer = new CountDownTimer(5000, 1000) {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onTick(long millisUntilFinished) {
                // Update timer text
                timerTextView.setText(getString(R.string.timer_text, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // End level
                endLevel();
            }
        }.start();
    }

    /**
     * Highlights the next view in the views list.
     */
    private void highlightNextView() {
        // Increment current view index
        currentViewIndex++;

        // Check if all views have been highlighted
        if (currentViewIndex >= viewsPerLevel) {
            // End level
            endLevel();
            return;
        }

        // Get view to highlight
        View viewToHighlight = viewsList.get(currentViewIndex);

        // Set all views as unhighlighted
        for (View view : viewsList) {
            view.setSelected(false);
        }

        // Highlight view
        viewToHighlight.setSelected(true);
    }

    /**
     * Handles a click on a view.
     *
     * @param view the view that was clicked
     */
    private void handleViewClick(View view) {
        // Check if game is running
        if (!gameRunning) {
            return;
        }

        // Check if view is highlighted
        if (!view.isSelected()) {
            return;
        }

        // Increment score
        score++;

        // Set score text
        setScoreText();

        // Highlight next view
        highlightNextView();
    }
    @SuppressLint("StringFormatInvalid")
    private void endLevel() {
        // Stop timer
        timer.cancel();

        // Increment level
        level++;

        // Update level text view
        levelTextView.setText(getString(R.string.level_text, level));

        // Clear views list
        viewsList.clear();
        layout.removeAllViews();

        // Increase views per level
        viewsPerLevel = (int) Math.pow(level + 1, 2);

        // Reset current view index
        currentViewIndex = -1;

        // Start new level after 1 second delay
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                startLevel();
            }
        }.start();
    }

    private void startLevel() {
        // Increment level
        level++;

        // Update level text view
        levelTextView.setText(getString(level, R.string.level_text));

        // Update number of views per level
        viewsPerLevel = level * level;

        // Create views
        //createViews(level);

        // Start timer
        startTimer();
    }
    




    // Set level text
    private void setLevelText() {
        levelTextView.setText("Level: " + level);
    }

    // Set score text
    private void setScoreText() {
        scoreTextView.setText("Score: " + score);
    }



}