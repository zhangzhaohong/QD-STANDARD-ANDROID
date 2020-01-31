package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.activity.Music_list_activity;
import com.autumn.framework.entertainment.manager.Data_manager_Music_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetWork_Analysis_Music_List
{
	
	private static int number = 10;

	private static int code;
	
	private static Data_manager_Music_List data;

	public static void Startprocessing(String JsonData) throws JSONException
	{
		JSONObject json=new JSONObject(JsonData);

		code = json.getInt("code");

		switch (code)
		{
			case 200:
				{
					JSONArray jsonarr= json.getJSONArray("data");

					if (jsonarr.length() == 0){
						Music_list_activity.Analysis_failed();
					}else {

						int i = 0;

						while (i < jsonarr.length()) {

							LogUtil.d("NetWork_Analysis_Music\n" +
									"{\n" +
									"NUM\n[" + i +
									"]\n" +
									"Info\n" +
									"[" + jsonarr.getJSONObject(i) +
									"]\n" +
									"}");

							JSONObject content = new JSONObject(jsonarr.getJSONObject(i).toString());

							String name = content.getString("name");

							String singer = content.getString("singer");

							//name_all[i] = name;

							//System.out.println(i + "_info_name" + name);

							String background = content.getString("pic");

							//background_all[i] = background;

							//System.out.println(i + "_info_background" + background);

							String url = content.getString("id");

							if (i == 0)
								data = new Data_manager_Music_List(jsonarr.length());

							data.set_data(name, singer, background, url, i);

							Music_list_activity.Analysis_ok();

							LogUtil.i("NetWork_Analysis_Music\n" +
									"{\n" +
									"NUM\n[" + i +
									"]\n" +
									"Name\n[" + name +
									"]\n" +
									"Singer\n[" + singer +
									"]\n" +
									"Background\n[" + background +
									"]\n" +
									"Url\n[" + url +
									"]\n" +
									"}");
							//System.out.println(i + "_info_url" + url);

							i += 1;

						}

					}
					
					//System.out.println("name_" + Arrays.toString(name_all));

					//System.out.println("background_" + Arrays.toString(background_all));
					
					//System.out.println("url_" + Arrays.toString(url_all));
					
					break;
				}
			}
		}
	
}
