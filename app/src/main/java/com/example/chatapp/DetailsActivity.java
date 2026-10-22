package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.RecyclerViewAdapter;

public class DetailsActivity extends AppCompatActivity {

    private ListView listViewId;
    DatabaseReference databaseReference;
    private List<Details> detailsList;
    private CustomAdapter customAdapter;
    private ImageView addContactsId;
    private ImageView chatsId;
    private ImageView myStatusId;
    private ImageView imageViewMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        addContactsId = findViewById(R.id.addContactsId);
        chatsId = findViewById(R.id.chatsId);
        myStatusId = findViewById(R.id.myStatusId);
        listViewId = findViewById(R.id.listViewId);
        imageViewMenu = findViewById(R.id.imageViewMenu);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        detailsList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                detailsList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Details details = dataSnapshot1.getValue(Details.class);
                    String uid = dataSnapshot1.getKey();
                    details.setUid(uid);
                    String url = dataSnapshot1.child("profileImage").getValue(String.class);
                    if (url == null || url.isEmpty()) {
                        details.setImgUrl("default");
                    } else {
                        details.setImgUrl(url);
                    }

                    detailsList.add(details);
                }
                customAdapter = new CustomAdapter(DetailsActivity.this, detailsList);
                listViewId.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MyMenu.class);
                startActivity(intent);
            }
        });

        chatsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), listview.class);
                startActivity(intent);
            }
        });

        myStatusId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MyStatus.class);
                startActivity(intent);
            }
        });
    }

}