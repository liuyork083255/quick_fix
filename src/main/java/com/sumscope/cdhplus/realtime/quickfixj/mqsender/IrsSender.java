package com.sumscope.cdhplus.realtime.quickfixj.mqsender;

import com.sumscope.cdhplus.realtime.quickfixj.model.FieldType;
import com.sumscope.cdhplus.realtime.quickfixj.model.IRSModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.*;
import quickfix.fix50sp2.Quote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
public class IrsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrsSender.class);

    public void convertData(IRSModel msg, List<String> targetList) throws Exception {
        Float bidPrice = msg.getBid_price()==null?null:Float.parseFloat(msg.getBid_price());
        Float ofrPrice = msg.getOfr_price()==null?null:Float.parseFloat(msg.getOfr_price());
        int bidVolume = msg.getBid_volume()==null?0:Integer.parseInt(msg.getBid_volume());
        int ofrVolume = msg.getOfr_volume()==null?0:Integer.parseInt(msg.getOfr_volume());
        String id = msg.getId()==null?"":msg.getId();
        String createTime = msg.getCreate_time()==null?"":msg.getCreate_time();
        String interestRateType = msg.getType_name()==null?"":msg.getType_name();
        String tradeDeadline = msg.getTerm_name()==null?"":msg.getTerm_name();
        String transactionMode = msg.getGoods_code()==null?"":msg.getGoods_code();

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
//        String ct = new SimpleDateFormat("yyyyMMdd-HH:mm:ss").format(date);
        TransactTime myTransactTime = new TransactTime();
        myTransactTime.setValue(date);


        Quote message = new Quote();
        message.getHeader().setField(new SenderCompID("SUMSCOPE"));
        if(bidPrice!=null) message.setField(new BidPx(bidPrice));
        if(ofrPrice!=null) message.setField(new OfferPx(ofrPrice));

        message.setField(new BidSize(bidVolume));
        message.setField(new OfferSize(ofrVolume));
        message.setField(new QuoteID(id));
        message.setField(new StringField(FieldType.INTERESTRATETYPE_FIELD, interestRateType));
        message.setField(new StringField(FieldType.TRADEDEADLINE_FIELD, tradeDeadline));
        message.setField(new StringField(FieldType.TRANSACTIONMODE_FIELD, transactionMode));
        message.setField(new SecurityType("IRS"));
        message.setField(new QuoteType(0));
        message.setField(myTransactTime);

        targetList.forEach((target) -> {
            message.getHeader().setField(new TargetCompID(target));
            try {
                Session.sendToTarget(message);
            } catch (SessionNotFound sessionNotFound) {
                LOGGER.error("send IRS message fail. msg:"+sessionNotFound.getMessage());
            }

        });

    }
}
