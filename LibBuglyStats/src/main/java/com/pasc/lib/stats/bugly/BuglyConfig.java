package com.pasc.lib.stats.bugly;

/**
 *  Created by huangtebian535 on 2018/09/03.
 *
 *  Bugly异常捕获配置
 */
public class BuglyConfig {
    private String appID;
    private String appVersion;
    private String channel;
    private boolean isDebug;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

}
