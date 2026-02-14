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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Assembler;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComponentsWaitlistActivity extends AppCompatActivity {

    private ListView listView;
    private ComponentsWaitlistAdapter adapter;
    private List<Component> componentsList;

    private Command currentCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_components_waitlist);

        listView = findViewById(R.id.listView);
        Assembler as = new Assembler();

        Button validateButton = findViewById(R.id.validatebutton);
        Button rejectButton = findViewById(R.id.buttonReject);


        componentsList = new ArrayList<>();
        adapter = new ComponentsWaitlistAdapter(this, componentsList, null);
        listView.setAdapter(adapter);

        // Retrieve commandId from Intent
        Intent intent = getIntent();
        String commandId = intent.getStringExtra("commandId");

        // Fetch the command from Firebase using the commandId
        getAllCommandsFromFirebase(new Command.CommandsCallback() {
            @Override
            public void onCommandRetrieved(Command command) {
                System.out.println("condition met: " + command.getIdCommande().equals(commandId));
                if (command.getIdCommande().equals(commandId)) {
                    currentCommand = command;


                    // Update the adapter with the fetched currentCommand
                    adapter = new ComponentsWaitlistAdapter(ComponentsWaitlistActivity.this, componentsList, currentCommand);
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
                            System.out.println("components list: " + componentsList);
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

        validateButton.setOnClickListener(v -> {
            as.approveCommand(this, currentCommand.getIdCommande(), new Assembler.ApproveCommandCallback() {
                @Override
                public void onCommandApproved() {
                    Toast.makeText(ComponentsWaitlistActivity.this, "This command has successfully been assembled", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ComponentsWaitlistActivity.this, AssemblerActivity.class);
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onException(Exception e) {
                    Toast.makeText(ComponentsWaitlistActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        });

        rejectButton.setOnClickListener(v -> {
            as.rejectCommand(currentCommand.getIdCommande(), new Assembler.RejectCommandCallback() {
                @Override
                public void onCommandRejected() {
                    Toast.makeText(ComponentsWaitlistActivity.this, "This command has successfully been rejected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ComponentsWaitlistActivity.this, AssemblerActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onException(Exception e) {
                    Toast.makeText(ComponentsWaitlistActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

