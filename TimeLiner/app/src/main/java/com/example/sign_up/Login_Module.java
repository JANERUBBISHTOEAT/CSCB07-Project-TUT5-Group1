package com.example.sign_up;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class Login_Module {
    DatabaseReference database;
    int status;
    // 0 represent pass, 1 represent wrong password, 2 represent user not exist, 3 represent fail database

    public Login_Module(  DatabaseReference database,int status) {
        this.database = database;
        this.status = status;
    }


    public void update_status(int goal){
        this.status = goal;
    }

    public void check_status(String txt_password,String txt_username,Login_View view){
        String pass_md5 = view.displayMD5(txt_password);
        String pass_md5_salt = view.displayMD5(txt_username + pass_md5);
        database.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {

                if (!snapshot.isSuccessful()) {
                    update_status(3);
                } else {
                    Intent intent = view.displayLogin(snapshot, txt_username, pass_md5, pass_md5_salt);
                    if (intent == null) {
                        update_status(2);
                    } else if (intent.hasExtra("wrongPassword")) {
                        update_status(1);
                    } else {
                        update_status(0);
                        view.goToPage(intent);
                    }
                }
            }
        });

    }

    public int get_status(){
        return status;
    }

}
