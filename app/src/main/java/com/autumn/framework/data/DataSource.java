package com.autumn.framework.data;


import com.autumn.framework.entity.AppInfo;
import com.autumn.sdk.manager.update.update_manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/7/8.
 */
public class DataSource {

    private static DataSource sDataSource = new DataSource();

    private static final String[] NAMES = {
            "奇点"
    };

    private static final String[] IMAGES = {
    };

    private static final String[] URLS = {
            update_manager.get_download_url()
    };

    public static DataSource getInstance() {
        return sDataSource;
    }

    public List<AppInfo> getData() {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (int i = 0; i < NAMES.length; i++) {
            AppInfo appInfo = new AppInfo(String.valueOf(i), NAMES[i], IMAGES[i], URLS[i]);
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
