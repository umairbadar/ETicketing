package com.example.lubna.eticketing.Ticketing.Quotation;

public class Model_Quotation_Item {

    private String id;
    private String product_name;
    private String qty;
    private String cost;
    private String sub_total;
    private String sr_product_id;
    private String approved_quantity;
    private String status;

    public Model_Quotation_Item(String sr_product_id, String id, String product_name, String qty, String cost, String sub_total, String approved_quantity, String status) {
        this.product_name = product_name;
        this.qty = qty;
        this.cost = cost;
        this.sub_total = sub_total;
        this.id = id;
        this.sr_product_id = sr_product_id;
        this.approved_quantity = approved_quantity;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getApproved_quantity() {
        return approved_quantity;
    }

    public String getSr_product_id() {
        return sr_product_id;
    }

    public String getId() {
        return id;
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
