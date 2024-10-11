package com.pasc.lib.statistics.custom.utils;

import com.pasc.lib.base.util.EncryptUtils;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.statistics.custom.PAConfigure;

import static com.pasc.lib.statistics.custom.PAConfigure.LOG_TAG;

public class SessionUtils {
    private static final String SESSION_NAME = "PASC_SESSION";
    private static final String SESSION_START_TIME = "session_start_time";
    private static final String SESSION_SAVE_TIME = "session_save_time";
    private static final String SESSION_ID = "session_id";

    public static boolean isNewSession() {
        try {
            long currenttime = System.currentTimeMillis();
            long sessionSaveTime = getSessionSaveTime();
            if (currenttime - sessionSaveTime > PAConfigure.getSessionInterval()) {
                if (PAConfigure.isLogEnable()) {
                    PascLog.i(LOG_TAG, "return true,create new session.");
                }
                return true;
            }
            if (PAConfigure.isLogEnable()) {
                PascLog.i(LOG_TAG, "return false.At the same session.");
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static String createNewSession() {

        String sessionId = "";
        long time = System.currentTimeMillis();
        String str = SESSION_NAME + time;
        sessionId = EncryptUtils.getMD5(str);

        saveSessionId(sessionId);
        setSessionStartTime(time);
        setSessionSaveTime(time);
        if (PAConfigure.isLogEnable()) {
            PascLog.d(LOG_TAG, "createNewSession " + sessionId);
        }
        return sessionId;
    }


    public static void setSessionSaveTime(long time) {
        SPUtils.getInstance().setParam(SESSION_SAVE_TIME, time);
    }

    public static long getSessionSaveTime() {
        return (long) SPUtils.getInstance().getParam(SESSION_SAVE_TIME, 0L);
    }

    public static void setSessionStartTime(long time) {
        SPUtils.getInstance().setParam(SESSION_START_TIME, time);
    }

    public static long getSessionStartTime() {
        return (long) SPUtils.getInstance().getParam(SESSION_START_TIME, 0L);
    }

    public static String getSessionId() {
        return (String) SPUtils.getInstance().getParam(SESSION_ID, "");
    }

    public static void saveSessionId(String sessionId) {
        SPUtils.getInstance().setParam(SESSION_ID, sessionId);
    }

}
