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
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComponentsWaitlistAdapter extends ArrayAdapter<Component> {
    private Context context;
    private List<Component> componentsList;

    private Command command;



    public ComponentsWaitlistAdapter(Context context, List<Component> componentsList, Command command) {
        super(context, 0, componentsList);
        this.context = context;
        this.componentsList = componentsList;
        this.command = command;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view if no reusable one exists
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.components_waitlist, parent, false);
        }

        // Get the current User object
        Component currentComponent = componentsList.get(position);

        // Get references to the TextViews and buttons in the custom layout
        TextView componentTextView = convertView.findViewById(R.id.component_text);
        TextView quantityTextView = convertView.findViewById(R.id.quant);




        // Set the user data in the views
        componentTextView.setText(currentComponent.getDescription());

        for(Component component : command.getComponentsQuantities().keySet()){
            if(component.getDescription().equals(currentComponent.getDescription())){
                quantityTextView.setText("" + command.getComponentsQuantities().get(component));
                break;
            }
        }




        return convertView;

    }


}
