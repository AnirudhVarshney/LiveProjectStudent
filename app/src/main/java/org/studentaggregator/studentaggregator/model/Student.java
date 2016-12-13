package org.studentaggregator.studentaggregator.model;

/**
 * Created by Ray on 4/27/2016.
 */
/*  id name dob gender father mother class division house contact    */
public class Student {
    private String id;
    private String name;
    private String dob;
    private String gender;
    private String father;
    private String mother;
    private String classOfStudent;
    private String division;
    private String house;
    private String contact;

    public Student() {
        this.id = "";
        this.name = "";
        this.dob = "";
        this.gender = "";
        this.father = "";
        this.mother = "";
        this.classOfStudent = "";
        this.division = "";
        this.house = "";
        this.contact = "";
    }

    public Student(String id, String name, String dob, String gender, String father, String mother,
                   String classOfStudent, String division, String house, String contact) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.classOfStudent = classOfStudent;
        this.division = division;
        this.house = house;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
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

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "id = " + id + " : name = " + name + " : dob = " + dob + " : gender = " + gender +
                " : father = " + father + " : mother = " + mother + " : class = " + classOfStudent +
                " : division = " + division + " : house = " + house+ " : contact = " + contact;
    }

}
