package com.example.lubna.eticketing.Ticketing;

public class ModelSMD {

    private String id;
    private String dep_name;


    public ModelSMD(String id, String dep_name) {
        this.id = id;
        this.dep_name = dep_name;
    }

    public String getId() {
        return id;
    }

    public String getDep_name() {
        return dep_name;
    }
}
