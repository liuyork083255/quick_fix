package com.sumscope.cdhplus.realtime.quickfixj.util;

import com.sumscope.cdhplus.realtime.quickfixj.model.FieldType;
import com.sumscope.cdhplus.realtime.quickfixj.model.QuoteBondDataDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.StringField;
import quickfix.field.TransactTime;
import quickfix.fix50sp2.IOI;
import quickfix.fix50sp2.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liu.yang on 2017/8/22.
 */
@Component
public class Util {
    @Autowired
    private CreditDebtUtil creditDebtUtil;

    public String getMethod(String IOITransType){
        if(IOITransType.equals("insert"))
            return "N";
        else if(IOITransType.equals("update"))
            return "R";
        else
            return "C";
    }

    public String getSecurityID(String securityID,String listedMarket){
        if(securityID.indexOf(".") == -1){
            if(listedMarket.equals("CIB"))
                securityID = securityID + ".IB";
            else if(listedMarket.equals("SSE"))
                securityID = securityID + ".SH";
            else if(listedMarket.equals("SZE"))
                securityID = securityID + ".SZ";
        }
        return securityID;
    }


    public List<String> getCreditDebtSendList(String bondId, Map<String,List<String>> creditdebtMap){

        if(!creditDebtUtil.checkIsCreditDebt(bondId)) return null;

        List<String> sendList = new ArrayList<>();
        String creditDebtType = creditDebtUtil.getCreditDebtType(bondId);

        creditdebtMap.forEach((user,types) -> {
            if(types.contains(creditDebtType)){
                sendList.add(user);
            }
        });
        return sendList;
    }

    public void setTransactTime(Object obj,String _TransactTime, TransactTime myTransactTime,String type){
//        if(type.equals("trade")){
//            Quote messageCreditDebt = (Quote)obj;
//
//        }else{
//            IOI messageCreditDebt = (IOI)obj;
//        }
//        messageCreditDebt.setField(myTransactTime);
//        String operationDate = _TransactTime.split(" ")[0];
//        String operationTime = _TransactTime.split(" ")[1];
//        messageCreditDebt.setField(new StringField(FieldType.OperationTime_Field,operationTime));
//        messageCreditDebt.setField(new StringField(FieldType.OperationDate_Field,operationDate));

    }


}
