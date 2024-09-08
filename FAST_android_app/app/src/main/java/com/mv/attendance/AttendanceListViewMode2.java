package com.mv.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mv.attendance.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttendanceListViewMode2 extends AppCompatActivity {

    ListView listViewPast;
    ImageButton new_attendance_session_button, button_sort_alphabetically;

    FirebaseDatabase database;

    DatabaseReference reference;

    //ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        setContentView(R.layout.activity_attendance_list_view_mode2);

        listViewPast=findViewById(R.id.listViewPast);
        new_attendance_session_button=findViewById(R.id.buttonNewAttendanceMode2ThroughQRCode);

        //ListElementsArrayList.add("Hello");
        /*if(!sh.getString("ListAttendanceSession", "").equals("")){
            //Gson gson = new Gson();
            //String jsonFromPreviousActivityAttendanceSession = sh.getString("ListAttendanceSession", "");
            //ListElementsArrayList = gson.fromJson(jsonFromPreviousActivityAttendanceSession, String.class);
            ListOfAttendanceSessions = getList(sh.getString("ListAttendanceSession", ""), AttendanceSession.class);

        }*/

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        new_attendance_session_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceListViewMode2.this);
                LinearLayout layout = new LinearLayout(AttendanceListViewMode2.this);
                layout.setBackgroundColor(Color.parseColor("#EEEEEE"));
                Drawable drawable = ContextCompat.getDrawable(AttendanceListViewMode2.this,  R.drawable.white_alert_dialogue_background);
                layout.setBackground(drawable);
                layout.setPadding(20,20,20,0);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

                final EditText inputEditTextTitle = new EditText(getApplicationContext());
                final EditText inputEditTextNameOfTeacher = new EditText(getApplicationContext());
                final EditText divisionOfClass = new EditText(getApplicationContext());
                //final EditText inputEditTextRollNoOfStudent = new EditText(getApplicationContext());
                inputEditTextTitle.setHint("Title");
                inputEditTextNameOfTeacher.setHint("Teacher PRN");
                inputEditTextNameOfTeacher.setText(sh.getString("PRN", ""));
                divisionOfClass.setHint("Division");
                //inputEditTextRollNoOfStudent.setHint("Roll No.");
                //inputEditTextRollNoOfStudent.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(inputEditTextTitle);
                layout.addView(inputEditTextNameOfTeacher);
                layout.addView(divisionOfClass);
                //layout.addView(inputEditTextRollNoOfStudent);
                builder.setMessage("Add a new lecture")
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                /*Student student = new Student(Integer.parseInt(inputEditTextRollNoOfStudent.getText().toString()), inputEditTextDivOfStudent.getText().toString(), inputEditTextNameOfStudent.getText().toString());
                                finalListAttendanceSession[0].get(countInListAttendance).addStudent(student);
                                Gson gson = new Gson();
                                String jsonToShareAttendanceSession = gson.toJson(attendanceSession);


                                String jsonToListElementsAdapterList= gson.toJson(finalListAttendanceSession[0]);
                                myEdit.putString("ListAttendanceSession", jsonToListElementsAdapterList);
                                myEdit.apply();
                                mainDisplayAttendanceTextView.setText(finalListAttendanceSession[0].get(countInListAttendance).generateStringNonRepeatative());*/
                                Intent intent = new Intent(AttendanceListViewMode2.this, Mode2TakeAttendanceShowQRCode.class);
                                intent.putExtra("Title", inputEditTextTitle.getText().toString());
                                intent.putExtra("Teacher_PRN", inputEditTextNameOfTeacher.getText().toString());
                                intent.putExtra("Division", divisionOfClass.getText().toString());
                                startActivity(intent);
                                dialog.dismiss();
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
                alert.setTitle("New Attendance Session");
                alert.setView(layout);
                drawable = ContextCompat.getDrawable(AttendanceListViewMode2.this,  R.drawable.grey_alert_dialogue_background);
                alert.getWindow().setBackgroundDrawable(drawable);
                alert.show();
                WindowManager.LayoutParams lp = alert.getWindow().getAttributes();
                lp.dimAmount = 0.75f;
                alert.getWindow().setAttributes(lp);
            }
        });

        database = FirebaseDatabase.getInstance();
        //reference = database.getReference("Lectures");
        reference = database.getReference("Division");

        /*reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {

                    DataSnapshot dataSnapshot = task.getResult();
                    String name = String.valueOf(dataSnapshot.getValue());
                    //System.out.println(name);
                    Log.d("QWERT", "Data:  " + name);
                    Log.d("QWERT", "Children:  " + dataSnapshot.getChildren());
                    Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                    List<String> teacherName = new ArrayList<>();
                    while (i.hasNext()) {
                        //Log.d("QWERT", "ChildrenValues:  " + i.next());
                        teacherName.add(i.next().getKey());
                    }

                    Log.d("QWERT", "Teachers -     " + teacherName);

                    Log.d("QWERT", "Teacher name - " + sh.getString("Name", "NotFound"));

                    if(teacherName.contains(sh.getString("Name", ""))){
                        Log.d("QWERT", "Teacher detected");
                        reference.child(sh.getString("Name", "")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {

                                DataSnapshot dataSnapshot = task.getResult();
                                String name = String.valueOf(dataSnapshot.getValue());
                                //System.out.println(name);
                                Log.d("QWERT", "Data:  " + name);
                                Log.d("QWERT", "Children:  " + dataSnapshot.getChildren());
                                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                                List<String> childrenList = new ArrayList<>();
                                while (i.hasNext()) {
                                    //Log.d("QWERT", "ChildrenValues:  " + i.next());
                                    childrenList.add(i.next().getKey());
                                }

                                Log.d("QWERT", "Titles   - " + String.valueOf(childrenList));

                                final ArrayAdapter<String> adapter = new ArrayAdapter<>(AttendanceListViewMode2.this, android.R.layout.simple_list_item_1, childrenList);
                                listViewPast.setAdapter(adapter);

                                listViewPast.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                                            @Override
                                                                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                                                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceListViewMode2.this);
                                                                                builder.setMessage("Do you want to delete this?");
                                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        // Delete the selected item
                                                                                        childrenList.remove(i);
                                                                                        adapter.notifyDataSetChanged();
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });
                                                                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });
                                                                                AlertDialog alert = builder.create();
                                                                                alert.setTitle("Delete");
                                                                                alert.show();

                                                                                return true;
                                                                            }
                                                                        }
                                );

                                listViewPast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(AttendanceListViewMode2.this, Mode2Studentslist.class);
                                        intent.putExtra("NameOfClass", childrenList.get(i));
                                        startActivity(intent);
                                    }
                                });


                            }

                        }
                    });
                    }
                }}});*/

        List<String> divisionListOfTeacher = new ArrayList<>();
        ArrayList<ListElement_class> element_ArrayList = new ArrayList<ListElement_class>();

        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    Iterator<DataSnapshot> divisionsIterator = dataSnapshot.getChildren().iterator();
                    while (divisionsIterator.hasNext()) {
                        //Log.d("QWERT", "ChildrenValues:  " + i.next());
                        String division = divisionsIterator.next().getKey();
                        Log.d("QWERT", "DivisionChecking -> " + division);
                        Iterator<DataSnapshot> subjectInDivisionsIterator = dataSnapshot.child(division).getChildren().iterator();
                        while (subjectInDivisionsIterator.hasNext()){
                            boolean inDivision = false;
                            String currentSubjects = subjectInDivisionsIterator.next().getKey().toString();
                            Log.d("QWERT", "Subject -> " + currentSubjects);
                            Iterator<DataSnapshot> teacherInSubjectInDivisionsIterator = dataSnapshot.child(division).child(currentSubjects).getChildren().iterator();
                            while (teacherInSubjectInDivisionsIterator.hasNext()){
                                String teacherName = teacherInSubjectInDivisionsIterator.next().getKey().toString();
                                Log.d("QWERT", "Name -> " + teacherName);
                                if(teacherName.equals(sh.getString("PRN", ""))){
                                    divisionListOfTeacher.add(division);
                                    //Image.add(R.drawable.classroom);
                                    ListElement_class element = new ListElement_class(division, R.drawable.training);
                                    element_ArrayList.add(element);


                                    inDivision = true;
                                    break;
                                }
                            }
                            if(inDivision==true){
                                break;
                            }
                        }
                    }
                    /*
                    Log.d("QWERT", "DivisionsWithTeacherPresent -> " + divisionListOfTeacher);
                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(AttendanceListViewMode2.this, android.R.layout.simple_list_item_1, divisionListOfTeacher);
                    listViewPast.setAdapter(adapter);
                    */




                    ListAdapter_cardview listAdapter = new ListAdapter_cardview(AttendanceListViewMode2.this,element_ArrayList );

                    listViewPast.setAdapter(listAdapter);

                    //binding.getRoot().

                    //binding.listViewPast.setClickable(true);



                  //  binding.listViewPast.setOnItemClickListner()


                listViewPast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(AttendanceListViewMode2.this, Mode2Teacher_DivisonList.class);
                            //intent.putExtra("NameOfClass", divisionListOfTeacher.get(i));
                            intent.putExtra("DivisionSelected", divisionListOfTeacher.get(i));
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}