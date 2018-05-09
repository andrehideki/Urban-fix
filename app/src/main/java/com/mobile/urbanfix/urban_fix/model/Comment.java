package com.mobile.urbanfix.urban_fix.model;

import com.mobile.urbanfix.urban_fix.Sentiments;

public class Comment {

    private int id;
    private String personName, content;
    private Sentiments.SENTIMENTS sentiment;

    public Comment() {}
    public Comment(int id, String personName, String content) {
        this.id = id;
        this.personName = personName;
        this.content = content;
        this.sentiment = Sentiments.SENTIMENTS.NEUTRAL;
    }

    public int getId() {
        return id;
    }

    public String getPersonName() {
        return personName;
    }

    public String getContent() {
        return content;
    }

    public Sentiments.SENTIMENTS getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiments.SENTIMENTS sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", personName='" + personName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
