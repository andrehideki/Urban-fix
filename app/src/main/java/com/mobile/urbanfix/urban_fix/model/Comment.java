package com.mobile.urbanfix.urban_fix.model;

public class Comment {

    private int id;
    private String personName, content;

    public Comment() {}
    public Comment(int id, String personName, String content) {
        this.id = id;
        this.personName = personName;
        this.content = content;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", personName='" + personName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
