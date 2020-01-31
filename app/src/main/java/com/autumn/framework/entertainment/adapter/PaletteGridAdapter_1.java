package com.autumn.framework.entertainment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autumn.framework.X5WebView.ui.X5WebGameActivity;
import com.autumn.framework.entertainment.manager.Data_manager_1;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * @创建者 CSDN_LQR
 * @描述 调色板网格适配器
 */
public class PaletteGridAdapter_1 extends RecyclerView.Adapter<PaletteGridAdapter_1.PaletteGridViewHolder> {

    final String[] imgURL = Data_manager_1.getBackground();
    final String[] name = Data_manager_1.getName();
    final String[] url = Data_manager_1.getUrl();
    /*final int[] picResId = new int[]{R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p43, R.mipmap.p44};*/

    @Override
    public PaletteGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_palette, parent, false);
        return new PaletteGridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaletteGridViewHolder holder, final int position) {
        RequestOptions options = new RequestOptions();
        //options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(R.drawable.aio_image_default);
        options.error(R.drawable.empty_photo);
        Glide.with(MyApplication.getAppContext())
                .load(imgURL[position])
                .apply(options)
                .into(holder.mIvPic);
        //holder.mIvPic.setImageResource(picResId[position]);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.mIvPic.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // 对于一张图片，它可能分析不出来暗、亮色，返回值为空，我这里采取的方案是当获取不到色调样品，则获取其他色调样品。
                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                if (swatch == null) {
                    swatch = palette.getDarkVibrantSwatch();
                }
                if (swatch == null) {
                    swatch = palette.getLightMutedSwatch();
                }
                if (swatch == null) {
                    swatch = palette.getLightVibrantSwatch();
                }
                if (swatch == null) {
                    swatch = palette.getMutedSwatch();
                }
                if (swatch == null) {
                    swatch = palette.getVibrantSwatch();
                }
                int titleTextColor = swatch.getTitleTextColor();
                int rgb = swatch.getRgb();

                holder.mTvTitle.setText(name[position]);
                holder.mTvTitle.setTextColor(titleTextColor);
                holder.mTvTitle.setBackgroundColor(generateTransparentColor(0.5f, rgb));

            }
        });
    }

    private int generateTransparentColor(float percent, int rgb) {
        int red = Color.red(rgb);
        int green = Color.green(rgb);
        int blue = Color.blue(rgb);
        int alpha = Color.alpha(rgb);
        alpha = (int) (percent * alpha);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public int getItemCount() {
        return imgURL.length;
    }

    class PaletteGridViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvPic;
        TextView mTvTitle;
        private Context mContext;

        public PaletteGridViewHolder(View itemView) {
            super(itemView);
            mIvPic = (ImageView) itemView.findViewById(R.id.ivPic);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mIvPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (url[getAdapterPosition()] == null){
                        FToastUtils.init().setRoundRadius(30).show("数据未加载或无法加载数据，请手动刷新！");
                    }else {
                        if (url[getAdapterPosition()].equals("")) {
                            FToastUtils.init().setRoundRadius(30).show("地址不合规，无法播放！");
                        } else {
                            mContext = MyApplication.getAppContext();
                            Intent intent = new Intent(mContext, X5WebGameActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("key", url[getAdapterPosition()]);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
            mTvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name[getAdapterPosition()] == null){
                        FToastUtils.init().setRoundRadius(30).show("数据未加载或无法加载数据，请手动刷新！");
                    }else {
                        if (name[getAdapterPosition()].equals("")){
                            FToastUtils.init().setRoundRadius(30).show("标题解析异常或标题为空！");
                        }else {
                            FToastUtils.init().setRoundRadius(30).showLong(name[getAdapterPosition()]);
                        }
                    }
                }
            });
        }
    }
}
