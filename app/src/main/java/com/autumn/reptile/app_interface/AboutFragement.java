package com.autumn.reptile.app_interface;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autumn.framework.about.phone_info;
import com.autumn.framework.data.App_info;
import com.autumn.reptile.BuildConfig;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.sdk.manager.user.user_info_manager;

import cn.hotapk.fastandrutils.utils.FToastUtils;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class AboutFragement extends Fragment {

    private TextView build;
    private TextView group;
    private TextView manufacturer;
    private TextView product;
    private TextView system_version;
    private TextView android_version;
    private TextView sdk_version;
    private String SETTING = "Config";
    private String AUTO_POST = "Auto_post";
    private String AUTO_SAVELOG = "Auto_savelog";
    private String ALLOW_D = "Allow_d";
    private String ALLOW_E = "Allow_e";
    private String ALLOW_I = "Allow_i";
    private String ALLOW_V = "Allow_v";
    private String ALLOW_W = "Allow_w";
    private String ALLOW_WTF = "Allow_wtf";
    private String ALLOW_DEBUG = "Allow_debug";
    private String ALLOW_ALL = "Allow_all";
    private static final int INFO_SDK = 19;
    private static final int INFO_MANUFACTURER = 7;
    private static final int INFO_PRODUCT = 10;
    private static final int INFO_TIME = 20;
    private static final int INFO_ANDROID_VERSION = 15;
    private static final int INFO_SYSTEM_VERSION = 4;
    private static final int INFO_PHONE_NUMBER = 5;
    private static final int INFO_CPU = 9;
    private static final int INFO_TYPE = 12;
    private TextView build_time;
    private TextView cpu_version;
    private TextView type;
    private TextView device_id;
    private TextView software_status;
    private TextView imei;
    private TextView imsi;
    private TextView mac;
    private static final String DEVICE_INFO = "Device_info";
    private static final String DEVICE_IMEI = "Device_imei";
    private static final String DEVICE_IMSI = "Device_imsi";
    private static final String DEVICE_MAC = "Device_mac";
    private static final String DEVICE_STATUS = "Device_status";
    private String DEVICE_SERIAL_NUMBER = "Device_serial_number";
    private String DEVICE_ID_CODE = "Device_id_code";
    private static final String USER_INFO = "User_info";
    private static final String LOGIN_USERNAME = "Login_username";
    private ImageView logo;
    private TextView device_id_code;
    private TextView version;
    private CardView cardView_1;
    private CardView cardView_2;
    private CardView cardView_3;
    private boolean create_alert = false;
    private String ALLOW_SETTING = "Allow_setting";
    private LinearLayout join_group;

    public static AboutFragement newInstance(String param1) {
        AboutFragement fragment = new AboutFragement();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AboutFragement() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_4, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
        //TextView tv = (TextView) view.findViewById(R.id.container);
        //tv.setText(agrs1);

        cardView_1 = (CardView)view.findViewById(R.id.cardView_1);
        cardView_1.setRadius(30);//设置图片圆角的半径大小
        cardView_1.setCardElevation(8);//设置阴影部分大小
        cardView_1.setContentPadding(5,5,5,5);//设置图片距离阴影大小
        cardView_2 = (CardView)view.findViewById(R.id.cardView_2);
        cardView_2.setRadius(30);//设置图片圆角的半径大小
        cardView_2.setCardElevation(8);//设置阴影部分大小
        cardView_2.setContentPadding(5,5,5,5);//设置图片距离阴影大小
        cardView_3 = (CardView)view.findViewById(R.id.cardView_3);
        cardView_3.setRadius(30);//设置图片圆角的半径大小
        cardView_3.setCardElevation(8);//设置阴影部分大小
        cardView_3.setContentPadding(5,5,5,5);//设置图片距离阴影大小

        logo =(ImageView)view.findViewById(R.id.imageView5);
        phone_info info = new phone_info();
        //version=(TextView)view4.findViewById(R.id.main4TextView1);
        build=(TextView)view.findViewById(R.id.main4TextView2);
        group=(TextView)view.findViewById(R.id.main4TextView3);
        manufacturer=(TextView)view.findViewById(R.id.main4TextView4);
        product=(TextView)view.findViewById(R.id.main4TextView5);
        type=(TextView)view.findViewById(R.id.main4TextView6);
        android_version=(TextView)view.findViewById(R.id.main4TextView7);
        sdk_version=(TextView)view.findViewById(R.id.main4TextView8);
        system_version=(TextView)view.findViewById(R.id.main4TextView9);
        build_time=(TextView)view.findViewById(R.id.main4TextView10);
        cpu_version=(TextView)view.findViewById(R.id.main4TextView11);
        device_id=(TextView)view.findViewById(R.id.main4TextView12);
        imei=(TextView)view.findViewById(R.id.main4TextView13);
        imsi=(TextView)view.findViewById(R.id.main4TextView14);
        mac=(TextView)view.findViewById(R.id.main4TextView15);
        device_id_code=(TextView)view.findViewById(R.id.main4TextView16);
        software_status=(TextView)view.findViewById(R.id.main4TextView17);
        join_group = (LinearLayout)view.findViewById(R.id.join_group);
        join_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinQQGroup("eq1UTq0JGwS1-BZR-2gAKwgx-v7dBdHJ");
            }
        });
        //version.setText(R.string.app_software_version);
        build.setText(R.string.app_build);
        group.setText(R.string.app_group);
        manufacturer.setText(info.get_info(INFO_MANUFACTURER));
        product.setText(info.get_info(INFO_PRODUCT));
        android_version.setText(info.get_info(INFO_ANDROID_VERSION));
        sdk_version.setText(info.get_info(INFO_SDK));
        system_version.setText(info.get_info(INFO_SYSTEM_VERSION));
        type.setText(info.get_info(INFO_TYPE));
        build_time.setText(info.get_info(INFO_TIME));
        cpu_version.setText(info.get_info(INFO_CPU));
        MainActivity rw = new MainActivity();
        if(rw.getBoolean(DEVICE_INFO,DEVICE_STATUS)==true) {
            imei.setText(rw.getValue(DEVICE_INFO,DEVICE_IMEI));
            imsi.setText(rw.getValue(DEVICE_INFO,DEVICE_IMSI));
            mac.setText(rw.getValue(DEVICE_INFO,DEVICE_MAC));
            device_id.setText(rw.getValue(DEVICE_INFO,DEVICE_SERIAL_NUMBER));
        }else{
            FToastUtils.init().setRoundRadius(30).show("请重新开启电话权限，并重新启动APP");
        }
        device_id_code.setText(rw.getValue(DEVICE_INFO,DEVICE_ID_CODE));
        software_status.setText(get_Status());

        device_id_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //复制内容到剪切板
                MainActivity rw = new MainActivity();
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager)getActivity().getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "User:"+rw.getValue(USER_INFO,LOGIN_USERNAME)+"\nDevice_id_code:"+rw.getValue(DEVICE_INFO,DEVICE_ID_CODE));
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                FToastUtils.init().setRoundRadius(30).show("已复制DEVICE_ID_CODE到剪贴板");
                return false;
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("info")//设置对话框标题
                        .setMessage("Build_time\n" + BuildConfig.App_Build_Time + "\nDebug_name:\n" + getString(R.string.app_name_debug))//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
            }
        });

        build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(v);
            }
        });

        return view;
    }

    long[] mHits = new long[3];
    //三击事件
    public void click(View view){
        //src 拷贝的源数组
        //srcPos 从源数组的那个位置开始拷贝.
        //dst 目标数组
        //dstPos 从目标数组的那个位子开始写数据
        //length 拷贝的元素的个数
        if (!create_alert) {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                //FToastUtils.init().setRoundRadius(30).show("恭喜你，5次点击了。");
                create_alert = true;
                final EditText et = new EditText(getActivity());
                MainActivity rw = new MainActivity();
                Boolean Allow_Setting = rw.getBoolean(SETTING, ALLOW_SETTING);

                if (Allow_Setting) {
                    FToastUtils.init().setRoundRadius(30).show("开发者模式已经关闭！");
                    //MainActivity rw = new MainActivity();
                    rw.writeBoolean(SETTING, ALLOW_SETTING, false);
                    create_alert = false;
                    if (user_info_manager.get_log_level().equals("无染色模式")){

                        rw.writeBoolean(SETTING,AUTO_POST,true);
                        rw.writeBoolean(SETTING,AUTO_SAVELOG,false);
                        rw.writeBoolean(SETTING,ALLOW_ALL,false);
                        rw.writeBoolean(SETTING,ALLOW_D,false);
                        rw.writeBoolean(SETTING,ALLOW_E,false);
                        rw.writeBoolean(SETTING,ALLOW_I,false);
                        rw.writeBoolean(SETTING,ALLOW_V,false);
                        rw.writeBoolean(SETTING,ALLOW_W,false);
                        rw.writeBoolean(SETTING,ALLOW_WTF,false);
                        rw.writeBoolean(SETTING,ALLOW_DEBUG,false);

                    }else if (user_info_manager.get_log_level().equals("一级染色")){

                        rw.writeBoolean(SETTING,AUTO_POST,true);
                        rw.writeBoolean(SETTING,AUTO_SAVELOG,true);
                        rw.writeBoolean(SETTING,ALLOW_ALL,false);
                        rw.writeBoolean(SETTING,ALLOW_D,false);
                        rw.writeBoolean(SETTING,ALLOW_E,false);
                        rw.writeBoolean(SETTING,ALLOW_I,true);
                        rw.writeBoolean(SETTING,ALLOW_V,false);
                        rw.writeBoolean(SETTING,ALLOW_W,false);
                        rw.writeBoolean(SETTING,ALLOW_WTF,false);
                        rw.writeBoolean(SETTING,ALLOW_DEBUG,true);

                    }else if (user_info_manager.get_log_level().equals("二级染色")){

                        rw.writeBoolean(SETTING,AUTO_POST,true);
                        rw.writeBoolean(SETTING,AUTO_SAVELOG,true);
                        rw.writeBoolean(SETTING,ALLOW_ALL,false);
                        rw.writeBoolean(SETTING,ALLOW_D,true);
                        rw.writeBoolean(SETTING,ALLOW_E,false);
                        rw.writeBoolean(SETTING,ALLOW_I,true);
                        rw.writeBoolean(SETTING,ALLOW_V,false);
                        rw.writeBoolean(SETTING,ALLOW_W,false);
                        rw.writeBoolean(SETTING,ALLOW_WTF,false);
                        rw.writeBoolean(SETTING,ALLOW_DEBUG,true);

                    }else if (user_info_manager.get_log_level().equals("三级染色")){

                        rw.writeBoolean(SETTING,AUTO_POST,true);
                        rw.writeBoolean(SETTING,AUTO_SAVELOG,true);
                        rw.writeBoolean(SETTING,ALLOW_ALL,false);
                        rw.writeBoolean(SETTING,ALLOW_D,true);
                        rw.writeBoolean(SETTING,ALLOW_E,true);
                        rw.writeBoolean(SETTING,ALLOW_I,true);
                        rw.writeBoolean(SETTING,ALLOW_V,false);
                        rw.writeBoolean(SETTING,ALLOW_W,false);
                        rw.writeBoolean(SETTING,ALLOW_WTF,false);
                        rw.writeBoolean(SETTING,ALLOW_DEBUG,true);

                    }else if (user_info_manager.get_log_level().equals("四级染色")){

                        rw.writeBoolean(SETTING,AUTO_POST,true);
                        rw.writeBoolean(SETTING,AUTO_SAVELOG,true);
                        rw.writeBoolean(SETTING,ALLOW_ALL,false);
                        rw.writeBoolean(SETTING,ALLOW_D,true);
                        rw.writeBoolean(SETTING,ALLOW_E,true);
                        rw.writeBoolean(SETTING,ALLOW_I,true);
                        rw.writeBoolean(SETTING,ALLOW_V,false);
                        rw.writeBoolean(SETTING,ALLOW_W,true);
                        rw.writeBoolean(SETTING,ALLOW_WTF,false);
                        rw.writeBoolean(SETTING,ALLOW_DEBUG,true);

                    }else if (user_info_manager.get_log_level().equals("五级染色")){

                        rw.writeBoolean(SETTING,AUTO_POST,true);
                        rw.writeBoolean(SETTING,AUTO_SAVELOG,true);
                        rw.writeBoolean(SETTING,ALLOW_ALL,true);
                        rw.writeBoolean(SETTING,ALLOW_D,true);
                        rw.writeBoolean(SETTING,ALLOW_E,true);
                        rw.writeBoolean(SETTING,ALLOW_I,true);
                        rw.writeBoolean(SETTING,ALLOW_V,true);
                        rw.writeBoolean(SETTING,ALLOW_W,true);
                        rw.writeBoolean(SETTING,ALLOW_WTF,true);
                        rw.writeBoolean(SETTING,ALLOW_DEBUG,true);

                    }
                } else {
                    new AlertDialog.Builder(getActivity()).setTitle("请输入密码")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(et)
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = et.getText().toString();
                                    if (input.equals("")) {
                                        FToastUtils.init().setRoundRadius(30).show("密码不能为空！");
                                        //MainActivity rw = new MainActivity();
                                        rw.writeBoolean(SETTING, ALLOW_SETTING, false);
                                    } else if (input.equals(getActivity().getString(R.string.app_beta_key)) && App_info.type.equals("beta")) {

                                        FToastUtils.init().setRoundRadius(30).show("开发者模式已经开启！");
                                        //MainActivity rw = new MainActivity();
                                        rw.writeBoolean(SETTING, ALLOW_SETTING, true);

                                    } else {
                                        FToastUtils.init().setRoundRadius(30).show("密码错误或App非BETA版本！");
                                        //MainActivity rw = new MainActivity();
                                        rw.writeBoolean(SETTING, ALLOW_SETTING, false);
                                    }
                                    create_alert = false;
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    create_alert = false;
                                }
                            })
                            .show();
                }
            }
        }
    }

    /****************
     *
     * 发起添加群流程。群号：奇点官方Q群(903292890) 的 key 为： eq1UTq0JGwS1-BZR-2gAKwgx-v7dBdHJ
     * 调用 joinQQGroup(eq1UTq0JGwS1-BZR-2gAKwgx-v7dBdHJ) 即可发起手Q客户端申请加群 奇点官方Q群(903292890)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            FToastUtils.init().setRoundRadius(30).show("打开失败，原因：未安装手Q或安装的版本不支持");
            return false;
        }
    }


    private String get_Status(){
        MainActivity rw = new MainActivity();
        Boolean is_savelog = rw.getBoolean(SETTING,AUTO_SAVELOG);
        Boolean Allow_D = rw.getBoolean(SETTING,ALLOW_D);
        Boolean Allow_E = rw.getBoolean(SETTING,ALLOW_E);
        Boolean Allow_I = rw.getBoolean(SETTING,ALLOW_I);
        Boolean Allow_V = rw.getBoolean(SETTING,ALLOW_V);
        Boolean Allow_W = rw.getBoolean(SETTING,ALLOW_W);
        Boolean Allow_WTF = rw.getBoolean(SETTING,ALLOW_WTF);
        Boolean Allow_ALL = rw.getBoolean(SETTING,ALLOW_ALL);
        Boolean Allow_DEBUG = rw.getBoolean(SETTING,ALLOW_DEBUG);

        if(Allow_D==true||Allow_E==true||Allow_I==true||Allow_V==true||Allow_W==true||Allow_WTF==true||Allow_ALL==true||Allow_DEBUG==true){
            return "Debug";
        }else{
            return "Release";
        }
    }

}
