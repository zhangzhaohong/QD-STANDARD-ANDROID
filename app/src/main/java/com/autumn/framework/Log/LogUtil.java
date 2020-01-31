package com.autumn.framework.Log;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.autumn.reptile.MyApplication;
import com.autumn.framework.data.SpUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/** 
 * Log工具，类似android.util.Log。 tag自动产生，格式: 
 * customTagPrefix:className.methodName(Line:lineNumber), 
 * customTagPrefix为空时只输出：className.methodName(Line:lineNumber)。 
 */  
public class LogUtil {

    public static String customTagPrefix = "";  // 自定义Tag的前缀，可以是作者名  
    public static boolean isSaveLog = false;    // 是否把保存日志到SD卡中
    public static String LOG_PATH = Environment.getExternalStorageDirectory().getPath(); // SD卡中的根目录

    private static final String FILESYSTEM = "FileSystem_LOG";
    private static final String LOG_PROGRESS = "log_progress_LOG";
    private static final String LOG_FILENAME_TIME = "log_filename_time_LOG";
    private static final String LOG_FILENAME_TIMESTAMP = "log_filename_timestamp_LOG";
    private static final int LOG_NUMBER = 100;

    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);


    private LogUtil() {
    }

    // 容许打印日志的类型，默认是true，设置为false则不打印  
    public static boolean allowD = true;  
    public static boolean allowE = true;  
    public static boolean allowI = true;  
    public static boolean allowV = true;  
    public static boolean allowW = true;  
    public static boolean allowWtf = true;

    private static String generateTag(StackTraceElement caller) {  
        String tag = "%s.%s(Line:%d)"; // 占位符  
        String callerClazzName = caller.getClassName(); // 获取到类名  
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);  
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber()); // 替换  
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;  

        return tag;  
    }  

    /** 
     * 自定义的logger 
     */  
    public static CustomLogger customLogger;

    public interface CustomLogger {  
        void d(String tag, String content);  
        void d(String tag, String content, Throwable e);  

        void e(String tag, String content);  
        void e(String tag, String content, Throwable e);  

        void i(String tag, String content);  
        void i(String tag, String content, Throwable e);  

        void v(String tag, String content);  
        void v(String tag, String content, Throwable e);  

        void w(String tag, String content);  
        void w(String tag, String content, Throwable e);  
        void w(String tag, Throwable tr);  

        void wtf(String tag, String content);  
        void wtf(String tag, String content, Throwable e);  
        void wtf(String tag, Throwable tr);  
    }  

    public static void d(String content) {  
        if (!allowD) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.d(tag, content);  
        } else {  
            Log.d(tag, content);  
        }
        if (isSaveLog) {
            point(LOG_PATH, tag, content);
        }
    }  

    public static void d(String content, Throwable e) {  
        if (!allowD) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.d(tag, content, e);  
        } else {  
            Log.d(tag, content, e);  
        }
        if (isSaveLog) {
            point(LOG_PATH, tag, e.getMessage());
        }
    }  

    public static void e(String content) {  
        if (!allowE) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.e(tag, content);  
        } else {  
            Log.e(tag, content);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void e(Throwable e) {  
        if (!allowE) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.e(tag, "error", e);  
        } else {  
            Log.e(tag, e.getMessage(), e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, e.getMessage());  
        }  
    }  

    public static void e(String content, Throwable e) {  
        if (!allowE) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.e(tag, content, e);  
        } else {  
            Log.e(tag, content, e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, e.getMessage());  
        }  
    }  

    public static void i(String content) {  
        if (!allowI) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.i(tag, content);  
        } else {  
            Log.i(tag, content);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void i(String content, Throwable e) {  
        if (!allowI) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.i(tag, content, e);  
        } else {  
            Log.i(tag, content, e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void v(String content) {  
        if (!allowV) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.v(tag, content);  
        } else {  
            Log.v(tag, content);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void v(String content, Throwable e) {  
        if (!allowV) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.v(tag, content, e);  
        } else {  
            Log.v(tag, content, e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void w(String content) {  
        if (!allowW) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.w(tag, content);  
        } else {  
            Log.w(tag, content);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void w(String content, Throwable e) {  
        if (!allowW) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.w(tag, content, e);  
        } else {  
            Log.w(tag, content, e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void w(Throwable e) {  
        if (!allowW) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.w(tag, e);  
        } else {  
            Log.w(tag, e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, e.toString());  
        }  
    }  

    public static void wtf(String content) {  
        if (!allowWtf) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.wtf(tag, content);  
        } else {  
            Log.wtf(tag, content);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void wtf(String content, Throwable e) {  
        if (!allowWtf) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.wtf(tag, content, e);  
        } else {  
            Log.wtf(tag, content, e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, content);  
        }  
    }  

    public static void wtf(Throwable e) {  
        if (!allowWtf) {  
            return;  
        }  

        StackTraceElement caller = getCallerStackTraceElement();  
        String tag = generateTag(caller);  

        if (customLogger != null) {  
            customLogger.wtf(tag, e);  
        } else {  
            Log.wtf(tag, e);  
        }  
        if (isSaveLog) {  
            point(LOG_PATH, tag, e.toString());  
        }  
    }  

    private static StackTraceElement getCallerStackTraceElement() {  
        return Thread.currentThread().getStackTrace()[4];  
    }  

    public static void point(String path, String tag, String msg) {
        if (isSDAva()) {

            Date date=new Date();  DateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String time_c=format.format(date);//time就是当前时间

            String sp_name;
            try {
                sp_name = tag.substring(0, tag.indexOf("$"));
            }catch (Exception e){
                sp_name = tag.substring(0,tag.indexOf("."));
            }

            SpUtil rw = new SpUtil();

            String info = rw.getValue(MyApplication.getAppContext(),sp_name,LOG_PROGRESS);
            if(info == null||info == "") {
                int i = 0;
                long timestamp = System.currentTimeMillis();
                String time = formatter.format(new Date());
                rw.writeValue(MyApplication.getAppContext(),sp_name, LOG_PROGRESS, i + "");
                rw.writeValue(MyApplication.getAppContext(),sp_name, LOG_FILENAME_TIME, time);
                rw.writeValue(MyApplication.getAppContext(),sp_name, LOG_FILENAME_TIMESTAMP, String.valueOf(timestamp));
                //String last_filename_time = rw.getValue(sp_name, LOG_FILENAME_TIME);
                //String last_filename_timestamp = rw.getValue(sp_name, LOG_FILENAME_TIMESTAMP);
            }

            long num = Integer.parseInt(rw.getValue(MyApplication.getAppContext(),sp_name,LOG_PROGRESS));

            if(num < LOG_NUMBER){

                String last_filename_time = rw.getValue(MyApplication.getAppContext(),sp_name, LOG_FILENAME_TIME);
                String last_filename_timestamp = rw.getValue(MyApplication.getAppContext(),sp_name, LOG_FILENAME_TIMESTAMP);
                String time = formatter.format(new Date());

                path = path +"/logs/log-" + sp_name + "-" + last_filename_time + "-" + last_filename_timestamp + ".log";

                //LogUtil.i(path);
                //LogUtil.i(LOG_PATH);

                File file = new File(path);
                if (!file.exists()) {
                    createDipPath(path);
                }

                BufferedWriter out = null;
                try {

                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                    out.write(time_c + " " + tag + " " + msg + "\r\n");

                   num = num +1;
                    //LogUtil.i(rw.getValue(sp_name,LOG_PROGRESS));


                    //LogUtil.i(tag.toString());
                    //FileSystem.Sdxie(path+"/log",time + " " + tag + " " + msg,sp_name);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }else{

                num = 0;
                long timestamp = System.currentTimeMillis();
                String time = formatter.format(new Date());

                path = path +"/logs/log-" + sp_name + "-" + time + "-" + timestamp + ".log";

                File file = new File(path);
                if (!file.exists()) {
                    createDipPath(path);
                }

                BufferedWriter out = null;
                try {

                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                    out.write(time_c + " " + tag + " " + msg + "\r\n");

                    num = num+1;
                    //LogUtil.i(rw.getValue(sp_name,LOG_PROGRESS));

                    rw.writeValue(MyApplication.getAppContext(),sp_name, LOG_FILENAME_TIME, time);
                    rw.writeValue(MyApplication.getAppContext(),sp_name, LOG_FILENAME_TIMESTAMP, String.valueOf(timestamp));

                    //LogUtil.i(tag.toString());
                    //FileSystem.Sdxie(path+"/log",time + " " + tag + " " + msg,sp_name);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            rw.writeValue(MyApplication.getAppContext(),sp_name, LOG_PROGRESS, num + "");


        }  
    }

    /** 
     * 根据文件路径 递归创建文件 
     */  
    public static void createDipPath(String file) {  
        String parentFile = file.substring(0, file.lastIndexOf("/"));  
        File file1 = new File(file);  
        File parent = new File(parentFile);  
        if (!file1.exists()) {  
            parent.mkdirs();  
            try {  
                file1.createNewFile();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  

    private static class ReusableFormatter {  

        private Formatter formatter;  
        private StringBuilder builder;  

        public ReusableFormatter() {  
            builder = new StringBuilder();  
            formatter = new Formatter(builder);  
        }  

        public String format(String msg, Object... args) {  
            formatter.format(msg, args);  
            String s = builder.toString();  
            builder.setLength(0);  
            return s;  
        }  
    }  

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {  
        @NonNull
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();  
        }  
    };  

    public static String format(String msg, Object... args) {  
        ReusableFormatter formatter = thread_local_formatter.get();  
        return formatter.format(msg, args);  
    }  

    private static boolean isSDAva() {  
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageDirectory().exists();  
    }
}  
