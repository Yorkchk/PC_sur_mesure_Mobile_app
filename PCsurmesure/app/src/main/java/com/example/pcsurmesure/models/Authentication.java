 package com.example.pcsurmesure.models;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pcsurmesure.models.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

 /**
 * A class that handles authentication using Firebase Realtime Database.
 */
public class Authentication {

    private static final String TAG = "Authentication";
    private final DatabaseReference usersTable;

    private static User loggedInUser;

    public Authentication() {
        // Initialize the reference to the "Users" node
        this.usersTable = FirebaseDatabase.getInstance().getReference("Users");
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Callback interface for authentication operations.
     */
    public interface AuthCallback {
        void onSuccess(User user);

        void onFailure(String errorMessage);
    }

    public interface logoutCallBack {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    /**
     * Log in a user with username and password.
     */
    public void login(String email, String password, AuthCallback callback) {
        usersTable.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userType = userSnapshot.child("role").getValue(String.class);
                        User user = null;

                        switch (userType) {
                            case "Admin":
                                user = userSnapshot.getValue(Admin.class);
                                break;
                            case "Client":
                                user = userSnapshot.getValue(Requester.class);
                                break;
                            case "Assembler":
                                user = userSnapshot.getValue(Assembler.class);
                                break;
                            case "StoreKeeper":
                                user = userSnapshot.getValue(StoreKeeper.class);
                                break;
                            default:
                                callback.onFailure("Unknown user type.");
                                return; // exit if user type is unknown
                        }

                        if (user != null) {
                            String userPassword = user.getPassword();
                            if (userPassword != null && userPassword.equals(password)) {
                                callback.onSuccess(user); // Successful login
                            } else {
                                callback.onFailure("Incorrect password. Please try again.");
                            }
                        } else {
                            callback.onFailure("User data is not available.");
                        }
                    }
                } else {
                    callback.onFailure("Email not found. Please sign up.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: ", error.toException());
                callback.onFailure("An error occurred. Please try again.");
            }
        });
    }

    /**
     * Sign up a new user.
     */
    public void signUp(User newUser, AuthCallback callback) {
        String email = newUser.getEmail();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        newUser.setDateCreation(currentDate);

        // Replace '.' in the email with a safe character, like ','
        String sanitizedEmail = email.replace(".", ",");

        // Save the user with the sanitized email as the key
        usersTable.child(sanitizedEmail).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(newUser);
                    } else {
                        callback.onFailure("Failed to sign up. Please try again.");
                    }
                });
    }





     public void onLogout(Activity currentActivity, Class<?> nextActivity, logoutCallBack logoutCallBack) {

        boolean isLogoutSuccessful = true; // This should be the result of your actual logout logic

        // Check if logout was successful
        if (isLogoutSuccessful) {
            logoutCallBack.onSuccess(); // Call onSuccess callback
        } else {
            logoutCallBack.onFailure("Logout failed. Please try again."); // Call onFailure callback
        }

        // Navigate to the next activity (moved below callbacks)
        Intent intent = new Intent(currentActivity, nextActivity);
        currentActivity.startActivity(intent);
        currentActivity.finish(); // Finish the current activity
        loggedInUser = null;
    }

}

