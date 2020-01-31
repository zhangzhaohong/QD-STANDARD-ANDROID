package com.autumn.framework.Log;

import android.content.Context;
import android.util.Log;

import com.autumn.framework.setting.super_setting;
import com.autumn.reptile.R;
import com.liyi.sutils.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * Created by zhang on 2018/3/28.
 */

public class DumpUtil {

    public static String LOG_PATH = "/dump/";

    public static boolean createDumpFile(Context context) {

        boolean bool = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ssss");
        String createTime = sdf.format(new Date(System.currentTimeMillis()));
        String state = android.os.Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if(android.os.Environment.MEDIA_MOUNTED.equals(state)){
            File file = new File(LOG_PATH);
            if(!file.exists()) {
                file.mkdirs();
            }
            String hprofPath = file.getAbsolutePath();
            if(!hprofPath.endsWith("/")) {
                hprofPath+= "/";
            }

            hprofPath+= createTime + ".hprof";
            try {
                android.os.Debug.dumpHprofData(hprofPath);
                bool= true;
                Log.d("ANDROID_LAB", "create dumpfile done!");
                super_setting.dump_finished = true;
                //ToastUtil.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                FToastUtils.init().setRoundRadius(30).show("dump完成！");
            }catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bool= false;
            Log.d("ANDROID_LAB", "nosdcard!");
        }

        return bool;
    }

}
