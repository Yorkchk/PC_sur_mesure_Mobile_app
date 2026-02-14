package com.example.pcsurmesure.models;

import android.content.Context;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class Assembler extends User {

    private static final String TAG = "Assembler";

    public Assembler(String username, String firstName, String lastName, String email, String password) {
        super(username, firstName, lastName, email, password);
        setRole(ROLE.Assembler);
    }

    public Assembler() {}

    // Callback interfaces
    public interface ApproveCommandCallback {
        void onCommandApproved();

        void onException(Exception e);
    }

    public interface RejectCommandCallback {
        void onCommandRejected();

        void onException(Exception e);
    }

    public interface StockUpdateCallback {
        void onStockUpdated();

        void onException(Exception e);
    }

    // Method to approve command
    public void approveCommand(Context context, String commandId, ApproveCommandCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commandsTable = database.getReference("Commands");

        try {
            Command.getAllCommandsFromFirebase(new Command.CommandsCallback() {
                @Override
                public void onCommandRetrieved(Command command) {
                    if (command.getIdCommande().equals(commandId)) {
                        Map<Component, Integer> componentsQuantities = command.getComponentsQuantities();

                        try {
                            updateStock(context, componentsQuantities, new StockUpdateCallback() {
                                @Override
                                public void onStockUpdated() {
                                    // After updating stock, update the command's status
                                    try {
                                        commandsTable.child(commandId).child("status").setValue(Command.Status.APPROVED.name())
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Command approved, stock updated, and status set to APPROVED successfully.");
                                                        callback.onCommandApproved();
                                                    } else {
                                                        Log.d(TAG, "Failed to update status to APPROVED after stock update.");
                                                        callback.onException(task.getException());
                                                    }
                                                });
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error while updating command status: " + e.getMessage());
                                        callback.onException(e);
                                    }
                                }

                                @Override
                                public void onException(Exception e) {
                                    Log.e(TAG, "Error during stock update: " + e.getMessage());
                                    callback.onException(e);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "Error in updateStock method: " + e.getMessage());
                            callback.onException(e);
                        }
                    }
                }

                @Override
                public void onComplete() {
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "Error retrieving command: " + e.getMessage());
                    callback.onException(e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "General exception in approveCommand method: " + e.getMessage());
            callback.onException(e);
        }
    }


    // Method to reject command
    public void rejectCommand(String commandId, RejectCommandCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commandsTable = database.getReference("Commands");

        // Update the status of the command to "REJECTED"
        commandsTable.child(commandId).child("status").setValue(Command.Status.REJECTED.name())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Command status updated to REJECTED successfully.");
                        callback.onCommandRejected();
                    } else {
                        Log.d(TAG, "Failed to update command status to REJECTED.");
                        callback.onException(task.getException());
                    }
                });
    }



    // Method to update stock for components
    void updateStock(Context context, Map<Component, Integer> componentsQuantities, StockUpdateCallback callback) throws Exception {
        StoreKeeper st = new StoreKeeper();
        for (Map.Entry<Component, Integer> entry : componentsQuantities.entrySet()) {
            Component component = entry.getKey();
            int quantityUsed = entry.getValue();
            if(quantityUsed > component.getQuantity()){
                throw new Exception("The components of the command exceed the number in stock");
            }
            Component newComponent = new Component(
                    component.getType(),
                    component.getSubType(),
                    component.getDescription(),
                    component.getQuantity() - quantityUsed,
                    component.getComment()
            );

            st.updateComponentInfo(context, component, newComponent);

        }
        callback.onStockUpdated();

    }
}