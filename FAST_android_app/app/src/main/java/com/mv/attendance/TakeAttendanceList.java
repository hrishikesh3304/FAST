package com.mv.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeAttendanceList extends AppCompatActivity {

    ListView listViewPast;
    ImageButton new_attendance_session_button, button_sort_alphabetically;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance_list);

        listViewPast=findViewById(R.id.listViewPast);
        new_attendance_session_button=findViewById(R.id.buttonNewAttendance);


        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();


        List<String> ListElementsArrayList = new ArrayList<>();
        List<AttendanceSession> ListAttendanceSession = new ArrayList<>();
        if(!sh.getString("ListAttendanceSession", "").equals("")){
            //Gson gson = new Gson();
            //String jsonFromPreviousActivityAttendanceSession = sh.getString("ListAttendanceSession", "");
            //ListElementsArrayList = gson.fromJson(jsonFromPreviousActivityAttendanceSession, String.class);
            ListAttendanceSession = getList(sh.getString("ListAttendanceSession", ""), AttendanceSession.class);

        }

        if(ListAttendanceSession.size()>0){
            for(int i=0;i<ListAttendanceSession.size();i++){
                ListElementsArrayList.add(ListAttendanceSession.get(i).title);
            }
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<> (TakeAttendanceList.this, android.R.layout.simple_list_item_1, ListElementsArrayList);

        listViewPast.setAdapter(adapter);





        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<AttendanceSession> finalListAttendanceSession = ListAttendanceSession;
        List<AttendanceSession> finalListAttendanceSession2 = ListAttendanceSession;
        new_attendance_session_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText inputEditTextDialogue = new EditText(getApplicationContext());
                inputEditTextDialogue.setHint("Title");
                LinearLayout layout = new LinearLayout(TakeAttendanceList.this);
                layout.setBackground(ContextCompat.getDrawable(TakeAttendanceList.this,  R.drawable.white_alert_dialogue_background));
                layout.setPadding(20,20,20,0);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                layout.addView(inputEditTextDialogue);

                //Setting message manually and performing action on button click
                builder.setMessage("Set a new name for the Attendance Session")
                        .setCancelable(false)
                        .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AttendanceSession attendanceSession = new AttendanceSession();
                                attendanceSession.title = inputEditTextDialogue.getText().toString();

                                Gson gson = new Gson();
                                String jsonToShareAttendanceSession = gson.toJson(attendanceSession);

                                Intent intent = new Intent(TakeAttendanceList.this, TakeAttendance.class);
                                intent.putExtra("Attendance Session", jsonToShareAttendanceSession);
                                intent.putExtra("CountInListAttendance", finalListAttendanceSession2.size());
                                startActivity(intent);

                                ListElementsArrayList.add(attendanceSession.title);
                                adapter.notifyDataSetChanged();
                                finalListAttendanceSession.add(attendanceSession);
                                String jsonToListElementsAdapterList= gson.toJson(finalListAttendanceSession);
                                myEdit.putString("ListAttendanceSession", jsonToListElementsAdapterList);
                                myEdit.apply();
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
                alert.setTitle("Set Title");
                alert.setView(layout);
                Drawable drawable = ContextCompat.getDrawable(TakeAttendanceList.this,  R.drawable.grey_alert_dialogue_background);
                alert.getWindow().setBackgroundDrawable(drawable);
                alert.show();
                WindowManager.LayoutParams lp = alert.getWindow().getAttributes();
                lp.dimAmount = 0.75f;
                alert.getWindow().setAttributes(lp);





            }
        });


        listViewPast.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                builder.setMessage("Do you want to delete this?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the selected item
                        finalListAttendanceSession.remove(i);
                        ListElementsArrayList.remove(i);
                        adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        String jsonToListElementsAdapterList= gson.toJson(finalListAttendanceSession);
                        myEdit.putString("ListAttendanceSession", jsonToListElementsAdapterList);
                        myEdit.apply();
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
        });

        List<AttendanceSession> finalListAttendanceSession1 = ListAttendanceSession;
        listViewPast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AttendanceSession attendanceSession = finalListAttendanceSession1.get(i);
                Gson gson = new Gson();
                String jsonToShareAttendanceSession = gson.toJson(attendanceSession);

                Intent intent = new Intent(TakeAttendanceList.this, TakeAttendanceDisplay.class);
                intent.putExtra("Attendance Session", jsonToShareAttendanceSession);
                intent.putExtra("CountInListAttendance", i);
                startActivity(intent);

            }
        });





    }

    public <T> List<T> getList(String jsonArray, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return new Gson().fromJson(jsonArray, typeOfT);
    }
}