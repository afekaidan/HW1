package com.example.hw1;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);


        Button startButton = findViewById(R.id.startButton);

        Button exitButton = findViewById(R.id.exitButton);
        // Set an OnClickListener for the "Start" button
        startButton.setOnClickListener(View -> startGameActivity());
        // Set an OnClickListener for the "Exit" button
        exitButton.setOnClickListener(View -> setContentView(0));
    }

    private void startGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}