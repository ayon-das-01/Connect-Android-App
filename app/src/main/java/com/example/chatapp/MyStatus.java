package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyStatus extends AppCompatActivity {

    private ImageView addContactsId;
    private ImageView chatsId;
    private ImageView myStatusId;
    private ImageView imageViewMenu, phone_icon;
    private ImageView address_icon, bloodGroupIcon, bloodGroupIcon2;
    private ImageView profession_icon, profession_icon2;

    private CircleImageView imageView3;

    private TextView seeProfilePicId;
    private TextView chooseProfilePicId;
    private TextView nameTextViewId;
    private TextView emailTextViewId;
    private TextView phoneTextViewId;
    private TextView addDetails;
    private TextView professionId;
    private TextView addressId, bloodGroupId;
    private TextView saveDetails;
    private TextView connectButton;

    private EditText inputProfession;
    private EditText inputBloodGroup;

    private BottomNavigationView bottomNavigationId;

    private DatabaseReference databaseReference;

    private Uri imageUri;

    private static final int IMAGE_REQUEST = 1;

    private String name;
    private String email;
    private String number;
    private String address, bloodGroup;
    private String profession;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);

        // Initialize Views
        addContactsId = findViewById(R.id.addContactsId);
        chatsId = findViewById(R.id.chatsId);
        myStatusId = findViewById(R.id.myStatusId);
        imageViewMenu = findViewById(R.id.imageViewMenu);
        bottomNavigationId = findViewById(R.id.bottomNavigationId);

        seeProfilePicId = findViewById(R.id.seeProfilePicId);
        chooseProfilePicId = findViewById(R.id.chooseProfilePicId);

        imageView3 = findViewById(R.id.imageView3);

        nameTextViewId = findViewById(R.id.nameTextViewId);
        emailTextViewId = findViewById(R.id.emailTextViewId);
        phoneTextViewId = findViewById(R.id.phoneTextViewId);

        addDetails = findViewById(R.id.addDetails);
        professionId = findViewById(R.id.professionId);
        addressId = findViewById(R.id.addressId);
        bloodGroupId = findViewById(R.id.bloodGroupId);

        profession_icon = findViewById(R.id.profession_icon);
        address_icon = findViewById(R.id.address_icon);
        profession_icon2 = findViewById(R.id.profession_icon2);
        bloodGroupIcon = findViewById(R.id.bloodGroupIcon);
        bloodGroupIcon2 = findViewById(R.id.bloodGroupIcon2);
        phone_icon = findViewById(R.id.phone_icon);

        inputProfession = findViewById(R.id.inputProfession);
        inputBloodGroup = findViewById(R.id.inputBloodGroup);

        saveDetails = findViewById(R.id.saveDetails);
        connectButton = findViewById(R.id.connectButton);

        // Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference("User")
                    .child(firebaseUser.getUid());
        }



        // Add Details Button
        addDetails.setOnClickListener(v -> {

            profession_icon2.setVisibility(View.VISIBLE);
            profession_icon.setVisibility(View.GONE);
            bloodGroupIcon2.setVisibility(View.VISIBLE);
            bloodGroupIcon.setVisibility(View.GONE);

            inputProfession.setVisibility(View.VISIBLE);
            inputBloodGroup.setVisibility(View.VISIBLE);

            professionId.setVisibility(View.GONE);
            bloodGroupId.setVisibility(View.GONE);

            saveDetails.setVisibility(View.VISIBLE);
            addDetails.setVisibility(View.GONE);

            inputProfession.setText("");
            inputBloodGroup.setText("");
        });

        // Save Details
        saveDetails.setOnClickListener(v -> {

            bloodGroup = inputBloodGroup.getText().toString().trim();
            profession = inputProfession.getText().toString().trim();

            if (bloodGroup.isEmpty()) {
                inputBloodGroup.setError("Please enter your blood group");
                inputBloodGroup.requestFocus();
                return;
            }

            if (profession.isEmpty()) {
                inputProfession.setError("Please enter your profession");
                inputProfession.requestFocus();
                return;
            }

            if (databaseReference != null) {
                databaseReference.child("bloodGroup").setValue(bloodGroup);
                databaseReference.child("profession").setValue(profession);
            }

            bloodGroupId.setText(bloodGroup);
            professionId.setText(profession);

            professionId.setVisibility(View.VISIBLE);
            bloodGroupId.setVisibility(View.VISIBLE);

            inputProfession.setVisibility(View.GONE);
            inputBloodGroup.setVisibility(View.GONE);
            bloodGroupIcon2.setVisibility(View.GONE);
            profession_icon2.setVisibility(View.GONE);

            saveDetails.setVisibility(View.GONE);
            addDetails.setVisibility(View.VISIBLE);
            addDetails.setText("Edit Details");
        });

        // Navigation Buttons
        addContactsId.setOnClickListener(v ->
                startActivity(new Intent(MyStatus.this, DetailsActivity.class)));

        chatsId.setOnClickListener(v ->
                startActivity(new Intent(MyStatus.this, listview.class)));

        imageViewMenu.setOnClickListener(v ->
                startActivity(new Intent(MyStatus.this, MyMenu.class)));



        chooseProfilePicId.setOnClickListener(v -> chooseProfilePic());

        imageView3.setOnClickListener(v ->
                bottomNavigationId.setVisibility(View.VISIBLE));
    }

    @Override
    protected void onStart() {
        super.onStart();

        professionId.setVisibility(View.VISIBLE);
        addressId.setVisibility(View.VISIBLE);
        profession_icon.setVisibility(View.VISIBLE);
        address_icon.setVisibility(View.VISIBLE);
        addDetails.setVisibility(View.VISIBLE);
        addDetails.setText("Edit Details");
        connectButton.setVisibility(View.GONE);

        String uid = getIntent().getStringExtra("uid");

        // Viewing another user's profile
        if (uid != null) {

            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("User")
                    .child(uid);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        Toast.makeText(MyStatus.this,
                                "User not found",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    name = snapshot.child("name").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);
                    profession = snapshot.child("profession").getValue(String.class);
                    bloodGroup = snapshot.child("bloodGroup").getValue(String.class);


                    nameTextViewId.setText(name);
                    emailTextViewId.setText(email);
                    addressId.setText(address);
                    phoneTextViewId.setVisibility(View.GONE);
                    phone_icon.setVisibility(View.GONE);

                    addDetails.setVisibility(View.GONE);
                    connectButton.setVisibility(View.VISIBLE);

                    if (bloodGroup != null && !bloodGroup.isEmpty()) {
                        bloodGroupId.setText(bloodGroup);
                        bloodGroupId.setVisibility(View.VISIBLE);
                        bloodGroupIcon.setVisibility(View.VISIBLE);
                    } else {
                        bloodGroupId.setText("");
                        bloodGroupIcon.setVisibility(View.GONE);
                    }

                    if (profession != null && !profession.isEmpty()) {
                        professionId.setText(profession);
                        professionId.setVisibility(View.VISIBLE);
                        profession_icon.setVisibility(View.VISIBLE);
                    } else {
                        professionId.setText("");
                        profession_icon.setVisibility(View.GONE);
                    }

                    String imageUrl = snapshot.child("profileImage")
                            .getValue(String.class);

                    if (imageUrl == null || imageUrl.isEmpty()) {

                        imageView3.setImageResource(R.drawable.name);

                    } else {

                        Picasso.with(MyStatus.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.name)
                                .error(R.drawable.name)
                                .into(imageView3);
                    }

                    seeProfilePicId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), SelectProfilePic.class);
                            intent.putExtra("profilePicUrl", imageUrl);
                            startActivity(intent);
                        }
                    });

                    connectButton.setOnClickListener(v -> {

                        Intent intent = new Intent(MyStatus.this,
                                ChatsActivity.class);

                        intent.putExtra("receiverId", uid);

                        startActivity(intent);

                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(MyStatus.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });

        } else {


            // Viewing own profile
            FirebaseUser firebaseUser =
                    FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser == null) {
                return;
            }

            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("User")
                    .child(firebaseUser.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        return;
                    }

                    name = snapshot.child("name").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    number = snapshot.child("number").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);
                    profession = snapshot.child("profession").getValue(String.class);
                    bloodGroup = snapshot.child("bloodGroup").getValue(String.class);

                    nameTextViewId.setText(name);
                    emailTextViewId.setText(email);
                    phoneTextViewId.setText(number);
                    addressId.setText(address);

                    if (bloodGroup != null && !bloodGroup.isEmpty()) {

                        bloodGroupId.setText(bloodGroup);
                        bloodGroupId.setVisibility(View.VISIBLE);
                        bloodGroupIcon.setVisibility(View.VISIBLE);

                    } else {

                        bloodGroupId.setText("");
                        bloodGroupIcon.setVisibility(View.GONE);
                        addDetails.setText("Add Details");
                    }

                    if (profession != null && !profession.isEmpty()) {

                        professionId.setText(profession);
                        professionId.setVisibility(View.VISIBLE);
                        profession_icon.setVisibility(View.VISIBLE);

                    } else {

                        professionId.setText("");
                        profession_icon.setVisibility(View.GONE);
                        addDetails.setText("Add Details");
                    }

                    String imageUrl = snapshot.child("profileImage")
                            .getValue(String.class);

                    if (imageUrl == null || imageUrl.isEmpty()) {

                        imageView3.setImageResource(R.drawable.name);

                    } else {

                        Picasso.with(MyStatus.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.name)
                                .error(R.drawable.name)
                                .into(imageView3);
                    }

                    seeProfilePicId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), SelectProfilePic.class);
                            intent.putExtra("profilePicUrl", imageUrl);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(MyStatus.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });


        }

        // Add Details Button
        addDetails.setOnClickListener(v -> {

            profession_icon2.setVisibility(View.VISIBLE);
            profession_icon.setVisibility(View.GONE);
            bloodGroupIcon2.setVisibility(View.VISIBLE);
            bloodGroupIcon.setVisibility(View.GONE);

            inputProfession.setVisibility(View.VISIBLE);
            inputBloodGroup.setVisibility(View.VISIBLE);

            professionId.setVisibility(View.GONE);
            bloodGroupId.setVisibility(View.GONE);

            saveDetails.setVisibility(View.VISIBLE);
            addDetails.setVisibility(View.GONE);

            inputProfession.setText("");
            inputBloodGroup.setText("");
        });
    }

    private void chooseProfilePic() {
        openFileChooser();
    }

    private void openFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(
                Intent.createChooser(intent, "Select Profile Picture"),
                IMAGE_REQUEST
        );
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != IMAGE_REQUEST) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        if (data == null || data.getData() == null) {
            Toast.makeText(this,
                    "No image selected",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        imageUri = data.getData();

        Picasso.with(this)
                .load(imageUri)
                .placeholder(R.drawable.name)
                .error(R.drawable.name)
                .into(imageView3);

        saveData();
    }

    private void saveData() {

        if (imageUri == null) {

            Toast.makeText(this,
                    "Please select an image first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseReference == null) {

            Toast.makeText(this,
                    "User not found",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MediaManager.get()
                .upload(imageUri)
                .unsigned("connect_preset")
                .callback(new UploadCallback() {

                    @Override
                    public void onStart(String requestId) {

                        Toast.makeText(MyStatus.this,
                                "Uploading...",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId,
                                           long bytes,
                                           long totalBytes) {
                    }

                    @Override
                    public void onSuccess(String requestId,
                                          Map resultData) {

                        Object secureUrl = resultData.get("secure_url");

                        if (secureUrl == null) {

                            Toast.makeText(MyStatus.this,
                                    "Upload failed",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String imageUrl = secureUrl.toString();

                        databaseReference.child("profileImage")
                                .setValue(imageUrl)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(MyStatus.this,
                                                "Profile picture updated",
                                                Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(MyStatus.this,
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onError(String requestId,
                                        ErrorInfo error) {

                        Toast.makeText(MyStatus.this,
                                error.getDescription(),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onReschedule(String requestId,
                                             ErrorInfo error) {
                    }

                }).dispatch();
    }
}
