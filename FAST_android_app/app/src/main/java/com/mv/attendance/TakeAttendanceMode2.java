package com.mv.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TakeAttendanceMode2 extends AppCompatActivity {


    ImageView setting_image;
    Button idBtoGiveAttendance2;
    Button idBtoTakeAttendance2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance_mode2);

        setting_image = findViewById(R.id.setting_image);
        idBtoGiveAttendance2 = findViewById(R.id.idBtoGiveAttendance2);
        idBtoTakeAttendance2 = findViewById(R.id.idBtoTakeAttendance2);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeAttendanceMode2.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        idBtoGiveAttendance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Math.abs(sh.getLong("savedTime", 167666442) - getTime()) > 20) {   ////////Change time here!!
                    Intent intent = new Intent(TakeAttendanceMode2.this, GiveAttendance2.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(TakeAttendanceMode2.this, "Not enough time has passed since you changed the settings!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        idBtoTakeAttendance2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeAttendanceMode2.this, AttendanceListViewMode2.class);
                startActivity(intent);

            }
        });

    }

    private long getTime() {
        return System.currentTimeMillis() / 1000L;
    }
}