
package com.example.pcsurmesure.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Requester;
import com.example.pcsurmesure.models.User;

public class AddUserActivity extends AppCompatActivity {

    private Button btnSignup;

    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser); // Ensure the layout is correct

        // Initialize the views
        EditText etUsername = findViewById(R.id.Username);
        EditText etEmail = findViewById(R.id.Email);
        EditText etPassword = findViewById(R.id.Password);
        EditText firstName = findViewById(R.id.FirstName);
        EditText lastName = findViewById(R.id.LastName);
        EditText confirmPassword = findViewById(R.id.confirm_passwordName);
        btnSignup = findViewById(R.id.add_button);

        // Initialize the Authentication class
        authentication = new Authentication();

        // Set up the button click listener for the sign-up button
        btnSignup.setOnClickListener(view -> handleSignup(firstName, lastName, etUsername, etEmail, etPassword, confirmPassword));
    }

    // Method to handle the sign-up process
    public void handleSignup(EditText etfirstName, EditText etlastName, EditText etUsername, EditText etEmail, EditText etPassword, EditText etconfirmPassword) {
        // Get the text from the EditText fields
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String firstName = etfirstName.getText().toString();
        String lastName = etlastName.getText().toString();
        String confirmPassword = etconfirmPassword.getText().toString().trim();


        // Validate input
        if (!validateInput(username, email, password,confirmPassword, etUsername, etEmail, etPassword, etconfirmPassword)) {
            return; // Stop execution if validation fails
        }

        // Create a new User object
        User newUser = new Requester(username, firstName, lastName, email, password);



        // Use the Authentication class to sign up
        authentication.signUp(newUser, new Authentication.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(AddUserActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();

                // Proceed to login activity
                Intent intent = new Intent(AddUserActivity.this, com.example.pcsurmesure.ui.AdminActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AddUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to validate input fields
    public static boolean validateInput(String username, String email, String password, String confirmPassword
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

        return true; // All validations passed
    }
}

