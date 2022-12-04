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

public class Login_Activity extends AppCompatActivity implements Login_View {

    private Login_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Button login_btn = findViewById(R.id.Login_Btn);
        Button goto_signup_btn = findViewById(R.id.signup);
        EditText username = findViewById(R.id.Username_Login);
        EditText password = findViewById(R.id.Password_Login);

        presenter = new Login_Presenter(this);

        // Goto Sign Up Page
        goto_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Signup_Activity.class));
            }
        });

        // If the user click the login_btn
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_password = password.getText().toString();

                presenter.loginUser(database, txt_username, txt_password);
            }
        });
    }

    @Override
    public void displayValidName() {
        Toast.makeText(Login_Activity.this, "Empty Username!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayValidPassword() {
        Toast.makeText(Login_Activity.this,
                "Password too short! Should be more than 8 characters.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayLogin(DatabaseReference database, String txt_username,
                             String pass_md5, String pass_md5_salt) {

        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {

                // Get the data snapshot
                DataSnapshot students = snapshot.getResult().child("DATABASE").child("STUDENTS");
                DataSnapshot admins = snapshot.getResult().child("DATABASE").child("ADMINS");

                if (!snapshot.isSuccessful()) {
                    Log.e("firebase", "Error getting data", snapshot.getException());
                    Toast.makeText(Login_Activity.this, "Error getting data",
                            Toast.LENGTH_SHORT).show();
                } else if (admins.hasChild(txt_username)) { // if the user is an administrator

                    // Get the correct password from the database
                    String pass_web = admins.child(txt_username).child("pass_hash").getValue().toString();
                    String pass_salt_web = admins.child(txt_username).child("salt_hash").getValue().toString();

                    // check if the password is correct.
                    // If it is, jump to the admin_page. If not, toast a message.
                    if (pass_web.equals(pass_md5) && pass_salt_web.equals(pass_md5_salt)) {
                        Log.i("Login", "Login Successful");
                        Toast.makeText(Login_Activity.this, "Login Successful!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login_Activity.this, AdminHome_Activity.class));
                        finish();
                    } else {
                        Log.e("Login", "Password Wrong!");
                        Toast.makeText(Login_Activity.this, "Wrong Password!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (students.hasChild(txt_username)) {   // if the user is a student

                    // Get the correct password from the database
                    String pass_web = students.child(txt_username).child("pass_hash").getValue().toString();
                    String pass_salt_web = students.child(txt_username).child("salt_hash").getValue().toString();

                    // check if the password is correct.
                    // If it is, jump to the admin_page. If not, toast a message.
                    if (pass_web.equals(pass_md5) && pass_salt_web.equals(pass_md5_salt)) {
                        Log.i("Login", "Login Successful");
                        Toast.makeText(Login_Activity.this, "Login Successful!",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_Activity.this, CoursesTaken_Activity.class);
                        intent.putExtra("studentID", txt_username);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("Login", "Password Wrong!");
                        Toast.makeText(Login_Activity.this, "Wrong Password!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else { // If the user's name is not found in the database, toast a message.
                    Log.e("firebase", "User not found");
                    Toast.makeText(Login_Activity.this, "User not found!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public String displayMD5(String input) {
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
}