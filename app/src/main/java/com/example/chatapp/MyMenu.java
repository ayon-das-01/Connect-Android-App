package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MyMenu extends AppCompatActivity {
    private ImageView addContactsId;
    private ImageView chatsId;
    private ImageView myStatusId;

    private TextView settingId;
    private TextView sinOutId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);

        addContactsId = findViewById(R.id.addContactsId);
        chatsId = findViewById(R.id.chatsId);
        myStatusId = findViewById(R.id.myStatusId);
        settingId = findViewById(R.id.settingId);
        sinOutId = findViewById(R.id.sinOutId);

        addContactsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyMenu.this, DetailsActivity.class);
                startActivity(intent);
            }
        });

        chatsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyMenu.this, listview.class);
                startActivity(intent);
            }
        });

        myStatusId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyMenu.this, MyStatus.class);
                startActivity(intent);
            }
        });

        settingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sinOutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyMenu.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}