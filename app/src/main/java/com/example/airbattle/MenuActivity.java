package com.example.airbattle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button startBtn;
    private Button diffBtn;
    private Button rankBtn;
    private Button aboutBtn;
    private Button logoutBtn;
    private Button exitBtn;

    private boolean isHard = false;

    void saveDifficulty() {
        SharedPreferences pref = getSharedPreferences("AirBattle", MODE_PRIVATE);
        pref.edit().putBoolean("isHard", isHard).apply();
        // Update Button
        if (isHard) {
            diffBtn.setText(R.string.diff_hard);
        } else {
            diffBtn.setText(R.string.diff_normal);
        }
    }

    void loadDifficutly() {
        SharedPreferences pref = getSharedPreferences("AirBattle", MODE_PRIVATE);
        isHard = pref.getBoolean("isHard", false);
        // Update Button
        if (isHard) {
            diffBtn.setText(R.string.diff_hard);
        } else {
            diffBtn.setText(R.string.diff_normal);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDifficutly();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get Button Views
        startBtn = findViewById(R.id.startBtn);
        diffBtn = findViewById(R.id.diffBtn);
        rankBtn = findViewById(R.id.rankBtn);
        aboutBtn = findViewById(R.id.aboutBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        exitBtn = findViewById(R.id.exitBtn);

        // Load Difficulty
        loadDifficutly();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("Debug", "Start Game");
                // Start the GameActivity
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                if (isHard) intent.putExtra("HARD_MODE", true);
                startActivity(intent);
            }
        });

        diffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update and Save difficulty
                isHard = !isHard;
                saveDifficulty();
            }
        });


        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to RankActivity
                Intent intent = new Intent(getApplicationContext(), RankNaviActivity.class);
                startActivity(intent);
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("Debug", "About Game ");
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle(R.string.about_title);
                builder.setMessage(R.string.about_msg);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 可选操作
                    }
                });
                builder.create().show();

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("Debug", "Log out user");
                // Log out to MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("Debug", "Exit Game");
                finishAffinity();
            }
        });
    }
}