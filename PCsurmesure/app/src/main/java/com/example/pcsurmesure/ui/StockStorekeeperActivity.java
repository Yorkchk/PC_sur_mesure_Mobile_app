package com.example.pcsurmesure.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcsurmesure.R;

public class StockStorekeeperActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storekeeper_page);


        ImageButton manageStockButton = findViewById(R.id.manageStockButton);
        ImageButton addStockButton = findViewById(R.id.addStock);

        addStockButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddComponentActivity.class);
            intent.putExtra("mode", "storekeeper");
            startActivity(intent);
        });

        manageStockButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ComponentList.class);
            startActivity(intent);
        });


    }
}
