package com.sumscope.cdhplus.realtime.quickfixj.mqclient;

import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.Receiver;
import com.sumscope.cdh.sumscopemq4j.ReceiverFactory;
import com.sumscope.cdhplus.realtime.quickfixj.callback.IRSCallback;
import com.sumscope.cdhplus.realtime.quickfixj.exception.FixException;
import com.sumscope.cdhplus.realtime.quickfixj.util.RabbitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class IRSReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(IRSReceiver.class);

    @Autowired
    private IRSCallback irs_callback;

    @Value("${irs.queue}")
    private String exchangeName;

    @Autowired
    private RabbitUtil rabbitUtil;

    private Receiver receiver = null;

    public void start(){
        CreateOptions createOptions = rabbitUtil.getOptions(exchangeName);
        try {
            receiver = ReceiverFactory.newReceiver(createOptions,irs_callback);
        } catch (Exception e) {
            LOGGER.error(exchangeName + " queue start fail. msg:" + e.getMessage());
            throw new FixException(exchangeName + " queue start fail. msg:" + e.getMessage());
        }
        receiver.receive();
        LOGGER.info(exchangeName + " queue start success.");
    }
    public void stop(){
        if(receiver == null)
            return;
        receiver.stop();
        LOGGER.info(exchangeName + " queue stop success.");
    }
}
