package com.ce.gec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Welcome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        ImageButton loginButton = findViewById(R.id.login_button);
        ImageButton deptButton = findViewById(R.id.login_button_dept);
//
//        Button loginButton = findViewById(R.id.login_button);
//        Button deptButton = findViewById(R.id.login_button_dept);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect the user to the login activity.
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);
            }
        });
        deptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect the user to the login activity.
                Intent intent = new Intent(Welcome.this, DeptLogin.class);
                startActivity(intent);
            }
        });
    }
}