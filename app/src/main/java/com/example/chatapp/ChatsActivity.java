package com.example.chatapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.MessageAdapter;
import adapter.RecyclerViewAdapter;

public class ChatsActivity extends AppCompatActivity {
    private EditText textMessage;
    private ImageView userPic;
    private ImageView receiverImage, galleryButton;
    private DatabaseReference chatReference;
    private ImageView sendButton;
    private TextView userName;
    private FirebaseAuth mAuth;
    private String name;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private RecyclerView chatRecyclerViewId;
    private ArrayList<MessageModel> messageList;
    private String senderImgUrl, receiverImgUrl;
    private MessageAdapter adapter;
    private String senderId, receiverId, messageId, chatId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);


        mAuth = FirebaseAuth.getInstance();
        textMessage = findViewById(R.id.textMessage);
        sendButton = findViewById(R.id.sendButton);
        galleryButton = findViewById(R.id.galleryButton);
        userPic = findViewById(R.id.userPic);
        receiverImage = findViewById(R.id.receiverImage);
        userName = findViewById(R.id.userName);
        database = FirebaseDatabase.getInstance();

        chatRecyclerViewId = findViewById(R.id.chatRecyclerViewId);
        chatRecyclerViewId.setLayoutManager(new LinearLayoutManager(this));

        senderId = mAuth.getCurrentUser().getUid();
        receiverId = getIntent().getStringExtra("receiverId");

        messageList = new ArrayList<>();

        adapter = new MessageAdapter(
                ChatsActivity.this,
                messageList,
                "",
                "",
                senderId
        );

        chatRecyclerViewId.setAdapter(adapter);

        if(receiverId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("User");

            databaseReference.child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        name = snapshot.child("name").getValue(String.class);
                        userName.setText(name);

                        receiverImgUrl = snapshot.child("profileImage").getValue(String.class);

                        if(receiverImgUrl == null || "default".equals(receiverImgUrl) || receiverImgUrl.isEmpty()){
                            userPic.setImageResource(R.drawable.name);
                        }
                        else{
                            Picasso.with(ChatsActivity.this).load(receiverImgUrl).into(userPic);
                        }
                    }

                    databaseReference.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            senderImgUrl = snapshot.child("profileImage").getValue(String.class);

                            adapter.setImageUrls(senderImgUrl, receiverImgUrl);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        if(senderId.compareTo(receiverId) < 0){
            chatId = senderId + "_" + receiverId;
        }
        else{
            chatId = receiverId + "_" + senderId;
        }

        chatReference = database.getReference("Chats").child(chatId);

        // Sending messages
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = textMessage.getText().toString().trim();

                if(message.isEmpty())
                    return;

                messageId = chatReference.child("messages").push().getKey();

                MessageModel messageModel = new MessageModel(senderId, receiverId,
                        messageId, message, System.currentTimeMillis());

                chatReference.child("messages").child(messageId)
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        HashMap<String, Object> lastMessage = new HashMap<>();

                        lastMessage.put("text", message);
                        lastMessage.put("time", System.currentTimeMillis());
                        lastMessage.put("senderId", senderId);

                        chatReference.child("lastMessage").setValue(lastMessage);
                        textMessage.setText("");
                    }
                });
            }
        });

        // Receiving messages
        chatReference.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                messageList.add(messageModel);
                adapter.notifyItemInserted(messageList.size()-1);
                chatRecyclerViewId.scrollToPosition(messageList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");

                intent.putExtra(Intent.EXTRA_MIME_TYPES,
                        new String[]{
                                "image/*",
                                "video/*"
                        });
                galleryLauncher.launch(intent);
            }
        });
    }

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){

                    Uri uri = result.getData().getData();

                    if(uri == null) return;

                    String mimeType = getContentResolver().getType(uri);

                    if(mimeType == null) return;

                    if(mimeType.startsWith("image")){
                        uploadImage(uri);
                    }
                    else if (mimeType.startsWith("video")) {
                        uploadVideo(uri);
                    }
                }
            });

    private void uploadImage(Uri imageUri){

        MediaManager.get()
                .upload(imageUri)
                .unsigned("connect_preset")
                .option("resource_type", "image")
                .callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {

            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String requestId, Map resultData) {

                String imageUrl = resultData.get("secure_url").toString();

                sendMediaMessage(imageUrl, "image");
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.e("CLOUDINARY", "Image upload failed: " + error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }

    private void uploadVideo(Uri videoUri) {

        MediaManager.get()
                .upload(videoUri)
                .unsigned("connect_preset")
                .option("resource_type", "video")
                .callback(new UploadCallback() {

                    @Override
                    public void onStart(String requestId) {
                        Log.d(
                                "CLOUDINARY",
                                "Video upload started: " + requestId
                        );
                    }

                    @Override
                    public void onProgress(
                            String requestId,
                            long bytes,
                            long totalBytes
                    ) {

                        Log.d(
                                "CLOUDINARY",
                                "Video upload progress: " +
                                        bytes + "/" + totalBytes
                        );
                    }

                    @Override
                    public void onSuccess(
                            String requestId,
                            Map resultData
                    ) {

                        String videoUrl =
                                resultData.get("secure_url").toString();

                        Log.d(
                                "CLOUDINARY",
                                "VIDEO UPLOADED: " + videoUrl
                        );

                        // Save video URL to Firebase
                        sendMediaMessage(
                                videoUrl,
                                "video"
                        );
                    }

                    @Override
                    public void onError(
                            String requestId,
                            ErrorInfo error
                    ) {

                        Log.e(
                                "CLOUDINARY",
                                "Video upload failed: " +
                                        error.getDescription()
                        );
                    }

                    @Override
                    public void onReschedule(
                            String requestId,
                            ErrorInfo error
                    ) {

                        Log.e(
                                "CLOUDINARY",
                                "Video upload rescheduled: " +
                                        error.getDescription()
                        );
                    }
                })
                .dispatch();
    }

    private void sendMediaMessage(String mediaUrl, String mediaType){

        String messageId = chatReference.child("messages").push().getKey();

        if(messageId == null) return;

        MessageModel model = new MessageModel(senderId, receiverId,
                messageId, "", System.currentTimeMillis());

        model.setMediaUrl(mediaUrl);
        model.setMediaType(mediaType);

        chatReference.child("messages")
                .child(messageId)
                .setValue(model)
                .addOnSuccessListener(unused ->  {

                        HashMap<String, Object> lastMessage = new HashMap<>();

                        if(mediaType.equals("image")){
                            lastMessage.put("text", "Photo");
                        }
                        else if (mediaType.equals("video")) {
                            lastMessage.put("text", "Video");
                        }

                        lastMessage.put("time", System.currentTimeMillis());
                        lastMessage.put("senderId", senderId);

                        chatReference.child("lastMessage").setValue(lastMessage);
                });
    }
}