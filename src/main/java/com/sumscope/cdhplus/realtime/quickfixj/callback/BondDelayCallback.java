package com.sumscope.cdhplus.realtime.quickfixj.callback;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.DelayBBOModel;
import com.sumscope.cdhplus.realtime.quickfixj.model.DelayTradeModel;
import com.sumscope.cdhplus.realtime.quickfixj.mqsender.DelaySender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liu.yang on 2017/8/24.
 */
@Component
public class BondDelayCallback extends AbstractReceiver{

    private static final Logger LOGGER = LoggerFactory.getLogger(BondDelayCallback.class);

    @Autowired
    private DelaySender delaySender;
    @Autowired
    private FixSessions fixSessions;

    @Override
    public boolean processString(String message) {
        LOGGER.info("receive message from [cdh.quickfix.bond.delay.fanout].");

        if(message.contains("qbBboId")){
            DelayBBOModel delayBBOModel;
            try {
                delayBBOModel = JSON.parseObject(message, DelayBBOModel.class);
            } catch (Exception e) {
                LOGGER.error("[cdh.quickfix.bond.delay.fanout] qbBboId message convert fail. msg : " + e.getMessage());
                LOGGER.error("the message => " + message);
                return true;
            }

            try {
                delaySender.convertBBO(delayBBOModel,fixSessions.getTargetList().get("BOND_DELAY"));
            } catch (Exception e) {
                LOGGER.error("catch exception from Bond_Delay_qbBboId convert function. msg:" + e.getMessage());
                LOGGER.error("the message => " + message);
            }

        }else if(message.contains("qbTradeId")){
            DelayTradeModel delayTradeModel;
            try {
                delayTradeModel = JSON.parseObject(message, DelayTradeModel.class);
            } catch (Exception e) {
                LOGGER.error("[cdh.quickfix.bond.delay.fanout] qbTradeId message convert fail.");
                LOGGER.error("the message => " + message);
                return true;
            }
            try {
                delaySender.convertTrade(delayTradeModel,fixSessions.getTargetList().get("BOND_DELAY"));
            } catch (Exception e) {
                LOGGER.error("catch exception from Bond_Delay_qbTradeId convert function. msg:" + e.getMessage());
                LOGGER.error("the message => " + message);
            }

        }else{
            LOGGER.error("catch [bond.best.offer] no match.");
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
