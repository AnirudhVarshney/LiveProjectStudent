package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 4/25/2016.
 */

/*  studentid    status   description   date    time    */

public class Attendance {
    private String studentid;
    private String status;
    private String description;
    private String date;
    private String time;

    public Attendance() {
        studentid = "";
        status = "";
        description = "";
        date = "";
        time = "";
    }

    public Attendance(String studentid, String status, String desc, String date, String time) {
        this.studentid = studentid;
        this.status = status;
        this.description = desc;
        this.date = date;
        this.time = time;
    }

    public String getStudentId() {
        return studentid;
    }

    public void setStudentId(String id) {
        this.studentid = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "studentid = " + studentid + " : status = " + status + " : description = " + description +
                " : date = " + date + " : time = " + time;
    }
}
