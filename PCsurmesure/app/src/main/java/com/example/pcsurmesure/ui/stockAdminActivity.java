package com.example.pcsurmesure.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Admin;
import com.example.pcsurmesure.models.StoreKeeper;

public class stockAdminActivity extends AppCompatActivity {

    Button deleteButton;
    ImageButton overrideDataBaseButton;
    ImageButton overrideStockButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockadmin);


        deleteButton = findViewById(R.id.deletebutton);
        overrideDataBaseButton = findViewById(R.id.overrideDataBase);
        overrideStockButton = findViewById(R.id.OverrideStock);

        deleteButton.setOnClickListener(v -> {
            Admin admin = new Admin();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete the database?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                admin.ResetDataBase(new StoreKeeper.ResetDataBaseCallback() {
                    @Override
                    public void onDataBaseReset() {
                        Toast.makeText(stockAdminActivity .this, "Reset succesful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Dismiss the dialog without performing any operation
                dialog.dismiss();
                Toast.makeText(this, "Operation canceled.", Toast.LENGTH_SHORT).show();
            });
            AlertDialog dialog = builder.create();
            dialog.show();


        });

        overrideDataBaseButton.setOnClickListener(v -> {
            Intent intent = new Intent(stockAdminActivity.this, AddComponentActivity.class);
            intent.putExtra("mode", "reinitialize database");
            startActivity(intent);
        });

        overrideStockButton.setOnClickListener(v -> {
            Intent intent = new Intent(stockAdminActivity.this, AddComponentActivity.class);
            intent.putExtra("mode", "reinitialize stock");
            startActivity(intent);
        });
    }
}
