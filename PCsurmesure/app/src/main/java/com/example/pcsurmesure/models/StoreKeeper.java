package com.example.pcsurmesure.models;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pcsurmesure.ui.AccountSetting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreKeeper extends User {

    private static DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("Stock");

    public StoreKeeper(String username, String firstName, String lastName, String email, String password) {
        super(username, firstName, lastName, email, password);
        setRole(ROLE.StoreKeeper);
    }

    public StoreKeeper() {
    }

    // Callback interface for removing a stock
    public interface RemoveStockCallback {
        void onStockRemoved();

        void onError(Exception e);
    }

    // Method to remove a stock from Firebase
    public void removeStockFromFirebase(Component c, StoreKeeper.RemoveStockCallback callback) {
        String desc = c.getDescription();
        Log.d(TAG, "Attempting to remove stock with description: " + desc);

        // Access the stock directly by its description key
        stockRef.child(desc).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Stock removed successfully.");
                callback.onStockRemoved();
            } else {
                Log.e(TAG, "Failed to remove stock.", task.getException());
                callback.onError(task.getException());
            }
        });
    }

    // Interface pour le callback de récupération des stocks
    public interface StocksCallback {
        void onStockRetrieved(Component component);

        void onComplete();

        void onError(Exception e);
    }

    public static void getAllStocksFromFirebase(StoreKeeper.StocksCallback callback) {

        stockRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Component c = snapshot.getValue(Component.class);


                    if (c != null) {
                        Log.d("COMPONENT", "Composant récupéré : " + c.getDescription());
                        callback.onStockRetrieved(c);
                    }
                }
                callback.onComplete();
            } else {
                Log.e("ERROR", "Erreur lors de la récupération des composantes : ", task.getException());
                callback.onError(task.getException());
            }
        });
    }

    public void removeAllComponentsFromDatabase(List<Component> componentsToRemove) {
        StoreKeeper st = new StoreKeeper();
        for (Component c : componentsToRemove) {
            System.out.println("Components to remove");
            System.out.println(c.getDescription());
            st.removeStockFromFirebase(c, new StoreKeeper.RemoveStockCallback() {
                @Override
                public void onStockRemoved() {
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    public interface ResetDataBaseCallback {
        void onDataBaseReset();

        void onError(Exception e);
    }

    public void addComponentsToDatabase(List<Component> componentsToAdd) {
        Stock stock = new Stock();
        stock.addComponentToFirebase(componentsToAdd, new Stock.AddComponentCallback() {
            @Override
            public void onComponentAdded() {
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void updateComponentInfo(Context context, Component componentToEdit, Component newComponent) {

        // Create a Map for the updates
        Map<String, Object> updates = new HashMap<>();
        updates.put("comment", newComponent.getComment());
        updates.put("description", componentToEdit.getDescription());
        updates.put("type", newComponent.getType());
        updates.put("subType", newComponent.getSubType());
        updates.put("quantity", newComponent.getQuantity());
        updates.put("dateModification", newComponent.getDateCréation());

        // Find the user by email in the database
        stockRef.orderByChild("description").equalTo(componentToEdit.getDescription()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Update the user info
                        userSnapshot.getRef().updateChildren(updates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Update successful.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to update user.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Error fetching user.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



