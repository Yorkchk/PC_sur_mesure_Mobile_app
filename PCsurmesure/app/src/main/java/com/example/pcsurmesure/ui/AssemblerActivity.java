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

public class AssemblerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private Authentication authentication;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembler);

        authentication = new Authentication();

        Toolbar toolbar = findViewById(R.id.my_toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ImageButton imageButton = findViewById(R.id.menu_button);
        Button ordersToAssembleButton = findViewById(R.id.button_orders_to_assemble);


        ordersToAssembleButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderWaitlistActivity.class);
            startActivity(intent);
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.menu_view);

        // Gérer la sélection des éléments du menu de navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(AssemblerActivity.this, AssemblerActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_orders_to_assemble) {
                    // Later when we arrive in the backend
                }else if (item.getItemId() == R.id.nav_createoreder) {
                    // Do something
//                    We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_orderlist) {
                    // Do something
//                    We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_logout) {
                    Intent intent = new Intent(AssemblerActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AssemblerActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }


                // Fermer le drawer après la sélection d'un élément
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        Button logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            authentication.onLogout(AssemblerActivity.this, LoginActivity.class, new Authentication.logoutCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AssemblerActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AssemblerActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            });
        });
        ImageButton profileButton = findViewById(R.id.profile_button);

        // Set OnClickListener on the ImageButton
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new Activity
                Intent intent = new Intent(AssemblerActivity.this, AccountSetting.class);
                intent.putExtra("mode", "self");
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
