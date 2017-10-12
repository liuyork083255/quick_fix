package com.sumscope.cdhplus.realtime.quickfixj.model;

/**
 * Created by liu.yang on 2017/8/24.
 */
public class MMUserInfoModel {
    private String orgType;
    private String operatorcode;
    private String province;

    public MMUserInfoModel(String orgType, String operatorcode, String province) {
        this.orgType = orgType;
        this.operatorcode = operatorcode;
        this.province = province;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOperatorcode() {
        return operatorcode;
    }

    public void setOperatorcode(String operatorcode) {
        this.operatorcode = operatorcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
