package org.studentaggregator.studentaggregator.model;

/**
 * Created by RAY on 29-08-2016.
 */
/*    studentid    message    reply    date    */
public class Query {

    private String studentid;
    private String message;
    private String reply;
    private String date;

    public Query() {
        studentid = "";
        message = "";
        reply = "";
        date = "";
    }

    public Query(String studentid, String message, String reply, String date) {
        this.studentid = studentid;
        this.message = message;
        this.reply = reply;
        this.date = date;
    }


    public String getStudentId() {
        return studentid;
    }

    public void setStudentId(String studentid) {
        this.studentid = studentid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String description) {
        this.message = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String datetime) {
        this.date = datetime;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "studentid = " + studentid + " : message = " +
                message + " : reply = " + reply + " : date = " + date;
    }
}
