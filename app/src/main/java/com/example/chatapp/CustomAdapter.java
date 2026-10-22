package com.example.chatapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import adapter.RecyclerViewAdapter;

public class CustomAdapter extends ArrayAdapter<Details> {
    private Activity context;
    private List<Details> detailsList;

    public CustomAdapter(Activity context, List<Details> detailsList) {
        super(context, R.layout.sample_layout, detailsList);
        this.context = context;
        this.detailsList = detailsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sample_layout,null,true);

        Details details = detailsList.get(position);

        TextView t1 = view.findViewById(R.id.textViewName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView t3 = view.findViewById(R.id.textViewAddress);
        ImageView profilePic = view.findViewById(R.id.profilePic);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button connectId = view.findViewById(R.id.connectId);

        connectId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details details = detailsList.get(position);
                Intent intent = new Intent(context, ChatsActivity.class);
                intent.putExtra("receiverId", details.getUid());
                context.startActivity(intent);
            }
        });

        t1.setText(details.getName());
        t3.setText(details.getAddress());

        if("default".equals(details.getImgUrl())){
            profilePic.setImageResource(R.drawable.user);
        }
        else{
            Glide.with(context).load(details.getImgUrl()).into(profilePic);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details details = detailsList.get(position);
                Intent intent = new Intent(context, MyStatus.class);
                intent.putExtra("uid", details.getUid());
                context.startActivity(intent);
            }
        });

        return view;
    }

}
