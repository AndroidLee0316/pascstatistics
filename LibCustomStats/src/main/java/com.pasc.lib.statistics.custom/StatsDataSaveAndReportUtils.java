package com.pasc.lib.statistics.custom;

import com.pasc.lib.reportdata.DataReportConfigure;
import com.pasc.lib.reportdata.DataReportManager;
import com.pasc.lib.reportdata.file.FileStorage;
import com.pasc.lib.reportdata.file.naming.NormalNameGenerator;
import com.pasc.lib.reportdata.param.BaseInfo;

import java.io.File;

public class StatsDataSaveAndReportUtils {
    private static final String SDCARD_FILE_DIR = "stats";

    private FileStorage mFileStorage;
    private StatsDataSaveAndReportUtils() {
        String fileDir = SDCARD_FILE_DIR;
        File file = PAConfigure.getContext().getExternalFilesDir(fileDir);
        String dir = "";
        if (file != null) {
            dir = file.getAbsolutePath();
        }
        DataReportConfigure configure = PAConfigure.getDataReportConfigure();
        String configName = configure != null ? configure.getConfigName() : null;
        boolean isReportEnable = DataReportManager.getInstance().isReportEnable(configName);
        mFileStorage = new FileStorage
                .Builder(dir)
                .setConfigName(configName)
                .setReportEnable(isReportEnable)
                .fileNameGenerator(new NormalNameGenerator("stats.txt"))
                .build();
    }

    private static final class SingletonHolder {
        public static final StatsDataSaveAndReportUtils instance = new StatsDataSaveAndReportUtils();
    }

    public static StatsDataSaveAndReportUtils getInstance() {
        return SingletonHolder.instance;
    }

    public void saveInfo(BaseInfo info) {
       if (info != null) {
           mFileStorage.save(info);
       }
    }

    public void reportAllInfo() {
        mFileStorage.reportFiles();
    }

}
