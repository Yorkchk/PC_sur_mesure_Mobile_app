package com.example.pcsurmesure.models;

import static android.content.ContentValues.TAG;

import static com.example.pcsurmesure.models.Command.checkQuantities;
//import static com.example.pcsurmesure.models.Command.commandsTable;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Requester extends User {

    public Requester(String username, String firstName, String lastName, String email, String password) {
        super(username, firstName, lastName, email, password);
        setRole(ROLE.Client);
    }

    public Requester() {
    }

    public interface AddCommand {
        void onComponentAdded();

        void onException(Exception e);
    }

    public void createCommand(Command command, AddCommand addCommandCallback) {
        // Check if quantities are valid before adding the command
        checkQuantities(command.getComponentsQuantities(), new Command.QuantityCheckCallback() {
            @Override
            public void onQuantityCheckComplete(boolean result, Component component) {
                if (result && component == null) {
                    // Quantities are correct, proceed with adding the command to Firebase
                    addCommandToFirebase(command, addCommandCallback);
                } else if (!result && component != null) {
                    // Notify about the component with an exceeded quantity
                    Log.d(TAG, component.getDescription() + " should not exceed " + component.getQuantity() + " pieces");
                    addCommandCallback.onException(new Exception(component.getDescription() + " should not exceed " + component.getQuantity() + " pieces"));
                }
            }
        });
    }

    // The method to actually add the command to Firebase (called after quantity check)
    private void addCommandToFirebase(Command command, AddCommand addCommandCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commandsTable = database.getReference("Commands");
        Map<String, Integer> componentsQuantitiesStringMap = new HashMap<>();
        Map<String, Object> commandData = new HashMap<>();

        // Add individual command attributes
        commandData.put("requesterId", command.getRequesterId());
        commandData.put("dateDeCreation", command.getDateDeCreation());
        commandData.put("dateDeModification", command.getDateDeModification());
        commandData.put("idCommande", command.getIdCommande());
        commandData.put("status", command.getStatus());


        for (Map.Entry<Component, Integer> entry : command.getComponentsQuantities().entrySet()) {
            String key = entry.getKey().getDescription(); // or another unique string identifier for the component
            Integer value = entry.getValue();
            componentsQuantitiesStringMap.put(key, value);
        }
        commandData.put("componentsQuantities", componentsQuantitiesStringMap);

        commandsTable.child(command.getIdCommande()).setValue(commandData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Component added successfully");
                        addCommandCallback.onComponentAdded();
                    } else {
                        Log.d(TAG, "Component added failed");
                        addCommandCallback.onException(task.getException());
                    }
                });
    }

    public interface DeleteCommandInterface {
        void onCommandDeleted();

        void onException(Exception e);
    }

    public void deleteCommand(String commandId, DeleteCommandInterface callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commandsTable = database.getReference("Commands");
        commandsTable.child(commandId).removeValue()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       Log.d(TAG, "Command removed successfully.");
                        callback.onCommandDeleted();
                   }
                   else{
                       Log.d(TAG, "Failed to remove command");
                       callback.onException(task.getException());
                   }
                });

        }



}
