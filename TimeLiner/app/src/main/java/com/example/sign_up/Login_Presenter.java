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
            module.check_status(txt_password,txt_username,view);
            if (module.get_status() == 3){
                view.displayMessage("Error getting data");
            }
            else if (module.get_status() == 1){
                view.displayMessage("Wrong Password!");
            }
            else if (module.get_status() == 2){
                view.displayMessage("User not found!");
            }
            else{
                view.displayMessage("Login Successful!");
            }

        }
    }
}
