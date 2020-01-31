package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Data_manager_kg_mv;
import com.autumn.framework.entertainment.manager.Data_manager_migu_mv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.hotapk.fastandrutils.utils.FToastUtils;

public class NetWork_Analysis_Music_Migu_MV
{
	
	private static int number = 10;

	private static int code;

	private static Data_manager_kg_mv dataManagerKgMv;

	private static Data_manager_migu_mv dataManagerMiguMv;

	public static void Startprocessing(int page, String data) throws JSONException
	{
		JSONObject json=new JSONObject(data);

		LogUtil.d("NetWork_Analysis_Mv_migu[status]:start");

		code = json.getInt("code");

		switch (code)
		{

			case 200:
			{
				JSONArray jsonarr= new JSONObject(json.getString("data")).getJSONArray("resultList");

				LogUtil.d("NetWork_Analysis_Mv_migu[json]" + jsonarr);

				if (jsonarr.length() == 0){
					FToastUtils.init().show("MV获取失败");
				}else {

					int i = 0;

					//KuGouMvBean kugouMvBean = new KuGouMvBean();

					//ArrayList<KuGouMvBean.KugouMvBean> kugouMvBeans = new ArrayList<>();

					while (i < jsonarr.length()) {

						LogUtil.d("NetWork_Analysis_Mv_migu\n" +
								"{\n" +
								"NUM\n[" + i +
								"]\n" +
								"Info\n" +
								"[" + jsonarr.get(i).toString() +
								"]\n" +
								"}");

						JSONObject content = new JSONObject(jsonarr.get(i).toString());
						//JSONObject content = new JSONObject(jsonarr.getJSONArray(i).toString());

						//LogUtil.d("NetWork_Analysis_Mv_migu" + content);

						String mv_id = "";

						if (content.toString().contains("mvList")){

							JSONArray MVlist = new JSONArray(content.getString("mvList"));

							LogUtil.d("NetWork_Analysis_Mv_migu" + MVlist);

							LogUtil.d("NetWork_Analysis_Mv_migu" + MVlist.get(0).toString());

							mv_id = new JSONObject(MVlist.get(0).toString()).getString("copyrightId");

						}
						//String name = content.getString("songname");

						//String hash = content.getString("hash");

						//String mvhash = content.getString("mvhash");

						//JSONObject info_al = new JSONObject(content.getString("al"));

						//String background = Host_manager.getMusicHost() + "/kugou/pic?id=" + getURLEncoderString(hash);

						//KuGouMvBean.KugouMvBean MvBean = new KuGouMvBean.KugouMvBean();
						//MvBean.setMvhash(mvhash);
						//kugouMvBeans.add(MvBean);

						//JSONArray info_singer = new JSONArray(content.getString("singer"));

						//singer = "";

						/*for (int singer_i = 0; singer_i < info_singer.length(); singer_i ++) {

							if (singer_i == 0) {
								singer = new JSONObject(info_singer.getJSONObject(singer_i).toString()).getString("name");
							}else {
								singer = singer + "," + new JSONObject(info_singer.getJSONObject(singer_i).toString()).getString("name");
							}
							//String singer_id = info_ar.getString("id");

						}*/

						//String singer = content.getString("singername");

						//JSONObject privilege = new JSONObject(content.getString("privilege"));

						//String max_br = privilege.getString("maxbr");

						//String song_id = privilege.getString("id");

						if (i == 0 && page <= 1) {
							//dataManager5 = new Data_manager_5(jsonarr.length());
							dataManagerMiguMv = new Data_manager_migu_mv(jsonarr.length());
						}

						if (i == 0 && page > 1) {
							//dataManager5.setTemp_number(jsonarr.length());
							dataManagerMiguMv = new Data_manager_migu_mv(jsonarr.length());
						}

						//dataManager5.set_data(page, name, singer, background, hash, i);

						dataManagerKgMv.set_data(page, mv_id, i);

						//Entertainment_fragement_5.Analysis_ok(page);

						/*LogUtil.i("NetWork_Analysis_Music\n" +
								"{\n" +
								"NUM\n[" + i +
								"]\n" +
								"Name\n[" + name +
								"]\n" +
								"Singer\n[" + singer +
								"]\n" +
								"Background\n[" + background +
								"]\n" +
								"Url\n[" + hash +
								"]\n" +
								"}");*/
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
