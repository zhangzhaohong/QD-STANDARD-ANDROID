package com.autumn.framework.update.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * If there is no bug, then it is created by ChenFengYao on 2017/4/19,
 * otherwise, I do not know who create it either.
 */
public class InstallUtil {
    /**
     *
     * @param context
     * @param apkPath 要安装的APK
     * @param rootMode 是否是Root模式
     */
    public static void install(Context context, String apkPath,boolean rootMode){
            installNormal(context,apkPath);
    }

    /**
     * 通过非Root模式安装
     * @param context
     * @param apkPath
     */
    public static void install(Context context,String apkPath){
        install(context,apkPath,false);
    }

    //普通安装
    private static void installNormal(Context context,String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.example.chenfengyao.installapkdemo", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
