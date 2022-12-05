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

        // Receive the data from the previous page
        Intent intent = getIntent();
        String txt_username = intent.getStringExtra("username");
        String txt_password = intent.getStringExtra("password");

        // Login Part
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Button login_btn = findViewById(R.id.Login_Btn);
        EditText username = findViewById(R.id.Username_Login);
        EditText password = findViewById(R.id.Password_Login);

        // Fill the username and password
        username.setText(txt_username);
        password.setText(txt_password);

        // Goto Sign Up Page
        Button goto_signup_btn = findViewById(R.id.signup);

        presenter = new Login_Presenter(this, database);

        // Goto Sign Up Page
        goto_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // If the user click the login_btn
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginUser();
            }
        });
    }

    @Override
    public void goToPage(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public String getUserName() {
        EditText username = findViewById(R.id.Username_Login);
        return username.getText().toString();
    }

    @Override
    public String getUserPassword() {
        EditText password = findViewById(R.id.Password_Login);
        return password.getText().toString();
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(Login_Activity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Intent displayLogin(Task<DataSnapshot> snapshot, String username,
            String pass_md5, String pass_md5_salt) {

        // Get the data snapshot
        DataSnapshot students = snapshot.getResult().child("DATABASE").child("STUDENTS");
        DataSnapshot admins = snapshot.getResult().child("DATABASE").child("ADMINS");

        if (admins.hasChild(username)) { // if the user is an administrator

            // Get the correct password from the database
            String pass_web = admins.child(username).child("pass_hash").getValue().toString();
            String pass_salt_web = admins.child(username).child("salt_hash").getValue().toString();

            Intent intent = new Intent(Login_Activity.this, AdminHome_Activity.class);

            // check if the password is correct.
            if (pass_web.equals(pass_md5) && pass_salt_web.equals(pass_md5_salt)) {
                return intent;
            } else {
                intent.putExtra("wrongPassword", true);
            }
            return intent;
        } else if (students.hasChild(username)) { // if the user is a student

            // Get the correct password from the database
            String pass_web = students.child(username).child("pass_hash").getValue().toString();
            String pass_salt_web = students.child(username).child("salt_hash").getValue().toString();

            Intent intent = new Intent(Login_Activity.this, CoursesTaken_Activity.class);
            intent.putExtra("studentID", username);

            // check if the password is correct.
            if (pass_web.equals(pass_md5) && pass_salt_web.equals(pass_md5_salt)) {
                return intent;
            } else {
                intent.putExtra("wrongPassword", true);
            }
            return intent;
        }
        // If the user's name is not found in the database, toast a message.
        return null;
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