package com.example.lubna.eticketing.Ticketing.Quotation;

public class ModelQD {

    private String id;
    private String dep_name;

    public ModelQD(String id,String dep_name) {
        this.dep_name = dep_name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDep_name() {
        return dep_name;
    }
}
