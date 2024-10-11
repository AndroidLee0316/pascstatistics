package com.pasc.lib.stats.umeng;

import android.content.Context;

import com.pasc.lib.statistics.IPascStatistics;

/**
 *  Created by huangtebian535 on 2018/09/03.
 *
 *  友盟埋点
 */
public class UmengStatisticsConfig {
    private String appKey;
    private String channel;
    private String pushSecret;
    private boolean logEnable;
    private boolean isOpenDuration = true;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPushSecret() {
        return pushSecret;
    }

    public void setPushSecret(String pushSecret) {
        this.pushSecret = pushSecret;
    }

    public boolean isLogEnable() {
        return logEnable;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }

    public boolean isOpenDuration() {
        return isOpenDuration;
    }

    public void openActivityDurationTrack(boolean isOpen) {
        this.isOpenDuration = isOpen;
    }

    public IPascStatistics createPascStats(Context context) {
        if (context != null) {
            UmengStatistics umengStatistics = new UmengStatistics(context);
            umengStatistics.init(appKey, channel, pushSecret, logEnable);
            umengStatistics.openActivityDurationTrack(isOpenDuration);
            return umengStatistics;
        }
        return null;
    }
}
