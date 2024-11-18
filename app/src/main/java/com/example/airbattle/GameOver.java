package com.example.airbattle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover); // Reference to the new layout

        // Get the score passed from the GameActivity
        int score = getIntent().getIntExtra("SCORE", 0);

        // Find views in the layout
        ImageView gameOverImage = findViewById(R.id.gameover_image);
        TextView scoreTextView = findViewById(R.id.score_text);
        Button restartButton = findViewById(R.id.restart_button);
        Button mainMenuButton = findViewById(R.id.main_menu_button);

        // Set the score text
        scoreTextView.setText("Score: " + score);

        // Set button listeners
        restartButton.setOnClickListener(v -> restartGame());
        mainMenuButton.setOnClickListener(v -> goToMainMenu());
    }

    private void restartGame() {
        Intent intent = new Intent(this, GameActivity.class); // Adjust to your main game activity
        startActivity(intent);
        finish(); // Finish this activity
    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MenuActivity.class); // Adjust to your main menu activity
        startActivity(intent);
        finish(); // Finish this activity
    }
}