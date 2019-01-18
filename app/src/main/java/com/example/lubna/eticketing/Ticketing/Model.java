package com.example.lubna.eticketing.Ticketing;

/**
 * Created by lubna on 10/26/2018.
 */
class Model {
    private String sNo;
    private String ticket;
    private String sitename;

    public Model(String sNo, String ticket,String sitename)
    {
        this.sNo = sNo;
        this.ticket = ticket;
        this.sitename=sitename;

    }
    public String getsNo() {
        return sNo;
    }
    public String getTicket() {
        return ticket;
    }
    public String getSitename() {
        return sitename;
    }
}

