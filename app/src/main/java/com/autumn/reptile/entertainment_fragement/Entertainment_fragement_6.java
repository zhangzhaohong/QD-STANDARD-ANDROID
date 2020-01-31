package com.autumn.reptile.entertainment_fragement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autumn.framework.entertainment.manager.Data_manager_6;
import com.autumn.framework.entertainment.manager.Host_manager;
import com.autumn.framework.entertainment.runtime.Entertainment_Daily_runtime;
import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.autumn.reptile.app_interface.EntertainmentFragement;
import com.autumn.sdk.manager.content.content_manager;
import com.autumn.sdk.runtime.content.content_runtime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import cn.hotapk.fastandrutils.utils.FShare;
import cn.hotapk.fastandrutils.utils.FToastUtils;

public class Entertainment_fragement_6 extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private static boolean Analysis_status = false;
    private QMUITipDialog tipDialog;
    private ImageView daily_pic;
    private CardView daily_one;
    private TextView daily_id;
    private TextView daily_date;
    private TextView daily_sentence;
    private TextView daily_from;
    private ImageView daily_save;

    public static void Analysis_ok() {

        Analysis_status = true;

    }

    public static Entertainment_fragement_6 newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        Entertainment_fragement_6 fragment = new Entertainment_fragement_6();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_entertainment_6,container,false);

        daily_one = (CardView)view.findViewById(R.id.daily_one);
        daily_pic = (ImageView)view.findViewById(R.id.daily_pic);
        daily_id = (TextView)view.findViewById(R.id.daily_id);
        daily_date = (TextView)view.findViewById(R.id.daily_date);
        daily_sentence = (TextView)view.findViewById(R.id.daily_sentence);
        daily_from = (TextView)view.findViewById(R.id.daily_from);
        daily_save = (ImageView)view.findViewById(R.id.daily_save);

        tipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在载入页面")
                .create(false);

        tipDialog.show();

        if (Host_manager.getDailyHost() == null){
            new Thread(new content_runtime(h1, getActivity(), getActivity().getString(R.string.entertainment_key_6), getActivity().getString(R.string.app_build))).start();
        }else if (Host_manager.getDailyHost().equals("")){
            new Thread(new content_runtime(h1, getActivity(), getActivity().getString(R.string.entertainment_key_6), getActivity().getString(R.string.app_build))).start();
        }else {
            new Thread(new Entertainment_Daily_runtime(h2)).start();
        }

        return view;
    }

    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case content_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());
                    //tipDialog.dismiss();

                    if (EntertainmentFragement.getFragementContext() == null){

                    }else {
                        if (String.valueOf(msg.obj).equals(EntertainmentFragement.getFragementContext().getString(R.string.notice_new_version))) {

                            String content = content_manager.get_app_content();
                            if (content == null) {
                                tipDialog.dismiss();
                                //SUtils.initialize(MyApplication.getInstance());
                                FToastUtils.init().setRoundRadius(30).show("数据异常！");
                            } else if (content.equals("")) {
                                tipDialog.dismiss();
                                //SUtils.initialize(MyApplication.getInstance());
                                FToastUtils.init().setRoundRadius(30).show("数据异常！");
                            } else {
                                Host_manager.setDailyHost(content);
                                new Thread(new Entertainment_Daily_runtime(h2)).start();
                            }

                        } else {
                            tipDialog.dismiss();
                            FToastUtils.init().setRoundRadius(30).show("该页面已在当前地区下线！");
                        }
                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    Handler h2=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case Entertainment_Daily_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());

                    tipDialog.dismiss();

                    if (String.valueOf(msg.obj).equals("访问成功")){

                        while (Analysis_status == true){
                            Analysis_status = false;
                        }

                        daily_one.setVisibility(View.VISIBLE);
                        RequestOptions options = new RequestOptions();
                        //options.diskCacheStrategy(DiskCacheStrategy.ALL);
                        options.placeholder(R.drawable.aio_image_default);
                        options.error(R.drawable.empty_photo);
                        Glide.with(MyApplication.getAppContext())
                                .load(Data_manager_6.getPic_url())
                                .apply(options)
                                .into(daily_pic);
                        daily_id.setText(Data_manager_6.getVolume());
                        daily_date.setText(Data_manager_6.getPost_date());
                        daily_sentence.setText(Data_manager_6.getForward());
                        daily_from.setText(Data_manager_6.getWords_info());
                        daily_save.setVisibility(View.VISIBLE);

                        daily_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                daily_save.setVisibility(View.GONE);
                                Bitmap bitmap = getBitmapByView(daily_one);//contentLly是布局文件
                                savePhoto(bitmap, "DailySentence" + Calendar.getInstance().getTimeInMillis());

                            }
                        });

                    }else{

                        //访问失败
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    //ScrollView 转成bitmap长图
    public static Bitmap getBitmapByView(CardView cardView) {
        Bitmap bitmap = null;

        bitmap = Bitmap.createBitmap(cardView.getWidth(), cardView.getHeight(),
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        cardView.draw(canvas);
        return bitmap;
    }

    public void savePhoto(Bitmap photoBitmap, String photoName) {

        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path));
        FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Picture");

        File path = new File(Environment.getExternalStorageDirectory().getPath() + MyApplication.getAppContext().getString(R.string.files_path) + "/Picture");

        File photoFile = new File(path, photoName + ".png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                    //FToastUtils.init().setRoundRadius(30).show("已经成功保存图片\n" + path + "/" + photoName + ".png");
                    EntertainmentFragement.getFragementContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(photoFile)));

                    //分享
                    new FShare.FShareBuilder(getActivity(), "com.autumn.reptile.fileProvider")
                            .setFilePath(path + "/" + photoName + ".png")
                            .setShareContent("每日一句")
                            .setShareSubject("奇点--每日分享")
                            //.setShareFilter(new String[]{"com.tencent.mm", "com.tencent.mobileqq"})
                            .build()
                            .shareBySystem();
                            //.shareByCustom();

                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        daily_save.setVisibility(View.VISIBLE);

    }

}
