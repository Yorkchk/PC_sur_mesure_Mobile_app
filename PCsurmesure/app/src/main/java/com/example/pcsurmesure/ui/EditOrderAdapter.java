package com.example.pcsurmesure.ui;

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
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.Requester;

import java.util.List;

public class EditOrderAdapter extends ArrayAdapter<Component> {

    private Context context;
    private List<Component> componentsList;

    private Command command;


    public EditOrderAdapter(Context context, List<Component> componentsList, Command command) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.edit_order, parent, false);
        }

        // Get the current Command object for this position
        Component currentComponent = componentsList.get(position);


        System.out.println("Current Command :" + command.getComponentsQuantities().get(currentComponent) );

        TextView componentText = convertView.findViewById(R.id.first_name_text);
        TextView quantity = convertView.findViewById(R.id.textView8);
        ImageButton plusButton = convertView.findViewById(R.id.imageButton6);
        ImageButton minusButton = convertView.findViewById(R.id.imageButton7);
        ImageButton deleteComponentButton = convertView.findViewById(R.id.imageButton8);

        Integer quantityInt = null;
        componentText.setText(currentComponent.getDescription());

        for (Component comp : command.getComponentsQuantities().keySet()) {
            if (comp.getDescription().equals(currentComponent.getDescription())) {
                // Retrieve the quantity for the matching component from the map
                quantityInt = command.getComponentsQuantities().get(comp);
                break;
            }
        }
        quantity.setText(String.valueOf(quantityInt));


                deleteComponentButton.setOnClickListener(v -> {
                    command.deleteComponentInCommand(currentComponent, new Command.DeleteComponentInCommandCallback() {
                        @Override
                        public void onComponentDeleted() {
                            Toast.makeText(context, "Component deleted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, UserActivity.class);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onException(Exception e) {
                            Toast.makeText(context, "Component deleteion failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
//            requester.deleteCommand(currentCommand.getIdCommande(), new Requester.DeleteCommandInterface() {
//                @Override
//                public void onCommandDeleted() {
//                    Toast.makeText(context, "Command deleted successfully", Toast.LENGTH_SHORT).show();
//
//                    // Remove the command from the list and update the ListView
//                    commandsList.remove(position);
//                    notifyDataSetChanged();
//
//                    // Optionally, you can navigate back to a specific activity if needed
//                    Intent intent = new Intent(context, UserActivity.class);
//                    context.startActivity(intent);
//                }
//
//                @Override
//                public void onException(Exception e) {
//                    Toast.makeText(context, "Command deletion failed", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });

        plusButton.setOnClickListener(v -> {
            command.changeQuantity(currentComponent,1);
            Toast.makeText(context, "Quantity added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, UserActivity.class);
            context.startActivity(intent);
        });

        minusButton.setOnClickListener(v -> {
            command.changeQuantity(currentComponent,-1);
            Intent intent = new Intent(context, UserActivity.class);
            context.startActivity(intent);
            Toast.makeText(context, "Quantity decreased successfully", Toast.LENGTH_SHORT).show();
        });



        return convertView;
    }
}

