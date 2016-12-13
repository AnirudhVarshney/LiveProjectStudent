package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 29-08-2016.
 */
/*    studentid    title    description    date    data    */
public class StudentResult {
    private String studentid;
    private String title;
    private String description;
    private String date;
    private String data;

    public StudentResult() {
        studentid = "";
        title = "";
        description = "";
        date = "";
        data = "";
    }

    public StudentResult(String studentid, String title, String description, String date, String data) {
        this.studentid = studentid;
        this.data = data;
        this.date = date;
        this.description = description;
        this.title = title;
    }

    @Override
    public String toString() {
        return "studentid = " + studentid + " : data = " + data
                + " : date = " + date + " : description = " + description + " : title = " + title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

}
