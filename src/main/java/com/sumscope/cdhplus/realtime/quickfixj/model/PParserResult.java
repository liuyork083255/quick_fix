package com.sumscope.cdhplus.realtime.quickfixj.model;

import java.util.List;

/**
 * Created by liu.yang on 2017/8/23.
 */
public class PParserResult {
    private Integer volume_high;
    private boolean buyout_repo;
    private boolean pledge_repo;
    private boolean bank_credit;
    private boolean bond_shclearing;
    private boolean bond_cdc;
    private boolean trade_direct;
    private boolean all_or_none;
    private Double rate;
    private Integer side;
    private Integer volume_low;
    private List<PTenors> tenors;
    private Integer confidence;
    private String id;
    private Integer[] bond_types;
    private Integer[] good_cp;
    private int repayment_type;
    private Integer fuzzy_settlement_time;
    private int bond_rating;
    private String fuzzy_volume;

    public String getFuzzy_volume() {
        return fuzzy_volume;
    }

    public void setFuzzy_volume(String fuzzy_volume) {
        this.fuzzy_volume = fuzzy_volume;
    }

    public int getBond_rating() {
        return bond_rating;
    }

    public void setBond_rating(int bond_rating) {
        this.bond_rating = bond_rating;
    }

    public Integer getFuzzy_settlement_time() {
        return fuzzy_settlement_time;
    }

    public void setFuzzy_settlement_time(Integer fuzzy_settlement_time) {
        this.fuzzy_settlement_time = fuzzy_settlement_time;
    }

    public int getRepayment_type() {
        return repayment_type;
    }

    public void setRepayment_type(int repayment_type) {
        this.repayment_type = repayment_type;
    }

    public Integer[] getGood_cp() {
        return good_cp;
    }

    public void setGood_cp(Integer[] good_cp) {
        this.good_cp = good_cp;
    }

    public Integer getVolume_high() {
        return volume_high;
    }

    public void setVolume_high(Integer volume_high) {
        this.volume_high = volume_high;
    }

    public boolean isBuyout_repo() {
        return buyout_repo;
    }

    public void setBuyout_repo(boolean buyout_repo) {
        this.buyout_repo = buyout_repo;
    }

    public boolean isPledge_repo() {
        return pledge_repo;
    }

    public void setPledge_repo(boolean pledge_repo) {
        this.pledge_repo = pledge_repo;
    }

    public boolean isBank_credit() {
        return bank_credit;
    }

    public void setBank_credit(boolean bank_credit) {
        this.bank_credit = bank_credit;
    }

    public boolean isBond_shclearing() {
        return bond_shclearing;
    }

    public void setBond_shclearing(boolean bond_shclearing) {
        this.bond_shclearing = bond_shclearing;
    }

    public boolean isBond_cdc() {
        return bond_cdc;
    }

    public void setBond_cdc(boolean bond_cdc) {
        this.bond_cdc = bond_cdc;
    }

    public boolean isTrade_direct() {
        return trade_direct;
    }

    public void setTrade_direct(boolean trade_direct) {
        this.trade_direct = trade_direct;
    }

    public boolean isAll_or_none() {
        return all_or_none;
    }

    public void setAll_or_none(boolean all_or_none) {
        this.all_or_none = all_or_none;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getSide() {
        return side;
    }

    public void setSide(Integer side) {
        this.side = side;
    }

    public Integer getVolume_low() {
        return volume_low;
    }

    public void setVolume_low(Integer volume_low) {
        this.volume_low = volume_low;
    }

    public List<PTenors> getTenors() {
        return tenors;
    }

    public void setTenors(List<PTenors> tenors) {
        this.tenors = tenors;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer[] getBond_types() {
        return bond_types;
    }

    public void setBond_types(Integer[] bond_types) {
        this.bond_types = bond_types;
    }
}
