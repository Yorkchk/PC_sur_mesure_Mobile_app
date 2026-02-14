package com.example.pcsurmesure.ui;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class ListOrder extends AppCompatActivity {

    private ListView listView;
    private listOrderAdapter adapter;
    private List<Command> myCommands;
    private Authentication auth = new Authentication();

    private String loggedOnUserId = auth.getLoggedInUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_my_orders);


        // Initialize views
        listView = findViewById(R.id.myorders);

        // Initialize the command list and adapter
        myCommands = new ArrayList<>();
        adapter = new listOrderAdapter(this, myCommands);
        listView.setAdapter(adapter);

        // Fetch all commands from Firebase
        Command.getAllCommandsFromFirebase(new Command.CommandsCallback() {
            @Override
            public void onCommandRetrieved(Command command) {
                if(command.getRequesterId().equals(loggedOnUserId)){
                myCommands.add(command);
                adapter.notifyDataSetChanged();  // Update ListView with each new item
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All commands retrieved successfully.");
                System.out.println("myCommands after retrieval: " + myCommands); // Confirm data retrieval
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error retrieving commands: " + e.getMessage());
            }
        });
    }
}
