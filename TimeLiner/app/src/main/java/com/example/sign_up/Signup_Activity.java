package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
// import MD5
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signup_Activity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] roles = { "Student", "Administrator" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        // Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = findViewById(R.id.role);
        spin.setOnItemSelectedListener(this);

        // Creating the ArrayAdapter instance having the roles list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, roles);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        Button btn = findViewById(R.id.Log_In);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup_Activity.this, Login_Activity.class));
            }
        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        EditText username = findViewById(R.id.Username);
        EditText password = findViewById(R.id.Password);
        Button signup = findViewById(R.id.Sign_up);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(Signup_Activity.this, "Empty Username!",
                            Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 8) {
                    Toast.makeText(Signup_Activity.this,
                            "Password too short! Should be more than 8 characters.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    registerStudent(database, txt_username, txt_password);
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

    private void registerStudent(DatabaseReference database,
            String txt_username, String txt_password) {
        // Boolean process_success = false;
        HashMap<String, Object> student = new HashMap<>();

        // MD5 the password
        String pass_md5 = md5(txt_password);

        // MD5 the password with the username
        String pass_md5_username = md5(txt_username + pass_md5);

        // Get a datasnapshot of the database
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "----Error getting data----", task.getException());
                    Toast.makeText(Signup_Activity.this, "Error getting data!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Log STUDENT_NUM
                    Log.d("firebase", String.valueOf(task.getResult().child("STUDENT_NUM").getValue()));

                    // Check if the username is already taken
                    if (task.getResult().child("DATABASE").child("STUDENTS").hasChild(txt_username)) {
                        Toast.makeText(Signup_Activity.this, "Username already taken!",
                                Toast.LENGTH_SHORT).show();
                    }

                    // Get the value of STUDENT_NUM
                    int id = Integer.parseInt(String.valueOf(task.getResult().child("STUDENT_NUM").getValue()));
                    student.put("id", id);
                    student.put("name", txt_username);
                    student.put("pass_hash", pass_md5);
                    student.put("salt_hash", pass_md5_username);

                    database.child("DATABASE").child("STUDENTS").child(txt_username).updateChildren(student);
                    Log.d("TAG", "registerStudent: " + student);
                    Toast.makeText(Signup_Activity.this, "Registering user successful!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // // Get the value of STUDENT_NUM
        // id = database.child("STUDENT_NUM").getValue(Integer.class);

        // student.put("id", id);
        // student.put("name", txt_username);
        // student.put("pass_hash", pass_md5);
        // student.put("salt_hash", pass_md5_username);

        // database.child("DATABASE").child("STUDENTS").child("1").updateChildren(student);
        // Log.d("TAG", "registerStudent: " + student);
        // Toast.makeText(Signup_Activity.this, "Registering user successful!",
        // Toast.LENGTH_SHORT).show();
        // return true;
    }

    // Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), roles[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}