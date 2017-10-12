package com.sumscope.cdhplus.realtime.quickfixj.callback;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.BrokerType;
import com.sumscope.cdhplus.realtime.quickfixj.model.TradeBondDataDetail;
import com.sumscope.cdhplus.realtime.quickfixj.mqsender.TradeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liu.yang on 2017/8/18.
 */
@Component
public class TradeCallback extends AbstractReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeCallback.class);

    @Autowired
    private TradeSender tradeSender;
    @Autowired
    private FixSessions fixSessions;
    @Override
    public boolean processString(String message) {
        LOGGER.info("receive message from [cdh.realtime.bond.trade_filter.fanout].");

        List<TradeBondDataDetail> tradeBondDataDetails;
        try {
            tradeBondDataDetails = JSON.parseArray(message, TradeBondDataDetail.class);
        } catch (Exception e) {
            LOGGER.error("[cdh.realtime.bond.trade_filter.fanout] message convert fail. msg : " + e.getMessage());
            LOGGER.error("the message => " + message);
            return true;
        }

        tradeBondDataDetails.forEach((msg) -> {
            String brokerId = msg.getBrokerId();
            try {
                switch (brokerId){
                    case BrokerType.TP:{tradeSender.convertData(msg,FixSessions.getTargetList().get("TP_Trade"),"TPB",fixSessions.getCreditDebtMap());break;}
                    case BrokerType.ICAP:{tradeSender.convertData(msg,FixSessions.getTargetList().get("ICAP_Trade"),"ICAPB",null);break;}
                    case BrokerType.CBBJ:{tradeSender.convertData(msg,FixSessions.getTargetList().get("CBBJ_Trade"),"CBBJB",null);break;}
                    case BrokerType.PATR:{tradeSender.convertData(msg,FixSessions.getTargetList().get("PATR_Trade"),"PATRB",null);break;}
                    case BrokerType.TJXT:{tradeSender.convertData(msg,FixSessions.getTargetList().get("TJXT_Trade"),"TJXTB",null);break;}
                    default:{
                        LOGGER.info("no match message fail.");
                        LOGGER.info("the message => " + message);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("catch exception from TradeCallback convert function. msg:" + e.getMessage());
                LOGGER.error("the message => " + msg);
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
