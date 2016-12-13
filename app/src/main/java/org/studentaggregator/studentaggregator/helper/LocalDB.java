package org.studentaggregator.studentaggregator.helper;

import android.content.Context;
import android.content.SharedPreferences;

import org.studentaggregator.studentaggregator.model.Fee;
import org.studentaggregator.studentaggregator.model.Student;

// Created by Ray on 3/28/2016.

public class LocalDB {

    //private String LOG_TAG = "mylog";
    public static final String SP_NAME = "UserDetails";
    SharedPreferences sp;

    public LocalDB(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    public boolean getInitialised() {
        return sp.getBoolean("initialized", false);
    }

    public void setInitialised(boolean b) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putBoolean("initialized", b);
        spEditor.apply();
    }

    public void setStudent(Student student) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("student-id", student.getId());
        spEditor.putString("student-name", student.getName());
        spEditor.putString("student-dob", student.getDob());
        spEditor.putString("student-gender", student.getGender());
        spEditor.putString("student-father", student.getFather());
        spEditor.putString("student-mother", student.getMother());
        spEditor.putString("student-class", student.getClassOfStudent());
        spEditor.putString("student-division", student.getDivision());
        spEditor.putString("student-house", student.getHouse());
        spEditor.putString("student-contact", student.getContact());
        MyUtils.logThis("LocalDB - set student " + student.toString());
        spEditor.apply();
    }

    public Student getStudent() {
        String id = sp.getString("student-id", "");
        String name = sp.getString("student-name", "");
        String dob = sp.getString("student-dob", "");
        String gender = sp.getString("student-gender", "");
        String father = sp.getString("student-father", "");
        String mother = sp.getString("student-mother", "");
        String classOfStudent = sp.getString("student-class", "");
        String division = sp.getString("student-division", "");
        String house = sp.getString("student-house", "");
        String contact = sp.getString("student-contact", "");
        Student student = new Student(id, name, dob, gender, father, mother, classOfStudent, division, house, contact);
        MyUtils.logThis("LocalDB - read student = " + student.toString());
        return student;
    }

    public void setTempStudent(Student student) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("temp-student-id", student.getId());
        spEditor.putString("temp-student-name", student.getName());
        spEditor.putString("temp-student-dob", student.getDob());
        spEditor.putString("temp-student-gender", student.getGender());
        spEditor.putString("temp-student-father", student.getFather());
        spEditor.putString("temp-student-mother", student.getMother());
        spEditor.putString("temp-student-class", student.getClassOfStudent());
        spEditor.putString("temp-student-division", student.getDivision());
        spEditor.putString("temp-student-house", student.getHouse());
        spEditor.putString("temp-student-contact", student.getContact());
        MyUtils.logThis("LocalDB - set temp student " + student.toString());
        spEditor.apply();
    }

    public Student getTempStudent() {
        String id = sp.getString("temp-student-id", "");
        String name = sp.getString("temp-student-name", "");
        String dob = sp.getString("temp-student-dob", "");
        String gender = sp.getString("temp-student-gender", "");
        String father = sp.getString("temp-student-father", "");
        String mother = sp.getString("temp-student-mother", "");
        String classOfStudent = sp.getString("temp-student-class", "");
        String division = sp.getString("temp-student-division", "");
        String house = sp.getString("temp-student-house", "");
        String contact = sp.getString("temp-student-contact", "");
        Student student = new Student(id, name, dob, gender, father, mother, classOfStudent, division, house, contact);
        MyUtils.logThis("LocalDB - read temp student = " + student.toString());
        return student;
    }


    public void setFCMID(String id) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("fcmid", id);
        spEditor.apply();
    }

    public String getFCMID() {
        return sp.getString("fcmid", null);
    }

    public void clear() {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.clear();
        spEditor.apply();
    }

    public void saveNextPayment(Fee fee) {
        SharedPreferences.Editor spEditor = sp.edit();

        String id = fee.getStudentId();
        spEditor.putString("time" + id, fee.getDate());
        spEditor.putString("amount" + id, fee.getAmount());
        spEditor.putString("description" + id, fee.getDescription());

        spEditor.apply();
    }

    public Fee getNextPayment(String studentID) {
        String date = sp.getString("time" + studentID, "");
        String amount = sp.getString("amount" + studentID, "");
        String type = "";
        String description = sp.getString("description" + studentID, "");
        if (amount.length() < 1) {
            return null;
        }
        return new Fee(studentID, date, amount, description, type);
    }

    public boolean isAlarmSet() {
        return sp.getBoolean("alarm", false);
    }

    public void setUri(String uri, String id) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("profilepic" + id, uri);
        spEditor.apply();
    }

    public String getUri(String id) {
        return sp.getString("profilepic" + id, "");
    }


    public void setAlarm(boolean b) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putBoolean("alarm", b);
        spEditor.apply();
    }

}
