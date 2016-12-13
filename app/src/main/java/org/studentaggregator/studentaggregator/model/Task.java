package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 15-07-2016.
 */
/*  classOfStudent    division    date    title   description     filename  */
public class Task {

    private String classOfStudent;
    private String division;
    private String date;
    private String title;
    private String description;
    private String filename;

    public Task() {
        classOfStudent = "";
        division = "";
        title = "";
        description = "";
        filename = "";
        date = "";
    }

    public Task(String classOfStudent, String division, String date, String title, String description, String filename) {
        this.classOfStudent = classOfStudent;
        this.division = division;
        this.title = title;
        this.description = description;
        this.filename = filename;
        this.date = date;
    }


    @Override
    public String toString() {
        return "classOfStudent = " + classOfStudent + " : division = " + division
                + " : title = " + title + " : description = " + description + " : filename = " + filename + " : date = " + date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
