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

import java.util.List;

/**
 * Created by liu.yang on 2017/8/18.
 */
@Component
public class QuoteCallback extends AbstractReceiver  {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteCallback.class);

    @Autowired
    private QuoteSender quoteSender;
    @Autowired
    private FixSessions fixSessions;

    @Override
    public boolean processString(String message) {
        LOGGER.info("receive message from [cdh.realtime.bond.quote_filter.fanout].");

        List<QuoteBondDataDetail> quoteBondDataDetails;
        try {
            quoteBondDataDetails = JSON.parseArray(message, QuoteBondDataDetail.class);
        } catch (Exception e) {
            LOGGER.error("[irs.market.update] message convert fail. msg:"+ e.getMessage());
            LOGGER.error("the message => " + message);
            return true;
        }
        quoteBondDataDetails.forEach((quote) -> {
            String brokerId = quote.getBrokerId();
            try {
                switch (brokerId){
                    case BrokerType.TP:{quoteSender.convertData(quote, fixSessions.getTargetList().get("TP_Best_Quote"),"TPB",fixSessions.getCreditDebtMap());break;}
                    case BrokerType.ICAP:{quoteSender.convertData(quote, fixSessions.getTargetList().get("ICAP_Best_Quote"),"ICAPB",null);break;}
                    case BrokerType.CBBJ:{quoteSender.convertData(quote, fixSessions.getTargetList().get("CBBJ_Best_Quote"),"CBBJB",null);break;}
                    case BrokerType.PATR:{quoteSender.convertData(quote, fixSessions.getTargetList().get("PATR_Best_Quote"),"PATRB",null);break;}
                    case BrokerType.TJXT:{quoteSender.convertData(quote, fixSessions.getTargetList().get("TJXT_Best_Quote"),"TJXTB",null);break;}
                    default:{
                        LOGGER.info("no match message fail.");
                        LOGGER.info("the message => " + message);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("catch exception from QuoteCallback convert function. msg:" + e.getMessage());
                LOGGER.error("the message => " + message);
            }
        });
        return true;
    }

    @Override
    public boolean processBytes(byte[] message) {
        processString(new String(message));
        return true;
    }
}
