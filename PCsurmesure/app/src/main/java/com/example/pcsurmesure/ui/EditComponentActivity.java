package com.example.pcsurmesure.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.StoreKeeper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditComponentActivity extends AppCompatActivity {

    private Component currentCompo = null;

    private TextView typeTextView;
    private TextView subTypeTextView;
    private TextView descripTextView;
    private TextView creationTextView;
    private TextView modifTextView;

    private EditText commentEditText;

    private Button save;

    private StoreKeeper st = new StoreKeeper();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcomponent);


        save = findViewById(R.id.add_button);

        typeTextView = findViewById(R.id.FirstName);
        subTypeTextView = findViewById(R.id.LastName);
        descripTextView = findViewById(R.id.Username);
        creationTextView = findViewById(R.id.Password);
        modifTextView = findViewById(R.id.confirm_passwordName);

        commentEditText = findViewById(R.id.Email);

        Intent intent = getIntent();
        String descCurrentCompo = intent.getStringExtra("componentDesc");
        System.out.println("Component description: " + descCurrentCompo);

        StoreKeeper.getAllStocksFromFirebase(new StoreKeeper.StocksCallback() {
            @Override
            public void onStockRetrieved(Component component) {
                System.out.println("if condition: " + component.getDescription().equals(descCurrentCompo));
                if(component.getDescription().equals(descCurrentCompo)){
                    currentCompo = component;
                    inizialiseUI();
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(EditComponentActivity.this, "Unable to retrieve component", Toast.LENGTH_SHORT);
            }
        });


    }
    private void inizialiseUI() {
        typeTextView.setText(currentCompo.getType());
        subTypeTextView.setText(currentCompo.getSubType());
        descripTextView.setText(currentCompo.getDescription());
        creationTextView.setText(currentCompo.getDateCréation());
        modifTextView.setText(currentCompo.getDateModification());


        save.setOnClickListener(v -> {
            String newCompoComment = commentEditText.getText().toString();
            System.out.println(newCompoComment);
            if (newCompoComment.equals("")) {
                Toast.makeText(EditComponentActivity.this, "Comment cannot be blank", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Component newComponent = new Component(currentCompo.getType(), currentCompo.getSubType(), currentCompo.getDescription(),
                        currentCompo.getQuantity(), newCompoComment);
                newComponent.setDateModification(dateFormat.format(new Date()));

                st.updateComponentInfo(EditComponentActivity.this, currentCompo, newComponent);

                Intent intent1 = new Intent(EditComponentActivity.this, ComponentList.class);
                startActivity(intent1);
                finish();
            }
        });

    }

}
