package com.pasc.debug.component.statistics;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.pasc.lib.statistics.StatisticsManager;
import com.pasc.lib.stats.umeng.UmengStatisticsConfig;

public class TheApplication extends Application {
  private static final String BetaHost = "http://smt-app-stg.pingan.com.cn/";

  public static final String UMENG_APPKEY = "5b8cd3bd8f4a9d294f000019";
  public static final String UMENG_PUSH_KEY = "";

  @Override
  public void onCreate() {
    super.onCreate();
    if (getPIDName(this).equals(getPackageName())) {//主进程
      //友盟埋点初始化
      UmengStatisticsConfig umengStatisticsConfig = new UmengStatisticsConfig();
      umengStatisticsConfig.setAppKey(UMENG_APPKEY);
      umengStatisticsConfig.setChannel("10000");
      umengStatisticsConfig.setPushSecret(UMENG_PUSH_KEY);
      umengStatisticsConfig.setLogEnable(true);
      umengStatisticsConfig.openActivityDurationTrack(false);
      StatisticsManager.getInstance().addStatistics(umengStatisticsConfig.createPascStats(this));
      Log.d("statistic", "StatisticsManager umeng");
    }
  }

  /**
   * 获得进程名字
   */
  public String getPIDName(Context context) {
    int pid = android.os.Process.myPid();
    ActivityManager mActivityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
      if (appProcess.pid == pid) {
        return appProcess.processName;
      }
    }
    return "";
  }
}
