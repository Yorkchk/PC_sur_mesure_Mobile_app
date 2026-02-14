package com.example.pcsurmesure.models;

import static android.content.ContentValues.TAG;

import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class User  {
    private String Username;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private ROLE Role ;

    private String dateCreation;

    private String dateModification;

    public enum ROLE {
        Admin,
        Client,
        StoreKeeper,
        Assembler
    }


    public User() {}

    public User(String username, String firstName, String lastName, String email, String password) {
        Username = username;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Password = password;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateCreation = dateFormat.format(new Date());
    }



    public String getFirstName() {
        return FirstName;
    }

    public ROLE getRole() {
        return Role;
    }

    public void setRole(ROLE role){this.Role = role;}


    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getDateCreation(){return dateCreation;}

    public void setDateCreation(String dateCreation){this.dateCreation = dateCreation;}

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }








    public static void getAllUsersFromFirebase(UsersCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersTable = database.getReference("Users");

        usersTable.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String role = snapshot.child("role").getValue(String.class);
                    User user = null;
                    if(role.equals("Client")){
                        user = snapshot.getValue(Requester.class);                    }
                    else if(role.equals("Assembler")){
                        user = snapshot.getValue(Assembler.class);                    }
                    if(role.equals("Admin")){
                        user = snapshot.getValue(Admin.class);                    }
                    if(role.equals("StoreKeeper")){
                        user = snapshot.getValue(StoreKeeper.class);
                    }


                    if (user != null) {
                        Log.d("USER", "Utilisateur récupéré : " + user.getUsername());
                        callback.onUserRetrieved(user);  // Transmettre l'utilisateur récupéré via le callback

                    }
                }
                callback.onComplete();  // Signale que tous les utilisateurs ont été récupérés
            } else {
                Log.e("ERROR", "Erreur lors de la récupération des utilisateurs : ", task.getException());
                callback.onError(task.getException());  // Gérer l'erreur si la récupération échoue
            }
        });
    }

    // Interface pour le callback de récupération des utilisateurs
    public interface UsersCallback {
        void onUserRetrieved(User user);
        void onComplete();
        void onError(Exception e);
    }
}



