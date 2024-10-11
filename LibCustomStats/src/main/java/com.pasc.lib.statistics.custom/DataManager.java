package com.pasc.lib.statistics.custom;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import com.pasc.lib.base.util.DateUtils;
import com.pasc.lib.base.util.NetworkUtils;
import com.pasc.lib.base.util.SPUtils;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.reportdata.DataReportConfigure;
import com.pasc.lib.reportdata.DataReportManager;
import com.pasc.lib.reportdata.net.NetReportDataUtil;
import com.pasc.lib.reportdata.param.BaseInfo;
import com.pasc.lib.reportdata.param.EventInfo;
import com.pasc.lib.reportdata.param.PageInfo;
import com.pasc.lib.statistics.custom.utils.SessionUtils;
import com.pasc.lib.stats.custom.PAStatisticsConfig;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pasc.lib.statistics.custom.PAConfigure.LOG_TAG;

public class DataManager {
    private static final String DEFAULT_TIME_KEY = "DEFAULT_TIME_KEY";

    public static void postInfo(BaseInfo info) {
        if (info == null) {
            return;
        }
        info.reportTime = DateUtils.dateAndtimeFormat(System.currentTimeMillis());
        info.sessionId = SessionUtils.getSessionId();
        DataReportConfigure configure = PAConfigure.getDataReportConfigure();
        String configName = configure != null ? configure.getConfigName() : null;
        if (TextUtils.isEmpty(info.longitudeLatitude)) {
            info.longitudeLatitude = DataReportManager.getInstance().getLoc(configName);
        }

        if (TextUtils.isEmpty(info.userId)) {
            info.userId = DataReportManager.getInstance().getUserId(configName);
        }

        StatsDataSaveAndReportUtils.getInstance().saveInfo(info);
        if (PAConfigure.postNow()
                && NetworkUtils.isNetworkAvailable()) {
            StatsDataSaveAndReportUtils.getInstance().reportAllInfo();
        }
    }

    public static void postWithHistoryInfo(final BaseInfo baseInfo) {
        postInfo(baseInfo);
        PascLog.e("traceId=="+baseInfo);
        StatsDataSaveAndReportUtils.getInstance().reportAllInfo();
    }

    public static void updateNetInfo(Context context, String traceId, String eventId, String label, String pageType,
                                     Map<String, String> map, Map<String, Object> locationMap,String longitudeLatitude) {
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
            eventInfo.plusInfo = locationMap;
            eventInfo.longitudeLatitude = longitudeLatitude;
            eventInfo.reportTime = getNowDate();
            if (null != context) {
                eventInfo.pageId = getPageId(context);
                PascLog.d(LOG_TAG, "pageId " + eventInfo.pageId);
            }
            if (map != null && map.size() != 0) {
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.putAll(map);
                eventInfo.setPlusInfo(infoMap);
            }
//            PageInfo pageInfo = new PageInfo();
//            pageInfo.pageId = getPageId(context);
//            pageInfo.pageType = pageType;
//            long timeLong = System.currentTimeMillis();
//            pageInfo.staySeconds = String.format("%.2f",
//                    (timeLong - (long) SPUtils.getInstance().getParam(DEFAULT_TIME_KEY, timeLong)) / 1000.0);
            List<EventInfo> eventInfos = new ArrayList<>();
            eventInfos.add(eventInfo);
//            List<PageInfo> pageInfos = new ArrayList<>();
//            pageInfos.add(pageInfo);
            NetReportDataUtil.reportDatas(DataReportManager.getInstance().mDefaultConfigure.getAppKey(), eventInfos, null);
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
    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return dateString;
    }
}
