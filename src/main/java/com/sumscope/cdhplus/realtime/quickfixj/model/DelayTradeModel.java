package com.sumscope.cdhplus.realtime.quickfixj.model;

import java.math.BigDecimal;

/**
 * Created by liu.yang on 2017/8/24.
 */
public class DelayTradeModel {
    private String symbol;
    private String side;
    private String settlDate;
    private String securityID;
    private String dealStatus;
    private String instrmtAssignmentMethod;
    private String quoteType;
    private String quoteID;
    private String quoteReqID;
    private String qbTradeId;
    private String securityType;
    private BigDecimal yield;
    private String transactTime;
    private Integer orderQty;
    private BigDecimal strikePrice;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSettlDate() {
        return settlDate;
    }

    public void setSettlDate(String settlDate) {
        this.settlDate = settlDate;
    }

    public String getSecurityID() {
        return securityID;
    }

    public void setSecurityID(String securityID) {
        this.securityID = securityID;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getInstrmtAssignmentMethod() {
        return instrmtAssignmentMethod;
    }

    public void setInstrmtAssignmentMethod(String instrmtAssignmentMethod) {
        this.instrmtAssignmentMethod = instrmtAssignmentMethod;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }

    public String getQuoteReqID() {
        return quoteReqID;
    }

    public void setQuoteReqID(String quoteReqID) {
        this.quoteReqID = quoteReqID;
    }

    public String getQbTradeId() {
        return qbTradeId;
    }

    public void setQbTradeId(String qbTradeId) {
        this.qbTradeId = qbTradeId;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }

    public String getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(String transactTime) {
        this.transactTime = transactTime;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(BigDecimal strikePrice) {
        this.strikePrice = strikePrice;
    }
}
