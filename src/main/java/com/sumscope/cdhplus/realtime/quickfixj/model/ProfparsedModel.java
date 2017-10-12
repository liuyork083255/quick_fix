package com.sumscope.cdhplus.realtime.quickfixj.model;

/**
 * Created by liu.yang on 2017/8/23.
 */
public class ProfparsedModel {
    private PParsedMsg parsedMsg;
    private String msgData;
    private long time;
    private String senderuin;

    public PParsedMsg getParsedMsg() {
        return parsedMsg;
    }

    public void setParsedMsg(PParsedMsg parsedMsg) {
        this.parsedMsg = parsedMsg;
    }

    public String getMsgData() {
        return msgData;
    }

    public void setMsgData(String msgData) {
        this.msgData = msgData;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSenderuin() {
        return senderuin;
    }

    public void setSenderuin(String senderuin) {
        this.senderuin = senderuin;
    }
}
