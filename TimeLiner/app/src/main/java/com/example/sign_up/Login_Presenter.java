package com.example.sign_up;


import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;

public class Login_Presenter {
    private Login_View view;
    private DatabaseReference database;
    private Login_Module module;

    public Login_Presenter(Login_View view, DatabaseReference database,Login_Module module) {
        this.view = view;
        this.database = database;
        this.module = module;
    }

    public void loginUser() {
        String txt_username = view.getUserName();
        String txt_password = view.getUserPassword();

        if (TextUtils.isEmpty(txt_username)) {
            view.displayMessage("Empty Username!");
        } else if (txt_password.length() < 8) {
            view.displayMessage("Password too short! Should be more than 8 characters.");
        } else {
            // All Toast messages have been moved to the module
            // Since Firebase is asynchronous, we need to toast it in the module

            module.check_status(txt_password,txt_username,view);
            if (module.get_status() == 3){ // fail database
                view.displayMessage("Error getting data");
            }
            else if (module.get_status() == 1){ // wrong password
                view.displayMessage("Wrong Password!");
            }
            else if (module.get_status() == 2){ // user not exist
                view.displayMessage("User not found!");
            }
            else{ // pass
                view.displayMessage("Login Successful!");
            }

        }
    }
}
