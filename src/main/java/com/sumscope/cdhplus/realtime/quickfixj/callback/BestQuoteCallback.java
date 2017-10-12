package com.sumscope.cdhplus.realtime.quickfixj.callback;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.BrokerType;
import com.sumscope.cdhplus.realtime.quickfixj.model.QuoteBondDataDetail;
import com.sumscope.cdhplus.realtime.quickfixj.mqsender.QuoteSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liu.yang on 2017/8/18.
 */
@Component
public class BestQuoteCallback extends AbstractReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BestQuoteCallback.class);

    @Autowired
    private QuoteSender quoteSender;
    @Autowired
    private FixSessions fixSession;

    @Override
    public boolean processString(String message) {
        LOGGER.info("receive message from [cdh.realtime.bond.quote.best.fanout].");

        QuoteBondDataDetail quote = null;
        try {
            quote = JSON.parseObject(message,QuoteBondDataDetail.class);
        } catch (Exception e) {
            LOGGER.error("[cdh.realtime.bond.quote.best.fanout] message convert fail. msg : " + e.getMessage());
            LOGGER.error("the message => " + message);
            return true;
        }
        String brokerId = quote.getBrokerId();
        try {
            switch (brokerId){
                case BrokerType.TP:{quoteSender.convertData(quote, fixSession.getTargetList().get("TP_Quote"),"TPBBO",null);break;}
                case BrokerType.ICAP:{quoteSender.convertData(quote, fixSession.getTargetList().get("ICAP_Quote"),"ICAPBBO",null);break;}
                case BrokerType.CBBJ:{quoteSender.convertData(quote, fixSession.getTargetList().get("CBBJ_Quote"),"BGCBBO",null);break;}
                case BrokerType.PATR:{quoteSender.convertData(quote, fixSession.getTargetList().get("PATR_Quote"),"PATRBBO",null);break;}
                case BrokerType.TJXT:{quoteSender.convertData(quote, fixSession.getTargetList().get("TJXT_Quote"),"TJXTBBO",null);break;}
                default:{
                    LOGGER.error("catch [cdh.realtime.bond.quote.best.fanout] no match.");
                    LOGGER.error("the message => " + message);
                }
            }
        } catch (Exception e) {
            LOGGER.error("catch exception from BestQuoteCallback convert function. msg:" + e.getMessage());
            LOGGER.error("the message => " + message);
        }
        return true;
    }

    @Override
    public boolean processBytes(byte[] message) {
        processString(new String(message));
        return true;
    }

}
