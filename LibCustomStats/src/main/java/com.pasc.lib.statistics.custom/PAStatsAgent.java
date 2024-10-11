package com.pasc.lib.statistics.custom;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import android.util.Log;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.reportdata.param.EventInfo;

import com.pasc.lib.reportdata.param.PageInfo;
import java.util.HashMap;
import java.util.Map;

import static com.pasc.lib.statistics.custom.PAConfigure.LOG_TAG;

public class PAStatsAgent {
  private static final String DEFAULT_TIME_KEY = "DEFAULT_TIME_KEY";

  public static void onResume(Context context) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    SPUtils.getInstance().setParam(DEFAULT_TIME_KEY, System.currentTimeMillis());
  }

  public static void onResume(Context context, String pageType) {
    onResume(context);
  }

  public static void onPause(Context context) {
    onPause(context, "");
  }

  public static void onPause(Context context, String pageType) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    PageInfo pageInfo = new PageInfo();
    pageInfo.pageId = getPageId(context);
    pageInfo.pageType = pageType;
    long timeLong = System.currentTimeMillis();
    pageInfo.staySeconds = String.format("%.2f",
        (timeLong - (long) SPUtils.getInstance().getParam(DEFAULT_TIME_KEY, timeLong)) / 1000.0);
    PascLog.d(LOG_TAG,"staySeconds "+pageInfo.staySeconds);
    DataManager.postInfo(pageInfo);
    SPUtils.getInstance().setParam(DEFAULT_TIME_KEY, timeLong);
  }

  public static void onEvent(Context context, String eventId) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    if (TextUtils.isEmpty(eventId)) {
      PascLog.e(LOG_TAG, "onEvent eventId is empty !");
    } else {
      EventInfo eventInfo = new EventInfo();
      eventInfo.eventID = eventId;
      if (null != context) {
        eventInfo.pageId = getPageId(context);
        PascLog.d(LOG_TAG, "pageId " + eventInfo.pageId);
      }
      DataManager.postInfo(eventInfo);
    }
  }

  public static void onEvent(Context context, String eventId, String label) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    if (TextUtils.isEmpty(eventId)) {
      PascLog.e(LOG_TAG, "onEvent eventId is empty !");
      return;
    } else if (TextUtils.isEmpty(label)) {
      PascLog.e(LOG_TAG, "onEvent label is empty !");
      return;
    } else {
      EventInfo eventInfo = new EventInfo();
      eventInfo.eventID = eventId;
      eventInfo.label = label;
      if (null != context) {
        eventInfo.pageId = getPageId(context);
        PascLog.d(LOG_TAG, "pageId " + eventInfo.pageId);
      }
      DataManager.postInfo(eventInfo);
    }
  }

  public static void onEvent(Context context, String eventId, Map<String, String> map) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    if (TextUtils.isEmpty(eventId)) {
      PascLog.e(LOG_TAG, "onEvent eventId is empty !");
      return;
    } else if (map == null && map.size() == 0) {
      PascLog.e(LOG_TAG, "onEvent map is empty !");
      return;
    } else {
      EventInfo eventInfo = new EventInfo();
      eventInfo.eventID = eventId;
      if (null != context) {
        eventInfo.pageId = getPageId(context);
        PascLog.d(LOG_TAG, "pageId " + eventInfo.pageId);
      }
      Map<String, Object> infoMap = new HashMap<>();
      infoMap.putAll(map);
      eventInfo.setPlusInfo(infoMap);
      DataManager.postInfo(eventInfo);
    }
  }

  public static void onEvent(Context context, String eventId, String label,
      Map<String, String> map) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    if (TextUtils.isEmpty(eventId)) {
      PascLog.e(LOG_TAG, "onEvent eventId is empty !");
      return;
    } else if (map == null && map.size() == 0) {
      PascLog.e(LOG_TAG, "onEvent map is empty !");
      return;
    } else {
      EventInfo eventInfo = new EventInfo();
      eventInfo.eventID = eventId;
      eventInfo.label = label;
      if (null != context) {
        eventInfo.pageId = getPageId(context);
        PascLog.d(LOG_TAG, "pageId " + eventInfo.pageId);
      }
      Map<String, Object> infoMap = new HashMap<>();
      infoMap.putAll(map);
      eventInfo.setPlusInfo(infoMap);
      DataManager.postInfo(eventInfo);
    }
  }

  public static void onEvent(Context context, String eventId, String label, String pageType,
      Map<String, String> map) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    if (TextUtils.isEmpty(eventId)) {
      PascLog.e(LOG_TAG, "onEvent eventId is empty !");
      return;
    } else {
      EventInfo eventInfo = new EventInfo();
//      eventInfo.eventID = eventId;
      eventInfo.traceId = eventId;
      eventInfo.eventID = "service_click";
      eventInfo.label = label;
      eventInfo.pageType = pageType;
      if (null != context) {
        eventInfo.pageId = getPageId(context);
        PascLog.d(LOG_TAG, "pageId " + eventInfo.pageId);
      }
      if (map != null && map.size() != 0) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.putAll(map);
        eventInfo.setPlusInfo(infoMap);
      }
      DataManager.postInfo(eventInfo);
    }
  }

  public static void onEvent(Context context, String traceId, String eventId, String label, String pageType,
                             Map<String, String> map) {
    if (!PAConfigure.isInited()) {
      PascLog.e(LOG_TAG, "PAConfigure is not init!");
      return;
    }
    if (TextUtils.isEmpty(eventId)) {
      PascLog.e(LOG_TAG, "onEvent eventId is empty !");
      return;
    } else {
      EventInfo eventInfo = new EventInfo();
      eventInfo.eventID = eventId;
      eventInfo.traceId = traceId;
      eventInfo.label = label;
      eventInfo.pageType = pageType;
      if (null != context) {
        eventInfo.pageId = getPageId(context);
        PascLog.d(LOG_TAG, "pageId " + eventInfo.pageId);
      }
      if (map != null && map.size() != 0) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.putAll(map);
        eventInfo.setPlusInfo(infoMap);
      }
      DataManager.postInfo(eventInfo);
    }
  }

  /**
   * 获取当前页面
   */

  private static String getPageId(Context context) {
    if (context instanceof Application) {
      ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
      PascLog.d(LOG_TAG, "application class" + cn.getClassName());
      return cn.getClassName();
    }
    if (context instanceof Activity) {
      PascLog.d(LOG_TAG, "activity class" + context.getClass().getName());
      return context.getClass().getName();
    }
    return "";
  }
}
