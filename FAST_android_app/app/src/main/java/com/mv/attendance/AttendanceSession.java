package com.mv.attendance;


import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;

public class AttendanceSession {

    ArrayList<Student> present;
    String title;

    public AttendanceSession(ArrayList<Student> present, String title) {
        this.present = present;
        this.title = title;
    }

    public AttendanceSession(String title){
        this.title = title;
    }

    public AttendanceSession() {
    }


    public ArrayList<Student> getPresentStudents(){
        return this.present;
    }

    public String getTitle() {
        return title;
    }

    public void addStudent(Student student){
        if(present!=null && !present.isEmpty()) {
            if (checkIfStudentPresent(student)<0) {
                int r = checkIfStudentPresent(student);
                present.add(student);
                Log.d("QWERTY", "Added student " + r);
            }
            else{
                int r = checkIfStudentPresent(student);
                Log.d("QWERTY", "NOT Added student " + r);
            }
        }
        else{
            present = new ArrayList<Student>();
            present.add(student);
            Log.d("QWERTY", "Added student here - " + student.name);
        }
    }

    private int checkIfStudentPresent(Student student){
        for(int i=0;i<present.size();i++){
            if(present.get(i).equals(student)){
                return i;
            }
        }
        return -1;
    }

    public int numberOfStudents(){
        return present.size();
    }


    public void sortStudents(){
        if (present!=null && present.size()!= 0) {
            present.sort(new Sortbyroll());
        }
    }

    public String generateString(){
        if(present==null){return "  - - None - -  \n\n\n\n\n";}
        StringBuilder generatedString = new StringBuilder(title + "\n\n");
        Log.d("QWERY", "SizeOf present = " + present.size());
        for(int i=0;i<present.size();i++){
            generatedString.append(String.valueOf(present.get(i).RollNo)).append(" ").append(present.get(i).div).append(" ").append(present.get(i).name).append("\n");
        }
        return generatedString.toString();
    }

    public String generateStringNonRepeatative(){
        if(present==null){return "  - - None - -  \n\n\n\n\n";}
        ArrayList<String> done = new ArrayList<>();
        done.add("Hello");
        StringBuilder generatedString = new StringBuilder(title + "\n\n");
        Log.d("QWERY", "SizeOf present = " + present.size());
        String current = "";
        for(int i=0;i<present.size();i++){
            current = String.valueOf(present.get(i).RollNo) + " " + present.get(i).div + " " + present.get(i).name;
            if(!done.contains(current)) {
                generatedString.append(current).append("\n");
            }
            done.add(current);
        }
        return generatedString.toString();
    }




    static class Sortbyroll implements Comparator<Student>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Student a, Student b)
        {
            return a.RollNo - b.RollNo;
        }
    }




}
