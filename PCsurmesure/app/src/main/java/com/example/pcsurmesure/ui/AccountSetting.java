
package com.example.pcsurmesure.ui;

import static com.example.pcsurmesure.ui.AddUserActivity.validateInput;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Admin;
import com.example.pcsurmesure.models.Assembler;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Requester;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AccountSetting extends AppCompatActivity {

    private TextView nameView, usernameView, emailView;
    private EditText newUsername, newEmail, newPassword, newConfirmPassword;
    private Authentication auth = new Authentication();

    private User loggedInUser = auth.getLoggedInUser();

    private User userToBeEdited;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);



        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        System.out.println("answer");
        System.out.println(mode);
        System.out.println("admin".equals(mode));
        if ("admin".equals(mode)) {
            String userEmail = intent.getStringExtra("userEmail");
            if (loggedInUser instanceof Admin) {
                Admin adminUser = (Admin) auth.getLoggedInUser();

                adminUser.getUserByEmail(userEmail, new Admin.UserCallback() {
                    @Override
                    public void onSuccess(User user) {
                        userToBeEdited = user;  // Assign the user here


                        // Now that userToBeEdited is set, continue with the rest of your logic
                        initializeUI();
                        // You can also update the UI elements here with user data
                        System.out.println("Answer1");
                        System.out.println(userToBeEdited);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle failure here
                    }
                });
            }
        }else {
            // For other modes, continue without waiting
            System.out.println("answer2");
            userToBeEdited = loggedInUser;
            initializeUI();

        }
    }

    // Move your logic that depends on userToBeEdited into a separate method
    private void initializeUI() {
        if (userToBeEdited == null) {
            Toast.makeText(AccountSetting.this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        nameView = findViewById(R.id.name);
        usernameView = findViewById(R.id.currentUsername);
        emailView = findViewById(R.id.emailaddress);

        String firstName = userToBeEdited.getFirstName();
        String lastName = userToBeEdited.getLastName();
        nameView.setText(firstName + " " + lastName);
        usernameView.setText("Current Username: " + userToBeEdited.getUsername());
        emailView.setText("Current Email Address: " + userToBeEdited.getEmail());

        newUsername = findViewById(R.id.usernameChange);
        newEmail = findViewById(R.id.emailaddresschange);
        newPassword = findViewById(R.id.passwordChange);
        newConfirmPassword = findViewById(R.id.newconfPass);

        MaterialButton saveButton = findViewById(R.id.save_button);

        User finalLoggedInUser = userToBeEdited;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated input
                String username = newUsername.getText().toString().trim();
                String email = newEmail.getText().toString().trim();
                String password = newPassword.getText().toString().trim();
                String confirmPassword = newConfirmPassword.getText().toString().trim();

                // Validate the input
                if (!validateInput(username, email, password, confirmPassword, newUsername, newEmail, newPassword, newConfirmPassword)) {
                    Toast.makeText(AccountSetting.this, "Failed to update.", Toast.LENGTH_SHORT).show();
                    return;
                }

                User updatedUser = null;
                // Create updated user object
                if(finalLoggedInUser.getRole() == User.ROLE.Admin) {
                    updatedUser = new Admin(username, finalLoggedInUser.getFirstName(), finalLoggedInUser.getLastName(), email, password);
                }
                else if(finalLoggedInUser.getRole() == User.ROLE.Client) {
                    updatedUser = new Requester(username, finalLoggedInUser.getFirstName(), finalLoggedInUser.getLastName(), email, password);
                }
                else if(finalLoggedInUser.getRole() == User.ROLE.Assembler) {
                    updatedUser = new Assembler(username, finalLoggedInUser.getFirstName(), finalLoggedInUser.getLastName(), email, password);
                }
                else if(finalLoggedInUser.getRole() == User.ROLE.StoreKeeper) {
                    updatedUser = new StoreKeeper(username, finalLoggedInUser.getFirstName(), finalLoggedInUser.getLastName(), email, password);
                }
                // Update user info in Firebase
                updateUserInfo(updatedUser);

                if(loggedInUser.getRole() == User.ROLE.Client) {
                    Intent intent = new Intent(AccountSetting.this, UserActivity.class);
                    startActivity(intent);
                }else if(loggedInUser.getRole() == User.ROLE.Admin) {
                    Intent intent = new Intent(AccountSetting.this, AdminActivity.class);
                    startActivity(intent);
                }if(loggedInUser.getRole() == User.ROLE.StoreKeeper) {
                    Intent intent = new Intent(AccountSetting.this, StoreKeeper.class);
                    startActivity(intent);
                }if(loggedInUser.getRole() == User.ROLE.Assembler) {
                    Intent intent = new Intent(AccountSetting.this, AssemblerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void updateUserInfo(User updatedUser) {
        String email = userToBeEdited.getEmail();

        // Create a Map for the updates
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", updatedUser.getFirstName());
        updates.put("lastName", updatedUser.getLastName());
        updates.put("username", updatedUser.getUsername());
        updates.put("email", updatedUser.getEmail());
        updates.put("password", updatedUser.getPassword());
        updates.put("role", updatedUser.getRole());

        // Find the user by email in the database
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Update the user info
                        userSnapshot.getRef().updateChildren(updates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AccountSetting.this, "Update successful.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AccountSetting.this, "Failed to update user.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        // Optionally update the logged-in user in Authentication
                        Intent intent = getIntent();
                        String mode = intent.getStringExtra("mode");

                        if (mode.equals("self")) {
                            if(updatedUser.getRole() == User.ROLE.Admin) {
                                auth.setLoggedInUser(new Admin(updatedUser.getUsername(), updatedUser.getFirstName(), updatedUser.getLastName(),
                                        updatedUser.getEmail(), updatedUser.getPassword()));
                                return; // Exit after updating
                            }
                            if(updatedUser.getRole() == User.ROLE.Client) {
                                auth.setLoggedInUser(new Requester(updatedUser.getUsername(), updatedUser.getFirstName(), updatedUser.getLastName(),
                                        updatedUser.getEmail(), updatedUser.getPassword()));
                                return; // Exit after updating
                            }
                            if(updatedUser.getRole() == User.ROLE.Assembler) {
                                auth.setLoggedInUser(new Assembler(updatedUser.getUsername(), updatedUser.getFirstName(), updatedUser.getLastName(),
                                        updatedUser.getEmail(), updatedUser.getPassword()));
                                return; // Exit after updating
                            }
                            if(updatedUser.getRole() == User.ROLE.StoreKeeper) {
                                auth.setLoggedInUser(new StoreKeeper(updatedUser.getUsername(), updatedUser.getFirstName(), updatedUser.getLastName(),
                                        updatedUser.getEmail(), updatedUser.getPassword()));
                                return; // Exit after updating
                            }
                        }
                    }
                }else {
                    Toast.makeText(AccountSetting.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AccountSetting.this, "Error fetching user.", Toast.LENGTH_SHORT).show();
            }
        });

        Command.getAllCommandsFromFirebase(new Command.CommandsCallback() {
            @Override
            public void onCommandRetrieved(Command command) {
                if (command.getRequesterId().equals(email)) {
                    command.setRequesterId(updatedUser.getEmail());
                    Command updatedCommand = new Command(
                            command.getIdCommande(),
                            command.getRequesterId(),
                            command.getDateDeCreation(),
                            command.getComponentsQuantities()
                    );
                    command.editCommand(updatedCommand);
                }
            }

            @Override
            public void onComplete() {
                System.out.println("All relevant commands processed.");
            }

            @Override
            public void onError(Exception e) {
                System.err.println("Error retrieving commands: " + e.getMessage());
            }
        });


    }



}

