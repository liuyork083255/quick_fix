package com.sumscope.cdhplus.realtime.quickfixj.mqsender;

import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.BestQuoteQBModel;
import com.sumscope.cdhplus.realtime.quickfixj.model.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.*;
import quickfix.fix50sp2.Quote;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liu.yang on 2017/8/22.
 */
@Component
public class BestQuoteQBSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(BestQuoteQBSender.class);

    @Autowired
    private FixSessions fixSessions;

    public void convertData(BestQuoteQBModel msg, List<String> targetList){

        long createTime = msg.getCt()==null?0:Long.parseLong(msg.getCt());
        long updateTime = msg.getMt()==null?0:Long.parseLong(msg.getMt());
        String companyId = msg.getCid()==null?"":msg.getCid();
        String bondCode = msg.getGc()==null?"":msg.getGc();
        String listedMarket = msg.getLm()==null?"":msg.getLm();
        String side = msg.getSym()==null?"":msg.getSym();
        String price = msg.getPri()==null?"":msg.getPri();
        String volume = msg.getVol()==null?"":msg.getVol();
        String dealStatus = msg.getDs()==null?"":msg.getDs();
        String status = msg.getSts()==null?"":msg.getSts();
        String shortName = msg.getGsn()==null?"":msg.getGsn();
        String exceriseFlag = "0";
        String priceType="";

        Quote message = new Quote();
        message.getHeader().setField(new SenderCompID("SUMSCOPE"));
        if(updateTime == 0){
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(createTime));
            message.setField(new StringField(FieldType.QuoteTime_Field,time));
        }else{
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(updateTime));
            message.setField(new StringField(FieldType.QuoteTime_Field,time));
        }

        message.setField(new StringField(FieldType.BrokerID_Field,companyId));
        message.setField(new StringField(FieldType.BondCode_Field,bondCode));
        message.setField(new StringField(FieldType.ListedMarket_Field,listedMarket));
        message.setField(new StringField(FieldType.ShorName_Field,shortName));

        if(side.equals("1")){
            message.setField(new Side('1'));
        }else{
            message.setField(new Side('2'));
        }

        if(price.length() == 0){
            price = "0";
        }
        message.setField(new Price(Double.parseDouble(price)));
        message.setField(new StringField(FieldType.Volume_Field,volume));

        if(price.equals("0")){
            priceType = "0";//意向
        }else{
            priceType = "2";//#收益率
        }

        message.setField(new StringField(FieldType.PriceType_Field,priceType));
        if(dealStatus.equals("-1")){
            return;//-1代表无效
        }

        if(status.equals("2") || !dealStatus.equals("0")){ //撤销状态
            message.setField(new StringField(FieldType.DelStatus_Field,"1"));
        }else{
            message.setField(new StringField(FieldType.DelStatus_Field,"0"));
        }

        message.setField(new StringField(FieldType.ExceriseFlag_Field,exceriseFlag));
        message.setField(new SecurityType("TPBBO"));

        sendFixMessage(message,targetList,shortName);
    }

    private void sendFixMessage(Quote message,List<String> targetList,String shortName){
        targetList.forEach((target) -> {
            message.getHeader().setField(new TargetCompID(target));

            if(fixSessions.getConvertInGbkList().contains(shortName)){
                try {
                    message.setField(new Symbol(new String(shortName.getBytes("UTF-8"),"GBK")));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("send to target fail when convert to GBK from UTF-8. msg:" + e.getMessage());
                }
            }

            try {
                Session.sendToTarget(message);
            } catch (SessionNotFound sessionNotFound) {
                LOGGER.error("send SendBestQuoteQB quickfix message fail. msg:" + sessionNotFound.getMessage());
            }
        });
    }
}
