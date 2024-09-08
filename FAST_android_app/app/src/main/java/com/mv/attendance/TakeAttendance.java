package com.mv.attendance;

import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import se.simbio.encryption.Encryption;

public class TakeAttendance extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private TextView scannerTV;

    List<AttendanceSession> ListAttendanceSession;
    Integer countInListAttendance;
    AttendanceSession attendanceSession;

    SharedPreferences sh;
    SharedPreferences.Editor myEdit;

    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);


        Gson gson = new Gson();
        Intent intent = getIntent();
        String jsonFromPreviousActivityAttendanceSession = intent.getStringExtra("Attendance Session");
        countInListAttendance = intent.getIntExtra("CountInListAttendance", -1);
        attendanceSession = gson.fromJson(jsonFromPreviousActivityAttendanceSession, AttendanceSession.class);


        sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        myEdit = sh.edit();

        List<String> ListElementsArrayList = new ArrayList<>();
        ListAttendanceSession = new ArrayList<>();
        if(!sh.getString("ListAttendanceSession", "").equals("")){
            //Gson gson = new Gson();
            //String jsonFromPreviousActivityAttendanceSession = sh.getString("ListAttendanceSession", "");
            //ListElementsArrayList = gson.fromJson(jsonFromPreviousActivityAttendanceSession, String.class);
            Type listType = new TypeToken<List<AttendanceSession>>() {}.getType();

            //ListAttendanceSession = getList(sh.getString("ListAttendanceSession", ""), AttendanceSession.class);
            ListAttendanceSession = gson.fromJson(sh.getString("ListAttendanceSession", ""), listType);

        }

        attendanceSession = ListAttendanceSession.get(countInListAttendance);


        LocalDateTime current_time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatted_date = current_time.format(formatter);


        scannerTV = findViewById(R.id.idTVScannedData);

        scannerTV.setText(attendanceSession.getTitle());

        CodeScannerView scannerView = findViewById(R.id.camView);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ScanQRCodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        String key = formatted_date;
                        String salt = "Mith";
                        Log.d("QWERT", "Decoding....");
                        byte[] iv = new byte[16];
                        Base64.Decoder decoder = Base64.getDecoder();
                        boolean runThis;
                        Encryption encryption = Encryption.getDefault(key, salt, iv);
                        String decrypted = "";
                        try {
                            decrypted = encryption.decryptOrNull(result.getText());
                            runThis=true;
                            Log.d("QWERT", "Decoded....");
                        } catch(Exception e) {
                            // That string wasn't valid.
                            e.printStackTrace();
                            runThis=false;
                            Log.d("QWERT", "Decoded -> False....");
                        }
                        if(runThis){
                            scannerTV.setText(decrypted);
                            Log.d("QWERT", result.getText() + " " + decrypted);
                            if(Student.checkIfFormatCorrect(decrypted)){
                                //Toast.makeText(TakeAttendance.this, "erer", Toast.LENGTH_SHORT).show();
                                Log.d("QWERTY", "Ikde");
                                Student scannedStudent = new Student(decrypted);
                                attendanceSession.addStudent(scannedStudent);
                                Log.d("QWERTY", attendanceSession.generateStringNonRepeatative());
                                ListAttendanceSession.set(countInListAttendance, attendanceSession);
                                Log.d("QWERTY", "Actaul kaay aahe -> " + ListAttendanceSession.get(countInListAttendance).generateString());
                                Gson gson = new Gson();
                                String jsonToListElementsAdapterList= gson.toJson(ListAttendanceSession);
                                Type listType = new TypeToken<List<AttendanceSession>>() {}.getType();
                                jsonToListElementsAdapterList= gson.toJson(ListAttendanceSession, listType);
                                myEdit.putString("ListAttendanceSession", jsonToListElementsAdapterList);
                                myEdit.apply();
                                //Log.d("QWERTY", "CHECK BY READING - " + );
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        // do something...
                                        mCodeScanner.startPreview();
                                        //scannerTV.setText("Scanned Data will appear here");
                                        scannerTV.setText(attendanceSession.getTitle());
                                    }
                                }, 500);
                            }
                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private boolean checkPermission() {
        int camer_permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
        return camer_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, VIBRATE}, PERMISSION_CODE);

    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        ListAttendanceSession.set(countInListAttendance, attendanceSession);
        Gson gson = new Gson();
        String jsonToListElementsAdapterList= gson.toJson(ListAttendanceSession);
        myEdit.putString("ListAttendanceSession", jsonToListElementsAdapterList);
        myEdit.apply();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mCodeScanner.releaseResources();
        ListAttendanceSession.set(countInListAttendance, attendanceSession);
        Gson gson = new Gson();
        String jsonToListElementsAdapterList= gson.toJson(ListAttendanceSession);
        myEdit.putString("ListAttendanceSession", jsonToListElementsAdapterList);
        myEdit.apply();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(TakeAttendance.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(TakeAttendance.this, new String[] { permission }, requestCode);
        }
        else {
            //Toast.makeText(GiveAttendance2.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(TakeAttendance.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(TakeAttendance.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public <T> List<T> getList(String jsonArray, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return new Gson().fromJson(jsonArray, typeOfT);
    }
}