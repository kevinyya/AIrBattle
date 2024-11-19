package com.example.airbattle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;


public class GamePause extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepause);

        Button resumeButton = findViewById(R.id.resume_button);
        Button mainMenuButton = findViewById(R.id.main_menu_button);

        SeekBar musicVolumeSeekBar = findViewById(R.id.musicVolumeSeekBar);
        SeekBar effectVolumeSeekBar = findViewById(R.id.effectVolumeSeekBar);

        SharedPreferences preferences = getSharedPreferences("game_settings", MODE_PRIVATE);
        float savedMusicVolume = preferences.getFloat("music_volume", 1.0f) * 100;
        float savedEffectVolume = preferences.getFloat("effect_volume", 1.0f) * 100;
        musicVolumeSeekBar.setProgress((int) savedMusicVolume);
        effectVolumeSeekBar.setProgress((int) savedEffectVolume);


        resumeButton.setOnClickListener(v -> {
            finish(); // Close pause activity to return to game
        });

        mainMenuButton.setOnClickListener(v -> {
            // Logic to go back to the main menu
            Intent intent = new Intent(GamePause.this, MenuActivity.class);
            startActivity(intent);
            finish(); // Close pause activity
        });

        musicVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                // Set the volume for your background music
                saveVolume("music_volume", volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        effectVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                saveVolume("effect_volume", volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    private void saveVolume(String key, float volume) {
        SharedPreferences preferences = getSharedPreferences("game_settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, volume);
        editor.apply();
    }
}
