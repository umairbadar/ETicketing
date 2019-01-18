package com.example.lubna.eticketing.Survey;

/**
 * Created by lubna on 11/15/2017.
 */

class ModelS {
    private String sNo;
    private String ticket;
    private String sitename;

    public ModelS(String sNo, String ticket, String sitename)
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
