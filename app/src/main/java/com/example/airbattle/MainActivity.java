package com.example.airbattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.airbattle.PlayerDatabase.PlayerData;
import com.example.airbattle.PlayerDatabase.PlayerDao;
import com.example.airbattle.PlayerDatabase.PlayerDatabase;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    // UI Elements
    private Button signinBtn, signupBtn;
    private EditText usernameET, passwordET;

    // Database Dao
    private PlayerDao playerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Database Instance
        playerDao = PlayerDatabase.getInstance(this).playerDao();

        // Get Username and Password
        usernameET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);

        // Remember me??
        // Get and Load Last Active Player
        loadActivePlayer();

        // Sign-in Button Click
        signinBtn = findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get EditView Content
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                // Empty EditView Check
                if (username.equals("") || password.equals("")) {
                    Snackbar.make(findViewById(android.R.id.content),
                            R.string.input_hint, Snackbar.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Database not Empty and Username Existed
                            if (isExisted(username)) {
                                // Disable Last Active Player
                                playerDao.disableActivePlayer();

                                // Get Player
                                PlayerData player = playerDao.getPlayer(username);

                                // Check Password
                                if (player.getPassword().equals(password)) {
                                    // Active User
                                    playerDao.enableActivePlayer(username);
                                    // Login and jump to MenuActivity
                                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(intent);
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content),
                                            R.string.password_error, Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(findViewById(android.R.id.content),
                                        R.string.signup_hint, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                }
            }
        });

        // Sign-up Button Click
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to SignupActivity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isEmpty() {
        return playerDao.getCnt() == 0 ? true : false;
    }

    private boolean isExisted(String username) {
        return playerDao.getExisted(username) != 0 ? true : false;
    }

    private void loadActivePlayer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Check Active User and load info
                if (!isEmpty() && playerDao.getActive() == 1) {
                    PlayerData activePlayer = playerDao.getActivePlayer();
                    usernameET.setText(activePlayer.getUsername());
                    passwordET.setText(activePlayer.getPassword());
                } else {
                    // Log.d("Debug", "No Active User");
                }
            }
        }).start();
    }

}