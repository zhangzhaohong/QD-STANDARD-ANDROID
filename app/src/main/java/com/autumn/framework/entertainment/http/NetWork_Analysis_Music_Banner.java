package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Data_manager_7;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_7;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetWork_Analysis_Music_Banner
{
	
	private static int number = 10;

	private static int code;

	private static Data_manager_7 dataManager7;

	public static void Startprocessing(String data) throws JSONException
	{
		JSONObject json=new JSONObject(data);

		code = json.getInt("code");

		switch (code)
		{
			case 200:
				{
					JSONArray jsonarr= json.getJSONArray("data");

					if (jsonarr.length() == 0){
						Entertainment_fragement_7.Analysis_failed();
					}else {

						int i = 0;

						while (i < jsonarr.length()) {

							LogUtil.d("NetWork_Analysis_Music_Banner\n" +
									"{\n" +
									"NUM\n[" + i +
									"]\n" +
									"Info\n" +
									"[" + jsonarr.getJSONObject(i) +
									"]\n" +
									"}");

							JSONObject content = new JSONObject(jsonarr.getJSONObject(i).toString());

							String type = content.getString("type");

							String jump_info = content.getString("jump_info");

							String url = new JSONObject(jump_info).getString("url");

							String pic_info = content.getString("pic_info");

							String pic_url = new JSONObject(pic_info).getString("url");

							//name_all[i] = name;

							//System.out.println(i + "_info_name" + name);

							//String background = content.getString("pic");

							//background_all[i] = background;

							//System.out.println(i + "_info_background" + background);

							//String url = content.getString("id");

							if (i == 0) {
								dataManager7 = new Data_manager_7(jsonarr.length());
							}

							dataManager7.set_data(type, url, pic_url, i);

							Entertainment_fragement_7.Analysis_ok();

							LogUtil.i("NetWork_Analysis_Music_Banner\n" +
									"{\n" +
									"NUM\n[" + i +
									"]\n" +
									"Type\n[" + type +
									"]\n" +
									"Url\n[" + url +
									"]\n" +
									"PicUrl\n[" + pic_url +
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
