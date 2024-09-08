package com.mv.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class HomeActivity1 extends AppCompatActivity {

    ImageView setting_image, mode2Img, mode1Img;
    CardView Mode1_card;
    CardView Mode2_card;
    CardView Stats_card;
    RelativeLayout backgroundRelativeLayout;
    TextView heading_text;

    //private AnimatedVectorDrawable animationOfSettings;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        setting_image = findViewById(R.id.setting_image);
        mode2Img = findViewById(R.id.imgOfMode2);
        mode1Img = findViewById(R.id.imgOfMode1);
        Mode1_card = findViewById(R.id.idMode1_card);
        Mode2_card = findViewById(R.id.idMode2_card);
        Stats_card = findViewById(R.id.idStats_card);
        backgroundRelativeLayout = findViewById(R.id.activity_home1_background);
        heading_text = findViewById(R.id.idTVHeading);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String type = sh.getString("TypeOfPerson", "Not Set");
        String prn = sh.getString("PRN", "");

        if(type.equals("Teacher")){
            backgroundRelativeLayout.setBackgroundResource(R.drawable.background_home_gradient_teacher);
            setting_image.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_light_blue), android.graphics.PorterDuff.Mode.SRC_IN);
            heading_text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_light_blue));
            Mode1_card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_light_blue));
            Mode2_card.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_light_blue));
            Stats_card.setVisibility(View.GONE);
        }


        /*Animation rotation = AnimationUtils.loadAnimation(HomeActivity1.this, R.anim.rotate_settings_logo);
        rotation.setFillAfter(true);
        setting_image.startAnimation(rotation);*/

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Teacher")) {
                    Intent intent = new Intent(HomeActivity1.this, Settings_teacher.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(HomeActivity1.this, SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });

        Mode1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prn.isEmpty()){
                    Log.d("QWERT", prn);
                    Toast.makeText(HomeActivity1.this, "Fill all credentials!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity1.this, HomeActivity1.class);
                    intent = new Intent(HomeActivity1.this, SettingsActivity.class);
                    startActivity(intent);
                }
                else{
                    Log.d("QWERT", prn);
                    if (type.equals("Teacher")) {
                        Intent intent = new Intent(HomeActivity1.this, TakeAttendanceList.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HomeActivity1.this, GiveAttendance.class);
                        startActivity(intent);
                    }
                }}
        });

        Stats_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity1.this, StatsActivity.class);
                startActivity(intent);
            }
        });



        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(HomeActivity1.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(HomeActivity1.this, GiveAttendance2.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                //loginbutton.setText("Login Successful");
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Security! 😁")
                .setDescription("Use your fingerprint to access the next screen!").setNegativeButtonText("Cancel").build();




        Mode2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prn.isEmpty()){
                    Log.d("QWERT", prn);
                    Toast.makeText(HomeActivity1.this, "Fill all credentials", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity1.this, SettingsActivity.class);
                    startActivity(intent);
                }
                else{
                    Log.d("QWERT", prn);
                    if (type.equals("Teacher")) {
                        Intent intent = new Intent(HomeActivity1.this, AttendanceListViewMode2.class);
                        startActivity(intent);
                    } else {
                        biometricPrompt.authenticate(promptInfo);
                    }
            }}
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        AnimatedVectorDrawable d = (AnimatedVectorDrawable) getDrawable(R.drawable.projector_mode2_animation);
        AnimatedVectorDrawable d_rev = (AnimatedVectorDrawable) getDrawable(R.drawable.projector_mode2_animation_reverese);

        mode2Img.setImageDrawable(d);
        //d.start();

        d.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mode2Img.setImageDrawable(d_rev);
                        d_rev.start();
                        //Log.d("QWERT", "unfdnvvfvfvffvvvvvvvvvvvvvvvvvvv");
                    }
                }, 800);

            }
        });

        d_rev.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mode2Img.setImageDrawable(d);
                        d.start();
                    }
                }, 800);

            }
        });

        d.start();




        AnimatedVectorDrawable d2 = (AnimatedVectorDrawable) getDrawable(R.drawable.phone_qr_mode1_animation);
        AnimatedVectorDrawable d2_rev = (AnimatedVectorDrawable) getDrawable(R.drawable.phone_qr_mode1_animation_reverse);

        mode1Img.setImageDrawable(d2);
        //d.start();

        d.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mode1Img.setImageDrawable(d2_rev);
                        d2_rev.start();
                        //Log.d("QWERT", "unfdnvvfvfvffvvvvvvvvvvvvvvvvvvv");
                    }
                }, 1200);

            }
        });

        d_rev.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mode1Img.setImageDrawable(d2);
                        d2.start();
                    }
                }, 1200);

            }
        });

        d.start();
        /*Drawable d = setting_image.getDrawable();
        if (d instanceof AnimatedVectorDrawable) {
            Log.d("QWERT", "onCreate: instancefound" + d.toString());
            animationOfSettings = (AnimatedVectorDrawable) d;
            animationOfSettings.start();
        }*/
    }

}