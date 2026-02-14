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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pcsurmesure.R;
import com.example.pcsurmesure.models.Admin;
import com.example.pcsurmesure.models.Assembler;
import com.example.pcsurmesure.models.Authentication;
import com.example.pcsurmesure.models.Command;
import com.example.pcsurmesure.models.Component;
import com.example.pcsurmesure.models.Requester;
import com.example.pcsurmesure.models.StoreKeeper;
import com.example.pcsurmesure.models.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderWaitlistAdapter extends ArrayAdapter<Command> {
    private Context context;
    private List<Command> commandsList;



    public OrderWaitlistAdapter(Context context, List<Command> commandsList) {
        super(context, 0, commandsList);
        this.context = context;
        this.commandsList = commandsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view if no reusable one exists
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.orders_waitlist, parent, false);
        }

        Command currentCommand = commandsList.get(position);

        TextView orderText = convertView.findViewById(R.id.Order_text);
        Button detailsButton = convertView.findViewById(R.id.button6);


        orderText.setText(currentCommand.getRequesterId());

        detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ComponentsWaitlistActivity.class);
            intent.putExtra("commandId",currentCommand.getIdCommande());
            context.startActivity(intent);

//            Requester rq = new Requester();
//            Assembler as = new Assembler();
//
//            Map<Component,Integer> map1 = new HashMap<>();
//            Map<Component,Integer> map2 = new HashMap<>();
//
//
//            Component component1 = new Component("Matériel","Display","4K Monitor 27 inch",10,"Ultra HD monitor with HDR support.");
//            Component component2 = new Component("Matériel","Storage","500GB External Hard Drive",30,"Portable hard drive for backup and storage.");
//
//            map1.put(component1, 1);
//            map2.put(component1,2);
//
//            Command command1 = new Command("youssef", map1);
//            Command command2 = new Command("youssef", map2);
//
//            rq.createCommand(command1, new Requester.AddCommand() {
//                @Override
//                public void onComponentAdded() {
//                    as.approveCommand(context, command1.getIdCommande(), new Assembler.ApproveCommandCallback() {
//                        @Override
//                        public void onCommandApproved() {
//                            System.out.println("command 1 approved");
//                        }
//
//                        @Override
//                        public void onException(Exception e) {
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onException(Exception e) {
//
//                }
//            });
//
//            rq.createCommand(command2, new Requester.AddCommand() {
//                @Override
//                public void onComponentAdded() {
//                    as.approveCommand(context, command2.getIdCommande(), new Assembler.ApproveCommandCallback() {
//                        @Override
//                        public void onCommandApproved() {
//                            System.out.println("command 2 approved");
//                        }
//
//                        @Override
//                        public void onException(Exception e) {
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onException(Exception e) {
//
//                }
//            });


        });

        return convertView;

    }


}
