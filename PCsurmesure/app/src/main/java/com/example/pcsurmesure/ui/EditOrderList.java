package com.example.pcsurmesure.ui;

import static android.content.ContentValues.TAG;

import static com.example.pcsurmesure.models.Command.getAllCommandsFromFirebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditOrderList extends AppCompatActivity {

    private ListView listView;
    private EditOrderAdapter adapter;
    private List<Component> componentsList;

    private Command currentCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_edit_orders);

        listView = findViewById(R.id.listView);
        Button addComponentsButton = findViewById(R.id.button5);

        componentsList = new ArrayList<>();
        adapter = new EditOrderAdapter(this, componentsList, null);
        listView.setAdapter(adapter);

        // Retrieve commandId from Intent
        Intent intent = getIntent();
        String commandId = intent.getStringExtra("commandId");

        // Fetch the command from Firebase using the commandId
        getAllCommandsFromFirebase(new Command.CommandsCallback() {
            @Override
            public void onCommandRetrieved(Command command) {
                if (command.getIdCommande().equals(commandId)) {
                    currentCommand = command;

                    addComponentsButton.setOnClickListener(v -> {
                        Intent intent = new Intent(EditOrderList.this, createOrderActivity.class);
                        intent.putExtra("commandId", currentCommand.getIdCommande());
                        intent.putExtra("mode", "EDIT");
                        startActivity(intent);
                    });

                    // Update the adapter with the fetched currentCommand
                    adapter = new EditOrderAdapter(EditOrderList.this, componentsList, currentCommand);
                    listView.setAdapter(adapter);

                    // Populate componentsList with components from currentCommand
                    List<String> componentDescs = new ArrayList<>();
                    for (Component component : currentCommand.getComponentsQuantities().keySet()) {
                        componentDescs.add(component.getDescription());
                    }

                    // Fetch all stocks from Firebase and filter based on component descriptions
                    StoreKeeper.getAllStocksFromFirebase(new StoreKeeper.StocksCallback() {
                        @Override
                        public void onStockRetrieved(Component component) {
                            if (componentDescs.contains(component.getDescription())) {
                                componentsList.add(component);
                                adapter.notifyDataSetChanged(); // Notify adapter of changes
                            }
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "Stocks retrieval complete");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Error retrieving stocks: " + e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Command retrieval complete");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error retrieving command: " + e.getMessage());
            }
        });
    }
}

