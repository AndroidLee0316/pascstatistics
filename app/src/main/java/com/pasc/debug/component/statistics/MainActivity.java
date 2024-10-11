package com.pasc.debug.component.statistics;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.pasc.lib.statistics.PageType;
import com.pasc.lib.statistics.StatisticsManager;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private TextView mTvCurrentEven;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_crash).setOnClickListener((v) -> {
            throw new NullPointerException();
        });

        findViewById(R.id.btn_on_event1).setOnClickListener((v) -> {
            StatisticsManager.getInstance().onEvent("我是谁-我在那里");
        });

        findViewById(R.id.btn_on_event2).setOnClickListener((v) -> {
            StatisticsManager.getInstance().onEvent("你怎么了-我在这里里", "label2");
        });

        findViewById(R.id.btn_on_event3).setOnClickListener((v) -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("key1", "value1");
            map.put("key2", "value2");
            StatisticsManager.getInstance().onEvent("不错啦-是的哦", map);
        });

        findViewById(R.id.btn_on_event4).setOnClickListener((v) -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("key3", "value3");
            map.put("key4", "value4");
            StatisticsManager.getInstance().onEvent("Event4", "label4", map);
        });
        findViewById(R.id.btn_on_event5).setOnClickListener((v) -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("key5", "value5");
            map.put("key51", "value51");
            StatisticsManager.getInstance().onEvent("evenId5", "evenLable5", PageType.APP, map);
        });
        findViewById(R.id.btn_on_event6).setOnClickListener((v) -> {
//            HashMap<String, String> map = new HashMap<>();
//            map.put("key3", "value3");
//            map.put("key4", "value4");
//            StatisticsManager.getInstance().onEvent("tralId6", "evenId6", "evenLable6", PageType.APP, map);
            updateLocation();
        });
        findViewById(R.id.btn_on_page_start).setOnClickListener((v) -> {
            Intent intent = new Intent(this, EndActivity.class);
            startActivity(intent);
        });
        mTvCurrentEven = findViewById(R.id.statistics_type);
        mTvCurrentEven.setText("友盟统计,天眼统计");

//        try {
//            String[] testDeviceInfo = UMConfigure.getTestDeviceInfo(this);
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("device_id", testDeviceInfo[0]);
//            jsonObject.put("mac", testDeviceInfo[1]);
//
//            String deviceInfo = jsonObject.toString();
//            ((TextView) findViewById(R.id.tv_device_info)).setText(deviceInfo);
//            Log.i("MainActivity", deviceInfo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticsManager.getInstance().onPageStart("测试第一页");
        StatisticsManager.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticsManager.getInstance().onPageEnd("测试第一页");
        StatisticsManager.getInstance().onPause(this);
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)) {
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    private void updateLocation() {
        Map<String,Object> map = new HashMap<>();
        map.put("country","中国");
        map.put("province","广东省");
        map.put("city","深圳");
        map.put("district","南市区");
        map.put("street","  前海街道");
//        DataManager.updateNetInfo(this,"traceId","eventId","lable",PageType.APP,new HashMap<>(),map);

    }
}
