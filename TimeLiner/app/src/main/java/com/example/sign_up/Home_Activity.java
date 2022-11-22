package com.example.sign_up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// import Log
import android.util.Log;

public class Home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Button start_btn = (Button)findViewById(R.id.start_button);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Home_Activity", "----onClick: Goto Login_Activity----");
                startActivity(new Intent(Home_Activity.this, Signup_Activity.class));
            }
        });


        // Goto Test Activity
        Button goto_test_btn = (Button)findViewById(R.id.start_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Home_Activity", "----onClick: Goto Test Activity----");
                startActivity(new Intent(Home_Activity.this, Test_Activity.class));
            }
        });
    }
}
