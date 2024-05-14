package com.example.oop;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable {
    int id;
    String name;
    String lastName;
    int age;
    String gender;
    String nationality;
    String wantedBy;
    String physicalDescription;
    String details;
    String charges;
    String photoPath;
    boolean archived;

    Card(int id, String name, String last_name, int age, String gender, String nationality,
         String wanted_by, String physical_description, String details, String charges, String photo, boolean archived) {
        this.id = id;
        this.name = name;
        this.lastName = last_name;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.wantedBy = wanted_by;
        this.physicalDescription = physical_description;
        this.details = details;
        this.charges = charges;
        this.photoPath = photo;
        this.archived = archived;
    }

    @NonNull
    @Override
    public String toString() {
        String put = "";

        put += this.name + "\n";

        return put;
    }
    public String iPtoString() {
        String put = "";
        put += this.lastName + "\n" + this.name + "\n" + this.gender + "\n" + this.age + "\n" + this.nationality;
        return put;
    }
    public String nameXlastName() {
        return this.lastName + ", " + this.name;
    }

    public boolean isPD() {
        return !Objects.equals(this.physicalDescription, "");
    }

    public boolean isDet() {
        return !Objects.equals(this.details, "");
    }
}
