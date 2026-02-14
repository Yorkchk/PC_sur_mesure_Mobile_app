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
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createOrderAdapter extends ArrayAdapter<Component> {
    private Context context;
    private List<Component> componentsList;

    Map<Component, Integer> componentsQuantities;

    public createOrderAdapter(Context context, List<Component> componentsList) {
        super(context, 0, componentsList);
        this.context = context;
        this.componentsList = componentsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view if no reusable one exists
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.creation_orders, parent, false);
        }

        // Get the current User object
        Component currentComponent = componentsList.get(position);

        TextView componentView = convertView.findViewById(R.id.textView11);
        EditText quantityOrder = convertView.findViewById(R.id.textView12);
        ImageButton addComponentButton = convertView.findViewById(R.id.imageButton5);

        componentView.setText(currentComponent.getDescription());


        componentsQuantities = new HashMap<>();

        addComponentButton.setOnClickListener(v -> {
            String quantity = quantityOrder.getText().toString();
            int quantint;

            try {
                quantint = Integer.parseInt(quantity);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Please enter a valid number for quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantint == 0) {
                Toast.makeText(context, "Cannot add component with quantity 0", Toast.LENGTH_SHORT).show();
            } else {
                componentsQuantities.put(currentComponent, quantint);
                Toast.makeText(context, "Component added with quantity: " + quantint, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;

    }

    public Map<Component, Integer> getComponentsQuantities(){
        return componentsQuantities;
    }


}
