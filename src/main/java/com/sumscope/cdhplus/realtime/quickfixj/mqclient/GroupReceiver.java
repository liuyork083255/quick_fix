package com.sumscope.cdhplus.realtime.quickfixj.mqclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class GroupReceiver {
    @Autowired
    private BestQuoteQBReceiver bestQuoteQBReceiver;
    @Autowired
    private BestQuoteReceiver bestQuoteReceiver;
    @Autowired
    private IRSReceiver irsReceiver;
    @Autowired
    private ProfparsedReceiver profparsedReceiver;
    @Autowired
    private QuoteReceiver quoteReceiver;
    @Autowired
    private TradeReceiver tradeReceiver;
    @Autowired
    private BondDelayReceiver bondDelayReceiver;

    public void startAllReceiver(){
        bestQuoteQBReceiver.start();
        bestQuoteReceiver.start();
        irsReceiver.start();
        profparsedReceiver.start();
        quoteReceiver.start();
        tradeReceiver.start();
        bondDelayReceiver.start();
    }

    public void stopAllReceiver(){
        bestQuoteQBReceiver.stop();
        bestQuoteReceiver.stop();
        irsReceiver.stop();
        profparsedReceiver.stop();
        quoteReceiver.stop();
        tradeReceiver.stop();
        bondDelayReceiver.stop();
    }
}
