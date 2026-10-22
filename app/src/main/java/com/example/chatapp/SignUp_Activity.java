package com.example.chatapp;

import static android.util.Patterns.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText nameId,emailId,passwordId,PhoneNumber, addressId;
    private Button signUp;
    private TextView textViewSignIn;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    MyStatus myStatus;
    public  static String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailId);
        passwordId = findViewById(R.id.passwordId);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        nameId = findViewById(R.id.nameId);
        signUp = findViewById(R.id.signUp);
        textViewSignIn = findViewById(R.id.textViewSignIn);
        progressBar = findViewById(R.id.progressBar);
        addressId = findViewById(R.id.addressId);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signupRegister();
            }
        });
    }

    private void signupRegister() {
        String email = emailId.getText().toString().trim();
        String password = passwordId.getText().toString().trim();
        String name = nameId.getText().toString().trim();
        String number = PhoneNumber.getText().toString().trim();
        String address = addressId.getText().toString().trim();

        if(email.isEmpty()){
            emailId.setError("Please enter an email address");
            emailId.requestFocus();
            return;
        }
        if(!EMAIL_ADDRESS.matcher(email).matches()){
            emailId.setError("Please enter a valid email address");
            emailId.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordId.setError("Please enter a password");
            passwordId.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordId.setError("Minimum length of a password should be 6");
            passwordId.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            nameId.setError("Please enter your name");
            nameId.requestFocus();
            return;
        }
        if(PHONE.matcher(name).matches()){
            nameId.setError("Character sequence only");
            nameId.requestFocus();
            return;
        }
        if(number.isEmpty()){
            PhoneNumber.setError("Please enter your number");
            PhoneNumber.requestFocus();
            return;
        }
        if(number.length()>16){
            PhoneNumber.setError("Please enter a valid digit of number");
            PhoneNumber.requestFocus();
            return;
        }
        if(address.isEmpty()){
            addressId.setError("Please enter your address");
            addressId.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(getApplicationContext(), EmailVerification.class);
                    startActivity(intent);
                    saveData();
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){

                        Toast.makeText(SignUp_Activity.this, "User is already registered", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        Toast.makeText(SignUp_Activity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void saveData() {

        String email = emailId.getText().toString().trim();
        password = passwordId.getText().toString().trim();
        String name = nameId.getText().toString().trim();
        String number = PhoneNumber.getText().toString().trim();
        String address = addressId.getText().toString().trim();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            String uid = firebaseUser.getUid();
            User user = new User(name, email, number, password, address);
            databaseReference.child(uid).setValue(user);
            Toast.makeText(this, "User information is saved", Toast.LENGTH_SHORT).show();
        }
    }
}