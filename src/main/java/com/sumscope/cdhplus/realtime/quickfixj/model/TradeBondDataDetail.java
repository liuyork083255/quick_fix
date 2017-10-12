package com.sumscope.cdhplus.realtime.quickfixj.model;


public class TradeBondDataDetail extends BondDataDetailBase {

    private String messageType = "trade";               //message type
    private String id;                                  // ----> inbound message field: id
    private long createTime;                            // ----> inbound message field: createTime
    private long modifyTime;                            // ----> inbound message field: modifyTime
    private long dealTime;                              // ----> inbound message field: dealTime
    private String bondKey;                             // ----> inbound message field: bondKey
    private String shortName;                           // ----> inbound message field: goodsShortName
    private String code;                                // ----> inbound message field: goodsCode
    private byte side;                                // ----> inbound message field: symbol
    private String quoteInstitution;                    // ----> inbound message field: financialCompanyId
    private String trader;                              // ----> inbound message field: traderId
    private String brokerId;                            // ----> inbound message field: companyId
    private String listedMarket;                        // ----> inbound message field: listedMarket
    //    private String quoteType;                           // ----> inbound message field: quoteType
    private int internally;                          // ----> inbound message field: internally
    private Double volume;                              // ----> inbound message field: volume
    private String volumeStr;
    private Double ytm;                                 // ----> inbound message field: yield
    private Double cleanPrice;                          // ----> inbound message field: netPrice
    private Double dirtyPrice;                          // ----> inbound message field: fullPrice

    /*below data same name as QPID fanout output*/
    private String settlementDate;
    private String bidBrokerId;
    private String bidBrokerName;
    private String bidBankId;
    private String bidAgentId;
    private String ofrBrokerId;
    private String ofrBrokerName;
    private String ofrBankId;
    private String ofrAgentId;
    private String priceDescription;
    private String priceStr;
    private Double price;
    private Double rebate;
    private int status;                       // ----> inbound message field: status: 0-normal, 2-referred
    private int dealStatus;                   // ----> inbound message field: dealStatus, 0-待单方确认, 1-待双方确认, 2-双方都已确认, 3-已完成 , 4-已作废 , 5-已取消.
    private String dealType;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public String getBondKey() {
        return bondKey;
    }

    public void setBondKey(String bondKey) {
        this.bondKey = bondKey;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte getSide() {
        return side;
    }

    public void setSide(byte side) {
        this.side = side;
    }

    public String getQuoteInstitution() {
        return quoteInstitution;
    }

    public void setQuoteInstitution(String quoteInstitution) {
        this.quoteInstitution = quoteInstitution;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getListedMarket() {
        return listedMarket;
    }

    public void setListedMarket(String listedMarket) {
        this.listedMarket = listedMarket;
    }

//    public String getQuoteType() {
//        return quoteType;
//    }
//
//    public void setQuoteType(String quoteType) {
//        this.quoteType = quoteType;
//    }

    public int getInternally() {
        return internally;
    }

    public void setInternally(int internally) {
        this.internally = internally;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getVolumeStr() {
        return volumeStr;
    }

    public void setVolumeStr(String volumeStr) {
        this.volumeStr = volumeStr;
    }

    public Double getYtm() {
        return ytm;
    }

    public void setYtm(Double ytm) {
        this.ytm = ytm;
    }

    public Double getCleanPrice() {
        return cleanPrice;
    }

    public void setCleanPrice(Double cleanPrice) {
        this.cleanPrice = cleanPrice;
    }

    public Double getDirtyPrice() {
        return dirtyPrice;
    }

    public void setDirtyPrice(Double dirtyPrice) {
        this.dirtyPrice = dirtyPrice;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getBidBrokerId() {
        return bidBrokerId;
    }

    public void setBidBrokerId(String bidBrokerId) {
        this.bidBrokerId = bidBrokerId;
    }

    public String getBidBrokerName() {
        return bidBrokerName;
    }

    public void setBidBrokerName(String bidBrokerName) {
        this.bidBrokerName = bidBrokerName;
    }

    public String getBidBankId() {
        return bidBankId;
    }

    public void setBidBankId(String bidBankId) {
        this.bidBankId = bidBankId;
    }

    public String getBidAgentId() {
        return bidAgentId;
    }

    public void setBidAgentId(String bidAgentId) {
        this.bidAgentId = bidAgentId;
    }

    public String getOfrBrokerId() {
        return ofrBrokerId;
    }

    public void setOfrBrokerId(String ofrBrokerId) {
        this.ofrBrokerId = ofrBrokerId;
    }

    public String getOfrBrokerName() {
        return ofrBrokerName;
    }

    public void setOfrBrokerName(String ofrBrokerName) {
        this.ofrBrokerName = ofrBrokerName;
    }

    public String getOfrBankId() {
        return ofrBankId;
    }

    public void setOfrBankId(String ofrBankId) {
        this.ofrBankId = ofrBankId;
    }

    public String getOfrAgentId() {
        return ofrAgentId;
    }

    public void setOfrAgentId(String ofrAgentId) {
        this.ofrAgentId = ofrAgentId;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(int dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }
    
    public Double getRebate()
    {
        return rebate;
    }

    public void setRebate(Double rebate)
    {
        this.rebate = rebate;
    }
}