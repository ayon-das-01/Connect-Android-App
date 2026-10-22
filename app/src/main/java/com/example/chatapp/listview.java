package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import adapter.RecyclerViewAdapter;


public class listview extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Details> detailsArrayList;
    private DatabaseReference chatReference;
    private TextView tvNoChats;
    private TextView tvNoChats2;
    private ImageView addContactsId;
    private ImageView imageViewMenu;
    private ImageView myStatusId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);


        recyclerView = findViewById(R.id.recyclerView);


        tvNoChats = findViewById(R.id.tvNoChats);
        tvNoChats2 = findViewById(R.id.tvNoChats2);


        addContactsId = findViewById(R.id.addContactsId);
        imageViewMenu = findViewById(R.id.imageViewMenu);
        myStatusId = findViewById(R.id.myStatusId);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        detailsArrayList = new ArrayList<>();


        recyclerViewAdapter = new RecyclerViewAdapter(this, detailsArrayList);

        recyclerView.setAdapter(recyclerViewAdapter);

        loadChatList();

        addContactsId.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            listview.this,
                            DetailsActivity.class
                    )
            );

        });



        imageViewMenu.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            listview.this,
                            MyMenu.class
                    )
            );

        });



        myStatusId.setOnClickListener(v -> {

            startActivity(new Intent(listview.this, MyStatus.class));

        });

    }


    private void loadChatList(){


        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        chatReference = FirebaseDatabase.getInstance().getReference("Chats");


        chatReference.addValueEventListener(
                new ValueEventListener() {


                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {



                        ArrayList<Details> tempList =
                                new ArrayList<>();



                        if(!snapshot.exists()){

                            showEmpty();

                            return;
                        }


                        for(DataSnapshot chatSnapshot :
                                snapshot.getChildren()) {



                            String chatId =
                                    chatSnapshot.getKey();



                            if(chatId == null ||
                                    !chatId.contains("_")) {

                                continue;
                            }



                            String[] ids =
                                    chatId.split("_");



                            if(ids.length != 2){

                                continue;
                            }


                            String otherUid;


                            if(ids[0].equals(currentUid)){

                                otherUid = ids[1];

                            }
                            else if(ids[1].equals(currentUid)){

                                otherUid = ids[0];

                            }
                            else{

                                continue;

                            }



                            String lastMessage =
                                    chatSnapshot
                                            .child("lastMessage")
                                            .child("text")
                                            .getValue(String.class);



                            Long time =
                                    chatSnapshot
                                            .child("lastMessage")
                                            .child("time")
                                            .getValue(Long.class);



                            // Ignore chats without messages
                            if(lastMessage == null ||
                                    time == null){

                                continue;

                            }


                            FirebaseDatabase.getInstance().getReference("User").child(otherUid)
                                    .addListenerForSingleValueEvent(
                                            new ValueEventListener() {


                                                @Override
                                                public void onDataChange(
                                                        @NonNull DataSnapshot userSnapshot) {


                                                    Details details =
                                                            userSnapshot
                                                                    .getValue(
                                                                            Details.class
                                                                    );



                                                    if(details == null){

                                                        return;

                                                    }



                                                    details.setUid(otherUid);


                                                    details.setLastMessage(
                                                            lastMessage
                                                    );


                                                    details.setTime(
                                                            time
                                                    );



                                                    String image =
                                                            userSnapshot
                                                                    .child("profileImage")
                                                                    .getValue(
                                                                            String.class
                                                                    );



                                                    if(image == null ||
                                                            image.isEmpty()){

                                                        details.setImgUrl(
                                                                "default"
                                                        );

                                                    }
                                                    else{

                                                        details.setImgUrl(
                                                                image
                                                        );

                                                    }

                                                    tempList.add(details);

                                                    updateRecyclerView(tempList);

                                                }

                                                @Override
                                                public void onCancelled(
                                                        @NonNull DatabaseError error) {

                                                }

                                            });


                        }

                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error) {

                    }

                });

    }


    private void updateRecyclerView(ArrayList<Details> list){



        Collections.sort(list, new Comparator<Details>() {

                    @Override
                    public int compare(Details d1, Details d2) {

                        return Long.compare(d2.getTime(), d1.getTime());

                    }

                }
        );

        detailsArrayList.clear();

        detailsArrayList.addAll(list);

        recyclerViewAdapter.notifyDataSetChanged();


        if(detailsArrayList.isEmpty()){

            showEmpty();

        }
        else{

            recyclerView.setVisibility(
                    View.VISIBLE
            );

            tvNoChats.setVisibility(
                    View.GONE
            );

            tvNoChats2.setVisibility(
                    View.GONE
            );

        }

    }


    private void showEmpty(){


        recyclerView.setVisibility(
                View.GONE
        );


        tvNoChats.setVisibility(
                View.VISIBLE
        );


        tvNoChats2.setVisibility(
                View.VISIBLE
        );

    }


}