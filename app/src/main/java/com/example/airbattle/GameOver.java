package com.example.airbattle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airbattle.PlayerDatabase.PlayerData;
import com.example.airbattle.PlayerDatabase.PlayerDao;
import com.example.airbattle.PlayerDatabase.PlayerDatabase;

public class GameOver extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private PlayerDao playerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover); // Reference to the new layout

        // Get PlayerDao
        playerDao = PlayerDatabase.getInstance(this).playerDao();

        // Play game over sound
        mediaPlayer = MediaPlayer.create(this, R.raw.gameover);
        mediaPlayer.start();

        // Get the score passed from the GameActivity
        int score = getIntent().getIntExtra("SCORE", 0);

        // Find views in the layout
        ImageView gameOverImage = findViewById(R.id.gameover_image);
        TextView scoreTextView = findViewById(R.id.score_text);
        Button restartButton = findViewById(R.id.restart_button);
        Button mainMenuButton = findViewById(R.id.main_menu_button);

        // Update score to database
        updateScore(score);

        // Set the score text
        scoreTextView.setText("Score: " + score);

        // Set button listeners
        restartButton.setOnClickListener(v -> restartGame());
        mainMenuButton.setOnClickListener(v -> goToMainMenu());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
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

    private void updateScore(int score) {
        // Get difficulty from preference
        SharedPreferences pref = getSharedPreferences("AirBattle", MODE_PRIVATE);
        boolean isHard = pref.getBoolean("isHard", false);

        Log.d("Debug", Boolean.toString(isHard));

        // Save data to database
        new Thread(new Runnable() {
            @Override
            public void run() {
                PlayerData player = playerDao.getActivePlayer();
                if (isHard) {
                    int history_score = playerDao.getHardScore();
                    if (score > history_score) {
                        playerDao.updateHardScore(player.getUsername(), score);
                    }
                } else {
                    int history_score = playerDao.getNormalScore();
                    if (score > history_score) {
                        playerDao.updateNormalScore(player.getUsername(), score);
                    }
                }
            }
        }).start();
    }

}