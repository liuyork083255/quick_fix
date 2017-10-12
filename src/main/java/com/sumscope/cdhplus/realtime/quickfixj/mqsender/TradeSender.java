package com.sumscope.cdhplus.realtime.quickfixj.mqsender;

import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.FieldType;
import com.sumscope.cdhplus.realtime.quickfixj.model.TradeBondDataDetail;
import com.sumscope.cdhplus.realtime.quickfixj.util.CreditDebtUtil;
import com.sumscope.cdhplus.realtime.quickfixj.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.*;
import quickfix.fix50sp2.Quote;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class TradeSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeSender.class);

    @Autowired
    private FixSessions fixSessions;
    @Autowired
    private Util util;
    @Autowired
    private CreditDebtUtil creditDebtUtil;

    public void convertData(TradeBondDataDetail msg, List<String> targetList, String securityType,Map<String,List<String>> creditdebtMap){
        String quoteID = msg.getId() == null ? "" : msg.getId();
        double price = msg.getPrice() == null ? 0.0 : msg.getPrice();
        String shortName = msg.getShortName() == null ? "" : msg.getShortName();
        int orderQty = msg.getVolume() == null?0:msg.getVolume().intValue();
        String dealType = msg.getDealType() == null ? "" : msg.getDealType();
        String settlDate = msg.getSettlementDate();
        long TransactTime = msg.getCreateTime();
        double cleanPrice= msg.getCleanPrice() == null ? 0.0 : msg.getCleanPrice();
        String method = msg.getTransType() == null ? "" :msg.getTransType();
        String securityID = msg.getCode() == null ? "" : msg.getCode();
        String listedMarket = msg.getListedMarket() == null ? "" : msg.getListedMarket();
        double ytm = msg.getYtm() == null ? 0.0 : msg.getYtm();
        TransactTime myTransactTime = null;
        String dealStatus = String.valueOf(msg.getDealStatus());

        String _TransactTime = null;
        if(TransactTime != 0){
            _TransactTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(TransactTime));

            myTransactTime = new TransactTime();
            myTransactTime.setValue(new Date(TransactTime));
        }

        method = util.getMethod(method);

        String side = "0";
        if(dealType.equals("taken")){
            side = "X";
        }else if(dealType.equals("given")){
            side = "Y";
        }else{
            side = "Z";
        }
        
        if(side.equals("0")){
            LOGGER.info("dealType is error");
            return;
        }

        securityID = util.getSecurityID(securityID,listedMarket);

        if(price == 0.0){
            ytm = 0.0;
        }

        Quote message = new Quote();
        message.getHeader().setField(new SenderCompID("SUMSCOPE"));
        message.setField(new Symbol(shortName));
        message.setField(new OrderQty(orderQty));
        message.setField(new Side(side.charAt(0)));
        message.setField(new QuoteID(quoteID));
        message.setField(new QuoteReqID(quoteID));
        message.setField(new QuoteType(1));//成交
        if(settlDate != null){
            message.setField(new SettlDate(settlDate));
        }else {
            message.setField(new SettlDate(""));
        }

        if(_TransactTime !=null){
            message.setField(myTransactTime);
        }

        message.setField(new StrikePrice(cleanPrice));
        message.setField(new InstrmtAssignmentMethod(method.charAt(0)));
        message.setField(new SecurityID(securityID));
        message.setField(new Yield(ytm));
        message.setField(new SecurityType(securityType));
        message.setField(new StringField(FieldType.DEALSTATUS_FIELD,dealStatus));

        sendFixMessage(message,targetList,shortName);

        /**
         * 信用债成交数据
         */

        if(creditdebtMap == null) return;

        String code = msg.getCode()==null?"":msg.getCode();
        if(code.length() == 0) return;

        List<String> cdSendList = util.getCreditDebtSendList(code, creditdebtMap);
        if(cdSendList == null || cdSendList.size() == 0) return;

        String rMaturity = creditDebtUtil.getRemainingDate(code);
        String bondType = creditDebtUtil.getCreditDebtType(code);
        String cbValue = creditDebtUtil.getDebtValuation(code);

        if(rMaturity == null || bondType == null || cbValue == null) return;

        Quote messageCreditDebt = new Quote();
        messageCreditDebt.getHeader().setField(new SenderCompID("SUMSCOPE"));
        messageCreditDebt.setField(new Symbol(shortName));
        messageCreditDebt.setField(new Side(side.charAt(0)));
        messageCreditDebt.setField(new Yield(ytm));
        messageCreditDebt.setField(new SecurityID(securityID));

        if(ytm != 0 && cbValue!= null){
            Double cbvaluediff = (ytm - Double.parseDouble(cbValue)) * 100;
            messageCreditDebt.setField(new StringField(FieldType.VALUATIONDEVIATION_FIELD,cbvaluediff.toString()));
        }
        messageCreditDebt.setField(new OrderDelay(orderQty));
        if(_TransactTime != null){
            messageCreditDebt.setField(myTransactTime);
            messageCreditDebt.setField(new StringField(FieldType.OperationTime_Field, _TransactTime.split(" ")[1]));
            messageCreditDebt.setField(new StringField(FieldType.OperationDate_Field, _TransactTime.split(" ")[0]));
        }

        messageCreditDebt.setField(new StringField(FieldType.REMAININGDATE_FIELD, rMaturity));
        messageCreditDebt.setField(new StringField(FieldType.CREDITDEBTTYPE_FIELD, bondType));
        messageCreditDebt.setField(new StringField(FieldType.DEBTVALUATION_FIELD, cbValue));
        messageCreditDebt.setField(new SecurityType("TPBCD"));
        messageCreditDebt.setField(new StringField(FieldType.DEALSTATUS_FIELD, dealStatus));

        sendFixMessage(messageCreditDebt,cdSendList,shortName);

    }

    private void sendFixMessage(Quote message,List<String> targetList,String shortName){
        targetList.forEach((target) -> {
            message.getHeader().setField(new TargetCompID(target));

            if(fixSessions.getConvertInGbkList().contains(shortName)){
                try {
                    message.setField(new Symbol(new String(shortName.getBytes("UTF-8"),"GBK")));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("send to target fail when convert to GBK from UTF-8. msg:" + e.getMessage());
                }
            }

            try {
                Session.sendToTarget(message);
            } catch (SessionNotFound sessionNotFound) {
                LOGGER.error("send SendTrade quickfix message fail. msg:" + sessionNotFound.getMessage());
            }
        });
    }
}
