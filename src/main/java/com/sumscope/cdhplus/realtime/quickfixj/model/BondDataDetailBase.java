package com.sumscope.cdhplus.realtime.quickfixj.model;


public class BondDataDetailBase{

    private String transType;
    private String bidOrAsk;
    private long locMsgCrtAt;
    private String uniDataID;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getBidOrAsk() {
        return bidOrAsk;
    }

    public void setBidOrAsk(String bidOrAsk) {
        this.bidOrAsk = bidOrAsk;
    }

    public long getLocMsgCrtAt() {
        return locMsgCrtAt;
    }

    public void setLocMsgCrtAt(long locMsgCrtAt) {
        this.locMsgCrtAt = locMsgCrtAt;
    }

    public String getUniDataID() {
        return uniDataID;
    }

    public void setUniDataID(String uniDataID) {
        this.uniDataID = uniDataID;
    }
}
