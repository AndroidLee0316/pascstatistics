package com.pasc.lib.stats.umeng;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;
import com.pasc.lib.statistics.IPascStatistics;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <meta-data android:value="5b3b2fd6a40fa314dc0002c6" android:name="UMENG_APPKEY"/>
 * <meta-data android:value="nt" android:name="UMENG_CHANNEL"/>
 */
public class UmengStatistics implements IPascStatistics {
    private Context context;
    private boolean encryptEnable = false;

    public UmengStatistics(Context context) {
        this.context = context;
    }

    public void setEncryptEnabled(boolean encryptEnable) {
        this.encryptEnable = encryptEnable;
    }


    public void init(String appKey, String channel, String pushSecret, boolean logEnable) {
        // 设置是否对日志信息进行加密, 默认false(不加密).
        UMConfigure.setEncryptEnabled(encryptEnable);
        UMConfigure.setLogEnabled(logEnable);
        /**
         * param 1:上下文，必须的参数，不能为空
         * param 2:友盟 app key，非必须参数，如果Manifest文件中已配置app key，该参数可以传空，则使用Manifest中配置的app key，否则该参数必须传入
         * param 3:友盟 channel，非必须参数，如果Manifest文件中已配置channel，该参数可以传空，则使用Manifest中配置的channel，否则该参数必须传入，channel命名请详见channel渠道命名规范
         * param 4:设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机
         * param 5:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
         */
        UMConfigure.init(context, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, pushSecret);
        UMConfigure.setLogEnabled(logEnable);

        //场景类型设置接口
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public void openActivityDurationTrack(boolean isOpen) {
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    public void onSignIn(String userID) {
        MobclickAgent.onProfileSignIn(userID);
    }

    public void onSignOut() {
        MobclickAgent.onProfileSignOff();
    }

    @Override
    public void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    @Override
    public void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    @Override
    public void onEvent(String eventID) {
        eventID = checkAndChangeToPinyin(eventID);
        MobclickAgent.onEvent(context, eventID);
    }

    @Override
    public void onEvent(String eventID, String label) {
        eventID = checkAndChangeToPinyin(eventID);
        if (TextUtils.isEmpty(label)) {
            MobclickAgent.onEvent(context, eventID);
        } else {
            MobclickAgent.onEvent(context, eventID, label);
        }
    }

    @Override
    public void onEvent(String eventID, Map<String, String> map) {
        eventID = checkAndChangeToPinyin(eventID);
        if (map == null) {
            MobclickAgent.onEvent(context, eventID);
        } else {
            MobclickAgent.onEvent(context, eventID, map);
        }
    }

    @Override
    public void onEvent(String eventID, String label, Map<String, String> map) {
        eventID = checkAndChangeToPinyin(eventID);
        if (TextUtils.isEmpty(label)) {
            if (map == null) {
                MobclickAgent.onEvent(context, eventID);
            } else {
                MobclickAgent.onEvent(context, eventID, map);
            }
        } else {
            if (map == null) {
                MobclickAgent.onEvent(context, eventID, label);
            } else {
                Map<String, String> newMap = new HashMap<>(map);
                newMap.put(eventID, label);
                MobclickAgent.onEvent(context, eventID, newMap);
            }
        }
    }

    @Override
    public void onPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    @Override
    public void onPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }


    public String checkAndChangeToPinyin(String eventID){
        if (!TextUtils.isEmpty(eventID)){
            if (Pinyin.isChinese(eventID.charAt(0))){//如果第一个是中文，表示eventID是中文，这里不检测所有文字，因为影响性能，需要产品确保规范
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < eventID.length(); i++) {
                    sb.append(Pinyin.toPinyin(eventID.charAt(i)));
                }
                eventID = sb.toString().toLowerCase(Locale.CHINA);
            }
        }
        return eventID;
    }
}
