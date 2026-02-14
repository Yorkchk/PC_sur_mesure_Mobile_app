package com.example.pcsurmesure.models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class Stock {
    private DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("Stock");

    public Stock(){
    }

    public interface AddComponentCallback {
        void onComponentAdded();
        void onError(Exception e);
    }

    public void addComponentToFirebase(List<Component> components, AddComponentCallback callback) {
        for (Component component : components) {
            String componentDescription = component.getDescription();

            // Check if a component with this description already exists
            stockRef.child(componentDescription).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Component with this description already exists
                        callback.onError(new Exception("Description already exists."));
                    } else {
                        // Component does not exist, proceed with adding it
                        stockRef.child(componentDescription).setValue(component)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Component added successfully.");
                                        callback.onComponentAdded();
                                    } else {
                                        Log.e(TAG, "Failed to add component.", task.getException());
                                        callback.onError(task.getException());
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onError(databaseError.toException());
                }
            });
        }
    }

}
