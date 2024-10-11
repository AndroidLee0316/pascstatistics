package com.pasc.debug.component.statistics;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.pasc.lib.statistics.StatisticsManager;
import java.util.HashMap;

public class EndActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        findViewById(R.id.btn_on_page_end).setOnClickListener((v)->{
            finish();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        StatisticsManager.getInstance().onPageStart("测试第二页");
        StatisticsManager.getInstance().onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticsManager.getInstance().onPageEnd("测试第二页");
        StatisticsManager.getInstance().onPause(this);
    }
}
