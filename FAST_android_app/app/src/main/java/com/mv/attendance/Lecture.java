package com.mv.attendance;

import android.util.Log;

import java.util.Arrays;

public class Lecture {

    String Title, Teacher_name;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTeacher_name() {
        return Teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        Teacher_name = teacher_name;
    }

    public Lecture(String teacher_name, String Title) {
        this.Teacher_name = teacher_name;
        this.Title = Title;
    }









}
