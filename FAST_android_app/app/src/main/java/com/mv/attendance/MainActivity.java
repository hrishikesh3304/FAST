package com.mv.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String type = sh.getString("TypeOfPerson", "Not Set");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (type.equals("Teacher")) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity1.class);
                    startActivity(intent);
                    finish();
                }
                else if (type.equals("Student")){
                    Intent intent = new Intent(MainActivity.this, HomeActivity1.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, NewUser.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 500);
    }
}