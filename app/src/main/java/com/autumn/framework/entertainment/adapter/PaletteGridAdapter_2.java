package com.autumn.framework.entertainment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autumn.framework.entertainment.manager.Data_manager_2;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.autumn.reptile.app_interface.EntertainmentFragement;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Arrays;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.view.listener.OnBigImageClickListener;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;
import cc.shinichi.library.view.listener.OnBigImagePageChangeListener;
import cc.shinichi.library.view.listener.OnOriginProgressListener;
import cn.hotapk.fastandrutils.utils.FToastUtils;

import static cc.shinichi.library.view.ImagePreviewActivity.TAG;

/**
 * @创建者 CSDN_LQR
 * @描述 调色板网格适配器
 */
public class PaletteGridAdapter_2 extends RecyclerView.Adapter<PaletteGridAdapter_2.PaletteGridViewHolder> {

    final String[] imgURL = Data_manager_2.getBackground();
    final String[] name = Data_manager_2.getName();
    final String[] url = Data_manager_2.getUrl();
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
                .load(url[position])
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
                            FToastUtils.init().setRoundRadius(30).show("地址不合规，无法打开！");
                        } else {
                            ImagePreview.getInstance()
                                    // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
                                    .setContext(EntertainmentFragement.getFragementContext())
                                    // 从第几张图片开始，索引从0开始哦~
                                    .setIndex(getAdapterPosition())

                                    //=================================================================================================
                                    // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                                    // 1：第一步生成的imageInfo List
                                    //.setImageInfoList(imageInfoList)

                                    // 2：直接传url List
                                    .setImageList(Arrays.asList(url))

                                    // 3：只有一张图片的情况，可以直接传入这张图片的url
                                    //.setImage(String image)
                                    //=================================================================================================

                                    // 加载策略，默认为手动模式
                                    .setLoadStrategy(ImagePreview.LoadStrategy.NetworkAuto)

                                    // 保存的文件夹名称，会在SD卡根目录进行文件夹的新建。
                                    // (你也可设置嵌套模式，比如："BigImageView/Download"，会在SD卡根目录新建BigImageView文件夹，并在BigImageView文件夹中新建Download文件夹)
                                    .setFolderName("com.autumn.reptile/PicDownload")

                                    // 缩放动画时长，单位ms
                                    .setZoomTransitionDuration(300)

                                    // 是否显示加载失败的Toast
                                    .setShowErrorToast(false)

                                    // 是否启用点击图片关闭。默认启用
                                    .setEnableClickClose(true)
                                    // 是否启用下拉关闭。默认不启用
                                    .setEnableDragClose(false)
                                    // 是否启用上拉关闭。默认不启用
                                    .setEnableUpDragClose(true)
                                    // 是否显示关闭页面按钮，在页面左下角。默认不显示
                                    .setShowCloseButton(false)
                                    // 设置关闭按钮图片资源，可不填，默认为库中自带：R.drawable.ic_action_close
                                    .setCloseIconResId(R.drawable.ic_action_close)

                                    // 是否显示下载按钮，在页面右下角。默认显示
                                    .setShowDownButton(true)
                                    // 设置下载按钮图片资源，可不填，默认为库中自带：R.drawable.icon_download_new
                                    .setDownIconResId(R.drawable.icon_download_new)

                                    // 设置是否显示顶部的指示器（1/9）默认显示
                                    .setShowIndicator(true)

                                    // 设置失败时的占位图，默认为库中自带R.drawable.load_failed，设置为 0 时不显示
                                    .setErrorPlaceHolder(R.drawable.load_failed)

                                    // 点击回调
                                    .setBigImageClickListener(new OnBigImageClickListener() {
                                        @Override public void onClick(View view, int position) {
                                            // ...
                                            Log.d(TAG, "onClick: ");
                                        }
                                    })
                                    // 长按回调
                                    .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                                        @Override public boolean onLongClick(View view, int position) {
                                            // ...
                                            Log.d(TAG, "onLongClick: ");
                                            return false;
                                        }
                                    })
                                    // 页面切换回调
                                    .setBigImagePageChangeListener(new OnBigImagePageChangeListener() {
                                        @Override
                                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                            Log.d(TAG, "onPageScrolled: ");
                                        }

                                        @Override public void onPageSelected(int position) {
                                            Log.d(TAG, "onPageSelected: ");
                                        }

                                        @Override public void onPageScrollStateChanged(int state) {
                                            Log.d(TAG, "onPageScrollStateChanged: ");
                                        }
                                    })

                                    //=================================================================================================
                                    // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
                                    .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {
                                        @Override public void progress(View parentView, int progress) {
                                            Log.d(TAG, "progress: " + progress);

                                            // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：
                                            ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
                                            TextView textView = parentView.findViewById(R.id.sh_progress_text);
                                            progressBar.setProgress(progress);
                                            String progressText = progress + "%";
                                            textView.setText(progressText);
                                        }

                                        @Override public void finish(View parentView) {
                                            Log.d(TAG, "finish: ");
                                        }
                                    })

                                    // 使用自定义百分比样式，传入自己的布局，并设置回调，再根据parentView找到进度控件进行百分比的设置：
                                    //.setProgressLayoutId(R.layout.image_progress_layout_theme_1, new OnOriginProgressListener() {
                                    //    @Override public void progress(View parentView, int progress) {
                                    //        Log.d(TAG, "progress: " + progress);
                                    //
                                    //        ProgressBar progressBar = parentView.findViewById(R.id.progress_horizontal);
                                    //        progressBar.setProgress(progress);
                                    //    }
                                    //
                                    //    @Override public void finish(View parentView) {
                                    //        Log.d(TAG, "finish: ");
                                    //    }
                                    //})
                                    //=================================================================================================

                                    // 开启预览
                                    .start();
                            //mContext = MyApplication.getAppContext();
                            //Intent intent = new Intent(mContext, X5WebGameActivity.class);
                            //Bundle bundle = new Bundle();
                            //bundle.putString("key", url[getAdapterPosition()]);
                            //intent.putExtras(bundle);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //mContext.startActivity(intent);
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
