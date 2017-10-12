package com.sumscope.cdhplus.realtime.quickfixj.model;


public class QuoteBondDataDetail extends BondDataDetailBase {

    private String messageType = "quote";        //message type
    private String id;                           // ----> inbound message field: id
    private long createTime;                     // ----> inbound message field: createTime
    private long modifyTime;                     // ----> inbound message field: modifyTime
    private String bondKey;                      // ----> inbound message field: bondKey
    private String shortName;                    // ----> inbound message field: goodsShortName
    private String code;                         // ----> inbound message field: goodsCode
    private byte side;                         // ----> inbound message field: symbol
    private String quoteInstitution;             // ----> inbound message field: financialCompanyId
    private String trader;                       // ----> inbound message field: traderId
    private String brokerId;                     // ----> inbound message field: companyId
    private String listedMarket;                 // ----> inbound message field: listedMarket
    private String quoteType;                    // ----> inbound message field: quoteType
    private int internally;                   // ----> inbound message field: internally: 1-external 2-internal
    private int status;                       // ----> inbound message field: status: 0-normal, 2-referred
    private byte isExercise;                     // ----> inbound message field: exercise bid=0 offer=1
    private byte bargainFlag;                    // ----> inbound message field: flagBargain
    private byte relationFlag;                   // ----> inbound message field: flagRelation
    private Double volume;                       // ----> inbound message field: volume
    private String volumeStr;
    private String volumeDesc;
    private Double ytm;                          // ----> inbound message field: yield
    private Double cleanPrice;                   // ----> inbound message field: netPrice
    private Double dirtyPrice;                   // ----> inbound message field: fullPrice
    private String priceDescription;             // ----> inbound message field: priceDescription
    private int dealStatus;                   // ----> inbound message field: dealStatus, 0-待单方确认, 1-待双方确认, 2-双方都已确认, 3-已完成 , 4-已作废 , 5-已取消.

    private String priceStr;
    private Double price;
    private Double rebate;

    /**
     * gets the uni key from bondkey, listedmarket, brokerid, and customized string side. side could be item.getSide() > 0 ? "B" : "O"
     *
     * @param item
     * @param side
     * @return uni key string
     */
    public static String getBondUniKey(QuoteBondDataDetail item, String side) {
        return getBondUniKey(item.getBondKey(), item.getListedMarket(), item.getBrokerId(), side);
    }

    public static String getBondUniKey(String bondKey, String market, String brokerId, String side) {
        return brokerId + "," + market + "," + bondKey + "," + side;
    }

    public static String getQuoteUniKey(QuoteBondDataDetail item) {
        return getQuoteUniKey(item.getBondKey(), item.getListedMarket(), item.getBrokerId(), item.getId(), item.getSide() > 0 ? "B" : "O");
    }

    public static String getQuoteUniKey(String bondKey, String market, String brokerId, String quoteId, String side) {
        return brokerId + "," + market + "," + bondKey + "," + side + "," + quoteId;
    }

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

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public int getInternally() {
        return internally;
    }

    public void setInternally(int internally) {
        this.internally = internally;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public byte getIsExercise() {
        return isExercise;
    }

    public void setIsExercise(byte isExercise) {
        this.isExercise = isExercise;
    }

    public byte getBargainFlag() {
        return bargainFlag;
    }

    public void setBargainFlag(byte bargainFlag) {
        this.bargainFlag = bargainFlag;
    }

    public byte getRelationFlag() {
        return relationFlag;
    }

    public void setRelationFlag(byte relationFlag) {
        this.relationFlag = relationFlag;
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

    public String getVolumeDesc() {
        return volumeDesc;
    }

    public void setVolumeDesc(String volumeDesc) {
        this.volumeDesc = volumeDesc;
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

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
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

    public Double getRebate()
    {
        return rebate;
    }

    public void setRebate(Double rebate)
    {
        this.rebate = rebate;
    }

    @Override
    public String toString() {
        return String.format("bondkey=%s listedmarket=%s brokerid=%s side=%d id=%s tanstype=%s cleanprice=%f ytm=%f price=%f priceStr=%s volume=%f volumestr=%s createTime=%d modifyTime=%d",
                bondKey, listedMarket, brokerId, side, id, getTransType(), cleanPrice, ytm, price, priceStr, volume, volumeStr, createTime, modifyTime);
    }

}
