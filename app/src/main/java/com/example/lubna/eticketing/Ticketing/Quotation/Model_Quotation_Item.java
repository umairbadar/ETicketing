package com.example.lubna.eticketing.Ticketing.Quotation;

public class Model_Quotation_Item {

    private String product_name;
    private String qty;
    private String cost;
    private String sub_total;

    public Model_Quotation_Item(String product_name, String qty, String cost, String sub_total) {
        this.product_name = product_name;
        this.qty = qty;
        this.cost = cost;
        this.sub_total = sub_total;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQty() {
        return qty;
    }

    public String getCost() {
        return cost;
    }

    public String getSub_total() {
        return sub_total;
    }
}
