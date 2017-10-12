package com.sumscope.cdhplus.realtime.quickfixj.mqsender;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.FieldType;
import com.sumscope.cdhplus.realtime.quickfixj.model.PParserResult;
import com.sumscope.cdhplus.realtime.quickfixj.model.PTenors;
import com.sumscope.cdhplus.realtime.quickfixj.model.ProfparsedModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.SecurityType;
import quickfix.field.SenderCompID;
import quickfix.field.Side;
import quickfix.field.TargetCompID;
import quickfix.fix50sp2.Quote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liu.yang on 2017/8/23.
 */
@Component
public class ProfParsedSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfParsedSender.class);

    @Autowired
    private FixSessions fixSessions;

    public void convertData(ProfparsedModel msg, List<String> mm_onLine_cash) {
        String quoteDate = "";
        String quoteTime = "";
        long dateTime = msg.getTime();
        if(dateTime != 0){
            quoteDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateTime));
            quoteTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateTime));
        }

        String sendId = msg.getSenderuin();

        String orgType = null;
        String province = null;
        String operatorcode = null;
        getUserInfo(sendId,orgType,province,operatorcode);
        if(orgType == null) orgType = "";
        if(province == null) province = "";
        if(operatorcode == null) operatorcode = "";

        String transType = "";
        List<PParserResult> dictQuote = msg.getParsedMsg().getParser_result();
        for (PParserResult obj : dictQuote) {
            if(obj.isBuyout_repo()){transType += "买断式,";}
            if(obj.isPledge_repo()){transType += "质押式,";}
            if(obj.isBank_credit()){transType += "拆借,";}

            if(transType.length()>0){
                transType = transType.substring(0,transType.length()-1);
            }

            Integer side = obj.getSide();
            String assetsType = obj.getBond_types() == null?"": JSON.toJSONString(obj.getBond_types());

            String bondShclearing = "";
            if(obj.isBond_shclearing()){
                bondShclearing = "1";
            }else{
                bondShclearing = "0";
            }

            String bondCdc = "";
            if(obj.isBond_cdc()){
                bondCdc = "1";
            }else {
                bondCdc = "0";
            }

            String uppervol = obj.getVolume_high() == null? "":obj.getVolume_high().toString();
            String lowervol = obj.getVolume_low() == null?"":obj.getVolume_low().toString();
            String price = obj.getRate()==null?"":obj.getRate().toString();

            String tradeLimits = "";
            if(obj.isTrade_direct()){
                tradeLimits += "限直连,";
            }
            Integer[] goodCp = obj.getGood_cp();
            if(goodCp != null){
                for (Integer integer : goodCp) {
                    if(integer == 0){
                        tradeLimits += "限银行,";
                    }else{
                        tradeLimits += "限农信,";
                    }
                }
            }

            int repaymentType = obj.getRepayment_type();
            if(repaymentType == 3 || repaymentType == 4){
                tradeLimits += "可早回款,";
            }

            if(obj.getFuzzy_settlement_time()!=null && obj.getFuzzy_settlement_time() == 0){
                tradeLimits += "钱在账上,";
            }
            if(tradeLimits.length() > 0){
                tradeLimits = tradeLimits.substring(0,tradeLimits.length() -1);
            }

            String ifBreakdown = "";
            if(obj.isAll_or_none()){
                ifBreakdown = "1";
            }else{
                ifBreakdown = "0";
            }

            String bondRating = "";
            int valueBondRating = obj.getBond_rating();
            if(valueBondRating == 1) {
                bondRating = "AAA";
            }else if(valueBondRating == 2){
                bondRating = "AA+";
            }else if(valueBondRating == 3){
                bondRating = "AA";
            }else if(valueBondRating == 4) {
                bondRating = "AA-";
            }else if(valueBondRating == 5){
                bondRating = "A";
            }

            String term = "";
            List<PTenors> tenors = obj.getTenors();
            if(tenors != null){
                for (PTenors tenor : tenors) {

                    Integer termHigh = tenor.getDays_high();
                    Integer termLow = tenor.getDays_low();
                    Integer termFlag = tenor.getTenor_flag();
                    if(termHigh != null && termLow != null && termFlag != null && termHigh == termLow){
                        if(termFlag == 0) termLow = null;
                        else if(termFlag == 1) termHigh = null;
                    }

                    String upperTerm = transTerm(termHigh);
                    String lowerTerm = transTerm(termLow);
                    if(upperTerm != null){
                        if(lowerTerm == null){
                            term += upperTerm + "以下,";
                        }else if(upperTerm.equals(lowerTerm)){
                            if(upperTerm.equals("1D")) term += "隔夜,";
                            else term += lowerTerm + ",";
                        }else term += lowerTerm + "-" + upperTerm + ",";
                    }else{
                        if(lowerTerm != null) term += lowerTerm + "以上,";
                    }
                }
            }

            if(term.length() > 0) term = term.substring(0,term.length() -1);
            String fuzzyVolume = obj.getFuzzy_volume() == null ? "" : obj.getFuzzy_volume();

            Quote message = new Quote();
            message.getHeader().setField(new SenderCompID("SUMSCOPE"));

            if(side == 0) message.setField(new Side('G'));
            else if (side == 1) message.setField(new Side('F'));


            message.setField(new StringField(FieldType.DATE_FIELD,quoteDate));
            message.setField(new StringField(FieldType.TIME_FIELD,quoteTime));
            message.setField(new StringField(FieldType.ORGTYPE_FIELD,orgType));
            message.setField(new StringField(FieldType.PROVINCE_FIELD,province));
            message.setField(new StringField(FieldType.OPERATORCODE_FIELD, operatorcode));
            message.setField(new StringField(FieldType.TRANSTYPE_FIELD,transType));
            message.setField(new StringField(FieldType.ASSETSTYPE_FIELD,assetsType));
            message.setField(new StringField(FieldType.BONDSHCLEARING_FIELD,bondShclearing));
            message.setField(new StringField(FieldType.BONDCDC_FIELD,bondCdc));
            message.setField(new StringField(FieldType.TRADELIMITS_FIELD,tradeLimits));
            message.setField(new StringField(FieldType.IFBREAKDOWN_FIELD,ifBreakdown));
            message.setField(new StringField(FieldType.BONDRATING_FIELD,bondRating));
            message.setField(new StringField(FieldType.TERM_FIELD,term));
            message.setField(new StringField(FieldType.FUZZYVOLUME_FIELD,fuzzyVolume));
            message.setField(new StringField(FieldType.UPPERVOL_FIELD,uppervol));
            message.setField(new StringField(FieldType.LOWERVOL_FIELD,lowervol));
            message.setField(new StringField(FieldType.PRICESTR_FIELD, price));

            message.setField(new SecurityType("MMQUOTE"));

            sendFixMessage(message,mm_onLine_cash);
        }
    }


    private void sendFixMessage(Quote message,List<String> targetList){
        targetList.forEach((target) -> {
            message.getHeader().setField(new TargetCompID(target));

            try {
                Session.sendToTarget(message);
            } catch (SessionNotFound sessionNotFound) {
                LOGGER.error("send SendBestQuoteQB quickfix message fail. msg:" + sessionNotFound.getMessage());
            }

        });
    }

    private String transTerm(Integer term){
        String result = null;
        if(term != null){
            if(term == 20) result = "1M";
            else if(term == 60) result = "2M";
            else if(term == 90) result = "3M";
            else if(term == 180) result = "6M";
            else if(term == 270) result = "9M";
            else if(term >= 365 && term % 365 == 0) result = (term / 365) + "Y";
            else if(term > 365 && term % 30 == 0) result = (term / 30) + "M";
            else result = term + "D";
        }
        return result;
    }

    private void getUserInfo(String userId,String orgType,String province,String operatorcode){
        if(userId == null)
            return ;
        if(fixSessions.getMmUserInfo().get(userId) != null){
            orgType = fixSessions.getMmUserInfo().get(userId).getOrgType();
            province = fixSessions.getMmUserInfo().get(userId).getProvince();
            operatorcode = fixSessions.getMmUserInfo().get(userId).getOperatorcode();
        }
    }
}
