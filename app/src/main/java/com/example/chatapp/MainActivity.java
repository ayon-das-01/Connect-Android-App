package com.example.chatapp;

import static android.util.Patterns.EMAIL_ADDRESS;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText emailId,passwordId;
    private Button signIn;
    private TextView textViewSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    public  static String email;
    public static String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailId = findViewById(R.id.emailId);
        passwordId = findViewById(R.id.passwordId);
        signIn = findViewById(R.id.signIn);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }

        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp_Activity.class);
                startActivity(intent);
            }
        });
    }
    private void userRegister() {
         email = emailId.getText().toString().trim();
         password = passwordId.getText().toString().trim();
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
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();

                    user.reload().addOnCompleteListener(task1 -> {

                        if(user.isEmailVerified()){
                            Intent intent = new Intent(getApplicationContext(), MyStatus.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please verify your email first", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), EmailVerification.class);
                            startActivity(intent);
                        }
                    });

                }
                else{
                    Toast.makeText(MainActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    }