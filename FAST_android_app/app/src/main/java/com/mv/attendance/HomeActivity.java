package com.mv.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HomeActivity extends AppCompatActivity {

    private Button giveAttendance, takeAttendance, settings;
    //j
    ImageView setting_image;

    RelativeLayout backgroundRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        giveAttendance = findViewById(R.id.idBtoGiveAttendanceActivityHome);
        takeAttendance = findViewById(R.id.idBtoTakeAttendance);
        //settings = findViewById(R.id.setting_image);
        setting_image = findViewById(R.id.setting_image);
        backgroundRelativeLayout = findViewById(R.id.activity_home_background);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String type = sh.getString("TypeOfPerson", "Not Set");

        if(type.equals("Teacher")){
            backgroundRelativeLayout.setBackgroundResource(R.drawable.background_home_gradient_teacher);
        }

        giveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GiveAttendance.class);
                startActivity(intent);
            }
        });

        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TakeAttendanceList.class);
                startActivity(intent);
            }
        });

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        /*settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });*/
    }
}