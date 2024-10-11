package com.pasc.lib.statistics;

import android.content.Context;

import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by huangtebian535 on 2018/09/03.
 *
 * 运营埋点与异常统计
 */
public class StatisticsManager implements IPascNewStatistics {
  private static final String TAG = StatisticsManager.class.getSimpleName();
  private static StatisticsManager INSTANCE;
  private List<IPascStatistics> pascStatisticsList = new CopyOnWriteArrayList<> ();

  public void updateSource(List<IPascStatistics> statistics){
      if (statistics!=null){
        statistics.addAll (pascStatisticsList);
        pascStatisticsList=statistics;
      }
  }

  /**
   * 获取实例对象
   */
  public static StatisticsManager getInstance() {
    if (null == INSTANCE) {
      synchronized (StatisticsManager.class) {
        if (null == INSTANCE) {
          INSTANCE = new StatisticsManager();
        }
      }
    }
    return INSTANCE;
  }

  /**
   * 启动埋点统计
   **/
  public void addStatistics(IPascStatistics statistics) {
    if (statistics != null) {
      pascStatisticsList.add(statistics);
    }
  }

  public List<IPascStatistics> getStatisticList(){
      return pascStatisticsList;
  }

  @Override
  public void onEvent(String eventID) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onEvent(eventID);
    }
  }

  @Override
  public void onEvent(String eventID, String label) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onEvent(eventID, label);
    }
  }

  @Override
  public void onEvent(String eventID, Map<String, String> map) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onEvent(eventID, map);
    }
  }

  @Override
  public void onEvent(String eventID, String label, Map<String, String> map) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onEvent(eventID, label, map);
    }
  }

  @Override
  public void onPageStart(String pageName) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onPageStart(pageName);
    }
  }

  @Override
  public void onPageEnd(String pageName) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onPageEnd(pageName);
    }
  }

  @Override
  public void onCreate(Context context) {
    for (IPascStatistics stat : pascStatisticsList) {
      if (stat instanceof IPascNewStatistics){
        ((IPascNewStatistics) stat).onCreate(context);
      }
    }
  }

  @Override
  public void onStart(Context context) {
    for (IPascStatistics stat : pascStatisticsList) {
      if (stat instanceof IPascNewStatistics){
        ((IPascNewStatistics) stat).onStart(context);
      }
    }
  }

  @Override
  public void onPause(Context context) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onPause(context);
    }
  }

  @Override
  public void onStop(Context context) {
    for (IPascStatistics stat : pascStatisticsList) {
      if (stat instanceof IPascNewStatistics){
        ((IPascNewStatistics) stat).onStop(context);
      }
    }
  }

  @Override
  public void onDestroy(Context context) {
    for (IPascStatistics stat : pascStatisticsList) {
      if (stat instanceof IPascNewStatistics){
        ((IPascNewStatistics) stat).onDestroy(context);
      }
    }
  }

  @Override
  public void onResume(Context context) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onResume(context);
    }
  }

  @Override
  public void onEvent(String eventID, String label, String pageType, Map<String, String> map) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onEvent(eventID, label, pageType, map);
    }
  }
  @Override
  public void onEvent(String traceId,String eventID,String label, String pageType, Map<String, String> map) {
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onEvent(traceId,eventID,label, pageType, map);
    }
  }
  @Override public void onPause(Context context, String pageType) {
    Log.d(TAG,"pageType "+pageType);
    for (IPascStatistics stat : pascStatisticsList) {
      stat.onPause(context, pageType);
    }
  }
}