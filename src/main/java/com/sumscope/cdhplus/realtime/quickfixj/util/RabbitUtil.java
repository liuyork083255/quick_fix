package com.sumscope.cdhplus.realtime.quickfixj.util;

import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class RabbitUtil {
    @Value("${application.rabbitmq.host}")
    private String host;
    @Value("${application.rabbitmq.port}")
    private String port;
    public CreateOptions getOptions(String exchangeName){
        CreateOptions createOptions = new CreateOptions();
        createOptions.setHost(host);
        createOptions.setPort(Integer.parseInt(port));
        createOptions.setRequestedHeartbeat(5);
        createOptions.setDurable(false);
        createOptions.setExchangeName(exchangeName);
        createOptions.setSenderType(CreateOptions.SenderType.FANOUT);
        return createOptions;
    }
}
