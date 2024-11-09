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

public class SignupActivity extends AppCompatActivity {
    private Button signupBtn;
    private EditText usernameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);

        // Sign-up Button Click
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", "Sign-up Button Click");

                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                // Empty EditView Check
                if (username.equals("") || password.equals("")) {
                    Snackbar.make(findViewById(android.R.id.content),
                            R.string.input_hint, Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.d("Debug", "Create New Player");
                    Log.d("Debug", "Jump to MainActivity");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }
}