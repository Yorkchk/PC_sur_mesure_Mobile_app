package com.example.pcsurmesure.ui;

import static com.example.pcsurmesure.models.Command.getAllCommandsFromFirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.Requester;
import com.example.pcsurmesure.models.StoreKeeper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class createOrderActivity extends AppCompatActivity {

    private ListView listView;
    private createOrderAdapter adapter;
    private Authentication auth = new Authentication();
    private List<Component> componentsList = new ArrayList<>();
    private Command currentCommand;

    private boolean stocksLoaded = false;
    private boolean commandLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_creation_orders);

        listView = findViewById(R.id.listView);
        Button saveButton = findViewById(R.id.button5);

        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        // Load stocks
        StoreKeeper.getAllStocksFromFirebase(new StoreKeeper.StocksCallback() {
            @Override
            public void onStockRetrieved(Component component) {
                componentsList.add(component);
            }

            @Override
            public void onComplete() {
                stocksLoaded = true;
                if ("EDIT".equals(mode)) {
                    String commandId = intent.getStringExtra("commandId");
                    loadCommandAndSetupEditMode(commandId, saveButton);
                } else {
                    setupCreateMode(saveButton);
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(createOrderActivity.this, "Error loading stocks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCommandAndSetupEditMode(String commandId, Button saveButton) {
        getAllCommandsFromFirebase(new Command.CommandsCallback() {
            @Override
            public void onCommandRetrieved(Command command) {
                if (command.getIdCommande().equals(commandId)) {
                    currentCommand = command;
                }
            }

            @Override
            public void onComplete() {
                commandLoaded = true;
                if (stocksLoaded && commandLoaded) {
                    filterAndSetupAdapter(saveButton);
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(createOrderActivity.this, "Error loading command: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterAndSetupAdapter(Button saveButton) {
        if (currentCommand != null) {
            // Remove components already in the command from the list
            Iterator<Component> iterator = componentsList.iterator();
            while (iterator.hasNext()) {
                Component allComponent = iterator.next();
                for (Component componentCommand : currentCommand.getComponentsQuantities().keySet()) {
                    if (allComponent.getDescription().equals(componentCommand.getDescription())) {
                        iterator.remove();
                    }
                }
            }
        }

        // Set up the adapter
        adapter = new createOrderAdapter(this, componentsList);
        listView.setAdapter(adapter);

        // Set up save button listener for EDIT mode
        saveButton.setOnClickListener(v -> {
            if (adapter.getComponentsQuantities().isEmpty()) {
                Toast.makeText(createOrderActivity.this, "Command is empty", Toast.LENGTH_SHORT).show();
            } else {
                currentCommand.addComponentsToCommand(adapter.getComponentsQuantities());
                Toast.makeText(createOrderActivity.this, "Command updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(createOrderActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void setupCreateMode(Button saveButton) {
        // Initialize adapter for CREATE mode and set it to ListView
        adapter = new createOrderAdapter(this, componentsList);
        listView.setAdapter(adapter);

        // Set up save button listener for CREATE mode
        saveButton.setOnClickListener(v -> {
            if (adapter.getComponentsQuantities().isEmpty()) {
                Toast.makeText(createOrderActivity.this, "Command is empty", Toast.LENGTH_SHORT).show();
            } else {
                Requester requester = new Requester();
                Command command = new Command(auth.getLoggedInUser().getEmail(), adapter.getComponentsQuantities());
                requester.createCommand(command, new Requester.AddCommand() {
                    @Override
                    public void onComponentAdded() {
                        Toast.makeText(createOrderActivity.this, "Command created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(createOrderActivity.this, UserActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onException(Exception e) {
                        Toast.makeText(createOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }




}
