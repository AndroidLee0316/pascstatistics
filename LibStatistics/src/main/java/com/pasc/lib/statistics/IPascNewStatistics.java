package com.pasc.lib.statistics;

import android.content.Context;

import java.util.Map;

/**
 * Created by huangtebian535 on 2018/09/03.
 * <p>
 * 埋点统计接口，包括事件与页面。
 */
public interface IPascNewStatistics extends IPascStatistics{

    void onCreate(Context context);

    void onStart(Context context);

    void onStop(Context context);

    void onDestroy(Context context);

    @Deprecated
    @Override
    void onPause(Context context);

    @Deprecated
    @Override
    void onPause(Context context, String pageType);
    @Deprecated
    @Override
    void onResume(Context context);
}
