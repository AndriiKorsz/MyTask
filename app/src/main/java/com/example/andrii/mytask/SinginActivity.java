package com.example.andrii.mytask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SinginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "Tag";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText emailET, passwordET;
    private Button registerBtn, singinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        mAuth = FirebaseAuth.getInstance();


        emailET = findViewById(R.id.emailEdit);
        passwordET = findViewById(R.id.emailPassword);

        registerBtn = findViewById(R.id.registerBtn);
        singinBtn = findViewById(R.id.singinBtn);

        registerBtn.setOnClickListener(this);
        singinBtn.setOnClickListener(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    Intent intent = new Intent(SinginActivity.this, TaskActivity.class);
                    startActivity(intent);
                }else {

                }
            }
        };


        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            Intent intent = new Intent(SinginActivity.this, TaskActivity.class);
            startActivity(intent);
        }

    }


    public void registration(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SinginActivity.this, "Registration successed", Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SinginActivity.this, "Registration failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void singin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SinginActivity.this, TaskActivity.class);
                            startActivity(intent);
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SinginActivity.this, "Sing In successed", Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SinginActivity.this, "Sing in failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerBtn:
                registration(emailET.getText().toString(), passwordET.getText().toString());
                break;
            case R.id.singinBtn:
                singin(emailET.getText().toString(), passwordET.getText().toString());
                break;
        }

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to leave this app?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).create().show();
    }

}

