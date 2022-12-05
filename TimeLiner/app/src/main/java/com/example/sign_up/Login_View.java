package com.example.sign_up;

import android.content.Intent;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public interface Login_View {

    void goToPage(Intent intent);

    String getUserName(); // Get the user's input name

    String getUserPassword(); // Get the user's input password

    void displayMessage(String message); // Handle with error to warn the user

    Intent displayLogin(Task<DataSnapshot> snapshot, String txt_username,
                      String pass_md5, String pass_md5_salt); // Login the user

    String displayMD5(String input); // encrypting the password
}