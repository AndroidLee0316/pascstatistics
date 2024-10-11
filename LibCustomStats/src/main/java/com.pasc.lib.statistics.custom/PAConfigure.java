package com.pasc.lib.statistics.custom;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.pasc.lib.base.util.DateUtils;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.reportdata.DataReportConfigure;
import com.pasc.lib.reportdata.DataReportManager;
import com.pasc.lib.reportdata.param.EventInfo;
import com.pasc.lib.statistics.custom.utils.SessionUtils;

import static com.pasc.lib.statistics.custom.PAConfigure.SendPolicy.POST_ONSTART;

public class PAConfigure {
  public static final String LOG_TAG = "PascStats";
  private static final String APP_START_TIME = "app_start_time";
  private static final long DEFAULT_SESSION_INTERVAL_TIME = 30 * 1000;
  public static final String EVENT_APP_START = "startApp";

  private static boolean sDebugLog = false;
  private static boolean sIsInited = false;
  private static long sSessionInterval = DEFAULT_SESSION_INTERVAL_TIME;
  private static SendPolicy sSendPolicy = POST_ONSTART;
  private static int sendPolicyCode;
  private static Context sContext;

  private static DataReportConfigure sDataReportConfigure;

  public static boolean isInited() {
    return sIsInited;
  }

  public static void init(Context context) {
    try {
      if (context == null) {
        if (sDebugLog) {
          PascLog.i(LOG_TAG, "init context is null !!!");
        }
        return;
      }

      sContext = context.getApplicationContext();

      SPUtils.getInstance().setParam(APP_START_TIME, System.currentTimeMillis());
      newSession();
      sIsInited = true;
      handleActivityLifecycle();
    } catch (Exception e) {
      if (sDebugLog) {
        PascLog.e(LOG_TAG, "init error is " + e);
      }
    }
  }

  private static void handleActivityLifecycle() {
    if (sContext != null) {
      ((Application) sContext).registerActivityLifecycleCallbacks(
          new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
              if (!sIsInited) {
                return;
              }
              if (SessionUtils.isNewSession()) {
                newSession();
              }
            }

            @Override
            public void onActivityPaused(Activity activity) {
              if (!sIsInited) {
                return;
              }
              SessionUtils.setSessionSaveTime(System.currentTimeMillis());
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
          });
    }
  }

  public static void setDataReportConfigure(DataReportConfigure dataReportConfigure) {
    PAConfigure.sDataReportConfigure = dataReportConfigure;
    if (dataReportConfigure != null) {
      DataReportManager.getInstance().addConfigure(dataReportConfigure);
    }
  }

  public static DataReportConfigure getDataReportConfigure() {
    return sDataReportConfigure;
  }

  public static Context getContext() {
    return sContext;
  }

  private static void newSession() {
    SessionUtils.createNewSession();
    EventInfo eventInfo = new EventInfo();
    eventInfo.eventID = EVENT_APP_START;
    eventInfo.reportTime = DateUtils.dateAndtimeFormat(SessionUtils.getSessionStartTime());
    DataManager manager = new DataManager();
    manager.postWithHistoryInfo(eventInfo);
  }

  public static void setLogEnabled(boolean debug) {
    sDebugLog = debug;
  }

  public static boolean isLogEnable() {
    return sDebugLog;
  }

  /**
   * milliseconds
   * @param sessionInterval
   */
  public static void setSessionInterval(long sessionInterval) {
    if (sessionInterval > 1000) {
      sSessionInterval = sessionInterval;
    }
  }

  public static long getSessionInterval() {
    return sSessionInterval;
  }

  @Deprecated
  /**
   * 使用传int值的方法
   */
  public static void setSendPolicy(SendPolicy policy) {
    sSendPolicy = policy;
  }
  @Deprecated
  /**
   * 使用返回int值的方法
   */
  public static SendPolicy getSendPolicy() {
    return sSendPolicy;
  }
  public static void setSendPolicy(int code){
    sendPolicyCode=code;
  }

  /**
   * 数据发送模式 <br>
   * POST_ONSTART 下次启动发送 <br>
   * POST_NOW 实时发送 <br>
   * POST_INTERVAL 定时发送
   */
  public enum SendPolicy {
    POST_ONSTART(1), POST_NOW(2), POST_INTERVAL(4);
    private int code;

    SendPolicy(int code) {
      this.code=code;
    }
    public SendPolicy from(int code){
      for (SendPolicy sendPolicy:SendPolicy.values()){
        if(sendPolicy.code==code){
          return sendPolicy;
        }
      }
      return null;
    }
    public int getCode(){
      return code;
    }

  }
  public static boolean postNow(){
    return (sendPolicyCode&SendPolicy.POST_NOW.code)==SendPolicy.POST_NOW.code;
  }
  public static boolean postOnStart(){
    return (sendPolicyCode&SendPolicy.POST_ONSTART.code)==SendPolicy.POST_ONSTART.code;
  }
  public static boolean postInternal(){
    return (sendPolicyCode&SendPolicy.POST_INTERVAL.code)==SendPolicy.POST_INTERVAL.code;
  }
  public static class Builder{
    private long sSessionInterval;
    private int sendPolicyCode;
    public Builder sSessionInterval(long sSessionInterval){
      this.sSessionInterval=sSessionInterval;
      return this;
    }

    /**
     * 支持混合上传
     * @param sendPolicyCode   POST_ONSTART.code|POST_INTERVAL.code
     * @return
     */
    public Builder sendPolicyCode(int sendPolicyCode){
      this.sendPolicyCode=sendPolicyCode;
      return this;
    }

    public long getsSessionInterval() {
      return sSessionInterval;
    }

    public int getSendPolicyCode() {
      return sendPolicyCode;
    }
  }
}
