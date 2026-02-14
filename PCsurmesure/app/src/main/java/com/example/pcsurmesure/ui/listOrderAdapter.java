package com.example.pcsurmesure.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Requester;

import java.util.List;

public class listOrderAdapter extends ArrayAdapter<Command> {

    private static int countCommand = 0;
    private Context context;
    private List<Command> commandsList;
    private Requester requester;



    public listOrderAdapter(Context context, List<Command> commandsList) {
        super(context, 0, commandsList);
        this.context = context;
        this.commandsList = commandsList;
        requester = new Requester();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view if no reusable one exists
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_orders, parent, false);
        }

        // Get the current Command object for this position
        Command currentCommand = commandsList.get(position);


        TextView commandText = convertView.findViewById(R.id.first_name_text);
        Button editButton = convertView.findViewById(R.id.button3);
        Button deleteButton = convertView.findViewById(R.id.button6);

        commandText.setText("Command " + countCommand );
        countCommand ++;

        deleteButton.setOnClickListener(v -> {
            System.out.println("Attempting to delete command: " + currentCommand.getIdCommande());
            requester.deleteCommand(currentCommand.getIdCommande(), new Requester.DeleteCommandInterface() {
                @Override
                public void onCommandDeleted() {
                    Toast.makeText(context, "Command deleted successfully", Toast.LENGTH_SHORT).show();

                    // Remove the command from the list and update the ListView
                    commandsList.remove(position);
                    notifyDataSetChanged();

                    // Optionally, you can navigate back to a specific activity if needed
                    Intent intent = new Intent(context, UserActivity.class);
                    context.startActivity(intent);
                }

                @Override
                public void onException(Exception e) {
                    Toast.makeText(context, "Command deletion failed", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditOrderList.class);
            intent.putExtra("commandId", currentCommand.getIdCommande());
            context.startActivity(intent);
        });

        return convertView;
    }

}

