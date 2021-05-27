package com.example.workers;

import com.google.firebase.firestore.GeoPoint;

public class Worker {

    private String Name,JopTitle,JobDescription,PhoneNumber,email,password;
    private GeoPoint latLng;
    private String type;

    public Worker() {
    }

    public Worker( String name, String jopTitle, String jobDescription ,
                   String phoneNumber , String email , String password , String type , GeoPoint latLng) {
        Name = name;
        JopTitle = jopTitle;
        this.JobDescription = jobDescription;
        this.PhoneNumber=phoneNumber;
        this.email=email;
        this.password=password;
        this.type=type;
        this.latLng=latLng;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getJopTitle() {
        return JopTitle;
    }

    public void setJopTitle(String jopTitle) {
        JopTitle = jopTitle;
    }

    public GeoPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(GeoPoint latLng) {
        this.latLng = latLng;
    }

    public String getJobDescription() {
        return JobDescription;
    }

    public void setJobDescription(String jobDescription) {
        JobDescription = jobDescription;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
