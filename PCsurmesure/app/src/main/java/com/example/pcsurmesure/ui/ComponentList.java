package com.example.pcsurmesure.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComponentList extends AppCompatActivity {

    private ListView listView;
    private ComponentListAdapter adapter;
    private List<Component> componentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_edit_remove_add_decrease_components);  // Le layout avec SearchView et ListView

        // Initialiser les vues
        listView = findViewById(R.id.viewComponent);

        SearchView searchView = findViewById(R.id.search_view);
        componentsList = new ArrayList<>();
        // Récupérer tous les components de Firebase et les afficher
        StoreKeeper.getAllStocksFromFirebase(new StoreKeeper.StocksCallback() {
            @Override
            public void onStockRetrieved(Component component) {
                componentsList.add(component);
            }

            @Override
            public void onComplete() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        adapter = new ComponentListAdapter(this, componentsList);
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