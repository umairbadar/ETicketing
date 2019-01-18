package com.example.lubna.eticketing.Ticketing.Quotation;

public class ModelQL {
    private String id;
    private String quotation_id;
    private String sender;
    private String site;


    public ModelQL(String id, String quotation_id, String sender, String site) {
        this.id = id;
        this.quotation_id = quotation_id;
        this.sender = sender;
        this.site = site;
    }

    public String getId() {
        return id;
    }

    public String getQuotation_id() {
        return quotation_id;
    }

    public String getSender() {
        return sender;
    }

    public String getSite() {
        return site;
    }
}
