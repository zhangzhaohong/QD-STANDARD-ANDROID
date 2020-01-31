package com.autumn.reptile.entertainment_fragement;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autumn.framework.View.ClearEditText;
import com.autumn.framework.data.NetworkChecker;
import com.autumn.framework.entertainment.DouYinDownloader.Douyin;
import com.autumn.framework.entertainment.DouYinDownloader.Encode;
import com.autumn.framework.entertainment.DouYinDownloader.MyService;
import com.autumn.framework.entertainment.manager.Host_manager;
import com.autumn.framework.entertainment.manager.Video_download_manager;
import com.autumn.reptile.R;
import com.autumn.reptile.app_interface.EntertainmentFragement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.hotapk.fastandrutils.utils.FToastUtils;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.DOWNLOAD_SERVICE;

public class Entertainment_fragement_8 extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private TextView Video_download_Dy;
    private ClearEditText Video_download_Dyurl;
    private static boolean Analysis_status = false;
    private static boolean Analysis_failed_status = false;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private ImageView Auto_on;
    private ImageView Auto_off;
    private Boolean Auto_status = false;
    private Intent intent_service;
    private ClipboardManager clipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener ocm;
    private Long time;
    private String timeStamp;

    public static Entertainment_fragement_8 newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        Entertainment_fragement_8 fragment = new Entertainment_fragement_8();
        fragment.setArguments(args);
        return fragment;
    }

    public static void Analysis_ok(int page_num) {

        Analysis_status = true;

    }

    public static void Analysis_failed(int page_num) {
        Analysis_failed_status = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_entertainment_8,container,false);
        //TextView textView = (TextView) view.findViewById(R.id.textView);
        //textView.setText("第"+mPage+"页");
        Video_download_Dy = (TextView)view.findViewById(R.id.Video_download_Dy);
        Video_download_Dyurl = (ClearEditText)view.findViewById(R.id.Video_download_Dyurl);
        Auto_on = (ImageView)view.findViewById(R.id.Auto_on);
        Auto_off = (ImageView)view.findViewById(R.id.Auto_off);
        initView();
        Auto_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_icon_status(false);
                if (NetworkChecker.isVpnUsed()){

                    //vpn
                    FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
                    Intent intent = VpnService.prepare(getActivity());
                    if (intent != null) {
                        startActivityForResult(intent, 0);
                    } else {
                        onActivityResult(0, RESULT_OK, null);
                    }

                    for (int i = 0; i <= 10; i ++){
                        if (NetworkChecker.isVpnUsed()){
                            i ++;
                        }else {

                            StartAutoService();

                            break;
                        }
                        if (i == 10){
                            FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                            break;
                        }
                    }

                }else if (NetworkChecker.isWifiProxy(getActivity())){
                    FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                }else {


                    StartAutoService();


                }
                //StartAutoService();
            }
        });
        Auto_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_icon_status(true);
                intent_service=new Intent(getActivity(), MyService.class);
                Objects.requireNonNull(getActivity()).stopService(intent_service);
                FToastUtils.init().show("抖音小视频下载自动检测服务已关闭！");
                //Toast.makeText(EntertainmentFragement.getFragementContext(),"即将退出",Toast.LENGTH_LONG).show();
                //MainActivity.this.finish();
                //System.exit(0);
            }
        });
        Video_download_Dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //page = 0;
                if (Video_download_Dyurl.getText().toString().equals("")){
                    FToastUtils.init().setRoundRadius(30).show("数据不能为空！");
                }else {
                    //Start download
                    if (NetworkChecker.isVpnUsed()){

                        //vpn
                        FToastUtils.init().setRoundRadius(30).showLong("该应用仅仅会关闭现有VPN申请，请勿拒绝该权限申请，否则程序将无法正常运行");
                        Intent intent = VpnService.prepare(getActivity());
                        if (intent != null) {
                            startActivityForResult(intent, 0);
                        } else {
                            onActivityResult(0, RESULT_OK, null);
                        }

                        for (int i = 0; i <= 10; i ++){
                            if (NetworkChecker.isVpnUsed()){
                                i ++;
                            }else {

                                String content = Video_download_Dyurl.getText().toString();

                                //Start
                                CheckDownloadDy(content);

                                break;
                            }
                            if (i == 10){
                                FToastUtils.init().setRoundRadius(30).show("网络异常，请关闭VPN后重试");
                                break;
                            }
                        }

                    }else if (NetworkChecker.isWifiProxy(getActivity())){
                        FToastUtils.init().setRoundRadius(30).show("该操作禁止使用一切代理，请关闭代理后重试");
                    }else {


                        String content = Video_download_Dyurl.getText().toString();
                        //Start
                        CheckDownloadDy(content);


                    }


                }
            }
        });
                //new Thread(new Entertainment_runtime(h1,"image","10")).start();
        return view;
    }

    private void CheckDownloadDy(String share_info) {
        final String PATH= Environment.getExternalStorageDirectory().getPath();
        clipboardManager=(ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        /*ClipData data=clipboardManager.getPrimaryClip();
        ClipData.Item item= data != null ? data.getItemAt(0) :null;
        if(item==null) {
            Toast.makeText(EntertainmentFragement.getFragementContext(),"该链接不是有效连接(奇点-抖音小视频下载服务)",Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(share_info.contains("v.douyin.com")){
            final String share_url="http"+share_info.split("http|复制此链接")[1];

            final ProgressDialog progressDialog=new ProgressDialog(EntertainmentFragement.getFragementContext());
            progressDialog.setMessage("正在解析....");
            progressDialog.setCancelable(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//针对安卓8.0对全局弹窗适配
                progressDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
            }else {
                progressDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
            }
            progressDialog.show();

            try {
                timeStamp = Encode.work(String.valueOf((int) (System.currentTimeMillis() / 1000)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Host_manager.getShortVideoHost() == null){
                FToastUtils.init().setRoundRadius(30).show("HOST出错");
                return;
            }else if (Host_manager.getHotMusicHost().equals("")){
                FToastUtils.init().setRoundRadius(30).show("HOST出错");
                return;
            }
            final Douyin douyin=new Douyin(Host_manager.getShortVideoHost() + "?url=" + share_url + "&sign_t=" + timeStamp, new Douyin.DYCallBack() {
                @Override
                public void HttpSuccessDo(final Douyin douyin,boolean error) {
                    progressDialog.hide();
                    if(!error){
                        ClipData data1=ClipData.newPlainText("douyin",douyin.getReal_url());
                        clipboardManager.setPrimaryClip(data1);
                        AlertDialog.Builder ab=new AlertDialog.Builder(EntertainmentFragement.getFragementContext())
                                .setTitle("奇点-抖音小视频下载服务")
                                .setMessage("检测到抖音分享视频:\n"+douyin.getUser_name()+"("+douyin.getVideo_id()+ ").mp4")
                                .setPositiveButton("视频下载", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        String path=PATH+ "/com.autumn.reptile/Douyin/Video/" + douyin.getUser_name()+"("+douyin.getVideo_id()+ ").mp4";
                                        if(isFileExcited(path)){
                                            Toast.makeText(EntertainmentFragement.getFragementContext(),"视频已存在",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        //创建下载任务,downloadUrl就是下载链接
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(douyin.getReal_url()));
                                        // 指定下载路径和下载文件名
                                        request.setDestinationInExternalPublicDir("/com.autumn.reptile/Douyin/Video/", douyin.getUser_name()+"("+douyin.getVideo_id()+ ").mp4");
                                        // 获取下载管理器
                                        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                        // 将下载任务加入下载队列，否则不会进行下载
                                        downloadManager.enqueue(request);
                                        Toast.makeText(EntertainmentFragement.getFragementContext(), "开始下载："+douyin.getUser_name()+"("+douyin.getVideo_id()+ ").mp4", Toast.LENGTH_LONG).show();

                                    }
                                })
                                .setCancelable(true);

                        if(douyin.getMusic_url()!=null){
                            ab.setNegativeButton("原声下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    String path=PATH+"/com.autumn.reptile/Douyin/Music/"+ douyin.getUser_name()+"("+douyin.getVideo_id()+ ")原声.mp3";
                                    if(isFileExcited(path)){
                                        Toast.makeText(EntertainmentFragement.getFragementContext(),"原声已存在",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    //创建下载任务,downloadUrl就是下载链接
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(douyin.getMusic_url()));
                                    // 指定下载路径和下载文件名
                                    request.setDestinationInExternalPublicDir("/com.autumn.reptile/Douyin/Music/", douyin.getUser_name()+"("+douyin.getVideo_id()+ ")原声.mp3");
                                    // 获取下载管理器
                                    DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                    // 将下载任务加入下载队列，否则不会进行下载
                                    downloadManager.enqueue(request);
                                    Toast.makeText(EntertainmentFragement.getFragementContext(), "开始下载："+douyin.getUser_name()+"("+douyin.getVideo_id()+ ")原声.mp3", Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                        if(douyin.isHas_long()){
                            ab.setNeutralButton("完整视频下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    String path=PATH+ "/com.autumn.reptile/Douyin/Video_FULL/" + douyin.getUser_name()+"("+douyin.getVideo_id()+ ")"+douyin.getQuantity_name()+".mp4";
                                    if(isFileExcited(path)){//去重复处理
                                        Toast.makeText(EntertainmentFragement.getFragementContext(),"视频已存在",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    //创建下载任务,downloadUrl就是下载链接
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(douyin.getLong_video()));
                                    // 指定下载路径和下载文件名
                                    request.setDestinationInExternalPublicDir("/com.autumn.reptile/Douyin/Video_FULL/", douyin.getUser_name()+"("+douyin.getVideo_id()+ ")"+douyin.getQuantity_name()+".mp4");
                                    // 获取下载管理器
                                    DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                    // 将下载任务加入下载队列，否则不会进行下载
                                    downloadManager.enqueue(request);
                                    Toast.makeText(EntertainmentFragement.getFragementContext(), "开始下载："+douyin.getUser_name()+"("+douyin.getVideo_id()+ ")"+douyin.getQuantity_name()+".mp4", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        AlertDialog alertDialog=ab.create();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//针对安卓8.0对全局弹窗适配
                            alertDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
                        }else {
                            alertDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                        }
                        alertDialog.show();

                    }else{
                        Toast.makeText(EntertainmentFragement.getFragementContext(), "该链接不是有效连接(奇点-抖音小视频下载服务)", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    douyin.cancle();
                }
            });
        }
    }

    private void StartAutoService() {
        /**
         * 权限检查、声明
         */
        List<String> permisions=new ArrayList<>();

        if(ContextCompat.checkSelfPermission(EntertainmentFragement.getFragementContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permisions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(EntertainmentFragement.getFragementContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permisions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(!permisions.isEmpty()){
            String []permisions_s=permisions.toArray(new String[permisions.size()]);
            ActivityCompat.requestPermissions(getActivity(),permisions_s,1);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(EntertainmentFragement.getFragementContext())) {
                    openDY();
                } else {
                    //若没有权限，提示获取.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    Toast.makeText(EntertainmentFragement.getFragementContext(),"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    //finish();
                }

            }else {
                //SDK在23以下，不用管.
                openDY();
            }
        }
    }

    void openDY(){
        intent_service=new Intent(getActivity(), MyService.class);
        intent_service.setAction(getActivity().getPackageName());
        intent_service.setPackage(getActivity().getPackageName());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent_service);
        }else {
            getActivity().startService(intent_service);
        }*/
        getActivity().startService(intent_service);
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.ss.android.ugc.aweme");
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(EntertainmentFragement.getFragementContext(),"打开抖音出错！请允许打开抖音或者安装抖音app",Toast.LENGTH_SHORT).show();
            change_icon_status(true);
            //this.finish();
            //System.exit(0);
        }
    }

    private boolean isFileExcited(String path){
        File file=new File(path);
        return file.exists();
    }

    private void initView() {
        try {
            if (isServiceRunning(EntertainmentFragement.getFragementContext(), "com.autumn.framework.entertainment.DouYinDownloader.MyService")) {
                Video_download_manager.setStatus(true);
                FToastUtils.init().setRoundRadius(30).show("抖音小视频下载自动检测服务已在运行中");
            } else {
                Video_download_manager.setStatus(false);
            }
            if (Video_download_manager.getStatus() == null) {
                //Video_download_manager.setStatus(false);
                Auto_status = false;
                change_icon_status(true);
            } else {
                Auto_status = Video_download_manager.getStatus();
                if (Auto_status)
                    change_icon_status(false);
                else
                    change_icon_status(true);
            }
        }catch (Exception e){
            FToastUtils.init().setRoundRadius(30).show("程序异常：" + e.toString());
        }
    }

    private void change_icon_status(Boolean auto_status) {
        if (auto_status){
            Video_download_manager.setStatus(false);
            Auto_on.setVisibility(View.GONE);
            Auto_off.setVisibility(View.VISIBLE);
        }else {
            Video_download_manager.setStatus(true);
            Auto_off.setVisibility(View.GONE);
            Auto_on.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service 类全名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(200);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : serviceInfoList) {
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

}
