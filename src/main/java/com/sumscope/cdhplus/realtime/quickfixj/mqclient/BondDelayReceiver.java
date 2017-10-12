package com.sumscope.cdhplus.realtime.quickfixj.mqclient;

import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.Receiver;
import com.sumscope.cdh.sumscopemq4j.ReceiverFactory;
import com.sumscope.cdhplus.realtime.quickfixj.callback.BondDelayCallback;
import com.sumscope.cdhplus.realtime.quickfixj.exception.FixException;
import com.sumscope.cdhplus.realtime.quickfixj.util.RabbitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by liu.yang on 2017/8/24.
 */
@Component
public class BondDelayReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BondDelayReceiver.class);

    @Autowired
    private BondDelayCallback bond_Delay_Callback;

    @Value("${bond.delay.queue}")
    private String exchangeName;

    @Autowired
    private RabbitUtil rabbitUtil;

    private Receiver receiver = null;

    public void start(){
        CreateOptions createOptions = rabbitUtil.getOptions(exchangeName);
        try {
            receiver = ReceiverFactory.newReceiver(createOptions,bond_Delay_Callback);
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
