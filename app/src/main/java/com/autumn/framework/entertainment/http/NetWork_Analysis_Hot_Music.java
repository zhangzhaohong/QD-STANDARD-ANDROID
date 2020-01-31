package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Data_manager_7_hotMusic;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_5;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_7;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetWork_Analysis_Hot_Music
{
	
	private static int number = 10;

	private static int code;
	
	private static Data_manager_7_hotMusic dataManager7;

	public static void Startprocessing(int page, String data) throws JSONException
	{
		JSONObject json=new JSONObject(data);

		code = json.getInt("code");

		switch (code)
		{
			case 200:
				{
					JSONArray jsonarr= json.getJSONArray("data");

					if (jsonarr.length() == 0){
						Entertainment_fragement_5.Analysis_failed(page);
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

							if (i == 0 && page <= 1)

								dataManager7 = new Data_manager_7_hotMusic(jsonarr.length());

							if (i == 0 && page > 1)
								dataManager7.setTemp_number(jsonarr.length());

							dataManager7.set_data(page, name, singer, background, url, i);

							Entertainment_fragement_7.Analysis_ok_HotMusic(page);

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
