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

public class StoreKeeperActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private Authentication authentication;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper);
        authentication = new Authentication();


        Toolbar toolbar = findViewById(R.id.my_toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ImageButton imageButton = findViewById(R.id.menu_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.menu_view);

        // Gérer la sélection des éléments de navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(StoreKeeperActivity.this, StoreKeeperActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_addstocks) {
                    // Later when we arrive in the backend
                }else if (item.getItemId() == R.id.nav_createoreder) {
                    // Do something
//                    We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_orderlist) {
                    // Do something
//                    We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_logout) {
                    Intent intent = new Intent(StoreKeeperActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(StoreKeeperActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }


                // Fermer le tiroir après la sélection d'un élément
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        Button logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            authentication.onLogout(StoreKeeperActivity.this, LoginActivity.class, new Authentication.logoutCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(StoreKeeperActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(StoreKeeperActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            });
        });
        ImageButton profileButton = findViewById(R.id.profile_button);

        // Set OnClickListener on the ImageButton
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new Activity
                Intent intent = new Intent(StoreKeeperActivity.this, AccountSetting.class);
                intent.putExtra("mode", "self");
                startActivity(intent); // Start the new activity
            }
        });
        Button stockButton = findViewById(R.id.button_Stock);

        // Set OnClickListener on the ImageButton
        stockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new Activity
                Intent intent = new Intent(StoreKeeperActivity.this, StockStorekeeperActivity.class);
                startActivity(intent); // Start the new activity
            }
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
