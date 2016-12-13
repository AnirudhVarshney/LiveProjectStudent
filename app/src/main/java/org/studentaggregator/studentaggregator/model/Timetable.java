package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 06-Sep-16.
 */
/*  classOfStudent   division    day  startTime   endTime     subject     */
public class Timetable {

    private String classOfStudent;
    private String division;
    private String day;
    private String startTime;
    private String endTime;
    private String subject;

    public Timetable() {
        classOfStudent = "";
        division = "";
        day = "";
        startTime = "";
        endTime = "";
        subject = "";
    }

    public Timetable(String classOfStudent, String division, String day, String startTime, String endTime, String subject) {
        this.classOfStudent = classOfStudent;
        this.division = division;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Timetable = classOfStudent = " + classOfStudent + " , division = " + division + " , day = " +
                day + " , startTime = " + startTime + " , endTime = " + endTime + " , subject = " + subject;
    }

    public String getClassOfStudent() {
        return classOfStudent;
    }

    public void setClassOfStudent(String classOfStudent) {
        this.classOfStudent = classOfStudent;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
