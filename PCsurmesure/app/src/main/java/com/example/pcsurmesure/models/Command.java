package com.example.pcsurmesure.models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.pcsurmesure.models.Component;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Command {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
    private Status status; // Adding the status attribute
//    Decided to add a unique id to each command to differentiate between them
    private String idCommande;

    private Map<Component,Integer> componentsQuantities;
    private String dateDeCreation;
    private String dateDeModification;
    private String requesterId;

//    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();
//    protected final static DatabaseReference commandsTable = database.getReference("Commands");

    public Command(String requesterId, Map<Component, Integer> componentsQuantities) {

        this.idCommande = UUID.randomUUID().toString(); // ID unique
        this.requesterId = requesterId;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateDeCreation = dateFormat.format(new Date());
        this.dateDeModification = null;
        this.componentsQuantities = componentsQuantities;
        this.status = Status.PENDING;
    }


    public Command(String idCommand, String requesterId, String dateDeCreation, Map<Component, Integer> componentsQuantities) {

        this.idCommande = idCommand; // ID unique
        this.requesterId = requesterId;
        this.dateDeCreation = dateDeCreation;
        this.componentsQuantities = componentsQuantities;
        this.status = Status.PENDING;
    }




    public Command(){};
    // Getters
    public Status getStatus() {
        return status;
    }
    public String getDateDeCreation() {
        return dateDeCreation;
    }
    public String getIdCommande(){
        return idCommande;
    }
    public void setComponentsQuantities(Map<Component, Integer> quantities){
        this.componentsQuantities = quantities;
    }

    public void setCommandId(String idCommande){this.idCommande = idCommande;}

    public String getDateDeModification() {
        return dateDeModification;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public Map<Component,Integer> getComponentsQuantities() {
        return componentsQuantities;
    }

    // Setters
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setDateDeModification(String dateDeModification) {
        this.dateDeModification = dateDeModification;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }
    //Check si les quantités dans la commande existe dans le stock
    public interface QuantityCheckCallback {
        void onQuantityCheckComplete(boolean result, Component component);
    }


    public interface EditCommand{
        void onCommandEdited();
        void onException(Exception e);
    }

//    Update l'attribut componentsQuantities de command avec laquelle tu appelles la méthode avec celle de newCommand
    public void editCommand(Command newCommand){
        checkQuantities(newCommand.getComponentsQuantities(), new QuantityCheckCallback() {
            @Override
            public void onQuantityCheckComplete(boolean result, Component component) {
                if(result && component == null){
                    Log.d(TAG, "All components have correct quantities");
                    updateCommand(newCommand, new EditCommand() {
                        @Override
                        public void onCommandEdited() {
                            Log.d(TAG, "The component has been edited successfully");
                        }

                        @Override
                        public void onException(Exception e) {
                            Log.d(TAG, "Failed to edit the component");
                        }
                    });
                }
                else if(!result && component != null){
                    Log.d(TAG, component.getDescription() + " should not exceed" + component.getQuantity() + "pieces");
                }
            }
        });



    }

    public void updateCommand(Command newCommand, EditCommand editCommandCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commandsTable = database.getReference("Commands");

        setComponentsQuantities(newCommand.getComponentsQuantities());



        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Integer> componentsQuantitiesStringMap = new HashMap<>();

        for (Map.Entry<Component, Integer> entry : getComponentsQuantities().entrySet()) {
            String key = entry.getKey().getDescription(); // or another unique string identifier for the component
            Integer value = entry.getValue();
            componentsQuantitiesStringMap.put(key, value);
        }

        Map<String,Object> dataUpdated = new HashMap<>();
        dataUpdated.put("componentsQuantities", componentsQuantitiesStringMap);
        dataUpdated.put("dateDeModification",dateFormat.format(new Date()) );
        dataUpdated.put("requesterId",newCommand.getRequesterId());


        commandsTable.child(this.idCommande).updateChildren(dataUpdated)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Edit component succesful");
                        editCommandCallback.onCommandEdited();
                    }
                    else{
                        editCommandCallback.onException(task.getException());
                    }
                });
    }

    public static boolean validateQuantity(Map<Component, Integer> compoQuant){
        for(Component component : compoQuant.keySet()){
            if(compoQuant.get(component)<=0 ||compoQuant.get(component) > component.getQuantity()){
                return false;
            }
        }
        return true;
    }


    public static void checkQuantities(Map<Component, Integer> map, QuantityCheckCallback callback) {
        StoreKeeper.getAllStocksFromFirebase(new StoreKeeper.StocksCallback() {
            boolean flag = true;
            Component componentExceeded = null;

            @Override
            public void onStockRetrieved(Component component) {
                for (Component componentMap : map.keySet()) {
                    if (componentMap.getDescription().equals(component.getDescription())) {
                        if (map.get(componentMap) > component.getQuantity()) {
                            flag = false;
                            componentExceeded = component;
                            return;
                        }
                    }
                }
            }

            @Override
            public void onComplete() {
                if(flag) {
//                    Tout les quantités ont une quantité raisonnable
                    callback.onQuantityCheckComplete(true, null);
                }
                else{
//                    La composante componentExceeded a une quantité qui dépasse celui du stock
                    callback.onQuantityCheckComplete(false, componentExceeded);
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle any errors, if necessary
                Log.d(TAG, e.toString());
            }
        });
    }

    public interface AddComponentToCommandInterface{
        void onComponentsAdded();

        void onException(Exception e);
    }
//This method add new components to the command
        public void addComponentsToCommand(Map<Component, Integer> componentsQuantities){
        Map<Component, Integer> dummyMap = getComponentsQuantities();
        dummyMap.putAll(componentsQuantities);

        setComponentsQuantities(dummyMap);
        Command newCommand = new Command(requesterId,getComponentsQuantities() );
        checkQuantities(componentsQuantities, new QuantityCheckCallback() {
                @Override
                public void onQuantityCheckComplete(boolean result, Component component) {
                    if(result && component == null){
                        Log.d(TAG, "All components have correct quantities");
                        updateCommand(newCommand, new EditCommand() {
                            @Override
                            public void onCommandEdited() {
                                Log.d(TAG, "The component has been edited successfully");
                            }

                            @Override
                            public void onException(Exception e) {
                                Log.d(TAG, "Failed to edit the component");
                            }
                        });
                    }
                    else if(!result && component != null){
                        Log.d(TAG, component.getDescription() + " should not exceed" + component.getQuantity() + "pieces");
                    }
                }
            });
        }

    public interface CommandsCallback {
        void onCommandRetrieved(Command command);

        void onComplete();

        void onError(Exception e);
    }

//    Gets all the commands in the data base

public static void getAllCommandsFromFirebase(CommandsCallback callback) {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference commandsTable = database.getReference("Commands");
    commandsTable.get().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            // A list to store all commands
            List<Command> commands = new ArrayList<>();

            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                // Extract attributes
                String idCommande = snapshot.child("idCommande").getValue(String.class);
                String requesterId = snapshot.child("requesterId").getValue(String.class);
                String dateCreation = snapshot.child("dateDeCreation").getValue(String.class);
                String status = snapshot.child("status").getValue(String.class);

                // Initialize components map and a counter to track completed component retrievals
                Map<Component, Integer> componentsQuantities = new HashMap<>();
                DataSnapshot componentsSnapshot = snapshot.child("componentsQuantities");
                int totalComponents = (int) componentsSnapshot.getChildrenCount();
                final int[] retrievedComponentsCount = {0};

                for (DataSnapshot componentSnapshot : componentsSnapshot.getChildren()) {
                    String description = componentSnapshot.getKey(); // Assuming the key is the description
                    Integer componentQuantity = componentSnapshot.getValue(Integer.class);

                    // Retrieve components using description
                    StoreKeeper.getAllStocksFromFirebase(new StoreKeeper.StocksCallback() {
                        @Override
                        public void onStockRetrieved(Component component) {
                            if (component.getDescription().equals(description)) {
                                componentsQuantities.put(component, componentQuantity);
                            }
                        }

                        @Override
                        public void onComplete() {
                            // Increase counter and check if all components are retrieved
                            retrievedComponentsCount[0]++;
                            if (retrievedComponentsCount[0] == totalComponents) {
                                // All components for this command are retrieved, so create the Command
                                Command command = new Command(idCommande, requesterId, dateCreation, componentsQuantities);
                                if(status.equals("APPROVED")){
                                    command.setStatus(Status.APPROVED);
                                }else if(status.equals("REJECTED")){
                                    command.setStatus(Status.REJECTED);
                                }else if(status.equals("PENDING")){
                                    command.setStatus(Status.PENDING);
                                }
                                commands.add(command);
                                callback.onCommandRetrieved(command);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("ERROR", "Error retrieving stock: " + e.getMessage());
                        }
                    });
                }
            }
            callback.onComplete();
        } else {
            Log.e("ERROR", "Error retrieving commands: ", task.getException());
            callback.onError(task.getException());
        }
    });
}


//    Change the quantity of a component in the command you call the method with

//    I presume that the component is present in the command
public void changeQuantity(Component component, int value) {
    for (Component componentMap : componentsQuantities.keySet()) {
        if (componentMap.getDescription().equals(component.getDescription())) {
            int updatedQuantity = componentsQuantities.get(componentMap) + value;
            componentsQuantities.replace(componentMap, updatedQuantity);
            editCommand(this);
            return;
        }
    }
}

    public interface DeleteComponentInCommandCallback{
            void onComponentDeleted();
        void onException(Exception e);
    }

    public void deleteComponentInCommand(Component component, DeleteComponentInCommandCallback callback){

        for (Component componentMap : componentsQuantities.keySet()) {
            if(componentMap.getDescription().equals(component.getDescription())){
                componentsQuantities.remove(componentMap);
                editCommand(this);
                callback.onComponentDeleted();
                break;
            }
        }
    }





}
