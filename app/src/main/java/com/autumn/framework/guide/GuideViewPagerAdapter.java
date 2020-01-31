package com.autumn.framework.guide;

/**
 * Created by zhang on 2018/4/1.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autumn.reptile.MyApplication;
import com.bumptech.glide.Glide;

import java.util.List;

public class GuideViewPagerAdapter extends PagerAdapter {
    private List<View> views;

    public GuideViewPagerAdapter(List<View> views) {
        super();
        this.views = views;
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //ImageView guide_image = (ImageView)views.get(position).findViewById(R.id.guide_image);
        //releaseImageViewResouce(guide_image);
        Glide.get(MyApplication.getAppContext()).clearMemory();
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

    /**

     * 释放图片资源的方法

     * @param imageView

     */

    public void releaseImageViewResouce(ImageView imageView) {

        if (imageView == null) return;

        Drawable drawable = imageView.getDrawable();

        if (drawable != null && drawable instanceof BitmapDrawable) {

            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

            Bitmap bitmap = bitmapDrawable.getBitmap();

            if (bitmap != null && !bitmap.isRecycled()) {

                bitmap.recycle();

                bitmap=null;

            }

        }

        System.gc();

    }

}
