package com.pasc.lib.statistics;

import android.content.Context;

import java.util.Map;

/**
 * Created by huangtebian535 on 2018/09/03.
 * <p>
 * 埋点统计接口，包括事件与页面。
 */
public interface IPascStatistics {

    /**
     * @param eventID 事件标识
     */
    void onEvent(String eventID);

    /**
     * @param eventID 事件标识
     * @param label   标签，可用于对事件标识的补充
     */
    void onEvent(String eventID, String label);

    /**
     * @param eventID 事件标识
     * @param map     事件值
     */
    void onEvent(String eventID, Map<String, String> map);

    /**
     * @param eventID 事件标识
     * @param label   标签，可用于对事件标识的补充
     * @param map     事件值
     */
    void onEvent(String eventID, String label, Map<String, String> map);

    /**
     * @param pageName 页面名称
     */
    void onPageStart(String pageName);

    /**
     * @param pageName 页面名称
     */
    void onPageEnd(String pageName);

    /**
     * 页面统计
     */
    void onResume(Context context);

    /**
     * 页面统计
     */
    void onPause(Context context);

    default void onEvent(String eventID, String label, String pageType, Map<String, String> map) {

    };

    default void onPause(Context context, String pageType) {

    };

    default void onEvent(String traceId, String eventID, String label, String pageType, Map<String, String> map) {

    };
}
