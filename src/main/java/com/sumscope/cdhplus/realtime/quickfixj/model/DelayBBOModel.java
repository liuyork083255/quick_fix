package com.sumscope.cdhplus.realtime.quickfixj.model;

import java.math.BigDecimal;

/**
 * Created by liu.yang on 2017/8/24.
 */
public class DelayBBOModel {
    private String sCDelStatus;
    private String sCPriceType;
    private String side;
    private String sCListedMarket;
    private String sCExceriseFlag;
    private String qbBboId;
    private String sCVolume;
    private BigDecimal price;
    private String sCShorName;
    private String securityType;
    private String sCQuoteTime;
    private String sCBrokerID;
    private String sCBondCode;

    public String getsCDelStatus() {
        return sCDelStatus;
    }

    public void setsCDelStatus(String sCDelStatus) {
        this.sCDelStatus = sCDelStatus;
    }

    public String getsCPriceType() {
        return sCPriceType;
    }

    public void setsCPriceType(String sCPriceType) {
        this.sCPriceType = sCPriceType;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getsCListedMarket() {
        return sCListedMarket;
    }

    public void setsCListedMarket(String sCListedMarket) {
        this.sCListedMarket = sCListedMarket;
    }

    public String getsCExceriseFlag() {
        return sCExceriseFlag;
    }

    public void setsCExceriseFlag(String sCExceriseFlag) {
        this.sCExceriseFlag = sCExceriseFlag;
    }

    public String getQbBboId() {
        return qbBboId;
    }

    public void setQbBboId(String qbBboId) {
        this.qbBboId = qbBboId;
    }

    public String getsCVolume() {
        return sCVolume;
    }

    public void setsCVolume(String sCVolume) {
        this.sCVolume = sCVolume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getsCShorName() {
        return sCShorName;
    }

    public void setsCShorName(String sCShorName) {
        this.sCShorName = sCShorName;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getsCQuoteTime() {
        return sCQuoteTime;
    }

    public void setsCQuoteTime(String sCQuoteTime) {
        this.sCQuoteTime = sCQuoteTime;
    }

    public String getsCBrokerID() {
        return sCBrokerID;
    }

    public void setsCBrokerID(String sCBrokerID) {
        this.sCBrokerID = sCBrokerID;
    }

    public String getsCBondCode() {
        return sCBondCode;
    }

    public void setsCBondCode(String sCBondCode) {
        this.sCBondCode = sCBondCode;
    }
}
