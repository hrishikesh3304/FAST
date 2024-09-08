package com.mv.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TakeAttendanceDisplay extends AppCompatActivity {

    TextView titleTextView;
    TextView mainDisplayAttendanceTextView;
    ImageButton continueSessionImageButton, button_sort_alphabetically, button_refresh_list, button_add_custom_student;

    List<AttendanceSession> ListAttendanceSession2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance_display);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String jsonFromPreviousActivityAttendanceSession = intent.getStringExtra("Attendance Session");
        int countInListAttendance = intent.getIntExtra("CountInListAttendance", -1);
        AttendanceSession attendanceSession = gson.fromJson(jsonFromPreviousActivityAttendanceSession, AttendanceSession.class);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        List<String> ListElementsArrayList = new ArrayList<>();
        final List<AttendanceSession>[] ListAttendanceSession = new List[]{new ArrayList<>()};
        if(!sh.getString("ListAttendanceSession", "").equals("")){
            //Gson gson = new Gson();
            //String jsonFromPreviousActivityAttendanceSession = sh.getString("ListAttendanceSession", "");
            //ListElementsArrayList = gson.fromJson(jsonFromPreviousActivityAttendanceSession, String.class);
            Type listType = new TypeToken<List<AttendanceSession>>() {}.getType();

            //ListAttendanceSession = getList(sh.getString("ListAttendanceSession", ""), AttendanceSession.class);
            ListAttendanceSession[0] = gson.fromJson(sh.getString("ListAttendanceSession", ""), listType);

        }


        titleTextView=findViewById(R.id.textViewTitle);
        mainDisplayAttendanceTextView=findViewById(R.id.textViewPast);
        continueSessionImageButton=findViewById(R.id.buttonContinueAttendance);
        button_sort_alphabetically=findViewById(R.id.buttonSort);
        button_refresh_list=findViewById(R.id.buttonRefresh);
        button_add_custom_student=findViewById(R.id.buttonAddStudent);


        titleTextView.setText(ListAttendanceSession[0].get(countInListAttendance).getTitle());
        mainDisplayAttendanceTextView.setText(ListAttendanceSession[0].get(countInListAttendance).generateStringNonRepeatative());

        continueSessionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonToShareAttendanceSession = gson.toJson(attendanceSession);

                Intent intent = new Intent(TakeAttendanceDisplay.this, TakeAttendance.class);
                intent.putExtra("Attendance Session", jsonToShareAttendanceSession);
                intent.putExtra("CountInListAttendance", countInListAttendance);
                startActivity(intent);
            }
        });

        final List<AttendanceSession>[] finalListAttendanceSession = new List[]{ListAttendanceSession[0]};
        button_sort_alphabetically.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalListAttendanceSession[0].get(countInListAttendance).sortStudents();
                mainDisplayAttendanceTextView.setText(finalListAttendanceSession[0].get(countInListAttendance).generateStringNonRepeatative());
            }
        });

        button_refresh_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListAttendanceSession2 = new ArrayList<>();
                if(!sh.getString("ListAttendanceSession", "").equals("")){
                    //Gson gson = new Gson();
                    //String jsonFromPreviousActivityAttendanceSession = sh.getString("ListAttendanceSession", "");
                    //ListElementsArrayList = gson.fromJson(jsonFromPreviousActivityAttendanceSession, String.class);
                    Type listType = new TypeToken<List<AttendanceSession>>() {}.getType();
                    ListAttendanceSession2 = gson.fromJson(sh.getString("ListAttendanceSession", ""), listType);
                    mainDisplayAttendanceTextView.setText(ListAttendanceSession2.get(countInListAttendance).generateStringNonRepeatative());
                    finalListAttendanceSession[0] = ListAttendanceSession2;
                }

            }
        });

        button_add_custom_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TakeAttendanceDisplay.this);
                LinearLayout layout = new LinearLayout(TakeAttendanceDisplay.this);
                layout.setBackgroundColor(Color.parseColor("#EEEEEE"));
                Drawable drawable = ContextCompat.getDrawable(TakeAttendanceDisplay.this,  R.drawable.white_alert_dialogue_background);
                layout.setBackground(drawable);
                layout.setPadding(20,20,20,0);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

                final EditText inputEditTextNameOfStudent = new EditText(getApplicationContext());
                final EditText inputEditTextDivOfStudent = new EditText(getApplicationContext());
                final EditText inputEditTextRollNoOfStudent = new EditText(getApplicationContext());
                inputEditTextNameOfStudent.setHint("Name");
                inputEditTextDivOfStudent.setHint("Div");
                inputEditTextRollNoOfStudent.setHint("Roll No.");
                inputEditTextRollNoOfStudent.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(inputEditTextNameOfStudent);
                layout.addView(inputEditTextDivOfStudent);
                layout.addView(inputEditTextRollNoOfStudent);
                builder.setMessage("Add a new student")
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Student student = new Student(Integer.parseInt(inputEditTextRollNoOfStudent.getText().toString()), inputEditTextDivOfStudent.getText().toString(), inputEditTextNameOfStudent.getText().toString());
                                finalListAttendanceSession[0].get(countInListAttendance).addStudent(student);
                                Gson gson = new Gson();
                                String jsonToShareAttendanceSession = gson.toJson(attendanceSession);


                                String jsonToListElementsAdapterList= gson.toJson(finalListAttendanceSession[0]);
                                myEdit.putString("ListAttendanceSession", jsonToListElementsAdapterList);
                                myEdit.apply();
                                dialog.dismiss();
                                mainDisplayAttendanceTextView.setText(finalListAttendanceSession[0].get(countInListAttendance).generateStringNonRepeatative());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("New Student");
                alert.setView(layout);
                drawable = ContextCompat.getDrawable(TakeAttendanceDisplay.this,  R.drawable.grey_alert_dialogue_background);
                alert.getWindow().setBackgroundDrawable(drawable);
                alert.show();
                WindowManager.LayoutParams lp = alert.getWindow().getAttributes();
                lp.dimAmount = 0.75f;
                alert.getWindow().setAttributes(lp);
                //alert.setView(inputEditTextDivOfStudent);
                //alert.setView(inputEditTextRollNoOfStudent);
            }
        });
    }

    public <T> List<T> getList(String jsonArray, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return new Gson().fromJson(jsonArray, typeOfT);
    }

}