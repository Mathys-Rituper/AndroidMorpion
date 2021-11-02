package com.example.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreboardActivity extends AppCompatActivity {

    private TextView textViewSubtitle;
    private ListView listViewScoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        textViewSubtitle= findViewById(R.id.scoreboardActivity_result);
        //TODO implement scoreboard logic and buttons

    }
}