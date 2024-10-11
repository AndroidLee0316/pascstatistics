package com.pasc.lib.stats.custom;

import android.content.Context;

import android.content.Intent;
import com.pasc.lib.reportdata.DataReportConfigure;
import com.pasc.lib.statistics.IPascStatistics;
import com.pasc.lib.statistics.custom.PAConfigure;
import com.pasc.lib.statistics.custom.ReportService;

public class PAStatisticsConfig {

    private boolean logEnable;
    private long intervalTime = 30 * 1000;
    private DataReportConfigure dataReportConfigure;

    public boolean isLogEnable() {
        return logEnable;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setDataReportConfigure(DataReportConfigure dataReportConfigure) {
        this.dataReportConfigure = dataReportConfigure;
    }

    public DataReportConfigure getDataReportConfigure() {
        return dataReportConfigure;
    }

    public IPascStatistics createPascStats(Context context) {
       return createPascStats(context,null);
    }

    /**
     *
     * @param context
     * @param builder  params :sendPolicyCode  支持位运算上传,如POST_ONSTART.code|POST_INTERVAL.code
     *                         sSessionInterval  milliseconds
     * @return
     */
    public IPascStatistics createPascStats(Context context,PAConfigure.Builder builder) {
        if (context != null) {
            PAStatistics paStatistics = new PAStatistics(context);
            paStatistics.init(this);
            if(builder!=null) {
                //支持位运算上传 sendPolicyCode  如POST_ONSTART.code|POST_INTERVAL.code
                PAConfigure.setSendPolicy(builder.getSendPolicyCode());
                PAConfigure.setSessionInterval(builder.getsSessionInterval());
            }
            if(PAConfigure.postInternal()){
                context.startService(new Intent(context, ReportService.class));
            }
            return paStatistics;
        }

        return null;
    }
}
