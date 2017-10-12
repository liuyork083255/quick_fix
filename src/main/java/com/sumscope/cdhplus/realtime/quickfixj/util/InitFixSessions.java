package com.sumscope.cdhplus.realtime.quickfixj.util;

import com.alibaba.fastjson.JSON;
import com.sumscope.cdhplus.realtime.quickfixj.FixServerApplication;
import com.sumscope.cdhplus.realtime.quickfixj.FixSessions;
import com.sumscope.cdhplus.realtime.quickfixj.exception.FixException;
import com.sumscope.cdhplus.realtime.quickfixj.model.MMUserInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import quickfix.*;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liu.yang on 2017/8/21.
 */
@Component
@Configuration
//@PropertySource(value = "classpath:application.properties")
public class InitFixSessions {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitFixSessions.class);

    private Connection con;
    private Connection idb_con;
    @Value("${application.mysql.driver}")
    private String driver;
    @Value("${application.mysql.connection}")
    private String url;
    @Value("${application.mysql.username}")
    private String user;
    @Value("${application.mysql.password}")
    private String password;

    @Value("${idb.mysql.center.connection}")
    private String idbCenterUrl;
    @Value("${idb.mysql.ssproduct.connection}")
    private String ssProduceUrl;

    @Value("${idb.mysql.user}")
    private String idbUser;
    @Value("${idb.mysql.password}")
    private String idbPassword;


    private static MessageFactory messageFactory;
    private ThreadedSocketAcceptor acceptor = null;

    private SessionSettings settings = FixSessions.getSessionSettings();
    private HashMap<String,List<String>> targetList = FixSessions.getTargetList();
    private HashMap<String,MMUserInfoModel> mmUserInfo = FixSessions.getMmUserInfo();

    @PostConstruct
    private void startConn(){
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url,user,password);
            idb_con = DriverManager.getConnection(idbCenterUrl,idbUser,idbPassword);
        } catch (Exception e) {
            LOGGER.error("get mysql connection fail. msg :" + e.getMessage());
            throw new FixException("get mysql connection fail. msg:" + e.getMessage());
        }
    }

    private void initFixSessions(){
        try {

            LOGGER.info("starting query user and authorize table ...");

            Statement statement = con.createStatement();
            String sql = "select * from `user`";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                encapsulateSession(
                        rs.getString("target_comp_id"),
                        rs.getString("begin_string"),
                        rs.getString("transport_data_dictionary"),
                        rs.getString("app_data_dictionary"),
                        rs.getString("default_app_ver_id"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("heart_beat"),
                        rs.getString("check_latency"),
                        rs.getString("max_latency"));
            }

            String sql2 = "select * from authorize";
            ResultSet rs2 = statement.executeQuery(sql2);

            while (rs2.next()){
                encapsulateTargetList(
                        rs2.getString("target_queue_name"),
                        rs2.getString("target_queue_list"));
            }

            rs.close();
            rs2.close();
            statement.close();
            con.close();

            LOGGER.info("starting query user and authorize table finish.");

            // init Money Market UserInfo
            getUserInfo();

        } catch (SQLException e) {
            LOGGER.error("query user or authorize fail. msg :" + e.getMessage());
            throw new FixException("query user or authorize fail. msg :" + e.getMessage());
        }
    }

    private void getUserInfo(){

        try {

            LOGGER.info("starting query money market user info ...");

            Statement statement = idb_con.createStatement();
            String sqlForOrgType = "SELECT d.name,a.INFO,b.COMPANY_ID\n" +
                    "        from idb_account_contact_info a\n" +
                    "        inner join idb_account b on a.UNIFIED_ACCOUNT_ID = b.UNIFIED_ACCOUNT_ID\n" +
                    "        inner join idb_financial_company c on b.COMPANY_ID = c.id\n" +
                    "        inner join idb_bank_nature d on c.bank_nature = d.bank_nature\n" +
                    "        where a.TYPE='QQ' and a.INFO<>''";

            String sqlForProvince = "SELECT d.Province,a.INFO\n" +
                    "        from idb_account_contact_info a\n" +
                    "        inner join idb_account b on a.UNIFIED_ACCOUNT_ID = b.UNIFIED_ACCOUNT_ID\n" +
                    "        inner join idb_financial_company c on b.COMPANY_ID = c.id\n" +
                    "        inner join idb_region d on c.CITY = d.Code\n" +
                    "        where a.TYPE='QQ' and a.INFO<>''";


            ResultSet rs = statement.executeQuery(sqlForOrgType);

            while (rs.next()){
                String key = rs.getString("INFO");
                if(mmUserInfo.get(key) == null){
                    mmUserInfo.put(key,new MMUserInfoModel(rs.getString("name"),rs.getString("COMPANY_ID"),null));
                }else{
                    mmUserInfo.get(key).setOrgType(rs.getString("name"));
                    mmUserInfo.get(key).setOperatorcode(rs.getString("COMPANY_ID"));
                }
            }

            ResultSet rs2 = statement.executeQuery(sqlForProvince);
            while (rs2.next()){
                String key = rs2.getString("INFO");
                if(mmUserInfo.get(key) == null){
                    mmUserInfo.put(key,new MMUserInfoModel(null,null,rs2.getString("Province")));
                }else{
                    mmUserInfo.get(key).setProvince(rs2.getString("Province"));
                }
            }

            rs.close();
            rs2.close();
            statement.close();
            idb_con.close();

            LOGGER.info("starting query money market user info finish.");

        } catch (SQLException e) {
            LOGGER.error("query money market user info fail. msg :" + e.getMessage());
            throw new FixException("query money market user info fail. msg :" + e.getMessage());
        }
    }


    private void encapsulateSession(String targetConmpID,String beginString,String transportDD,String appDD,
                               String defaultAVI,String startT,String endT,String heartB,String checkL,String maxL){
        SessionID sessionId = new SessionID(beginString,"SUMSCOPE",targetConmpID);
        settings.setString(sessionId,"TransportDataDictionary",transportDD);
        settings.setString(sessionId,"AppDataDictionary",appDD);
        settings.setString(sessionId,"DefaultApplVerID",defaultAVI);
        settings.setString(sessionId,"StartTime",startT);
        settings.setString(sessionId,"EndTime",endT);
        settings.setString(sessionId,"HeartBtInt",heartB);
        settings.setString(sessionId,"CheckLatency",checkL);
        settings.setString(sessionId,"MaxLatency",maxL);
    }


    private void encapsulateTargetList(String targetQN,String targetQL){

        List<String> list;
        try {
            list = Arrays.asList(targetQL.split(","));
        } catch (Exception e) {
            LOGGER.error("init target list from mysql data fail. msg:" + e.getMessage());
            throw new FixException("init target list fail. msg:" + e.getMessage());
        }

        targetList.put(targetQN,list);
    }

    public void startQuickfix(){

        this.initFixSessions();

        Application application = new FixServerApplication();
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        messageFactory = new DefaultMessageFactory();

        try {
            acceptor = new ThreadedSocketAcceptor(application, storeFactory,settings, logFactory, messageFactory);
            acceptor.start();
        } catch (ConfigError e) {
            LOGGER.error("quickfix server start fail. msg:" + e.getMessage());
            throw new FixException("quickfix server start fail. msg:" + e.getMessage());
        }
    }

    public void close(){
        if(acceptor == null)
            return;
        acceptor.stop();
        LOGGER.info("quickfix server closed.");
    }

}
