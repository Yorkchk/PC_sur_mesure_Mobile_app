package com.example.pcsurmesure.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.Requester;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderWaitlistActivity extends AppCompatActivity {

    private ListView listView;
    private OrderWaitlistAdapter adapter;
    private List<Command> commandsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_orders_wailist);  // Le layout avec SearchView et ListView

        // Initialiser les vues
        listView = findViewById(R.id.orders_waitlist);

        commandsList = new ArrayList<>();
        adapter = new OrderWaitlistAdapter(this, commandsList);

        //        Add all the commands not assembled yet

        Command.getAllCommandsFromFirebase(new Command.CommandsCallback() {
            @Override
            public void onCommandRetrieved(Command command) {
                System.out.println("Status");
                System.out.println(command.getStatus() == Command.Status.PENDING);
                if(command.getStatus() == Command.Status.PENDING) {
                    commandsList.add(command);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onComplete() {
                listView.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });






//        Test ui

//        Map<Component, Integer> map1 = new HashMap<Component, Integer>();
//        Map<Component, Integer> map2 = new HashMap<Component, Integer>();
//
//        map1.put(new Component("Matériel", "Display", "4K Monitor 27 inch", 10, "Ultra HD monitor with HDR support."), 6);
//        map1.put(new Component("Matériel", "Storage", "500GB External Hard Drive", 30, "Portable hard drive for backup and storage."), 20);
//
//        map2.put(new Component("Matériel", "Display", "4K Monitor 27 inch", 10, "Ultra HD monitor with HDR support."), 8);
//        map2.put(new Component("Logiciel", "Security", "VPN Software License", 150, "Virtual private network for secure browsing."), 80 );
//        Command command1 = new Command("123", map1);
//        Command command2 = new Command("123", map2);
//
//
//        Requester rq = new Requester();
//        rq.createCommand(command1, new Requester.AddCommand() {
//            @Override
//            public void onComponentAdded() {
//
//            }
//
//            @Override
//            public void onException(Exception e) {
//
//            }
//        });
//        rq.createCommand(command2, new Requester.AddCommand() {
//            @Override
//            public void onComponentAdded() {
//
//            }
//
//            @Override
//            public void onException(Exception e) {
//
//            }
//        });
//
//        commandsList.add(command1);
//        commandsList.add(command2);









    }
}