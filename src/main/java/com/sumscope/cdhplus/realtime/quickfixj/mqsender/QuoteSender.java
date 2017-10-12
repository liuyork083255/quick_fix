package com.sumscope.cdhplus.realtime.quickfixj.mqsender;

import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.model.FieldType;
import com.sumscope.cdhplus.realtime.quickfixj.model.QuoteBondDataDetail;
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
import quickfix.fix50sp2.IOI;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class QuoteSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteSender.class);

    @Autowired
    private FixSessions fixSessions;
    @Autowired
    private Util util;
    @Autowired
    private CreditDebtUtil creditDebtUtil;

    public void convertData(QuoteBondDataDetail msg, List<String> targetList, String securityType, Map<String,List<String>> creditdebtMap){
        String IOIId = msg.getId();
        long quoteTime = msg.getCreateTime();
        String side = msg.getSide()!=1 ? "2":"1";
        String IOITransType = msg.getTransType()==null?"":msg.getTransType();
        String shortName = msg.getShortName()==null?"":msg.getShortName();
        String securityID = msg.getCode()==null?"":msg.getCode();
        String listedMarket = msg.getListedMarket()==null?"":msg.getListedMarket();
        int priceType = 0;
        priceType = msg.getQuoteType()==null?-1:Integer.parseInt(msg.getQuoteType());

        TransactTime myTransactTime = null;
        int IOIQty = msg.getVolume()!=null?msg.getVolume().intValue():0;
        double price = msg.getPrice()==null ? 0.0 : msg.getPrice();
        String flagBargain = msg.getBargainFlag()==0?"0":Byte.toString(msg.getBargainFlag());
        String remarks = "{\"flagBargain\":"+flagBargain+"}";
        double cleanPrice = msg.getCleanPrice()==null ? 0.0 : msg.getCleanPrice();
        int ifExercise = msg.getIsExercise();
        String priceDesc = msg.getPriceDescription()==null?"":msg.getPriceDescription();

        IOITransType = util.getMethod(IOITransType);

        String yieldType = "";
        if(securityType.equals("TPB")){
            yieldType = getYieldType(priceType,ifExercise,price);
            if(yieldType.equals("")){
                System.out.println("priceType field is not found");
                return;
            }
        }else{
            yieldType = getYieldType(3,ifExercise,price);
        }

        securityID = util.getSecurityID(securityID,listedMarket);

        String _quoteTime = null;
        if(quoteTime != 0){
            _quoteTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(quoteTime));
            myTransactTime = new TransactTime();
            myTransactTime.setValue(new Date(quoteTime));
        }

        IOI message = new IOI();
        message.getHeader().setField(new SenderCompID("SUMSCOPE"));
        message.setField(new Symbol(shortName));
        message.setField(new IOIID(IOIId));
        message.setField(new IOITransType(IOITransType.charAt(0)));
        message.setField(new Side(side.charAt(0)));
        message.setField(new IOIQty("U"));
        message.setField(new Price(cleanPrice));
        message.setField(new OrderQty(IOIQty));
        message.setField(new IOIRefID(IOIId));
        message.setField(new SecurityID(securityID));
        message.setField(new SecurityType(securityType));
        message.setField(new SecurityDesc(remarks));

        if(!yieldType.equals("NONE")){
            message.setField(new Yield(price));
        }
        if(!priceDesc.equals("")){
            message.setField(new Text(priceDesc));
        }
        message.setField(new YieldType(yieldType));

        if(_quoteTime != null){
            message.setField(myTransactTime);
        }

        sendFixMessage(message,targetList,shortName);

        /**
         * 信用债报价数据
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

        IOI messageCreditDebt = new IOI();
        messageCreditDebt.getHeader().setField(new SenderCompID("SUMSCOPE"));
        messageCreditDebt.setField(new Symbol(msg.getShortName()));
        messageCreditDebt.setField(new SecurityID(securityID));
        messageCreditDebt.setField(new Side(side.charAt(0)));

        if(_quoteTime != null){
            messageCreditDebt.setField(myTransactTime);
            String operationDate = _quoteTime.split(" ")[0];
            String operationTime = _quoteTime.split(" ")[1];
            messageCreditDebt.setField(new StringField(FieldType.OperationTime_Field,operationTime));
            messageCreditDebt.setField(new StringField(FieldType.OperationDate_Field,operationDate));
        }

        if(yieldType.equals("NONE")){
            messageCreditDebt.setField(new Yield(price));

            if(cbValue != null){
                Double cbvaluediff = (price - Double.parseDouble(cbValue)) * 100;
                messageCreditDebt.setField(new StringField(FieldType.VALUATIONDEVIATION_FIELD,cbvaluediff.toString()));
            }
        }
        messageCreditDebt.setField(new OrderQty(IOIQty));
        messageCreditDebt.setField(new StringField(FieldType.REMAININGDATE_FIELD, rMaturity));
        messageCreditDebt.setField(new StringField(FieldType.CREDITDEBTTYPE_FIELD, bondType));
        messageCreditDebt.setField(new StringField(FieldType.DEBTVALUATION_FIELD, cbValue));
        messageCreditDebt.setField(new SecurityType("TPBCD"));

        sendFixMessage(messageCreditDebt,cdSendList,shortName);

    }

    private void sendFixMessage(IOI message,List<String> targetList,String shortName){
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
                LOGGER.error("send SendQuote quickfix message fail. msg:" + sessionNotFound.getMessage());
            }
        });
    }


    private String getYieldType(int priceType,int ifExercise,double price){

        String yieldType = "";

        if(priceType == -1)
            return "NONE";

        if(priceType == 3 || priceType == 0){
            if(ifExercise==-1)
                return "";
            else if(ifExercise == 0)
                yieldType = "NEXTREFUND";
            else if(ifExercise == 1)
                yieldType =  "MATURITY";
        }else if(priceType ==4){
            yieldType = "CHANGE";
        }else{
            yieldType = "NONE";
        }

        if(price == 0.0)
            yieldType = "NOPRICE";

        return yieldType;
    }

}
