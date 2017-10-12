package com.sumscope.cdhplus.realtime.quickfixj.callback;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.BestQuoteQBModel;
import com.sumscope.cdhplus.realtime.quickfixj.mqsender.BestQuoteQBSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class BestQuoteQBCallback extends AbstractReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BestQuoteQBCallback.class);

    @Autowired
    private FixSessions fixSession;
    @Autowired
    private BestQuoteQBSender bestQuoteQBSender;

    @Override
    public boolean processString(String message) {
        LOGGER.info("receive message from [bond.best.offer].");

        int start = message.indexOf(":[{");
        int end = message.indexOf("}]}");

        List<BestQuoteQBModel> BestQuoteQBModels = null;
        try {
            message = message.substring(start +1,end + 2);
            BestQuoteQBModels = JSON.parseArray(message, BestQuoteQBModel.class);
        } catch (Exception e) {
            LOGGER.error("[bond.best.offer] message convert fail. msg : " + e.getMessage());
            LOGGER.error("the message => " + message);
            return true;
        }

        BestQuoteQBModels.forEach((msg) ->{
            if(msg.getCid().equals("1")){
                try {
                    bestQuoteQBSender.convertData(msg,fixSession.getTargetList().get("TP_Best_Quote_QB"));
                } catch (Exception e) {
                    LOGGER.error("catch exception from Best_Quote_QB convert function. msg:" + e.getMessage());
                    LOGGER.error("the message => " + msg);
                }
            }else{
                LOGGER.info("catch [bond.best.offer] no match.");
                LOGGER.info("the message => " + JSON.toJSONString(msg));
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
