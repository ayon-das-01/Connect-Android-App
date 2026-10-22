package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity{
    private Button verifyEmail;
    private ProgressBar progressBar2;
    private TextView textView2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_verification);

        verifyEmail = findViewById(R.id.verifyEmail);
        progressBar2 = findViewById(R.id.progressBar2);
        textView2 = findViewById(R.id.textView2);

        mAuth = FirebaseAuth.getInstance();
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                textView2.setText("A verification email is sent to your email address");

                FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){
                    user.sendEmailVerification().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            progressBar2.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            textView2.setText(task.getException().getMessage());
                        }
                    });
                }
            }
        });
    }
}
