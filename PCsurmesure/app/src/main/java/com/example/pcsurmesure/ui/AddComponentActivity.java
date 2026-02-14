package com.example.pcsurmesure.ui;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Admin;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.ReadXmlDomParser;
import com.example.pcsurmesure.models.Stock;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddComponentActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private List<Uri> fileUris = new ArrayList<>();
    private TextView textFileName;
    private Button buttonReadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        Button buttonSelectFile = findViewById(R.id.button_select_file);
        textFileName = findViewById(R.id.text_file_name);
        buttonReadFile = findViewById(R.id.button_read_file);

        // Select file button click listener
        buttonSelectFile.setOnClickListener(v -> openFilePicker());

        // Read file button click listener
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        if(mode.equals("storekeeper")) {
            buttonReadFile.setText("Add Components");
            buttonReadFile.setOnClickListener(v -> addComponentStoreKeeper());
        }
        else if(mode.equals("reinitialize database")){
            buttonReadFile.setText("Reinitialize DataBase");
            buttonReadFile.setOnClickListener(v -> reinitializeDataBase());
        }
        else if(mode.equals("reinitialize stock")){
            buttonReadFile.setText("reinitialize stock");
            buttonReadFile.setOnClickListener(v -> reinitializeStock());
        }

    }

    // Method to open the file picker
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Accept any file type
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                // Get the file name
                String fileName = getFileName(fileUri);
                if(textFileName.getText().equals("No file selected")) {
                    textFileName.setText(fileName + "\n");
                }
                else{
                    textFileName.setText(textFileName.getText() + fileName + "\n");
                }
                fileUris.add(fileUri);
                buttonReadFile.setEnabled(true); // Enable the read file button
            }
        }
    }

    // Helper method to get file name from URI
    private String getFileName(Uri uri) {
        String fileName = "";
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                fileName = cursor.getString(nameIndex);
            }
        }
        return fileName;
    }
//Add components from xml to database
    private void addComponentStoreKeeper() {
        Stock s = new Stock();
        try {
            for (Uri fileUri : fileUris) {
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                List<Component> components = ReadXmlDomParser.getComponentsFromStream(inputStream);
                System.out.println("Components: " + components);

                s.addComponentToFirebase(components, new Stock.AddComponentCallback() {
                    @Override
                    public void onComponentAdded() {
                        Toast.makeText(AddComponentActivity.this, "Components added!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AddComponentActivity.this, "Error adding components: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getPathFromUri(Uri uri) {
        String filePath = null;

        // Check if the URI is of type "content"
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }
        } else if ("file".equals(uri.getScheme())) {
            // If it's a file scheme, you can just use the path directly
            filePath = uri.getPath();
        }

        return filePath;
    }



//    Delete components and add new components from xml file
    private void reinitializeStock() {
        Admin admin = new Admin();
        try {
            for (Uri fileUri : fileUris) {
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                List<Component> components = ReadXmlDomParser.getComponentsFromStream(inputStream);
                System.out.println("Components: " + components);

                admin.ResetFirebaseComponents(components, new StoreKeeper.ResetDataBaseCallback() {
                    @Override
                    public void onDataBaseReset() {
                        Toast.makeText(AddComponentActivity.this, "Components added!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AddComponentActivity.this, "Error adding components: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




//Delete database and override it with new users and components from xml file
    private void reinitializeDataBase() {
    Admin admin = new Admin();
    try {
        for (Uri fileUri : fileUris) {
            // Open the stream and read components
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            List<Component> components = ReadXmlDomParser.getComponentsFromStream(inputStream);

            // Close the stream after reading components
            if (inputStream != null) {
                inputStream.close();
            }

            // Reopen the stream for reading users
            inputStream = getContentResolver().openInputStream(fileUri);
            List<User> users = ReadXmlDomParser.getUsersFromStream(inputStream);
            System.out.println("Users: " + users);

            // Close the stream after reading users
            if (inputStream != null) {
                inputStream.close();
            }


            // Reset Firebase components
            admin.ResetFirebaseComponents(components, new StoreKeeper.ResetDataBaseCallback() {
                @Override
                public void onDataBaseReset() {
                    Toast.makeText(AddComponentActivity.this, "Components added!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(AddComponentActivity.this, "Error adding components: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Reset Firebase users
            admin.ResetFirebaseUsers(users, new StoreKeeper.ResetDataBaseCallback() {
                @Override
                public void onDataBaseReset() {
                    Toast.makeText(AddComponentActivity.this, "Users and components added!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(AddComponentActivity.this, "Error adding users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}






}
