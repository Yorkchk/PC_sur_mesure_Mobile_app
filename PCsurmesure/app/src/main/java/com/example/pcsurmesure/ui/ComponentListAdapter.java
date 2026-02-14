package com.example.pcsurmesure.ui;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Admin;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComponentListAdapter extends ArrayAdapter<Component> {
    private Context context;
    private List<Component> componentsList;

    private Authentication auth;
    private User loggedInUser;

    public ComponentListAdapter(Context context, List<Component> componentsList) {
        super(context, 0, componentsList);
        this.context = context;
        this.componentsList = componentsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view if no reusable one exists
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_component, parent, false);
        }

        // Get the current User object
        Component currentComponent = componentsList.get(position);

        // Get references to the TextViews and buttons in the custom layout
        TextView typeTextView = convertView.findViewById(R.id.type);
        TextView subTypeTextView = convertView.findViewById(R.id.subType);
        TextView descripTextView = convertView.findViewById(R.id.textView9);
        TextView quantityTextView = convertView.findViewById(R.id.textView10);


        Button editButton = convertView.findViewById(R.id.button4);
        Button removeButton = convertView.findViewById(R.id.button2);
        ImageButton minusButton = convertView.findViewById(R.id.imageView4);
        ImageButton plusButton = convertView.findViewById(R.id.imageView3);


        // Set the user data in the views
        typeTextView.setText(currentComponent.getType());
        descripTextView.setText(currentComponent.getDescription());
        subTypeTextView.setText(currentComponent.getSubType());
        quantityTextView.setText("" + currentComponent.getQuantity());


//Come back to it later when sofia makes the ui of editComponent

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditComponentActivity.class);
            intent.putExtra("componentDesc",  currentComponent.getDescription());
            context.startActivity(intent);

        });

        plusButton.setOnClickListener(v -> {
            Component newComponent = currentComponent;
            newComponent.setQuantity(currentComponent.getQuantity() + 1);
            quantityTextView.setText("" + currentComponent.getQuantity());
            StoreKeeper st = new StoreKeeper();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentComponent.setDateModification(dateFormat.format(new Date()));

            st.updateComponentInfo(this.getContext(), currentComponent, newComponent);

        });

        minusButton.setOnClickListener(v -> {
            if(currentComponent.getQuantity() == 0){
                Toast.makeText(context, "Cannot go lower than 0", Toast.LENGTH_SHORT).show();
            }
            else {
                Component newComponent = currentComponent;
                newComponent.setQuantity(currentComponent.getQuantity() - 1);
                quantityTextView.setText("" + currentComponent.getQuantity());
                StoreKeeper st = new StoreKeeper();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentComponent.setDateModification(dateFormat.format(new Date()));
                st.updateComponentInfo(this.getContext(), currentComponent, newComponent);
            }
        });

        // Set an OnClickListener for the Remove button
        removeButton.setOnClickListener(v -> {
            StoreKeeper st = new StoreKeeper();

            st.removeStockFromFirebase(currentComponent, new StoreKeeper.RemoveStockCallback() {
                @Override
                public void onStockRemoved() {
                    Toast.makeText(context, "Removal successful!", Toast.LENGTH_SHORT).show();
//
//                        // Proceed to login activity
                        Intent intent = new Intent(context, com.example.pcsurmesure.ui.StoreKeeperActivity.class);
                        context.startActivity(intent);
                }

                @Override
                public void onError(Exception e) {
                        Toast.makeText(context, "Failed to remove user. Please try again.", Toast.LENGTH_SHORT).show();

                }

                });
        });

        return convertView;

        }


}
