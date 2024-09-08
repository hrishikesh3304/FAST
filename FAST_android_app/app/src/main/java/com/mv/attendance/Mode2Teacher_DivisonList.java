package com.mv.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mode2Teacher_DivisonList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2_teacher_divison_list);

        String nameOfDivision = getIntent().getExtras().getString("DivisionSelected");

        ListView listViewOfStudents=findViewById(R.id.listViewPast);
        List<String> listOfClasses = new ArrayList<>();

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Division");

        List<String> lectureList_Actual = new ArrayList<>();
        List<String> lectureList_Actual_CorrespondingSubjects = new ArrayList<>();
        List<String> lectureList_ForListView = new ArrayList<>();
        ArrayList<ListElement_class> subject_list = new ArrayList<>();

        reference.child(nameOfDivision).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    Iterator<DataSnapshot> subjectListIterator = dataSnapshot.getChildren().iterator();
                    while(subjectListIterator.hasNext()){
                        String subject =  subjectListIterator.next().getKey();
                        Log.d("QWERT", "SubjectList -> " + subject);
                        Iterator<DataSnapshot> teachersInSubjectListIterator = dataSnapshot.child(subject).getChildren().iterator();
                        while (teachersInSubjectListIterator.hasNext()){
                            String teacherName = teachersInSubjectListIterator.next().getKey();
                            if(teacherName.equals(sh.getString("PRN", ""))){
                                Iterator<DataSnapshot> lecturesInSubjectListIterator = dataSnapshot.child(subject).child(teacherName).getChildren().iterator();
                                while(lecturesInSubjectListIterator.hasNext()){
                                    String lectureName = lecturesInSubjectListIterator.next().getKey();
                                    Log.d("QWERT", "LetureName -> " + lectureName);
                                    lectureList_Actual.add(lectureName);
                                    lectureList_ForListView.add(subject + "_" + lectureName);
                                    lectureList_Actual_CorrespondingSubjects.add(subject);

                                    if(subject.equals("Math"))
                                    {
                                        ListElement_class element = new ListElement_class(subject + "_" + lectureName,R.drawable.maths);
                                        subject_list.add(element);
                                    }

                                    else if(subject.equals("Chemistry"))
                                    {
                                        ListElement_class element = new ListElement_class(subject + "_" + lectureName,R.drawable.chemistry);
                                        subject_list.add(element);
                                    }
                                    else if(subject.equals("Mechanics"))
                                    {
                                        ListElement_class element = new ListElement_class(subject + "_" + lectureName,R.drawable.physics);
                                        subject_list.add(element);
                                    }
                                    else if (subject.equals("PDD"))
                                    {
                                        ListElement_class element = new ListElement_class(subject + "_" + lectureName,R.drawable.newproduct);
                                        subject_list.add(element);
                                    }
                                    else if (subject.equals("C programming"))
                                    {
                                        ListElement_class element = new ListElement_class(subject + "_" + lectureName,R.drawable.programming);
                                        subject_list.add(element);
                                    }
                                    else
                                    {
                                        ListElement_class element = new ListElement_class(subject + "_" + lectureName,R.drawable.textbooks);
                                        subject_list.add(element);
                                    }
                                }

                            }
                        }
                    }
                   /* final ArrayAdapter<String> adapter = new ArrayAdapter<>(Mode2Teacher_DivisonList.this, android.R.layout.simple_list_item_1, lectureList_ForListView);
                    listViewOfStudents.setAdapter(adapter);
                   */

                    ListAdapter_cardview listAdapter = new ListAdapter_cardview(Mode2Teacher_DivisonList.this, subject_list );
                    listViewOfStudents.setAdapter(listAdapter);


                    listViewOfStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(Mode2Teacher_DivisonList.this, Mode2Studentslist.class);
                            //intent.putExtra("NameOfClass", divisionListOfTeacher.get(i));
                            intent.putExtra("DivisionSelected", nameOfDivision);
                            intent.putExtra("NameOfClass", lectureList_Actual.get(i));
                            intent.putExtra("Subject", lectureList_Actual_CorrespondingSubjects.get(i));
                            startActivity(intent);
                        }

                    });
                }
            }
        });
    }
}