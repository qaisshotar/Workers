package com.example.workers;

import android.content.Context;

import java.util.Date;

public class Request {

    private String RID,UID,WID;
    private Date requestdate,responsedate;
    private String status;

    public Request() {
    }

    public Request(String UID, String WID, Date requestdate,String status) {
        this.UID = UID;
        this.WID=WID;
        this.requestdate = requestdate;
        this.status=status;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getWID() {
        return WID;
    }

    public void setWID(String WID) {
        this.WID = WID;
    }

    public Date getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(Date requestdate) {
        this.requestdate = requestdate;
    }

    public Date getResponsedate() {
        return responsedate;
    }

    public void setResponsedate(Date responsedate) {
        this.responsedate = responsedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRID() {
        return RID;
    }

    public void setRID(String RID) {
        this.RID = RID;
    }
}
