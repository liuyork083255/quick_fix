package com.sumscope.cdhplus.realtime.quickfixj.receiver;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdh.sumscopemq4j.CreateOptions;
import com.sumscope.cdh.sumscopemq4j.SenderFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu.yang on 2017/8/23.
 */
public class Sender {

    private String host = "rabbitmq-cdh.dev.sumscope.com";
    private int port = 5672;

    public com.sumscope.cdh.sumscopemq4j.Sender getOptions(String queueName) throws Exception {
        CreateOptions createOptions = new CreateOptions();
        createOptions.setHost(host);
        createOptions.setPort(port);
        createOptions.setRequestedHeartbeat(5);
        createOptions.setDurable(false);
        createOptions.setSenderType(CreateOptions.SenderType.FANOUT);

        switch (queueName){
            case "cdh.realtime.bond.quote.best.fanout":createOptions.setExchangeName(queueName);break;
            case "bond.best.offer":createOptions.setExchangeName(queueName);break;
            case "irs.market.update":createOptions.setExchangeName(queueName);break;
            case "profparsed":createOptions.setExchangeName(queueName);break;
            case "cdh.realtime.bond.quote_filter.fanout":createOptions.setExchangeName(queueName);break;
            case "cdh.realtime.bond.trade_filter.fanout":createOptions.setExchangeName(queueName);break;
            case "cdh.quickfix.bond.delay.fanout":createOptions.setExchangeName(queueName);break;
            default:
                throw new RuntimeException("no match queue name !");
        }

        return SenderFactory.newSender(createOptions);
    }

