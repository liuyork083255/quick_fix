package com.sumscope.cdhplus.realtime.quickfixj.mqsender;

import com.sumscope.cdhplus.realtime.quickfixj.model.DelayBBOModel;
import com.sumscope.cdhplus.realtime.quickfixj.model.DelayTradeModel;
import com.sumscope.cdhplus.realtime.quickfixj.model.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.*;
import quickfix.fix50sp2.Quote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liu.yang on 2017/8/24.
 */
@Component
public class DelaySender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelaySender.class);

    public void convertBBO(DelayBBOModel msg,List<String> targetList){
        Quote message = new Quote();
        message.getHeader().setField(new SenderCompID("SUMSCOPE"));
        message.setField(new StringField(FieldType.DelStatus_Field,msg.getsCDelStatus()==null?"":msg.getsCDelStatus()));
        message.setField(new StringField(FieldType.PriceType_Field,msg.getsCPriceType()==null?"":msg.getsCPriceType()));
        message.setField(new Side(msg.getSide()==null ? "".charAt(0):msg.getSide().charAt(0)));
        message.setField(new StringField(FieldType.ListedMarket_Field,msg.getsCListedMarket()==null?"":msg.getsCListedMarket()));
        message.setField(new StringField(FieldType.ExceriseFlag_Field,msg.getsCExceriseFlag()==null?"":msg.getsCExceriseFlag()));
        message.setField(new StringField(FieldType.Volume_Field,msg.getsCVolume()==null?"":msg.getsCVolume()));
        message.setField(new Price(msg.getPrice().doubleValue()));
        message.setField(new StringField(FieldType.ShorName_Field,msg.getsCShorName()==null?"":msg.getsCShorName()));
        message.setField(new SecurityType(msg.getSecurityType()));
        message.setField(new StringField(FieldType.QuoteTime_Field,msg.getsCQuoteTime()==null?"":msg.getsCQuoteTime()));
        message.setField(new StringField(FieldType.BrokerID_Field,msg.getsCBrokerID()==null?"":msg.getsCBrokerID()));
        message.setField(new StringField(FieldType.BondCode_Field,msg.getsCBondCode()==null?"":msg.getsCBondCode()));

        targetList.forEach((target) -> {
            message.getHeader().setField(new TargetCompID(target));
            try {
                Session.sendToTarget(message);
            } catch (SessionNotFound sessionNotFound) {
                LOGGER.error("send delay BBO quickfix message fail. msg:" + sessionNotFound.getMessage());
            }
        });
    }
    public void convertTrade(DelayTradeModel msg, List<String> targetList){
        Quote message = new Quote();
        message.getHeader().setField(new SenderCompID("SUMSCOPE"));

        message.setField(new Symbol(msg.getSymbol()==null?"":msg.getSymbol()));
        message.setField(new Side(msg.getSide()==null?"".charAt(0):msg.getSide().charAt(0)));
        message.setField(new SettlDate(msg.getSettlDate()==null?"":msg.getSettlDate()));
        message.setField(new SecurityType(msg.getSecurityType()==null?"":msg.getSecurityType()));
        message.setField(new StringField(FieldType.DEALSTATUS_FIELD,msg.getDealStatus()==null?"":msg.getDealStatus()));
        message.setField(new InstrmtAssignmentMethod(msg.getInstrmtAssignmentMethod()==null?"".charAt(0):msg.getInstrmtAssignmentMethod().charAt(0)));
        message.setField(new QuoteType(Integer.parseInt(msg.getQuoteType())));
        message.setField(new QuoteID(msg.getQuoteID()==null?"":msg.getQuoteID()));
        message.setField(new QuoteReqID(msg.getQuoteReqID()==null?"":msg.getQuoteReqID()));
        message.setField(new SecurityType(msg.getSecurityType()==null?"":msg.getSecurityType()));
        message.setField(new Yield(msg.getYield()==null?0.0:msg.getYield().doubleValue()));

        Date parse;
        if(msg.getTransactTime()==null){
            parse = new Date();
        }else{
            try {
                parse = new SimpleDateFormat("yyyyMMdd-HH:mm:ss").parse(msg.getTransactTime());
            } catch (ParseException e) {
                parse = new Date();
            }
        }

        message.setField(new TransactTime(parse));

        message.setField(new OrderQty(msg.getOrderQty()==null?0:msg.getOrderQty()));
        message.setField(new StrikePrice(msg.getStrikePrice()==null?0:msg.getStrikePrice().doubleValue()));

        targetList.forEach((target) -> {
            message.getHeader().setField(new TargetCompID(target));
            try {
                Session.sendToTarget(message);
            } catch (SessionNotFound sessionNotFound) {
                LOGGER.error("send delay Trade quickfix message fail. msg:" + sessionNotFound.getMessage());
            }
        });


    }
}
