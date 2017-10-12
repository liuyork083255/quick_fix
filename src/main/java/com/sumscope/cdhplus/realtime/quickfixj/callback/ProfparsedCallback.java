package com.sumscope.cdhplus.realtime.quickfixj.callback;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.ProfparsedModel;
import com.sumscope.cdhplus.realtime.quickfixj.mqsender.ProfParsedSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class ProfparsedCallback extends AbstractReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfparsedCallback.class);

    @Autowired
    private ProfParsedSender profParsedSender;
    @Autowired
    private FixSessions fixSessions;
    @Override
    public boolean processString(String message) {
        LOGGER.info("receive message from [profparsed].");

        ProfparsedModel parse;
        try {
            parse = JSON.parseObject(message, ProfparsedModel.class);
        } catch (Exception e) {
            LOGGER.error("[profparsed] message convert fail. msg : " + e.getMessage());
            LOGGER.error("the message => " + message);
            return true;
        }

        try {
            profParsedSender.convertData(parse,fixSessions.getTargetList().get("MM_OnLine_Cash"));
        } catch (Exception e) {
            LOGGER.error("catch exception from Profparsed convert function. msg:" + e.getMessage());
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
