package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Data_manager_6;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_6;

import org.json.JSONException;
import org.json.JSONObject;

public class NetWork_Analysis_Daily
{

	private static int code;

	public static void Startprocessing(String data) throws JSONException
	{
		JSONObject json=new JSONObject(data);

		code = json.getInt("code");

		switch (code)
		{

			case 200:
				{
					JSONObject jsonObject = json.getJSONObject("data");
					String pic_url = jsonObject.getString("img_url");
					String volume = jsonObject.getString("volume");
					String post_date = jsonObject.getString("post_date");
					String forward = jsonObject.getString("forward");
					String words_info = jsonObject.getString("words_info");
					Data_manager_6 data_manager_6 = new Data_manager_6(pic_url, volume, post_date, forward, words_info);
					LogUtil.i("NetWork_Analysis_Daily\n" +
							"{\n" +
							"pic_url\n[" + pic_url +
							"]\n" +
							"volume\n[" + volume +
							"]\n" +
							"post_date\n[" + post_date +
							"]\n" +
							"forward\n[" + forward +
							"]\n" +
							"words_info\n[" + words_info +
							"]\n" +
							"}");

					Entertainment_fragement_6.Analysis_ok();
					
					//System.out.println("name_" + Arrays.toString(name_all));

					//System.out.println("background_" + Arrays.toString(background_all));
					
					//System.out.println("url_" + Arrays.toString(url_all));
					
					break;
				}
			}
		}
	
}
