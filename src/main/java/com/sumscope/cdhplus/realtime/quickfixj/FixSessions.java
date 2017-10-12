package com.sumscope.cdhplus.realtime.quickfixj;

import com.sumscope.cdhplus.realtime.quickfixj.model.MMUserInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import quickfix.SessionSettings;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liu.yang on 2017/8/22.
 */

@Component
public class FixSessions {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixSessions.class);

    @Value("${credit.debt.list}")
    private String creditListStr;

    @Value("${convert.user.ingbk}")
    private String convertInGbkStr;

    @Value("${quickfix.server.port}")
    private String quickfixPort;

    private static final SessionSettings settings = new SessionSettings();

    private static final HashMap<String, List<String>> targetList = new HashMap<String, List<String>>();

    private static final HashMap<String, MMUserInfoModel> mmUserInfo = new HashMap<String, MMUserInfoModel>();

    private Map<String,List<String>> creditDebtMap = new HashMap<>();
    private List<String> convertInGbkList;

    private FixSessions() {}

    @PostConstruct
    public void init() {

        initQuickFixServer();

        String[] lines = creditListStr.split("\\|");

        if(lines.length > 0){
            for(int i=0;i<lines.length;i++){
                String user = lines[i].split(":")[0];
                List<String> types = Arrays.asList(lines[i].split(":")[1].split(","));
                creditDebtMap.put(user,types);
            }
        }

        convertInGbkList = Arrays.asList(convertInGbkStr.split(","));
        LOGGER.info("creditDebeList and convertInGBK success.");
    }

    private void initQuickFixServer(){
        settings.setString("FileStorePath", "store");
        settings.setString("FileLogPath", "log");
        settings.setString("ConnectionType", "acceptor");
        settings.setString("ReconnectInterval", "60");
        settings.setString("SocketAcceptPort", quickfixPort);
        settings.setString("StartTime", "00:00:00");
        settings.setString("EndTime", "00:00:00");
    }

    public static HashMap<String, List<String>> getTargetList() {
        return targetList;
    }

    public static SessionSettings getSessionSettings() {
        return settings;
    }

    public Map<String, List<String>> getCreditDebtMap() {
        return creditDebtMap;
    }

    public List<String> getConvertInGbkList() {
        return this.convertInGbkList;
    }

    public static HashMap<String, MMUserInfoModel> getMmUserInfo() {
        return mmUserInfo;
    }
}
