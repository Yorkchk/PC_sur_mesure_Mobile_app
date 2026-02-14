package com.example.pcsurmesure.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Remove extends AppCompatActivity {

    private ListView listView;
    private UserListAdapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_and_remove_user_activity);  // Le layout avec SearchView et ListView

        // Initialiser les vues
        listView = findViewById(R.id.list_view);
        SearchView searchView = findViewById(R.id.search_view);
        userList = new ArrayList<>();
        // Récupérer tous les utilisateurs de Firebase et les afficher
        User.getAllUsersFromFirebase(new User.UsersCallback() {
            @Override
            public void onUserRetrieved(User user) {
                userList.add(user);  // Ajoutez l'utilisateur à la liste
            }

            @Override
            public void onComplete() {
                Iterator<User> it = userList.iterator();
                while(it.hasNext()){
                    User user = it.next();
                    if(user.getRole() != User.ROLE.Client){
                        it.remove();
                    }
                }
                adapter.notifyDataSetChanged();  // Mettre à jour le ListView une fois que tous les utilisateurs ont été récupérés
            }

            @Override
            public void onError(Exception e) {
                // Gérer l'erreur
            }
        });
        adapter = new UserListAdapter(this, userList);
        listView.setAdapter(adapter);


        // Rechercher des utilisateurs avec le SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrer la liste des utilisateurs
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }
}