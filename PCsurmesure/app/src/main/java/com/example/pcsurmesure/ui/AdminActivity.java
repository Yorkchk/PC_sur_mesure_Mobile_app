package com.example.pcsurmesure.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private Authentication authentication;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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


        ImageButton profileButton = findViewById(R.id.profile_button);

        // Définir OnClickListener sur le bouton d'image
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour démarrer la nouvelle activité
                Intent intent = new Intent(AdminActivity.this, AccountSetting.class);
                intent.putExtra("mode", "self");
                startActivity(intent); // Start the new activity
            }
        });

        NavigationView navigationView = findViewById(R.id.menu_view);

        // Gérer la sélection des éléments de navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.nav_addusers) {
                    Intent intent = new Intent(AdminActivity.this, AddUserActivity.class);
                    startActivity(intent);

                }else if (item.getItemId() == R.id.nav_removeusers) {
                    Intent intent = new Intent(AdminActivity.this, Remove.class);  // Remove est la classe où vous avez implémenté getAllUsers()
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_createoreder) {
                   
//                    //We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_orderlist) {
                    
//                    //We are going to implement it later because it is not part of livrable 1
                }else if (item.getItemId() == R.id.nav_logout) {
                    Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    authentication.setLoggedInUser(null);
                    Toast.makeText(AdminActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }


                // Close the drawer after selecting an item
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Button addUserButton = findViewById(R.id.button_add_users);
        addUserButton.setOnClickListener(v -> {
            // Naviguer vers SignUpActivity
            Intent intent = new Intent(AdminActivity.this, AddUserActivity.class);
            startActivity(intent);

        });

        Button removeUserButton = findViewById(R.id.button_remove_users);
        removeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige vers l'activité Remove où les utilisateurs sont affichés
                Intent intent = new Intent(AdminActivity.this, Remove.class);  // Remplacez RemoveActivity par le nom de votre classe qui affiche les utilisateurs
                startActivity(intent);
            }
        });

        Button stockButton = findViewById(R.id.button_home);
        stockButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, stockAdminActivity.class);
            startActivity(intent);
        });

        Button logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            authentication.onLogout(AdminActivity.this, LoginActivity.class, new Authentication.logoutCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AdminActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AdminActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

            });
        });

    }

}
