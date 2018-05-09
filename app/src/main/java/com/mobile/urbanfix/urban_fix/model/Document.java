package com.mobile.urbanfix.urban_fix.model;

public class Document {
    private String id, language, text;
    private double score;
    public Document (){}
    public Document (String id, String language, String text){
        this.id = id;
        this.language = language;
        this.text = text;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language){
        this.language = language;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public double getScore() {
        return score;
    }
}
