package com.mv.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiveAttendance2 extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private TextView scannerTV;
    private SeekBar zoomSeekBar;

    FirebaseDatabase database;
    DatabaseReference reference;

    //List<AttendanceSession> ListAttendanceSession;
    //Integer countInListAttendance;
    //AttendanceSession attendanceSession;

    //SharedPreferences sh;
    //SharedPreferences.Editor myEdit;


    private static final int CAMERA_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_attendance2);

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

        scannerTV = findViewById(R.id.idTVScannedData2_Mode2);

        scannerTV.setText("Scan the code shown by your teacher");

        zoomSeekBar = findViewById(R.id.zoomSeekBar);
        zoomSeekBar.setProgress(0);

        CodeScannerView scannerView = findViewById(R.id.camView_Mode2);
        mCodeScanner = new CodeScanner(this, scannerView);
        //mCodeScanner.setZoom(1);
        Log.d("QWERT", "Zoom - " + mCodeScanner.getZoom());
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCodeScanner.stopPreview();
                            long unixTimeStamp = System.currentTimeMillis() / 1000L;
                            String textFromQRCode = result.getText();
                            Log.d("QWERT", "FromQR  -> " + textFromQRCode);

                            textFromQRCode = Mode2QRCodeProperties.decodeScannedString(textFromQRCode, 5);
                            Log.d("QWERT", "Decoded after FromQR  -> " + textFromQRCode);

                            if(Mode2QRCodeProperties.check_textFromQRCode_isCorrect(textFromQRCode, unixTimeStamp) == true){
                                //QR Code is in correct format. Now we need to up-load it

                                Mode2QRCodeProperties QRProperties = new Mode2QRCodeProperties(textFromQRCode, unixTimeStamp);


                                if(QRProperties.checkIfTimeInBuffer(20)){

                                    Log.d("QWERT", "Time checked");

                                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                String nameOfStudent = sh.getString("Name", "");
                                int RollNoOfStudent = Integer.parseInt(sh.getString("Roll No", ""));
                                String divOfStudent = sh.getString("Div", "");
                                String PRN = sh.getString("PRN","");

                                Student std = new Student(RollNoOfStudent,divOfStudent,nameOfStudent,textFromQRCode);

                                //Lecture Lecture1 = new Lecture(Mode2QRCodeProperties.Teacher1,Mode2QRCodeProperties.Title1);

                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("Division");

                                //realtimedatabase

                                Map<String, Object> addToFirebase = new HashMap<String,Object>();
                                addToFirebase.put(nameOfStudent, QRProperties.QR_CODE_Encoded_time);
                                reference.child(std.getDiv()).child(Mode2QRCodeProperties.subject).child(Mode2QRCodeProperties.Teacher1).child(Mode2QRCodeProperties.Title1).child("Student").child(PRN).setValue(std);
                                //Toast.makeText(GiveAttendance2.this, "Attendance marked  successfully!", Toast.LENGTH_SHORT).show();
                                //Snackbar.make(zoomSeekBar, "Attendance marked successfully!", Snackbar.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(GiveAttendance2.this);

                                LinearLayout layout = new LinearLayout(GiveAttendance2.this);
                                final TextView successfulAlertDialogTextView = new TextView(GiveAttendance2.this);
                                successfulAlertDialogTextView.setText("Attendance marked\nsuccessfully!");
                                successfulAlertDialogTextView.setTextSize(30);
                                successfulAlertDialogTextView.setTextColor(getResources().getColor(R.color.green));
                                Typeface face= getResources().getFont(R.font.freckleface_regular_downloaded);
                                successfulAlertDialogTextView.setTypeface(face);
                                successfulAlertDialogTextView.setLineSpacing(0,0.8f);
                                successfulAlertDialogTextView.setGravity(Gravity.CENTER);

                                final ImageView checkBox = new ImageView(GiveAttendance2.this);
                                checkBox.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
                                checkBox.setElevation(10);
                                //checkBox.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

                                //VideoView checkVideo = new VideoView(GiveAttendance2.this);
                                //String path = "android.resource://" + getPackageName() + "/" + R.raw.check_mark_video;
                                //checkVideo.setVideoURI(Uri.parse(path));
                                //checkVideo.start();

                                layout.setPadding(20,20,20,0);
                                layout.setOrientation(LinearLayout.VERTICAL);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                params.weight = 1.0f;
                                params.gravity = Gravity.CENTER_HORIZONTAL;
                                successfulAlertDialogTextView.setLayoutParams(params);
                                layout.addView(successfulAlertDialogTextView);
                                layout.addView(checkBox);

                                builder.setMessage("")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("");
                                alert.setView(layout);
                                Drawable drawable = ContextCompat.getDrawable(GiveAttendance2.this,  R.drawable.background_home_gradient);
                                alert.getWindow().setBackgroundDrawable(drawable);
                                alert.show();
                                Button OKbutton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                OKbutton.setScaleX(2);
                                OKbutton.setScaleY(2);
                                LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) OKbutton.getLayoutParams();
                                positiveButtonLL.gravity = Gravity.CENTER;
                                positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                OKbutton.setLayoutParams(positiveButtonLL);
                                Animation expandIn = AnimationUtils.loadAnimation(GiveAttendance2.this, R.anim.pop_from_nothing);
                                //successfulAlertDialogTextView.startAnimation(expandIn);
                                checkBox.startAnimation(expandIn);
                                //((Animatable) checkBox.getDrawable()).start();
                                //finish();

                            }
                            else{
                                mCodeScanner.startPreview();
                            }


                        }
                        else{
                            mCodeScanner.startPreview();
                        }



                        }
                    });
                }

            });

        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCodeScanner.setZoom(1+i/2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(GiveAttendance2.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GiveAttendance2.this, new String[] { permission }, requestCode);
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
                Toast.makeText(GiveAttendance2.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(GiveAttendance2.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onStop() {
        mCodeScanner.releaseResources();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }
}

    //After scanning successfully ://
