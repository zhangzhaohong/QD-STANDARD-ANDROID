package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Data_manager_5;
import com.autumn.framework.entertainment.manager.Host_manager;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NetWork_Analysis_Music_Tencent
{
	
	private static int number = 10;

	private static int code;
	
	private static Data_manager_5 dataManager5;
	private static String singer;

	public static void Startprocessing(int page, String data) throws JSONException
	{
		JSONObject json=new JSONObject(data);

		code = json.getInt("code");

		switch (code)
		{

			case 200:
			{
				JSONArray jsonarr= new JSONObject(json.getString("data")).getJSONArray("list");

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

						String name = content.getString("songname");

						String song_mid = content.getString("songmid");

						//JSONObject info_al = new JSONObject(content.getString("al"));

						String background = Host_manager.getMusicHost() + "/tencent/pic?id=" + getURLEncoderString(song_mid);

						JSONArray info_singer = new JSONArray(content.getString("singer"));

						singer = "";

						for (int singer_i = 0; singer_i < info_singer.length(); singer_i ++) {

							if (singer_i == 0) {
								singer = new JSONObject(info_singer.getJSONObject(singer_i).toString()).getString("name");
							}else {
								singer = singer + "," + new JSONObject(info_singer.getJSONObject(singer_i).toString()).getString("name");
							}
							//String singer_id = info_ar.getString("id");

						}

						//JSONObject privilege = new JSONObject(content.getString("privilege"));

						//String max_br = privilege.getString("maxbr");

						//String song_id = privilege.getString("id");

						if (i == 0 && page <= 1)
							dataManager5 = new Data_manager_5(jsonarr.length());

						if (i == 0 && page > 1)
							dataManager5.setTemp_number(jsonarr.length());

						dataManager5.set_data(page, name, singer, background, song_mid, i);

						Entertainment_fragement_5.Analysis_ok(page);

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
								"Url\n[" + song_mid +
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

	public	static String getURLEncoderString(String str) {
		String result = "";
		if(null == str) {
			return"";
		}
		try
		{
			String ENCODE = null;
			result = URLEncoder.encode(str, "UTF8");
		}
		catch
				(UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}
	
}
