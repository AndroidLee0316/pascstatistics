package com.pasc.lib.stats.bugly;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by huangtebian535 on 2018/09/03.
 * <p>
 * Bugly异常捕获
 */
public class BuglyClient {
    private static BuglyClient INSTANCE;
    private boolean mIsInited = false;

    private BuglyClient() {
    }

    public static BuglyClient getInstance() {
        if (null == INSTANCE) {
            synchronized (BuglyClient.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BuglyClient();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化bugly
     * @param context
     * @param config
     */
    public void init(Context context, BuglyConfig config) {
        if (mIsInited || config == null || TextUtils.isEmpty(config.getAppID())) {
            return;
        }
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppPackageName(context.getPackageName());
        strategy.setAppVersion(config.getAppVersion());
        strategy.setAppChannel(config.getChannel());
        CrashReport.initCrashReport(context, config.getAppID(), config.isDebug(), strategy);
        mIsInited = true;
    }
}
