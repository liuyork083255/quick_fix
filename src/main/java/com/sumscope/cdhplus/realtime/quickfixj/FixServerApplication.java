package com.sumscope.cdhplus.realtime.quickfixj;

import org.quickfixj.CharsetSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.MsgType;

import java.io.UnsupportedEncodingException;

/**
 * Created by liu.yang on 2017/8/22.
 */
public class FixServerApplication extends MessageCracker implements Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixServerApplication.class);

    /**
     * <pre>
     *      onCreate –>当一个Fix Session建立是调用
     *
     *      onLogon –>当一个Fix Session登录成功时候调用
     *
     *      onLogout –>当一个Fix Session退出时候调用
     *
     *      fromAdmin–>当收到一个消息,经过一系列检查，合格后，属于Admin 类型时候调用
     *
     *      fromApp–>当收到一个消息,经过一系列检查，合格后，不属于Admin 类型时候调用
     *
     *      toAdmin–>当发送一个admin类型消息调用toApp—>当发送一个非admin(业务类型)消息调用
     * </pre>
     */

    public void fromAdmin(Message message, final SessionID sessionID) {
        LOGGER.info(String.format("receive fromAdmin message from [%s] .",sessionID.getTargetCompID()));
        LOGGER.warn(String.format("receive fromAdmin message from [%s] .",sessionID.getTargetCompID()));
        try {
            crack(message, sessionID);
        } catch (UnsupportedMessageType unsupportedMessageType) {
            unsupportedMessageType.printStackTrace();
            LOGGER.error(String.format("crack [%s] message fail. msg:%s",sessionID.getTargetCompID(),unsupportedMessageType.getMessage()));
        } catch (FieldNotFound fieldNotFound) {
            fieldNotFound.printStackTrace();
            LOGGER.error(String.format("crack [%s] message fail. msg:%s",sessionID.getTargetCompID(),fieldNotFound.getMessage()));
        } catch (IncorrectTagValue incorrectTagValue) {
            incorrectTagValue.printStackTrace();
            LOGGER.error(String.format("crack [%s] message fail. msg:%s",sessionID.getTargetCompID(),incorrectTagValue.getMessage()));
        }
    }

    protected void onMessage(Message message, SessionID sessionID) {
        try {
            String msgType = message.getHeader().getString(35);
            Session session = Session.lookupSession(sessionID);
            LOGGER.info(String.format("receive client order message -> %s",msgType));

            switch (msgType) {
                case MsgType.LOGON: // 登陆
                    session.logon();
                    session.sentLogon();
                    LOGGER.info(String.format("[%s] login",sessionID));
                    LOGGER.warn(String.format("[%s] login",sessionID));
                    break;
                case MsgType.HEARTBEAT: // 心跳
                    session.generateHeartbeat();
                    LOGGER.info(String.format("[%s] heartbeat",sessionID));
                    LOGGER.warn(String.format("[%s] heartbeat",sessionID));
                    break;
            }
        } catch (FieldNotFound e) {
            LOGGER.info(String.format("server onMessage function catch exception from [%s]. msg :%s",sessionID.getTargetCompID(),e.getMessage()));
        }
    }

    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {
        LOGGER.info(String.format("receive service message [%s]",sessionID.getTargetCompID()));
        crack(message, sessionID);
    }

    public void onCreate(SessionID sessionID) {
        // setting encode UTF-8
        try {
            CharsetSupport.setCharset("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LOGGER.info("server begging ...");
    }

    public void onLogon(SessionID sessionID) {
        LOGGER.info("client [" + sessionID.getTargetCompID() + "] login success.");
    }

    public void onLogout(SessionID sessionID) {
        LOGGER.info(String.format("client [%s] logout.",sessionID.getTargetCompID()));
        LOGGER.warn(String.format("client [%s] logout.",sessionID.getTargetCompID()));
    }

    public void toAdmin(Message message, SessionID sessionID) {
    }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        LOGGER.info(String.format("send quickfix message to [%s]",sessionID.getTargetCompID()));
    }
}
