package com.example.appbanhangonline.model;

import java.util.Date;

public class ChatMessage {
    public String sendid, receiveid, mess, datetime;
    public Date dateObj;

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

    public String getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getDatetine() {
        return datetime;
    }

    public void setDatetine(String datetine) {
        this.datetime = datetine;
    }

    public Date getDateObj() {
        return dateObj;
    }

    public void setDateObj(Date dateObj) {
        this.dateObj = dateObj;
    }
}