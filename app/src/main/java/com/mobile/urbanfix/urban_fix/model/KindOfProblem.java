package com.mobile.urbanfix.urban_fix.model;

public class KindOfProblem {

    private int id;
    private String kindOfProblem;

    public KindOfProblem(int id, String kindOfProblem) {
        this.id = id;
        this.kindOfProblem = kindOfProblem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKindOfProblem() {
        return kindOfProblem;
    }

    public void setKindOfProblem(String kindOfProblem) {
        this.kindOfProblem = kindOfProblem;
    }
}
