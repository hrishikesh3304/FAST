package com.mv.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewUser extends AppCompatActivity {

    CardView studentCardView, teacherCardView;
    EditText teacherPRN;
    Button teacherSubmitButton;

    String PRN_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        studentCardView = findViewById(R.id.cardViewStudent);
        teacherCardView = findViewById(R.id.cardViewTeacher);
        teacherPRN = findViewById(R.id.teacherInputPhoneNo);
        teacherSubmitButton = findViewById(R.id.teacherInputSubmitButton);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();

        studentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEdit.putString("TypeOfPerson", "Student");
                myEdit.apply();
                Intent intent = new Intent(NewUser.this, HomeActivity1.class);
                startActivity(intent);
                //intent = new Intent(NewUser.this, SettingsActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        teacherCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentCardView.setVisibility(View.GONE);
                teacherCardView.setVisibility(View.GONE);
                studentCardView.animate().alpha(0).setDuration(500).start();
                teacherCardView.animate().alpha(0).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        teacherPRN.setVisibility(View.VISIBLE);
                        teacherPRN.setAlpha(0);
                        teacherPRN.animate().alpha(1).setDuration(500);
                        teacherSubmitButton.setVisibility(View.VISIBLE);
                        teacherSubmitButton.setAlpha(0);
                        teacherSubmitButton.animate().alpha(1).setDuration(500);
                    }
                }).start();

            }
        });

        teacherSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Upload number on Firebase
                PRN_teacher = teacherPRN.getText().toString();
                myEdit.putString("TypeOfPerson", "Teacher");
                myEdit.putString("PRN", PRN_teacher );
                myEdit.apply();
                Intent intent = new Intent(NewUser.this, HomeActivity1.class);
                startActivity(intent);
                //intent = new Intent(NewUser.this, Settings_teacher.class);
                //startActivity(intent);
                finish();
            }
        });

    }
}