package com.mobile.urbanfix.urban_fix.model;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Problem {

    private String id;
    private String location;
    private String description;
    private String kindOfProblem;
    private String date;
    private String status;
    private Bitmap photo;
    private String checked;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKindOfProblem() {
        return kindOfProblem;
    }

    public void setKindOfProblem(String kindOfProblem) {
        this.kindOfProblem = kindOfProblem;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", localizacao='" + location + '\'' +
                ", description='" + description + '\'' +
                ", kindOfProblem='" + kindOfProblem + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", photo=" + photo +
                '}';
    }
}
