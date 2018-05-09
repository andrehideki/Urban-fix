package com.mobile.urbanfix.urban_fix.model;

import java.util.ArrayList;
import java.util.List;

public class Documents {
    private List<Document> documents;

    public Documents (){
        this.documents = new ArrayList<>();
    }

    public void add (String id, String language, String text){
        documents.add(new Document(id, language, text));
    }
    public List <Document> getDocuments (){
        return this.documents;
    }
}
