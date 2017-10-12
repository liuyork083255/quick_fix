package com.sumscope.cdhplus.realtime.quickfixj.util;

import com.sumscope.cdhplus.realtime.quickfixj.exception.FixException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by liu.yang on 2017/8/29.
 */
@Component
public class CreditDebtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditDebtUtil.class);

    private Connection con;
    private Statement statement;
    private ResultSet rs;

    @Value("${application.mysql.driver}")
    private String driver;
    @Value("${idb.mysql.ssproduct.connection}")
    private String url;
    @Value("${idb.mysql.user}")
    private String user;
    @Value("${idb.mysql.password}")
    private String password;

    private final Map<String,String> mapAllCreditDebt = new HashMap<>();//剩余期限的全部内容
    private final Map<String,String> mapDebtValuation = new HashMap<>();//全部债券类型
    private final Map<String,String> mapRemainingDate = new HashMap<>();//中债估值

    private String remainingDateSQL;
    private String allCreditDebtSQL;
    private String debtValuationSQL;

    {
        this.remainingDateSQL = "SELECT Bond_ID, Short_Name, Bond_Subtype FROM bond WHERE ((Bond_Subtype IN ('CCP', 'LCP', 'SSB', 'CSP', 'LSP', 'CMN', 'LMN', 'CCB', 'COB', 'RAB ', 'SCV', 'CVB', 'SPD', 'MCD', 'SHD', 'CCD', 'RTD', 'RRD', 'FRD', 'OTD', 'AMP', 'CBS', 'LBS', 'MBS', 'TET', 'PDB','INT')) OR\n" +
                "          (Bond_Subtype IN ('CEB', 'LEB') AND Ent_Cor = 'ENT') OR\n" +
                "          (Bond_Subtype IN ('CEB', 'LEB') AND Ent_Cor = 'COR') OR\n" +
                "          (Bond_Subtype IN ('PPN') AND Ent_Cor IS NULL)) and Issuer_Code<>'Z000506'";

        this.allCreditDebtSQL = "select Bond_ID,Val_Yield,Valuate_Date from cdc_bond_valuation where Valuate_Date=(select max(Valuate_Date) from cdc_bond_valuation)";

        this.debtValuationSQL = "SELECT `sc`.`Bond_ID`,sc.`Bond_Key`,  `sc`.`Short_Name`,CONCAT( REPLACE(REPLACE(\n" +
                "                if ((ISNULL(`sc`.`b`) OR(`sc`.`b` < 1)),\n" +
                "                  if ((`sc`.`d` < 1),\n" +
                "            \tCONCAT(`sc`.`f`, \"D\"),\n" +
                "            \tCONCAT(TRUNCATE(ROUND(`sc`.`d`, 4), 2), \"Y\")),\n" +
                "                if ((NOT ISNULL(`sc`.`Term_structure`)),\n" +
                "                    if ((`sc`.`a` < 1),\n" +
                "                CONCAT(CONCAT(`sc`.`b`, \"D + \"), CONCAT(TRUNCATE(ROUND(`sc`.`c`, 3), 1), \"Y\")),\n" +
                "                CONCAT(CONCAT(TRUNCATE(`sc`.`a`, 2), \"Y + \"), CONCAT(TRUNCATE(ROUND(`sc`.`c`, 3), 1), \"Y\"))),\n" +
                "                    if ((`sc`.`d` < 1),\n" +
                "                        CONCAT(`sc`.`f`, \"D\"),\n" +
                "                        CONCAT(TRUNCATE(ROUND(`sc`.`d`, 4), 2), \"Y\")))\n" +
                "                            ), \".0Y\", \"Y\"), \".00Y\", \"Y\"),\n" +
                "                        if ((`sc`.`Option_Type` = \"ETS\"), \" + N\", \"\")\n" +
                "                    )\n" +
                "                    AS `residual_maturity`,\n" +
                "                    `sc`.`f` AS `residual_maturity_value`\n" +
                "                FROM\n" +
                "                (select `bond`.`Bond_Key`, `bond`.`Bond_ID`, `bond`.`Short_Name`, `bond`.`Term_structure`, `bond`.`Option_Type`,\n" +
                "                                ((TO_DAYS(`bond`.`Option_Date`) - if ((TO_DAYS(NOW()) < TO_DAYS(`bond`.`Interest_Start_Date`)), TO_DAYS(`bond`.`Interest_Start_Date`), TO_DAYS(NOW()))) /365) AS `a`,\n" +
                "                                ( TO_DAYS(`bond`.`Option_Date`)- if(( TO_DAYS( NOW()) <  TO_DAYS(`bond`.`Interest_Start_Date`)), TO_DAYS(`bond`.`Interest_Start_Date`), TO_DAYS( NOW()))) AS `b`,\n" +
                "                                (( TO_DAYS(`bond`.`Maturity_Date`)- TO_DAYS(`bond`.`Option_Date`))/365) AS `c`,\n" +
                "                                ((TO_DAYS(`bond`. `Maturity_Date`) - if ((TO_DAYS(NOW()) < TO_DAYS(`bond`.`Interest_Start_Date`)), TO_DAYS(`bond`.`Interest_Start_Date`), TO_DAYS(NOW()))) /365) AS `d`,\n" +
                "                                ( TO_DAYS(`bond`.`Maturity_Date`)- if(( TO_DAYS( NOW()) <  TO_DAYS(`bond`.`Interest_Start_Date`)), TO_DAYS(`bond`.`Interest_Start_Date`), TO_DAYS( NOW()))) AS `f`\n" +
                "                                FROM `bond` as `bond`  WHERE (`bond`.`delflag` = 0)) AS sc\n" +
                "                WHERE `sc`.`f` > 0";
    }


    public void initCreditDebt(){
        LOGGER.info("start init credit debt data ...");
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            LOGGER.error("get CreditDebt mysql connection fail. msg :" + e.getMessage());
            throw new FixException("get mysql connection fail. msg:" + e.getMessage());
        }

        try {
            setRemainingDate();
            setAllCreditDebt();
            setDebtValuation();
        } catch (SQLException e) {
            LOGGER.error("get CreditDebt data fail. msg :" + e.getMessage());
            throw new FixException("get CreditDebt data fail. msg:" + e.getMessage());
        }

        closeResource();
        LOGGER.info("init credit debt success !!!");
    }


    /**
     * 获取剩余期限
     * @param key
     * @return
     */
    public String getRemainingDate(String key){
        return mapRemainingDate.get(key);
    }

    /**
     * 获取信用债
     * @param key
     * @return
     */
    public String getCreditDebtType(String key){
        return mapAllCreditDebt.get(key);
    }

    /**
     * 获取中债估值
     * @param key
     * @return
     */
    public String getDebtValuation(String key){
        return mapDebtValuation.get(key);
    }


    /**
     * 校验是否属于信用债
     * @param key
     * @return
     */
    public boolean checkIsCreditDebt(String key){
        return mapAllCreditDebt.containsKey(key);
    }



    /**
     * 剩余期限的全部内容
     * @throws SQLException
     */
    private void setRemainingDate() throws SQLException {
        statement = con.createStatement();
        rs = statement.executeQuery(remainingDateSQL);

        while(rs.next()){
            mapAllCreditDebt.put(rs.getString("Bond_ID"),rs.getString("Bond_Subtype"));
        }
    }

    /**
     * 全部债券类型
     * @throws SQLException
     */
    private void setAllCreditDebt() throws SQLException {
        statement = con.createStatement();
        rs = statement.executeQuery(allCreditDebtSQL);

        while (rs.next()){
            mapDebtValuation.put(rs.getString("Bond_ID"),rs.getString("Val_Yield"));
        }
    }

    /**
     * 中债估值
     * @throws SQLException
     */
    private void setDebtValuation() throws SQLException {
        statement = con.createStatement();
        ResultSet rs = statement.executeQuery(debtValuationSQL);

        while (rs.next()){
            mapRemainingDate.put(rs.getString("Bond_ID"),rs.getString("residual_maturity"));
        }
    }


    private void closeResource(){
        try {
            if(rs != null)
                rs.close();
            if(statement != null)
                statement.close();
            if(con != null)
                con.close();
        } catch (SQLException e) {
            LOGGER.error("close mysql connection fail. msg :" + e.getMessage());
            throw new FixException("close mysql connection fail. msg:" + e.getMessage());
        }
    }
}
