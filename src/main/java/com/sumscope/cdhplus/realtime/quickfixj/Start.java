package com.sumscope.cdhplus.realtime.quickfixj;

import com.sumscope.cdhplus.realtime.quickfixj.mqclient.GroupReceiver;
import com.sumscope.cdhplus.realtime.quickfixj.util.CreditDebtUtil;
import com.sumscope.cdhplus.realtime.quickfixj.util.InitFixSessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Created by liu.yang on 2017/8/22.
 */
@Component
public class Start {
    @Autowired
    private InitFixSessions initFixSessions;
    @Autowired
    private GroupReceiver groupReceiver;
    @Autowired
    private CreditDebtUtil creditDebtUtil;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(QuickfixjApplication.class,args);
        Start bean = context.getBean(Start.class);
        bean.start();
    }

    private void start() {
        creditDebtUtil.initCreditDebt();
        initFixSessions.startQuickfix();
        groupReceiver.startAllReceiver();
    }

    @PreDestroy
    private void close() {
        initFixSessions.close();
        groupReceiver.stopAllReceiver();
    }
}
