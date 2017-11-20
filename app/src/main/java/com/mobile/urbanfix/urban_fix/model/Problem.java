package com.mobile.urbanfix.urban_fix.model;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Problem {

    private long id;
    private String localizacao;
    private String descricao;
    private KindOfProblem kindOfProblem;
    private Date date;
    private String status;
    private Bitmap photo;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public KindOfProblem getKindOfProblem() {
        return kindOfProblem;
    }

    public void setKindOfProblem(KindOfProblem kindOfProblem) {
        this.kindOfProblem = kindOfProblem;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
}
