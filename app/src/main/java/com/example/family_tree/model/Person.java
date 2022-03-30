package com.example.family_tree.model;

import java.util.Date;

public class Person {
    private String name;
    private String surname;
    private String sex;
    private String image;
    private Person Mother = null;
    private Person Father = null;
    private Date date;

    public Person getMother() {
        return Mother;
    }

    public void setMother(Person mother) {
        Mother = mother;
    }

    public Person getFather() {
        return Father;
    }

    public void setFather(Person father) {
        Father = father;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Person(String name, String surname, String sex, String image, Date date) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.date = date;
        this.image = image;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
