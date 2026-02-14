package com.example.pcsurmesure.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Requester;
import com.example.pcsurmesure.models.User;

public class SignUpActivity extends AppCompatActivity {


    private Button btnSignup;

    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page); // Ensure the layout is correct

        // Initialiser les vues
        EditText etFirstName = findViewById(R.id.firstName);
        EditText etLastName = findViewById(R.id.lastName);
        EditText etUsername = findViewById(R.id.username);
        EditText etEmail = findViewById(R.id.email);
        EditText etPassword = findViewById(R.id.signuPassword);
        EditText etConfirmPassword = findViewById(R.id.confPassword);
        btnSignup = findViewById(R.id.signupbtn);

        // Initialiser la classe Authentication
        authentication = new Authentication();

        // Configurer le listener de clic pour le bouton d'inscription
        btnSignup.setOnClickListener(view -> handleSignup(etFirstName,etLastName,  etUsername, etEmail, etPassword, etConfirmPassword));
    }

    // Méthode pour gérer le processus d'inscription
    private void handleSignup(EditText etFirstName, EditText etLastName, EditText etUsername, EditText etEmail, EditText etPassword, EditText etConfirmPassword) {
        // Get the text from the EditText fields
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Valider les entrées
        if (!validateInput(username, email, password, confirmPassword, etUsername, etEmail, etPassword, etConfirmPassword)) {
            return;// Arrêter l'exécution si la validation échoue
        }


        // Create a new User object
        User newUser = new Requester(username, firstName, lastName, email, password);


        // Utiliser la classe Authentication pour s'inscrire
        authentication.signUp(newUser, new Authentication.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(SignUpActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();

                // Passer à LoginActivity
                Intent intent = new Intent(SignUpActivity.this, com.example.pcsurmesure.ui.LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour valider les champs d'entrée
    private boolean validateInput(String username, String email, String password, String confirmPassword
    , EditText etUsername, EditText etEmail, EditText etPassword, EditText etConfirmPassword) {
        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true; // Toutes les validations sont passées
    }


}