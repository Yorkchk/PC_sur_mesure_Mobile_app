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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Admin;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.User;

import java.io.Serializable;
import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> userList;

    private Authentication auth;
    private User loggedInUser;

    public UserListAdapter(Context context, List<User> userList) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view if no reusable one exists
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        }

        // Get the current User object
        User currentUser = userList.get(position);

        // Get references to the TextViews and buttons in the custom layout
        TextView firstNameTextView = convertView.findViewById(R.id.first_name_text);
        TextView lastNameTextView = convertView.findViewById(R.id.last_name_text);
        Button editButton = convertView.findViewById(R.id.edit_button);
        Button removeButton = convertView.findViewById(R.id.remove_button);

        // Set the user data in the views
        firstNameTextView.setText(currentUser.getFirstName());
        lastNameTextView.setText(currentUser.getLastName());

        // Set an OnClickListener for the Edit button
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AccountSetting.class);
            intent.putExtra("userEmail",  currentUser.getEmail());
            intent.putExtra("mode", "admin");
            context.startActivity(intent);

        });

        // Set an OnClickListener for the Remove button
        removeButton.setOnClickListener(v -> {
            System.out.println("check instance");
            System.out.println(loggedInUser instanceof Admin);

                Admin admin = new Admin();
                admin.removeUserFromFirebase(currentUser, new Admin.RemoveUserCallback() {
                    @Override
                    public void onUserRemoved() {
                        Toast.makeText(context, "Removal successful!", Toast.LENGTH_SHORT).show();

                        // Proceed to login activity
                        Intent intent = new Intent(context, com.example.pcsurmesure.ui.AdminActivity.class);
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
