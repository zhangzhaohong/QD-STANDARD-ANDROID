package com.autumn.framework.entertainment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autumn.framework.data.NetworkChecker;
import com.autumn.framework.data.SpUtil;
import com.autumn.framework.entertainment.OkHttpDownloader.DownManager;
import com.autumn.framework.entertainment.manager.Data_manager_5;
import com.autumn.framework.entertainment.manager.Host_manager;
import com.autumn.framework.entertainment.manager.Music_player_manager;
import com.autumn.framework.entertainment.music_player.MusicPlayerActivity;
import com.autumn.framework.entertainment.runtime.Music_Checker_runtime;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.autumn.reptile.app_interface.EntertainmentFragement;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * @创建者 CSDN_LQR
 * @描述 调色板网格适配器
 */
public class PaletteGridAdapter_5 extends RecyclerView.Adapter<PaletteGridAdapter_5.PaletteGridViewHolder> {

    static String[] name = new String[0];
    static String[] singer = new String[0];
    static String[] imgURL = new String[0];
    static String[] url = new String[0];
    private QMUITipDialog tipDialog;
    private String MUSIC_SERVICE;
    private String MUSIC_CONFIG = "Music_config";
    private String NETEASE = "Netease";
    private String TENCENT = "Tencent";
    private String KUGOU = "Kugou";
    private String KUWO = "Kuwo";
    private String Netease_quality;
    private String Tencent_quality;
    private String Kugou_quality;
    private String Kuwo_quality;
    private String key;
    private DownManager downManger;
    private String MIGU = "Migu";
    private String BAIDU = "Baidu";
    private String Migu_quality;
    private String Baidu_quality;
    //private boolean player_status = false;

    public static void refreshData() {
        name = Data_manager_5.getName();
        singer = Data_manager_5.getSinger();
        imgURL = Data_manager_5.getBackground();
        url = Data_manager_5.getUrl();
    }

    public static void initData() {
        name = new String[0];
        singer = new String[0];
        imgURL = new String[0];
        url = new String[0];
    }
    /*final int[] picResId = new int[]{R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p43, R.mipmap.p44};*/

