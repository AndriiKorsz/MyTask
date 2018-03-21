package com.example.andrii.mytask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class TaskActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;



    FirebaseListAdapter listAdapter;

    EditText text;
    FloatingActionButton newTask;

    ImageButton signOutBtn;

    FirebaseUser user = auth.getInstance().getCurrentUser();

    ListView listView;

    public TaskActivity() throws IOException {
    }

    //not back?!
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) findViewById(R.id.listView);

        listAdapter = new FirebaseListAdapter<String>(this,
                String.class, android.R.layout.simple_list_item_1,
                databaseReference.child(user.getUid()).child("Tasks")) {
            @Override
            protected void populateView(View v, String s, int position) {

                TextView textView = v.findViewById(android.R.id.text1);
                textView.setText(s);
            }
        };
        listView.setAdapter(listAdapter);




        newTask = findViewById(R.id.floatBtn);
        text = findViewById(R.id.addET);


        newTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {

                if (text.getText().toString().isEmpty()) {
                    Toast.makeText(TaskActivity.this, "Please enter the text", Toast.LENGTH_SHORT).show();
                }else {
                //  String id = "Zyg4ADu0XMPipfm3LPsq0mSoE2Y2";
                    databaseReference.child(user.getUid())
                            .child("Tasks").push()
                            .setValue(text.getText().toString());
                    text.setText("");

                }

            }
        });

        signOutBtn = findViewById(R.id.signOutImBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getInstance().signOut();
                Intent intent = new Intent(TaskActivity.this, SinginActivity.class);
                startActivity(intent);
            }
        });

    }


}
