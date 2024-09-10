package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.hw1.Utilities.SignalManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignalManager.init(this);

        startMenu();
    }

    private void startMenu(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
        finish();

    }

}