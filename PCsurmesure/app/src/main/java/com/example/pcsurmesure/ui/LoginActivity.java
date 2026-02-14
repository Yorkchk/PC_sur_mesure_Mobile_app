package com.example.pcsurmesure.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcsurmesure.MainActivity;
import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Admin;
import com.example.pcsurmesure.models.Assembler;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Requester;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupbtn;

    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.testloginpage); 

        // Initialize the views
        emailEditText = findViewById(R.id.Emaillogin); // Assuming this is the username field

        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signupbtn = findViewById(R.id.signupbtn);


       

        authentication = new Authentication();

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                authentication.login(email, password, new Authentication.AuthCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();


                        // Proceed to the corresponding page
                        if(user instanceof Requester){

                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                            // Passer à la page correspondante
                            startActivity(intent);
                            finish();
                        }
                        else if(user instanceof Admin){
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            // Passer les données utilisateur si nécessaire
                            startActivity(intent);
                            finish();
                        }
                        if(user instanceof Assembler){
                            Intent intent = new Intent(LoginActivity.this, AssemblerActivity.class);
                            // Passer les données utilisateur si nécessaire
                            startActivity(intent);
                            finish();
                        }
                        if(user instanceof StoreKeeper){
                            Intent intent = new Intent(LoginActivity.this, StoreKeeperActivity.class);
                            // Passer les données utilisateur si nécessaire
                            startActivity(intent);
                            finish();
                        }
                        authentication.setLoggedInUser(user);

                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            }
        });

        signupbtn.setOnClickListener(v -> {
            // Navigate to SignUpActivity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
}