package com.example.pcsurmesure.models;

import static android.content.ContentValues.TAG;


import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Admin extends User{

    public Admin(String username, String firstName, String lastName, String email, String password) {
        super(username, firstName, lastName, email, password);
        setRole(ROLE.Admin);
    }
    public Admin(){}

    public interface AddUserCallback {
        void onUserAdded();
        void onError(Exception e);
    }
    public void addUserToFirebase(User user, AddUserCallback callback) {
        // Replace '.' with ',' to sanitize the email for Firebase database key
        String sanitizedEmail = user.getEmail().replace(".", ",");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersTable = database.getReference("Users");
        // Use the sanitized email as the key
        usersTable.child(sanitizedEmail).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User added successfully.");
                        callback.onUserAdded();
                    } else {
                        Log.e(TAG, "Failed to add user.", task.getException());
                        callback.onError(task.getException());
                    }
                });
    }


    // Callback interface for removing a user
    public interface RemoveUserCallback {
        void onUserRemoved();
        void onError(Exception e);
    }

    // Method to remove a user from Firebase
    public void removeUserFromFirebase(RemoveUserCallback callback) {
        String email = this.getEmail();  // Assume your User class has a getEmail() method
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersTable = database.getReference("Users");
        // Query the database to find the user by email
        usersTable.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Remove the user based on the key retrieved
                        userSnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User removed successfully.");
                                callback.onUserRemoved();
                            } else {
                                Log.e(TAG, "Failed to remove user.", task.getException());
                                callback.onError(task.getException());
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "No user found with the given email.");
                    callback.onError(new Exception("No user found with the given email."));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to query user by email.", databaseError.toException());
                callback.onError(databaseError.toException());
            }
        });
    }
    //    Get user from database
    public interface UserCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }


    public void getUserByEmail(String emailToSearch, UserCallback callback) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersTable = database.getReference("Users");
        usersTable.orderByChild("email").equalTo(emailToSearch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        String firstName = userSnapshot.child("firstName").getValue(String.class);
                        String lastName = userSnapshot.child("lastName").getValue(String.class);
                        String username = userSnapshot.child("username").getValue(String.class);
                        String password = userSnapshot.child("password").getValue(String.class);
                        String dateDeCreation = userSnapshot.child("dateCreation").getValue(String.class);
                        String dateModification = userSnapshot.child("dateModification").getValue(String.class);

                        String role = userSnapshot.child("role").getValue(String.class);
                        User user = null;
                        if(role.equals("Client")){
                            user = new Requester(username, firstName, lastName, email, password);
                        }
                        else if(role.equals("Assembler")){
                            user = new Assembler(username, firstName, lastName, email, password);
                        }
                        if(role.equals("Admin")){
                            user = new Admin(username, firstName, lastName, email, password);
                        }
                        if(role.equals("StoreKeeper")){
                            user = new StoreKeeper(username, firstName, lastName, email, password);
                        }
                        callback.onSuccess(user);
                        return;
                    }
                }
                callback.onSuccess(null); // User not found
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        });
    }

    public void ResetDataBase(StoreKeeper.ResetDataBaseCallback callback){

        ResetFirebaseComponents(new ArrayList<>(), new StoreKeeper.ResetDataBaseCallback() {
            @Override
            public void onDataBaseReset() {
            }

            @Override
            public void onError(Exception e) {
            }
        });
        ResetFirebaseUsers(new ArrayList<>(), new StoreKeeper.ResetDataBaseCallback() {
            @Override
            public void onDataBaseReset() {
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    public void ResetFirebaseComponents(List<Component> componentsToAdd, StoreKeeper.ResetDataBaseCallback callback) {
//        We get all components first
        List<Component> componentsToRemove = new ArrayList<>();
        StoreKeeper st = new StoreKeeper();
        Stock stock = new Stock();
        StoreKeeper.getAllStocksFromFirebase(new StoreKeeper.StocksCallback() {
            @Override
            public void onStockRetrieved(Component component) {
                System.out.println("component to add to remove");
                System.out.println(component.getDescription());
                componentsToRemove.add(component);
            }

            @Override
            public void onComplete() {
                st.removeAllComponentsFromDatabase(componentsToRemove);
                st.addComponentsToDatabase(componentsToAdd);
            }

            @Override
            public void onError(Exception e) {

            }
        });


    }


    public void ResetFirebaseUsers(List<User> usersToAdd, StoreKeeper.ResetDataBaseCallback callback) {
        List<User> usersToRemove = new ArrayList<>();

        // Step 1: Retrieve all users from Firebase
        User.getAllUsersFromFirebase(new UsersCallback() {
            @Override
            public void onUserRetrieved(User user) {
                if(user.getRole() == ROLE.Client){
                usersToRemove.add(user);
                }
            }

            @Override
            public void onComplete() {
                // Step 2: Remove all retrieved users from Firebase
                removeUsersSequentially(usersToRemove, new StoreKeeper.ResetDataBaseCallback() {
                    @Override
                    public void onDataBaseReset() {
                        // Step 3: After users are removed, add new users
                        addUsersToFirebase(usersToAdd, callback); // Method to add all users
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);  // If any error during removal
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);  // Error in retrieving users
            }
        });
    }
    private void addUsersToFirebase(List<User> users, StoreKeeper.ResetDataBaseCallback callback) {
        if (users.isEmpty()) {
            callback.onDataBaseReset();  // No users to add, we are done
            return;
        }

        for (User user : users) {
            addUserToFirebase(user, new AddUserCallback() {
                @Override
                public void onUserAdded() {
                    users.remove(user);
                    if (users.isEmpty()) {
                        callback.onDataBaseReset();  // All users added
                    }
                }

                @Override
                public void onError(Exception e) {
                    callback.onError(e);  // Handle error
                }
            });
        }
    }

    private void removeUsersSequentially(List<User> usersToRemove, StoreKeeper.ResetDataBaseCallback callback) {
        if (usersToRemove.isEmpty()) {
            callback.onDataBaseReset();  // No users to remove, proceed to the next step
            return;
        }

        User userToRemove = usersToRemove.get(0);  // Remove the first user
        removeUserFromFirebase(userToRemove, new RemoveUserCallback() {
            @Override
            public void onUserRemoved() {
                usersToRemove.remove(userToRemove);  // Remove the user from the list
                // Recursively remove the next user
                removeUsersSequentially(usersToRemove, callback);  // Call this method again
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);  // Handle error
            }
        });
    }



    public void removeUserFromFirebase(User user, RemoveUserCallback callback) {
        String email = user.getEmail();  // Assume your User class has a getEmail() method
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersTable = database.getReference("Users");
        // Query the database to find the user by email
        usersTable.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Remove the user based on the key retrieved
                        userSnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User removed successfully.");
                                callback.onUserRemoved();
                            } else {
                                Log.e(TAG, "Failed to remove user.", task.getException());
                                callback.onError(task.getException());
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "No user found with the given email.");
                    callback.onError(new Exception("No user found with the given email."));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to query user by email.", databaseError.toException());
                callback.onError(databaseError.toException());
            }
        });
    }




}
