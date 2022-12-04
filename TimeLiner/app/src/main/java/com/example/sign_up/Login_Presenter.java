package com.example.sign_up;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class Login_Presenter {
    private Login_View view;

    public Login_Presenter(Login_View view) {
        this.view = view;
    }

    public void loginUser(DatabaseReference database, String txt_username, String txt_password) {
        if (TextUtils.isEmpty(txt_username)) {
            view.displayValidName();
        } else if (txt_password.length() < 8) {
            view.displayValidPassword();
        } else {
            // md5 the password based on user's input
            String pass_md5 = view.displayMD5(txt_password);
            String pass_md5_salt = view.displayMD5(txt_username + pass_md5);

            view.displayLogin(database, txt_username, pass_md5, pass_md5_salt);
        }
    }
}