    @Override
    public PaletteGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_palette_song, parent, false);
        return new PaletteGridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaletteGridViewHolder holder, final int position) {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(R.drawable.aio_image_default);
        options.error(R.drawable.empty_photo);
        Glide.with(MyApplication.getAppContext())
                .load(imgURL[position])
                .apply(options)
                .into(holder.song_image);
        //holder.mIvPic.setImageResource(picResId[position]);
        holder.song_name.setText(name[position]);
        holder.song_singer.setText(singer[position]);
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

        ImageView song_image;
        TextView song_name;
        TextView song_singer;
        private Context mContext;

        public PaletteGridViewHolder(View itemView) {
            super(itemView);
            song_image = (ImageView)itemView.findViewById(R.id.song_image);
            song_name = (TextView)itemView.findViewById(R.id.song_name);
            song_singer = (TextView)itemView.findViewById(R.id.song_singer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jump_to_Player();
                }
            });
            song_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jump_to_Player();
                }
            });
            song_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jump_to_Player();
                }
            });
            song_singer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jump_to_Player();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_click_operation();
                    //FToastUtils.init().setRoundRadius(30).show("Long Click");
                    return true;
                }
            });
            song_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_click_operation();
                    return true;
                }
            });
            song_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_click_operation();
                    return true;
                }
            });
            song_singer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long_click_operation();
                    return true;
                }
            });
        }

        private void jump_to_Player() {
            if (url[getAdapterPosition()] == null){
                FToastUtils.init().setRoundRadius(30).show("数据未加载或无法加载数据，请手动刷新！");
            }else {
                if (url[getAdapterPosition()].equals("")) {
                    FToastUtils.init().setRoundRadius(30).show("地址不合规，无法播放！");
                } else {
                    mContext = MyApplication.getAppContext();
                    /*if (Music_player_manager.getSearchMusicService() != null)
                        FToastUtils.init().setRoundRadius(30).show("地址不合规，无法播放！");
                    else if (Music_player_manager.getSearchMusicService().equals(""))
                        FToastUtils.init().setRoundRadius(30).show("地址不合规，无法播放！");
                    else
                        Music_player_manager.setMusicService(Music_player_manager.getSearchMusicService());*/
                    Intent intent = new Intent(mContext, MusicPlayerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", getAdapterPosition());
                    bundle.putString("from","SearchPage");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        }

        private void long_click_operation() {
            //FToastUtils.init().setRoundRadius(30).show("Long Click");
            showProgress(true);

            if (NetworkChecker.isVpnUsed()){

                if (tipDialog.isShowing())
                    tipDialog.dismiss();

                FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                //super.onBackPressed();

            }else if (NetworkChecker.isWifiProxy(EntertainmentFragement.getFragementContext())){
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                //super.onBackPressed();
            }else {
                getInfo(getAdapterPosition());
            }
        }

        private void getInfo(int id) {

            //status = 0;
            //MUSIC_SERVICE = Music_player_manager.getService();
            if (Music_player_manager.getService() == null){
                FToastUtils.init().setRoundRadius(30).show("服务异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else if (Music_player_manager.getService().equals("")){
                FToastUtils.init().setRoundRadius(30).show("服务异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else {
                switch (Music_player_manager.getService()){
                    case "0":
                        MUSIC_SERVICE = "netease";
                        break;
                    case "1":
                        MUSIC_SERVICE = "tencent";
                        break;
                    case "2":
                        MUSIC_SERVICE = "kugou";
                        break;
                    case "3":
                        MUSIC_SERVICE = "kuwo";
                        break;
                    case "4":
                        MUSIC_SERVICE = "migu";
                        break;
                    case "5":
                        MUSIC_SERVICE = "baidu";
                        break;
                }
            }
            final SpUtil rw = new SpUtil();
            switch (MUSIC_SERVICE){
                case "netease":
                    Netease_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, NETEASE);
                    if (Netease_quality == null){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, NETEASE, "320000");
                        Netease_quality = "320000";
                    }else if (Netease_quality.equals("")){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, NETEASE, "320000");
                        Netease_quality = "320000";
                    }
                    break;
                case "tencent":
                    Tencent_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, TENCENT);
                    if (Tencent_quality == null){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, TENCENT, "192");
                        Tencent_quality = "192";
                    }else if (Tencent_quality.equals("")){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, TENCENT, "192");
                        Tencent_quality = "192";
                    }
                    break;
                case "kugou":
                    Kugou_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUGOU);
                    if (Kugou_quality == null){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUGOU, "128");
                        Kugou_quality = "128";
                    }else if (Kugou_quality.equals("")){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUGOU, "128");
                        Kugou_quality = "128";
                    }
                    break;
                case "kuwo":
                    Kuwo_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUWO);
                    if (Kuwo_quality == null){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUWO, "128");
                        Kuwo_quality = "128";
                    }else if (Kuwo_quality.equals("")){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, KUWO, "128");
                        Kuwo_quality = "128";
                    }
                    break;
                case "migu":
                    Migu_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, MIGU);
                    if (Migu_quality == null){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, MIGU, "128");
                        Migu_quality = "128";
                    }else if (Migu_quality.equals("")){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, MIGU, "128");
                        Migu_quality = "128";
                    }
                    break;
                case "baidu":
                    Baidu_quality = rw.getValue(MyApplication.getAppContext(), MUSIC_CONFIG, BAIDU);
                    if (Baidu_quality == null){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, BAIDU, "128");
                        Baidu_quality = "128";
                    }else if (Baidu_quality.equals("")){
                        rw.writeValue(MyApplication.getAppContext(), MUSIC_CONFIG, BAIDU, "128");
                        Baidu_quality = "128";
                    }
            }
            if (Host_manager.getMusicHost() == null){
                FToastUtils.init().setRoundRadius(30).show("域名异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else if (Host_manager.getMusicHost().equals("")){
                FToastUtils.init().setRoundRadius(30).show("域名异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else {
                switch (MUSIC_SERVICE) {
                    case "netease":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Netease_quality);
                        break;
                    case "tencent":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Tencent_quality);
                        break;
                    case "kugou":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kugou_quality);
                        break;
                    case "kuwo":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Kuwo_quality);
                        break;
                    case "migu":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Migu_quality);
                        break;
                    case "baidu":
                        key = Host_manager.getMusicHost() + "/" + MUSIC_SERVICE + "/url?id=" + getURLEncoderString(url[id]) + "&quality=" + getURLEncoderString(Baidu_quality);
                        break;
                }
            }
            init_service();
            /*if (key == null){
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else if (key.equals("")){
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                //MusicPlayerActivity.super.onBackPressed();
            }else {
                LogUtil.i("KEY\n[\n" + key + "\n]");
                new Thread(new Music_Checker_runtime(h1, key)).start();
                //key = url[id];
                //music_name = name[id];
                //music_singer = singer[id];
                //music_imgURL = imgURL[id];
                //init_view();
            }*/
        }

        public String getURLEncoderString(String str) {
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

        private void showProgress(Boolean status){
            if (status){
                tipDialog = new QMUITipDialog.Builder(EntertainmentFragement.getFragementContext())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在加载")
                        .create(false);
                tipDialog.show();
                //player_play.setEnabled(false);
                //player_stop.setEnabled(false);
                //FToastUtils.init().setRoundRadius(30).show("正在加载音乐，请稍候！");
            }else {
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                //player_play.setEnabled(true);
                //player_stop.setEnabled(true);
                //FToastUtils.init().setRoundRadius(30).show("加载完成！");
            }
        }

        private void init_service() {

            if (key == null){
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
            }else if (key.equals("")){
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("链接异常！");
            }else
                new Thread(new Music_Checker_runtime(h1, key)).start();
        /*Intent intent=new Intent(this,MusicService.class);
        intent.putExtra("url",key);
        myConn = new MyConn();
        //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
        bindService(intent, myConn,BIND_AUTO_CREATE);


        //【4】动态注册广播（具体操作由通知栏触发---PendingIntent）
        receive = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PALYER_TAG);
        registerReceiver(receive, filter);*/

        }

        Handler h1=new Handler()
        {

            @Override
            public void handleMessage(Message msg)
            {
                // TODO: Implement this method
                switch (msg.what)
                {
                    case Music_Checker_runtime.FINDER_IMAGE_1:
                        //inderout1.setTextSize(15);
                        //finderout1.setGravity(Gravity.CENTER);
                        //finderout1.setText(msg.obj.toString());



                        if (String.valueOf(msg.obj).equals("访问成功")){

                            String url_302 = Music_player_manager.getUrl();
                            if (url_302 == null) {
                                //player_status = false;
                                if (tipDialog.isShowing())
                                    tipDialog.dismiss();
                                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            }else if (url_302.equals("")){
                                //player_status = false;
                                if (tipDialog.isShowing())
                                    tipDialog.dismiss();
                                FToastUtils.init().setRoundRadius(30).show("链接异常！");
                            }else {
                                FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path));
                                FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Music");

                                File path = new File(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Music");

                                String downPath = "";
                                if (singer == null){
                                    FToastUtils.init().setRoundRadius(30).show("数据异常，请稍候重试");
                                }else if (singer.length == 0){
                                    FToastUtils.init().setRoundRadius(30).show("数据异常，请稍候重试");
                                }else {
                                    try {
                                        String music_name_d = "";
                                        String music_singer_d = "";
                                        if (name[getAdapterPosition()].contains("/"))
                                            music_name_d = name[getAdapterPosition()].replaceAll("/", "-");
                                        else
                                            music_name_d = name[getAdapterPosition()];
                                        if (singer[getAdapterPosition()].contains("/"))
                                            music_singer_d = singer[getAdapterPosition()].replaceAll("/", "-");
                                        else
                                            music_singer_d = singer[getAdapterPosition()];
                                        downPath = path + "/" + music_name_d + "-" + music_singer_d;
                                        if (url_302.contains(".mp3")){
                                            downPath = downPath + ".mp3";
                                        }else if (url_302.contains(".m4a")){
                                            downPath = downPath + ".m4a";
                                        }else if (url_302.contains(".ape")){
                                            downPath = downPath + ".ape";
                                        }else if (url_302.contains(".flac")){
                                            downPath = downPath + ".flac";
                                        }else if (url_302.contains(".dff")){
                                            downPath = downPath + ".dff";
                                        }else {
                                            FToastUtils.init().setRoundRadius(30).show("未知文件名，请在下载完成后手动修改");
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        FToastUtils.init().setRoundRadius(30).show("数据异常，请稍候重试");
                                        e.printStackTrace();
                                    }
                                }
                                //String downPath = path + "/" + name[getAdapterPosition()] + "-" + singer[getAdapterPosition()] + ".mp3";
                                if (tipDialog.isShowing())
                                    tipDialog.dismiss();
                                try {
                                    downManger = new DownManager(EntertainmentFragement.getFragementContext());
                                    downManger.downSatrt(url_302, downPath, "是否要下载" + singer[getAdapterPosition()] + "唱的《" + name[getAdapterPosition()] + "》？");
                                }catch (ArrayIndexOutOfBoundsException e){
                                    FToastUtils.init().setRoundRadius(30).show("数据异常，请稍候重试");
                                    e.printStackTrace();
                                }
                                /*player_status = true;
                            Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
                            intent.putExtra("url", url_302);
                            myConn = new MyConn();
                            //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
                            bindService(intent, myConn, BIND_AUTO_CREATE);


                            //【4】动态注册广播（具体操作由通知栏触发---PendingIntent）
                            receive = new MusicReceiver();
                            IntentFilter filter = new IntentFilter();
                            filter.addAction(PALYER_TAG);
                            registerReceiver(receive, filter);*/
                            }
                        }else{

                            //访问失败
                            //player_status = false;
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                            //mRefreshLayout.finishRefresh();
                            //mRefreshLayout.finishLoadmore();

                        }

                        this.removeCallbacksAndMessages(null);
                        break;
                }
            }

        };

    }

}
