package com.autumn.framework.View.cycleviewpager.cache.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.autumn.reptile.R;
import com.autumn.framework.View.cycleviewpager.cache.manager.ThreadManager;
import com.autumn.framework.View.cycleviewpager.cache.utils.HttpUtils;

/**
 * 用于管理网络缓存的类
 *
 * @author HSW
 *
 */
public class NetCacheUtils {
	private FileCacheUtils fileCacheUtils;
	private Context mContext;
	private Handler mHandler;

	public NetCacheUtils(Context context, Handler handler,FileCacheUtils fileCacheUtils) {
		this.fileCacheUtils = fileCacheUtils;
		mContext = context;
		mHandler = handler;
	}

	public void loadBitmap(ImageView imageView, String url) {
		Runnable runnable = new BitmapLoadRunable(imageView, url);
		ThreadManager.getInstance().creatLongPool().execute(runnable);
	}

	/** 加载图片异步任务 */
	private class BitmapLoadRunable implements Runnable {
		private String mUrl;
		private ImageView mImageView;
		private Bitmap bitmap;

		public BitmapLoadRunable(ImageView imageView, String url) {
			mImageView = imageView;
			mUrl = url;
		}

		@Override
		public void run() {
			bitmap = HttpUtils.loadBitmap(mUrl);

			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (bitmap == null) {
						// 设置默认图片
						// mImageView.setImageResource();
						Context context;

						bitmap = BitmapFactory.decodeResource(mContext.getResources(),
								R.drawable.ic_launcher_main);
					}

					// System.out.println("从网络缓存中取");
					mImageView.setImageBitmap(bitmap);
					mImageView.setTag(mUrl);

					// 保存到FileCache中
					fileCacheUtils.put(mUrl, bitmap);

				}
			});

		}
	}
}

