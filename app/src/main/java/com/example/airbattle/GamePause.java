package com.example.airbattle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GamePause extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepause);

        Button resumeButton = findViewById(R.id.resume_button);
        Button mainMenuButton = findViewById(R.id.main_menu_button);

        resumeButton.setOnClickListener(v -> {
            finish(); // Close pause activity to return to game
        });

        mainMenuButton.setOnClickListener(v -> {
            // Logic to go back to the main menu
            Intent intent = new Intent(GamePause.this, MenuActivity.class);
            startActivity(intent);
            finish(); // Close pause activity
        });
    }

}
