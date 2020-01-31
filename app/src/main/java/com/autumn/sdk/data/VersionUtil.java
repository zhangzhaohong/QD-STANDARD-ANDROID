package com.autumn.sdk.data;

import android.content.Context;

import com.autumn.reptile.R;


public class VersionUtil {
    public static String build_version(Context context , String software_version) {

        String BUILD_VERSION = null;
        String SP_VERSION = null;
        String PATCH_VERSION = null;
        String SOFTWARE_VERSION = software_version;
        BUILD_VERSION = DataUtil.replace_words_first(context.getString(R.string.app_name),SOFTWARE_VERSION,"").substring(1,10).replaceAll("\\.","");
        LogUtil.i(BUILD_VERSION);

        return BUILD_VERSION;

    }

    public static String sp_version(Context context , String software_version) {

        String BUILD_VERSION = null;
        String SP_VERSION = null;
        String PATCH_VERSION = null;
        String SOFTWARE_VERSION = software_version;
        //PATCH_VERSION =SOFTWARE_VERSION .substring(SOFTWARE_VERSION.indexOf("Patch") + 5,SOFTWARE_VERSION.indexOf("Patch") + 7);
        //LogUtil.i(String.valueOf(PATCH_VERSION));
        SP_VERSION = SOFTWARE_VERSION.substring(SOFTWARE_VERSION.indexOf("SP") + 2,SOFTWARE_VERSION.indexOf("SP") + 4);
        LogUtil.i(String.valueOf(SP_VERSION));
        BUILD_VERSION = DataUtil.replace_words_first(context.getString(R.string.app_name),SOFTWARE_VERSION,"").substring(1,10).replaceAll("\\.","")+SP_VERSION;
        LogUtil.i(BUILD_VERSION);

        return BUILD_VERSION;

    }

    public static String patch_version(Context context , String software_version) {

        String BUILD_VERSION = null;
        String SP_VERSION = null;
        String PATCH_VERSION = null;
        String SOFTWARE_VERSION = software_version;
        PATCH_VERSION =SOFTWARE_VERSION .substring(SOFTWARE_VERSION.indexOf("Patch") + 5,SOFTWARE_VERSION.indexOf("Patch") + 7);
        LogUtil.i(String.valueOf(PATCH_VERSION));
        SP_VERSION = SOFTWARE_VERSION.substring(SOFTWARE_VERSION.indexOf("SP") + 2,SOFTWARE_VERSION.indexOf("SP") + 4);
        LogUtil.i(String.valueOf(SP_VERSION));
        BUILD_VERSION = DataUtil.replace_words_first(context.getString(R.string.app_name),SOFTWARE_VERSION,"").substring(1,10).replaceAll("\\.","")+SP_VERSION+PATCH_VERSION;
        LogUtil.i(BUILD_VERSION);

        return BUILD_VERSION;

    }
}
