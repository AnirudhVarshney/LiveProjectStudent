package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 5/12/2016.
 */
/*    studentid date amount description type  */
public class Fee {

    private String studentid;
    private String date;
    private String amount;
    private String description;
    private String type;

    public Fee(String studentid, String date, String amount, String description, String type) {
        this.studentid = studentid;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public Fee() {
        this.studentid = "";
        this.date = "";
        this.amount = "";
        this.type = "";
        this.description = "";
    }

    @Override
    public String toString() {
        return "studentId=" + studentid + " : date=" + date + " : amount=" + amount + " : type=" +
                type + " : description=" + description;
    }

    public String getStudentId() {
        return studentid;
    }

    public void setStudentId(String studentId) {
        this.studentid = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
