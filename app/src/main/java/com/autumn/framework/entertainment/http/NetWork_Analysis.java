package com.autumn.framework.entertainment.http;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.entertainment.manager.Data_manager_1;
import com.autumn.framework.entertainment.manager.Data_manager_2;
import com.autumn.framework.entertainment.manager.Data_manager_3;
import com.autumn.framework.entertainment.manager.Data_manager_4;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_1;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_2;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_3;
import com.autumn.reptile.entertainment_fragement.Entertainment_fragement_4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetWork_Analysis
{
	
	private static int number = 10;

	private static int code;
	
	private static String data = "{\"code\":200,\"message\":\"成功!\",\"result\":[{\"sid\":\"28934242\",\"text\":\"签装修合同时这样问，避开报价陷阱！收藏转发！\",\"type\":\"video\",\"thumbnail\":\"http://wimg.spriteapp.cn/picture/2018/1125/5bfa9aec8b5d9__b.jpg\",\"video\":\"http://wvideo.spriteapp.cn/video/2018/1125/5bfa9aecb5608_wpd.mp4\",\"images\":null,\"up\":\"110\",\"down\":\"7\",\"forward\":\"11\",\"comment\":\"9\",\"uid\":\"19634336\",\"name\":\"全球领先在线视频 [OMEGA帮派]\",\"header\":\"http://wimg.spriteapp.cn/profile/large/2018/08/06/5b6742613dbe7_mini.jpg\",\"top_comments_content\":null,\"top_comments_voiceuri\":null,\"top_comments_uid\":null,\"top_comments_name\":null,\"top_comments_header\":null,\"passtime\":\"2018-11-27 02:15:02\"},{\"sid\":\"28896320\",\"text\":\"集麻辣鲜香嫩爽于一身的口水鸡，吃了会不由自主流口水\",\"type\":\"video\",\"thumbnail\":\"http://wimg.spriteapp.cn/picture/2018/1120/28896320_616.jpg\",\"video\":\"http://wvideo.spriteapp.cn/video/2018/1120/5bf3b45111db4_wpd.mp4\",\"images\":null,\"up\":\"199\",\"down\":\"26\",\"forward\":\"15\",\"comment\":\"38\",\"uid\":\"22897619\",\"name\":\"喊菜哥啊\",\"header\":\"http://tvax2.sinaimg.cn/crop.0.5.668.668.50/006Uvynply8fpj46bzrsbj30iw0iw0tf.jpg\",\"top_comments_content\":null,\"top_comments_voiceuri\":null,\"top_comments_uid\":null,\"top_comments_name\":null,\"top_comments_header\":null,\"passtime\":\"2018-11-22 21:48:01\"},{\"sid\":\"29182469\",\"text\":\"什么歌都能被你唱骚\",\"type\":\"video\",\"thumbnail\":\"http://wimg.spriteapp.cn/picture/2019/0203/5c567be4f3cec_wpd.jpg\",\"video\":\"http://wvideo.spriteapp.cn/video/2019/0203/5c567be4f3cec_wpd.mp4\",\"images\":null,\"up\":\"252\",\"down\":\"18\",\"forward\":\"2\",\"comment\":\"49\",\"uid\":\"17349056\",\"name\":\"韩语教师跑调的音乐人\",\"header\":\"http://wimg.spriteapp.cn/profile/large/2018/11/20/5bf370d020096_mini.jpg\",\"top_comments_content\":\"丑是丑了点，好在骚劲够了\",\"top_comments_voiceuri\":\"\",\"top_comments_uid\":\"12146881\",\"top_comments_name\":\"牛黄改毒片\",\"top_comments_header\":\"http://wimg.spriteapp.cn/profile/large/2015/07/25/55b2d8a9ceadc_mini.jpg\",\"passtime\":\"2019-02-04 23:40:03\"},{\"sid\":\"29178575\",\"text\":\"良心老板来了，注意别让老婆看到\",\"type\":\"video\",\"thumbnail\":\"http://wimg.spriteapp.cn/picture/2019/0201/5c546767571b0_wpd.jpg\",\"video\":\"http://wvideo.spriteapp.cn/video/2019/0201/5c546767571b0_wpd.mp4\",\"images\":null,\"up\":\"388\",\"down\":\"12\",\"forward\":\"9\",\"comment\":\"24\",\"uid\":\"20686841\",\"name\":\"牛啊\",\"header\":\"http://wimg.spriteapp.cn/profile/large/2018/10/06/5bb8c33cdd8fd_mini.jpg\",\"top_comments_content\":\"年终奖:兰博基尼5元抵用券一张\",\"top_comments_voiceuri\":\"\",\"top_comments_uid\":\"18031370\",\"top_comments_name\":\"打败小清新\",\"top_comments_header\":\"http://wimg.spriteapp.cn/profile/large/2018/01/25/5a69e7e363dd0_mini.jpg\",\"passtime\":\"2019-02-02 22:06:02\"},{\"sid\":\"29262358\",\"text\":\"猜猜她是谁…？\",\"type\":\"video\",\"thumbnail\":\"http://wimg.spriteapp.cn/picture/2019/0302/5c7a438fc1bea_wpd.jpg\",\"video\":\"http://uvideo.spriteapp.cn/video/2019/0302/5c7a438fc1bea_wpd.mp4\",\"images\":null,\"up\":\"257\",\"down\":\"9\",\"forward\":\"2\",\"comment\":\"27\",\"uid\":\"20540438\",\"name\":\"风从这边来\",\"header\":\"http://wimg.spriteapp.cn/profile/large/2017/04/14/58f0c4db27114_mini.jpg\",\"top_comments_content\":\"会长大人…\",\"top_comments_voiceuri\":\"\",\"top_comments_uid\":\"19740975\",\"top_comments_name\":\"我本丶沉默\",\"top_comments_header\":\"http://wimg.spriteapp.cn/profile/large/2019/02/15/5c6681a9e7686_mini.jpg\",\"passtime\":\"2019-03-02 21:06:01\"}]}";
	private static Data_manager_1 dataManager1;
	private static Data_manager_2 dataManager2;
	private static Data_manager_3 dataManager3;
	private static Data_manager_4 dataManager4;

	public static void Startprocessing(String data) throws JSONException
	{
		JSONObject json=new JSONObject(data);

		code = json.getInt("code");

		switch (code)
		{

			case 200:
				{
					JSONArray jsonarr= json.getJSONArray("result");
					
					int i = 0;
					
					while (i < number){
						
						LogUtil.d("NetWork_Analysis\n" +
										"{\n" +
								        "NUM\n[" + i +
								        "]\n" +
										"Info\n" +
										"[" + jsonarr.getJSONObject(i) +
										"]\n" +
										"}");
						
						JSONObject content=new JSONObject(jsonarr.getJSONObject(i).toString());

						String type = content.getString("type");
						
						String name = content.getString("text");
						
						//name_all[i] = name;
						
						//System.out.println(i + "_info_name" + name);
						
						String background = content.getString("thumbnail");
						
						//background_all[i] = background;
						
						//System.out.println(i + "_info_background" + background);
						
						String v_url = content.getString("video");
						String i_url = content.getString("images");
						String url;
						if (!v_url.equals("null") && type.equals("video")){
							url = v_url;
						}else if (!i_url.equals("null") && type.equals("image")){
							url = i_url;
						}else if (!i_url.equals("null") && type.equals("gif")){
							url = i_url;
						}else {
							url = "";
						}
						//String url = content.getString("video");
						
						//url_all[i] = url;

						switch (type){
							case "video":
								if (i == 0)
									dataManager1 = new Data_manager_1();
								dataManager1.setData(name, background, url, i);
								Entertainment_fragement_1.Analysis_ok();
								break;
							case "image":
								if (i == 0)
									dataManager2 = new Data_manager_2();
								dataManager2.setData(name, background, url, i);
								Entertainment_fragement_2.Analysis_ok();
								break;
							case "gif":
								if (i == 0)
									dataManager3 = new Data_manager_3();
								dataManager3.setData(name, background, url, i);
								Entertainment_fragement_3.Analysis_ok();
								break;
							case "text":
								if (i == 0)
									dataManager4 = new Data_manager_4();
								dataManager4.setData(name, i);
								Entertainment_fragement_4.Analysis_ok();
								break;
						}
						
						LogUtil.i("NetWork_Analysis\n" +
								"{\n" +
								"TYPE\n[" + type +
								"]\n" +
								"NUM\n[" + i +
								"]\n" +
								"Name\n[" + name +
								"]\n" +
								"Background\n[" + background +
								"]\n" +
								"Url\n[" + url +
								"]\n" +
								"}");
						//System.out.println(i + "_info_url" + url);
						
						i += 1;
						
					}
					
					//System.out.println("name_" + Arrays.toString(name_all));

					//System.out.println("background_" + Arrays.toString(background_all));
					
					//System.out.println("url_" + Arrays.toString(url_all));
					
					break;
				}
			}
		}
	
}
