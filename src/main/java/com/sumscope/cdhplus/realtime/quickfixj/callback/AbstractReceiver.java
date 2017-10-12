package com.sumscope.cdhplus.realtime.quickfixj.callback;

import com.rabbitmq.client.AMQP;
import com.sumscope.cdh.sumscopemq4j.MqReceiverCallback;

/**
 * Created by liu.yang on 2017/8/18.
 */
public abstract class AbstractReceiver implements MqReceiverCallback{
    @Override
    public boolean processString(String message, AMQP.BasicProperties basicProperty) {
        return false;
    }

    @Override
    public boolean processBytes(byte[] message, AMQP.BasicProperties basicProperty) {
        return false;
    }
}
