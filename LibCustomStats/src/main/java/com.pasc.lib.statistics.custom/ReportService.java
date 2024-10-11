package com.pasc.lib.statistics.custom;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.pasc.lib.log.PascLog;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangxu678 on 2019-10-29.
 */
public class ReportService extends Service {
  private static Timer timer = new Timer();
  private Context context;
  private static String TAG = "ReportService";
  SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
  public IBinder onBind(Intent intent) {
    return null;
  }

  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  public void onCreate() {
    super.onCreate();
    PascLog.i(TAG,"onCreate");
    context = this;
    startService();
  }

  private void startService() {

    timer.scheduleAtFixedRate(new ReportTask(), 2000, PAConfigure.getSessionInterval());
  }

  private class ReportTask extends TimerTask {
    public void run() {

      PascLog.i(TAG,dateFormat.format(System.currentTimeMillis())+" upload");
      StatsDataSaveAndReportUtils.getInstance().reportAllInfo();
    }
  }

  public void onDestroy() {
    super.onDestroy();
    if (timer != null) {
      timer.cancel();
    }
  }
}
