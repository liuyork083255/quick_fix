package com.sumscope.cdhplus.realtime.quickfixj.callback;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.IRSModel;
import com.sumscope.cdhplus.realtime.quickfixj.mqsender.IrsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class IRSCallback extends AbstractReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(IRSCallback.class);
    @Autowired
    private IrsSender irsSender;
    @Autowired
    private FixSessions fixSessions;

    @Override
    public boolean processString(String message) {
        LOGGER.info("receive message from [irs.market.update].");
        List<IRSModel> irsModel;
        try {
            irsModel = JSON.parseArray(message, IRSModel.class);
        } catch (Exception e) {
            LOGGER.error("[irs.market.update] message convert fail. msg : " + e.getMessage());
            LOGGER.error("the message => " + message);
            return true;
        }
        irsModel.forEach((msg) ->{
            try {
                irsSender.convertData(msg,fixSessions.getTargetList().get("IRS_Quote"));
            } catch (Exception e) {
                LOGGER.error("catch exception from IRS convert function. msg:" + e.getMessage());
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
