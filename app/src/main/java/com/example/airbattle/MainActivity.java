package com.example.airbattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    // UI Elements
    private Button signinBtn, signupBtn;
    private EditText usernameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get and Load Last Active Player
        // Remember Me Checkbox

        // Get Username and Password
        usernameET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);

        // Sign-in Button Click
        signinBtn = findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", "Sign-in Button Click");

                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                // Empty EditView Check
                if (username.equals("") || password.equals("")) {
                    Snackbar.make(findViewById(android.R.id.content),
                            R.string.input_hint, Snackbar.LENGTH_SHORT).show();
                } else {
                    // Check Player Validation by Username

                    // Reset and Set New Active Player

                    // Login and jump to MenuActivity

                    Log.d("Debug", "Login and jump to MenuActivity");
                }

            }
        });

        // Sign-up Button Click
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", "Sign-up Button Click");
                // Jump to SignupActivity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}