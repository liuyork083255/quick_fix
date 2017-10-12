package com.sumscope.cdhplus.realtime.quickfixj.model;

/**
 * Created by liu.yang on 2017/8/23.
 */
public class PTenors {
    private Integer days_low;
    private Integer days_high;
    private Integer tenor_flag;

    public Integer getTenor_flag() {
        return tenor_flag;
    }

    public void setTenor_flag(Integer tenor_flag) {
        this.tenor_flag = tenor_flag;
    }

    public Integer getDays_low() {
        return days_low;
    }

    public void setDays_low(Integer days_low) {
        this.days_low = days_low;
    }

    public Integer getDays_high() {
        return days_high;
    }

    public void setDays_high(Integer days_high) {
        this.days_high = days_high;
    }
}