    public void sendBest(long time) throws Exception {
        com.sumscope.cdh.sumscopemq4j.Sender sender = getOptions("cdh.realtime.bond.quote.best.fanout");
        List<String> msg = new ArrayList<>();
        msg.add("{\"transType\":\"update\",\"bidOrAsk\":\"bid\",\"locMsgCrtAt\":1505183054233,\"uniDataID\":\"2,CIB,G0001242017FINPBB15,B,21702151\",\"messageType\":\"quote\",\"id\":\"21702151\",\"createTime\":1505183053000,\"modifyTime\":1505183053000,\"bondKey\":\"G0001242017FINPBB15\",\"listedMarket\":\"CIB\",\"code\":\"170215\",\"shortName\":\"17国开15\",\"side\":1,\"brokerId\":\"2\",\"internally\":1,\"status\":1,\"dealStatus\":0,\"bargainFlag\":2,\"relationFlag\":0,\"rebate\":0.0,\"ytm\":4.2575,\"price\":4.2575,\"priceDescription\":\"--(明天 +0)+--(**,明天 +0)\",\"cleanPrice\":99.8561,\"dirtyPrice\":100.0768,\"priceStr\":\"4.2575\",\"volumeStr\":\"--+--\"}");

    }
    public void sendQBBest(long time) throws Exception {
        com.sumscope.cdh.sumscopemq4j.Sender sender = getOptions("bond.best.offer");
        List<String> msg = new ArrayList<>();
        msg.add("");

    }
    public void sendIRS(long time) throws Exception {
        com.sumscope.cdh.sumscopemq4j.Sender sender = getOptions("irs.market.update");
        List<String> msg = new ArrayList<>();

    }
    public void sendProfparsed(long time) throws Exception {
        com.sumscope.cdh.sumscopemq4j.Sender sender = getOptions("profparsed");
        List<String> msg = new ArrayList<>();
        msg.add("");

    }
    public void sendQuote(long time) throws Exception {
        com.sumscope.cdh.sumscopemq4j.Sender sender = getOptions("cdh.realtime.bond.quote_filter.fanout");
        List<String> msg = new ArrayList<>();
        msg.add("[{\"transType\":\"update\",\"bidOrAsk\":\"bid\",\"locMsgCrtAt\":1505183054233,\"messageType\":\"quote\",\"id\":\"21702151\",\"createTime\":1505183053000,\"modifyTime\":1505183053000,\"bondKey\":\"G0001242017FINPBB15\",\"listedMarket\":\"CIB\",\"code\":\"170215\",\"shortName\":\"17国开15\",\"side\":1,\"brokerId\":\"2\",\"internally\":1,\"status\":1,\"dealStatus\":0,\"bargainFlag\":2,\"relationFlag\":0,\"rebate\":0.0,\"ytm\":4.2575,\"price\":4.2575,\"priceDescription\":\"--(明天 +0)+--(**,明天 +0)\",\"cleanPrice\":99.8561,\"dirtyPrice\":100.0768,\"priceStr\":\"4.2575\",\"volumeStr\":\"--+--\"},{\"transType\":\"update\",\"bidOrAsk\":\"ask\",\"locMsgCrtAt\":1505183054233,\"messageType\":\"quote\",\"id\":\"2170215-1\",\"createTime\":1505183053000,\"modifyTime\":1505183053000,\"bondKey\":\"G0001242017FINPBB15\",\"listedMarket\":\"CIB\",\"code\":\"170215\",\"shortName\":\"17国开15\",\"side\":-1,\"brokerId\":\"2\",\"internally\":1,\"status\":1,\"dealStatus\":0,\"bargainFlag\":0,\"relationFlag\":0,\"rebate\":0.0,\"volume\":2000.0,\"ytm\":4.2565,\"price\":4.2565,\"priceDescription\":\"\",\"cleanPrice\":99.8641,\"dirtyPrice\":100.0848,\"priceStr\":\"4.2565\",\"volumeStr\":\"2000\"}]");



        while(true){
            msg.forEach((m) -> {
                try {
                    sendMessage(sender,JSON.toJSONString(JSON.parseArray(m)));
                    Thread.sleep(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public void sendTrade(long time) throws Exception {
        com.sumscope.cdh.sumscopemq4j.Sender sender = getOptions("cdh.realtime.bond.trade_filter.fanout");
        List<String> msg = new ArrayList<>();
        msg.add("");

    }
    public void sendDelay(long time) throws Exception {
        com.sumscope.cdh.sumscopemq4j.Sender sender = getOptions("cdh.quickfix.bond.delay.fanout");
        List<String> msg = new ArrayList<>();
//        msg.add("{\"symbol\":\"15富力债\",\"side\":\"2\",\"iolRefId\":\"8e420f21801b4bc29715f1f557708d20\",\"cdhBboId\":\"19155cf4-d894-4b3c-a0a4-91894cd30f2c\",\"securityID\":\"122395.SH\",\"securityDesc\":\"{'flagBargain': '1'}\",\"iolTransType\":\"C\",\"price\":99.4500,\"securityType\":\"TPB\",\"transactTime\":\"20170830-05:45:18\",\"orderQty\":4000,\"cdhbbomonth\":201708,\"iolId\":\"8e420f21801b4bc29715f1f557708d20\",\"yieldType\":\"NONE\"}");
        msg.add("{\"sCDelStatus\":\"0\",\"sCPriceType\":\"0\",\"side\":\"2\",\"sCListedMarket\":\"SSE\",\"sCExceriseFlag\":\"0\",\"qbBboId\":\"001d8719-7656-4009-b785-219fabd36a98\",\"sCVolume\":\"2000\",\"price\":0.0000,\"sCShorName\":\"14常德源\",\"securityType\":\"TPBBO\",\"sCQuoteTime\":\"2017-08-21 16:30:37\",\"sCBrokerID\":\"1\",\"sCBondCode\":\"124818\"}");

        while(true){

            msg.forEach((m) -> {
                try {
                    sendMessage(sender,JSON.toJSONString(JSON.parseObject(m)));
                    Thread.sleep(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void sendMessage(com.sumscope.cdh.sumscopemq4j.Sender sender, String msg){
            try {
                sender.send(msg);
                System.out.println("send success.");
            } catch (IOException e) {
                System.out.println("send message fail .");
            }

    }


    @Test
    public void start() throws Exception {
//        sendBest(1000*1100);
//        sendQBBest(1000*1001);
//        sendIRS(1000*1100);
//        sendProfparsed(1000*1100);
        sendQuote(1000*1100);
//        sendTrade(1000*1100);
//        sendDelay(1000*100);
    }



    public static void main(String[] args) throws Exception {
        CreateOptions createOptions = new CreateOptions();
        createOptions.setHost("rabbitmq-cdh.dev.sumscope.com");
        createOptions.setPort(5672);
        createOptions.setRequestedHeartbeat(5);
        createOptions.setDurable(false);
        createOptions.setSenderType(CreateOptions.SenderType.FANOUT);
        createOptions.setExchangeName("cdh.quickfix.bond.delay.fanout");
//        createOptions.setExchangeName("irs.market.update");


        com.sumscope.cdh.sumscopemq4j.Sender sender = SenderFactory.newSender(createOptions);

        while (true){
//            String msg = "{\"symbol\":\"17附息国债07\",\"side\":\"Y\",\"settlDate\":\"\",\"securityID\":\"170007.IB\",\"dealStatus\":\"1\",\"instrmtAssignmentMethod\":\"N\",\"quoteType\":\"1\",\"quoteID\":\"ICAP170007114058\",\"quoteReqID\":\"ICAP170007114058\",\"qbTradeId\":\"ff851bf5-e460-4771-8e75-8d030820e462\",\"securityType\":\"ICAPB\",\"yield\":3.6450,\"transactTime\":\"20170822-02:47:42\",\"orderQty\":0,\"strikePrice\":97.8242}";
//            String msg1 = "{\"sCDelStatus\":\"0\",\"sCPriceType\":\"0\",\"side\":\"2\",\"sCListedMarket\":\"SSE\",\"sCExceriseFlag\":\"0\",\"qbBboId\":\"001d8719-7656-4009-b785-219fabd36a98\",\"sCVolume\":\"2000\",\"price\":0.0000,\"sCShorName\":\"14常德源\",\"securityType\":\"TPBBO\",\"sCQuoteTime\":\"2017-08-21 16:30:37\",\"sCBrokerID\":\"1\",\"sCBondCode\":\"124818\"}";                                                //NEXTREFUND
            String msg2 = "{'iolRefId': '0b237e9b3e7d40ffb66d723214a3ed78', 'cdhBboId': '179e0cd7-b510-475a-aac3-36832e5b5451', 'iolTransType': 'N', 'iolId': '0b237e9b3e7d40ffb66d723214a3ed78', 'text': '\\u884c\\u6743', 'symbol': '16\\u676d\\u6c7d01', 'cdhbbomonth': 201708, 'orderQty': 0, 'securityType': 'TPB', 'price': 99.5422, 'securityDesc': \"{'flagBargain': '1'}\", 'securityID': '122451.SH', 'Type': 'NONE', 'transactTime': '20170830-07:46:03', 'side': '1'}";
            String msg3 = "{\"symbol\":\"15富力债\",\"side\":\"2\",\"iolRefId\":\"8e420f21801b4bc29715f1f557708d20\",\"cdhBboId\":\"19155cf4-d894-4b3c-a0a4-91894cd30f2c\",\"securityID\":\"122395.SH\",\"securityDesc\":\"{'flagBargain': '1'}\",\"iolTransType\":\"C\",\"price\":99.4500,\"securityType\":\"TPB\",\"transactTime\":\"20170830-05:45:18\",\"orderQty\":4000,\"cdhbbomonth\":201708,\"iolId\":\"8e420f21801b4bc29715f1f557708d20\",\"yieldType\":\"NONE\"}";
//            sender.send(JSON.toJSONString(JSON.parseArray(msg)));
//            sender.send(JSON.toJSONString(JSON.parseObject(msg)));
            sender.send(JSON.toJSONString(JSON.parseObject(msg3)));
            System.out.println("success");
            Thread.sleep(10000);
            break;
        }
    }


}
