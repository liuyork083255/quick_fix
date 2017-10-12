package com.sumscope.cdhplus.realtime.quickfixj.receiver;

import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.Receiver;
import com.sumscope.cdh.sumscopemq4j.ReceiverFactory;
import com.sumscope.cdhplus.realtime.quickfixj.callback.AbstractReceiver;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by liu.yang on 2017/8/15.
 */
public class RMReceiver {

    public static void main(String[] args) throws IOException, TimeoutException {

        CreateOptions createOptions = new CreateOptions();
        createOptions.setHost("rabbitmq-cdh.qa.sumscope.com");
        createOptions.setPort(5672);
        createOptions.setRequestedHeartbeat(5);
        createOptions.setDurable(false);



//        [{"transType":"insert","bidOrAsk":"ask","locMsgCrtAt":1502938293983,"messageType":"quote","id":"3111798369-1","createTime":1502938290000,"modifyTime":1502938290000,"bo
//        全是报价----- 5 家基本单边报价
//        createOptions.setQueueName("realtime_quote_filter_fanout");
//        createOptions.setExchangeName("cdh.realtime.bond.quote_filter.fanout");

//        [{"transType":"insert","locMsgCrtAt":1502938258055,"messageType":"trade","id":"3ccd8cd6cb5345ad92c2aaba37f6166c","createTime":1502938254000,"dealTime":1502938254000,"bondKey":"X0000172015CORCVB01"
//        全是 成交
//        createOptions.setQueueName("realtime_trade_filter_fanout");
//        createOptions.setExchangeName("cdh.realtime.bond.trade_filter.fanout");


//        createOptions.setQueueName("irs_market_update");
//        createOptions.setExchangeName("irs.market.update");
////
//
//        {"senderuin":"408925069","parsedMsg":{"parser_result":[{"confidence":100,"tenors":[{"days_high":14,"days_low":1}],"pledge_repo":true,"side":0,"bond_types":[0],"id":"19505617837515736"}]},"msgData":"\u501f1-14\u5929\uff0c\u62bc\u5229\u7387","time":1502937811}
//        createOptions.setQueueName("profparsed");
//        createOptions.setExchangeName("profparsed");

//        {"AckMsgHead":{"exectime":0.0,"total":1,"retcode":0,"idx":0},"AckMsgBody":{"BusinessCode":"NCD","IMQ_OUTCOME":[{"fbar":"1","vol":"10000","pri":"4.00","gc":"111696700","id":"2111696700-1","cn":"ICAP","
//        createOptions.setQueueName("bond_best_offer");
//        createOptions.setExchangeName("bond.best.offer");


//        {"transType":"insert","bidOrAsk":"ask","locMsgCrtAt":1502937931010,"uniDataID":"4,CIB,Z0001072017CORCSP14,O,3498498","messageType":"quote","id":"
//        createOptions.setQueueName("webbbo_bond_quote");
//        createOptions.setExchangeName("cdh.realtime.bond.quote.best.fanout");
//

        createOptions.setExchangeName("cdh.quickfix.bond.delay1.fanout");


        createOptions.setSenderType(CreateOptions.SenderType.FANOUT);
        Receiver receiver = ReceiverFactory.newReceiver(createOptions, new AbstractReceiver() {
            @Override
            public boolean processString(String message) {

                System.out.println(message);

//                int start = message.indexOf(":[{");
//                int end = message.indexOf("}]}");
//                System.out.println(message.substring(start +1,end + 2));
                return false;
            }

            @Override
            public boolean processBytes(byte[] message) {
                processString(new String(message));
                return false;
            }
        });
        receiver.receive();

    }
}
