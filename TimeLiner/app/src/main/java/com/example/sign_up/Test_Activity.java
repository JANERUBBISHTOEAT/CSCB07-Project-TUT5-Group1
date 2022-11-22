package com.example.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// import Log
import android.util.Log;

// import Firebase Realtime Database
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
// import getResult
import com.google.android.gms.tasks.Task;

public class Test_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);

        Log.i("Test_Activity", "----onCreate: In Test Page Now----");

        Button btn = (Button) findViewById(R.id.test_database_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Connect to Firebase Realtime Database
                String database_url = "https://cscb07-project-tut5-grou-f0436-default-rtdb.firebaseio.com/";
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                // Write a message to the database
//                myRef.child("Test").setValue("Hello, World!");
//                Log.i("Test_Activity", "----onClick: Write to Database----");

                // Log all the data in the database
                myRef.child("DATABASE").child("STUDENTS").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "----Error getting data----", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
            }
        });
    }
}
