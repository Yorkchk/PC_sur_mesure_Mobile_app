
package com.example.pcsurmesure.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.google.android.material.navigation.NavigationView;

public class UserActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private Authentication authentication;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        authentication = new Authentication();

        drawerLayout = findViewById(R.id.drawer_layout);
        Button button_create_order = findViewById(R.id.button_create_order);
        Button myOrdersButton = findViewById(R.id.button_Orders_list);

        myOrdersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListOrder.class);
            intent.putExtra("mode", "createOrder");
            startActivity(intent);
        });

        ImageButton imageButton = findViewById(R.id.menu_button);
        button_create_order.setOnClickListener(v -> {
            Intent intent = new Intent(this, createOrderActivity.class);
            startActivity(intent);
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.menu_view);

        // Handle navigation item selection
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(UserActivity.this, UserActivity.class);
                    startActivity(intent);
                    finish();

                }else if (item.getItemId() == R.id.nav_createoreder) {
                    // Do something
//                    We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_orderlist) {
                    // Do something
//                    We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_logout) {
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    authentication.setLoggedInUser(null);
                    Toast.makeText(UserActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }


                // Close the drawer after selecting an item
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ImageButton profileButton = findViewById(R.id.profile_button);

        // Set OnClickListener on the ImageButton
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new Activity
                Intent intent = new Intent(UserActivity.this, AccountSetting.class);
                intent.putExtra("mode", "self");
                startActivity(intent); // Start the new activity
            };
        });
        Button logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            authentication.onLogout(UserActivity.this, LoginActivity.class, new Authentication.logoutCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(UserActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(UserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            });
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

