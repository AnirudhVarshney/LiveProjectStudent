package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 4/21/2016.
 */
/*  date description    */
public class Holiday {

    private String date;
    private String description;

    public Holiday() {
        date = "";
        description = "";
    }

    public Holiday(String date, String description) {
        this.date = date;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "date = " + date + " : description = " + description;
    }
}
