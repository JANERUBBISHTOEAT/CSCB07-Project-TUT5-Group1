package com.example.sign_up;

import com.google.firebase.database.DatabaseReference;

public interface Login_View {
    void displayValidName(); // Handle with error if the username is empty

    void displayValidPassword(); // Handle with error if password is too short

    void displayLogin(DatabaseReference database, String txt_username,
                      String pass_md5, String pass_md5_salt); // Login the user

    String displayMD5(String input); // encrypting the password
}