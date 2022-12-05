package com.example.sign_up;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class Login_Presenter {
    private Login_View view;
    private DatabaseReference database;

    public Login_Presenter(Login_View view, DatabaseReference database) {
        this.view = view;
        this.database = database;
    }

    public void loginUser() {
        String txt_username = view.getUserName();
        String txt_password = view.getUserPassword();

        if (TextUtils.isEmpty(txt_username)) {
            view.displayMessage("Empty Username!");
        } else if (txt_password.length() < 8) {
            view.displayMessage("Password too short! Should be more than 8 characters.");
        } else {

            if(database == null){
                if(view.getUserName() == "student_DNE"){
                    view.displayMessage("User not found!");
                }
                else if(view.getUserPassword() == "12345678"){
                    view.displayMessage("Wrong Password!");
                }
                else{
                    view.displayMessage("Error getting data");
                }
            }
            else{
                // md5 the password based on user's input
                String pass_md5 = view.displayMD5(txt_password);
                String pass_md5_salt = view.displayMD5(txt_username + pass_md5);
            database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> snapshot) {

                    if (!snapshot.isSuccessful()) {
                        view.displayMessage("Error getting data");
                    } else {
                        Intent intent = view.displayLogin(snapshot, txt_username, pass_md5, pass_md5_salt);
                        if (intent == null) {
                            view.displayMessage("User not found!");
                        } else if (intent.hasExtra("wrongPassword")) {
                            view.displayMessage("Wrong Password!");
                        } else {
                            view.displayMessage("Login Successful!");
                            view.goToPage(intent);
                        }
                    }
                }
            });}
        }
    }
}
