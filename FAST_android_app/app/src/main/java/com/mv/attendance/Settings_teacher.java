package com.mv.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;

public class Settings_teacher extends AppCompatActivity {

    EditText editTextName;
    //EditText editTextRollNo;
    //EditText editTextDivision;
    ImageButton clearDataButton;

    EditText editTextPRN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_teacher);

        editTextName = findViewById(R.id.editTextName);

        clearDataButton = findViewById(R.id.ClearDataButton);

        editTextPRN = findViewById(R.id.editTextPRN);

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        editTextName.setText(sh.getString("Name", ""));
        editTextPRN.setText(sh.getString("PRN", ""));

        SharedPreferences.Editor myEdit = sh.edit();

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                myEdit.putString("Name", editTextName.getText().toString());
                try {
                    long t = getTime();
                    myEdit.putLong("savedTime", t);
                    Log.d("QWERT", "Time Saved as " + t);
                } catch (Exception e) {
                    long t = System.currentTimeMillis() / 1000L;
                    myEdit.putLong("savedTime", t);
                }
                myEdit.apply();
            }
        });



        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });

        editTextPRN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                myEdit.putString("PRN", editTextPRN.getText().toString());
                try {
                    long t = getTime();
                    myEdit.putLong("savedTime", t);
                    Log.d("QWERT", "Time Saved as " + t);
                } catch (Exception e) {
                    long t = System.currentTimeMillis() / 1000L;
                    myEdit.putLong("savedTime", t);
                }
                myEdit.apply();
            }
        });
    }


    public void clearData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings_teacher.this);

        builder.setMessage("Do you really want to logout?");

        builder.setTitle("Alert!");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {


            SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sh.edit();
            editor.clear().apply();

            Intent intent = new Intent(Settings_teacher.this, NewUser.class);
            startActivity(intent);

        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private long getTime() throws Exception {
        String url = "https://time.is/Unix_time_now";
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
        String[] tags = new String[] {
                "div[id=time_section]",
                "div[id=clock0_bg]"
        };
        Elements elements= doc.select(tags[0]);
        for (int i = 0; i <tags.length; i++) {
            elements = elements.select(tags[i]);
        }
        return Long.parseLong(elements.text());
    }

}