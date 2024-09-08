package com.mv.attendance;

import android.util.Log;

import java.util.Arrays;

public class Student {

    int RollNo;
    String roll;
    String div;
    String name;

    long time;






    public String getName() {
        return name;
    }

    public String getDiv() {
        return div;
    }

    public int getRollNo() {
        return RollNo;
    }

    public long getTime(){return time;}



    public Student(int RollNo, String div, String name, String textFromQRCode) {
        this.RollNo = RollNo;
        this.div = div;
        this.name = name;
        String[] strsplit = textFromQRCode.split("\\|");
        this.time = Long.parseLong(strsplit[5]);
    }

    public Student(int RollNo, String div, String name) {
        this.RollNo = RollNo;
        this.div = div;
        this.name = name;
        //String[] strsplit = textFromQRCode.split("\\|");
        //this.time = Long.parseLong(strsplit[5]);
    }

    public Student(String name, String roll, String div, long time){
        this.roll = roll;
        this.name = name;
        this.div = div;
        this.time = time;
    }


    public Student(String result) {
        String[] splitted = result.split("\\|");
        Log.d("QWERTY", Arrays.toString(splitted));
        RollNo = Integer.parseInt(splitted[2]);
        div = splitted[3];
        name = splitted[4];
        Log.d("QWERTY", "name = " + name);
    }

    public static boolean checkIfFormatCorrect(String result) {
        if(result==null||result.isEmpty()){ return false; }
        if(!result.startsWith("{}")){ return false; }
        String[] splitted = result.split("\\|");
        Log.d("QWERTY", Arrays.toString(splitted));
        if(splitted.length != 5){ return false; }
        if(splitted[0].length() != 2){ return false; }
        if(splitted[2].length() > 2){ return false; }
        if(splitted[3].length() > 1){ return false; }
        return true;
    }




}
