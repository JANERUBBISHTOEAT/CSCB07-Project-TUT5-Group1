package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

// import MD5
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// import R
import com.example.sign_up.R;

// import Log
import android.util.Log;

public class Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Goto Sign Up Page
        Button goto_signup_btn = (Button) findViewById(R.id.signup);
        goto_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Signup_Activity.class));
            }
        });

        // Login Part
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Button login_btn = (Button) findViewById(R.id.Login_Btn);
        EditText username = (EditText) findViewById(R.id.Username_Login);
        EditText password = (EditText) findViewById(R.id.Password_Login);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(Login_Activity.this, "Empty Username!",
                            Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 8) {
                    Toast.makeText(Login_Activity.this,
                            "Password too short! Should be more than 8 characters.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    loginStudent(database, txt_username, txt_password);
                }
            }
        });
    }

    private String md5(String input) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void loginStudent(DatabaseReference database, String txt_username, String txt_password) {

        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {
                // database.child("DATABASE").child("STUDENTS").child(txt_username)
                // .addListenerForSingleValueEvent(new ValueEventListener() {
                // @Override
                // public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot data_snap = snapshot.getResult().child("DATABASE").child("STUDENTS");

                if (!snapshot.isSuccessful()) {
                    Log.e("firebase", "Error getting data", snapshot.getException());
                    Toast.makeText(Login_Activity.this, "Error getting data",
                            Toast.LENGTH_SHORT).show();
                } else if (!data_snap.hasChild(txt_username)) {
                    Log.e("firebase", "User not found");
                    Toast.makeText(Login_Activity.this, "User not found!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // HashMap<String, Object> student = new HashMap<>();
                    String pass_md5 = md5(txt_password);
                    String pass_md5_salt = md5(txt_username + pass_md5);
                    String pass_web = data_snap.child(txt_username).child("pass_hash").getValue().toString();
                    String pass_salt_web = data_snap.child(txt_username).child("salt_hash").getValue().toString();
                    if (pass_web.equals(pass_md5) && pass_salt_web.equals(pass_md5_salt)) {
                        Log.i("Login", "Login Successful");
                        Toast.makeText(Login_Activity.this, "Login Successful!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login_Activity.this, CoursesTaken_Activity.class));
                        finish();
                    } else {
                        Log.e("Login", "Password Wrong!");
                        Toast.makeText(Login_Activity.this, "Wrong Password!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}