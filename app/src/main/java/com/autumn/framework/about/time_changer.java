package com.autumn.framework.about;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class time_changer
{
	
	/* 
     * 将时间转换为时间戳
     */ public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
	
	/* 
     * 将时间戳转换为时间
     */ public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
	
}
