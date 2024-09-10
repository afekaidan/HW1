package com.example.hw1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Utilities.SignalManager;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int ROWS = 6;
    private static final int COLS = 3;
    private final ImageView[] heart = new ImageView[3];
    private final ImageView[][] gameMatrix = new ImageView[ROWS][COLS];
    private final ImageView[][] rockMatrix = new ImageView[ROWS][COLS];
    private int lives = 3;
    private TextView scoreTextView ;
    private int currentColumn = 1;
    private int curScore = 0;
    private static final int DELAY = 1400;
    private static final int ROCK = R.drawable.meteor;
    private static final int SPACESHIP_IMAGE = R.drawable.spaceship;

    Random random = new Random();

    private Handler delayHandler = new Handler();

    private Button leftArrowButton;
    private Button rightArrowButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setGameMatrix();
        startScoreTimer();


        leftArrowButton.setOnClickListener(View -> moveCarLeft());
        rightArrowButton.setOnClickListener(View -> moveCarRight());


        delayHandler.postDelayed(runnable, DELAY);
    }

    @SuppressLint("DiscouragedApi")
    private void findViews(){
        leftArrowButton = findViewById(R.id.leftArrowButton);
        rightArrowButton = findViewById(R.id.rightArrowButton);
        scoreTextView = findViewById(R.id.score);

        for (int i = 0; i < 3; i++) {
            heart[i] = findViewById(getResources().getIdentifier("heart" + (i + 1), "id", getPackageName()));
        }
    }

    private void spawnRocks() {
        delayHandler.postDelayed(this::randomRocks, DELAY);
    }

    private void randomRocks() {
        // Generate random rocks
        int i = random.nextInt(3);
        if (rockMatrix[0][i].getDrawable() == null) {
            rockMatrix[0][i].setImageResource(ROCK);

        }
    }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    moveRocks();
                    delayHandler.postDelayed(this, DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                spawnRocks();

            }
        };



    private void moveRocks() throws InterruptedException {
        // Move rocks downward

        for (int i = ROWS - 2; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                if (rockMatrix[i][j].getDrawable() != null) {
                    rockMatrix[i + 1][j].setImageDrawable(rockMatrix[i][j].getDrawable());
                    rockMatrix[i][j].setImageResource(0);

                    if (i + 1 == ROWS - 1 && (gameMatrix[ROWS - 1][j].getDrawable() != null) ) {
                        // Rock reached the bottom, remove it
                        rockMatrix[ROWS - 1][j].setImageDrawable(null);
                    }if(gameMatrix[ROWS - 1][currentColumn].getDrawable() == rockMatrix[i + 1][j].getDrawable()){
                        handleCollision();
                        gameMatrix[ROWS - 1][currentColumn].setImageResource(SPACESHIP_IMAGE);
                    }

                }
            }

        }
    }

    private void moveCarLeft() {
        // Move the car left only if it's not at the leftmost column
        if (currentColumn != 0) {

            moveCar(currentColumn - 1);

        }
    }

    private void moveCarRight() {
        // Move the car right only if it's not at the rightmost column
        if (currentColumn != COLS - 1) {

            moveCar(currentColumn + 1);
        }
    }

    private void moveCar(int newCol) {
        gameMatrix[ROWS - 1][currentColumn].setImageResource(0);

        gameMatrix[ROWS - 1][newCol].setImageResource(SPACESHIP_IMAGE);
        currentColumn = newCol;
    }

    private void setGameMatrix() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gameMatrix[i][j] = findViewById(R.id.row1_col1 + (i * COLS) + j);
                rockMatrix[i][j] = findViewById(R.id.row1_col1 + (i * COLS) + j);
            }
        }
    }


    private void handleCollision() {
        // Reduce lives
        lives--;
        // Show a toast message
        SignalManager.getInstance().toast("Collision! Lives remaining: " + lives);
        // Vibrate the device for 500 milliseconds
        SignalManager.getInstance().vibrate(500);
        // Update the UI with remaining lives
        updateLivesUI();
        // Check for game over
        if (lives == 0) {
            gameOver();
        }
    }

    private void updateLivesUI() {
        // Update UI to display remaining lives
        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                heart[i].setVisibility(View.VISIBLE);
            } else {
                heart[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void startScoreTimer()  {
        Handler scoreHandler = new Handler();

        scoreHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    // Increment the score and update the TextView
                    curScore++;
                    scoreTextView.setText("Score: " + curScore);

                // Call this runnable again after a delay (e.g., 1000 milliseconds for 1 second)
                scoreHandler.postDelayed(this, 1000);
            }
        }, 1000); // Start the timer after 1 second
    }

    @Override
    protected void onStop() {
        super.onStop();
        delayHandler.removeCallbacks(runnable);
        delayHandler.removeCallbacksAndMessages(null);



    }

    private void gameOver() {
        // Display game over message and handle game over actions
        SignalManager.getInstance().toast("Game Over!");
        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);
        finish();

    }


}
