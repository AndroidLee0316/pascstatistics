package com.pasc.lib.stats.custom;

import android.content.Context;

import com.pasc.lib.statistics.IPascStatistics;
import com.pasc.lib.statistics.custom.PAConfigure;
import com.pasc.lib.statistics.custom.PAStatsAgent;

import java.util.Map;

/**
 * <meta-data android:value="5b3b2fd6a40fa314dc0002c6" android:name="PASC_STATS_APPKEY"/>
 */
public class PAStatistics implements IPascStatistics {
    private Context context;

    public PAStatistics(Context context) {
        this.context = context;
    }

    public void init(PAStatisticsConfig config) {
        PAConfigure.setLogEnabled(config.isLogEnable());
        PAConfigure.setDataReportConfigure(config.getDataReportConfigure());
        PAConfigure.init(context);
    }

    public void setSessionInterval(long sessionInterval) {
        PAConfigure.setSessionInterval(sessionInterval);
    }

    @Override
    public void onResume(Context context) {
        PAStatsAgent.onResume(context);
    }

    @Override
    public void onPause(Context context) {
        PAStatsAgent.onPause(context);
    }

    @Override
    public void onEvent(String eventID) {

        PAStatsAgent.onEvent(context, eventID);
    }

    @Override
    public void onEvent(String eventID, String label) {
        PAStatsAgent.onEvent(context, eventID, label);
    }

    @Override
    public void onEvent(String s, Map map) {
        PAStatsAgent.onEvent(context, s, map);
    }

    @Override
    public void onEvent(String s, String s1, Map map) {
        PAStatsAgent.onEvent(context, s, s1, map);
    }

    @Override
    public void onPageStart(String pageName) {

    }

    @Override
    public void onPageEnd(String pageName) {
    }

    @Override
    public void onPause(Context context, String pageType) {
        PAStatsAgent.onPause(context, pageType);
    }

    @Override
    public void onEvent(String s, String s1, String s2, Map map) {
        PAStatsAgent.onEvent(context, s, s1, s2, map);
    }

    @Override
    public void onEvent(String s, String s1, String s2, String s3, Map map) {
        PAStatsAgent.onEvent(context, s, s1, s2, s3, map);
    }
}
