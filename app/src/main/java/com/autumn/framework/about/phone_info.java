package com.autumn.framework.about;

import android.os.Build;

public class phone_info
{
	
	public String get_info(int type){
		String result = "";
		if(type == 1){
			result = Build.BOARD; // 主板
			return result;
			}
		if(type == 2){
			result = Build.BRAND; // 系统定制商
			return result;
			}
		if(type == 3){
			result = Build.DEVICE; // 设备参数
			return result;
		}
		if(type == 4){
			result = Build.DISPLAY; // 显示屏参数
			return result;
		}
		if(type == 5){
			result = Build.SERIAL; // 硬件序列号
			return result;
		}
		if(type == 6){
			result = Build.ID; // 修订版本列表
			return result;
		}
		if(type == 7){
			result = Build.MANUFACTURER; // 硬件制造商
			return result;
		}
		if(type == 8){
			result = Build.MODEL; //版本
			return result;
		}
		if(type == 9){
			result = Build.HARDWARE; //硬件名
			return result;
		}
		if(type == 10){
			result = Build.PRODUCT; //手机产品名
			return result;
		}
		if(type == 11){
			result = Build.TAGS; // 描述build的标签
			return result;
		}
		if(type == 12){
			result = Build.TYPE; // Builder类型
			return result;
		}
		if(type == 13){
			result = Build.VERSION.CODENAME; //当前开发代号
			return result;
		}
		if(type == 14){
			result = Build.VERSION.INCREMENTAL; //源码控制版本号
			return result;
		}
		if(type == 15){
			result = Build.VERSION.RELEASE; //版本字符串
			return result;
		}
		if(type == 16){
			result = Build.HOST; // HOST值
			return result;
		}
		if(type == 18){
			result = Build.USER; // User名
			return result;
		}
		if(type == 19){
			result = String.valueOf(Build.VERSION.SDK_INT); //版本号
			return result;
		}
		if(type == 20){
			result = time_changer.stampToDate(Build.TIME+"");//编译时间
			return result;
		}
			
		return null;
	}
	
}
