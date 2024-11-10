package com.example.airbattle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.MenuItem;

import com.example.airbattle.PlayerDatabase.Player;
import com.example.airbattle.PlayerDatabase.PlayerDao;
import com.example.airbattle.PlayerDatabase.PlayerDatabase;
import com.google.android.material.snackbar.Snackbar;

public class SignupActivity extends AppCompatActivity {
    private Button signupBtn;
    private EditText usernameET, passwordET;

    private PlayerDao playerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);

        // Get Database Instance
        playerDao = PlayerDatabase.getInstance(this).playerDao();

        // Return
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        // Sign-up Button Click
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                // Empty EditView Check
                if (username.equals("") || password.equals("")) {
                    Snackbar.make(findViewById(android.R.id.content),
                            R.string.input_hint, Snackbar.LENGTH_SHORT).show();
                } else {
                    // Create New User and Insert to Database
                    Player newPlayer = new Player(username, password);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                playerDao.insert(newPlayer);
                            } catch (SQLiteConstraintException e) {
                                Log.d("Debug", "Insert New Player Failed");
                            }
                        }
                    }).start();

                    // Return to MainActivity
                    returnToMain();
                }
            }
        });
    }

    private void returnToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Return to MainActivity
                returnToMain();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}