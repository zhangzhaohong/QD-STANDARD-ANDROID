package com.autumn.reptile.app_interface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.View.cycleviewpager.view.CycleViewPager;
import com.autumn.framework.X5WebView.ui.PublicShActivity;
import com.autumn.framework.X5WebView.ui.X5WebGameActivity;
import com.autumn.framework.data.BannerDataManager.BannerBean;
import com.autumn.framework.data.HttpUtil;
import com.autumn.framework.entertainment.DouYinDownloader.Encode;
import com.autumn.framework.music.activity.SplashActivity;
import com.autumn.reptile.R;
import com.autumn.sdk.manager.content.content_manager;
import com.autumn.sdk.runtime.content.content_runtime;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import cn.hotapk.fastandrutils.utils.FToastUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class HomeFragment extends Fragment implements OnBannerListener {
    private CycleViewPager mCycleViewPager;
    private TextView beta_sys;
    private ProgressBar progressBar;
    private Button public_sh;
    private CardView cardView_1;
    private CardView cardView_2;
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private Button net_radio;
    private QMUITipDialog tipDialog;
    private OkHttpClient client;
    private ArrayList<String> list_url;

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_1, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");

        tipDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create(false);

        tipDialog.show();

        new Thread(new content_runtime(h1, getActivity(), getActivity().getString(R.string.main_banner_key), getActivity().getString(R.string.app_build))).start();

        //cardView_1 = (CardView) view.findViewById(R.id.cardView_1);
        //cardView_1.setRadius(30);//设置图片圆角的半径大小
        //cardView_1.setCardElevation(8);//设置阴影部分大小
        //cardView_1.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
        cardView_2 = (CardView) view.findViewById(R.id.cardView_2);
        cardView_2.setRadius(30);//设置图片圆角的半径大小
        cardView_2.setCardElevation(8);//设置阴影部分大小
        cardView_2.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小

        banner = (Banner) view.findViewById(R.id.banner);
        beta_sys = (TextView) view.findViewById(R.id.beta_sys);
        //init_changelog();
        //LogUtil.i("this");

        //initView();

        net_radio = (Button) view.findViewById(R.id.net_radio);
        net_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(getActivity());
            }
        });

        public_sh = (Button) view.findViewById(R.id.public_sh);
        public_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublicShActivity.class);
                startActivity(intent);
                AtyTransitionUtil.exitToRight(getActivity());
            }
        });

        return view;
    }

    private void init_changelog() {

        beta_sys.setText("【版本说明】" +
                "\n1.第四期第三轮测试已经开始，此版本为BETA版" +
                "\n\n版本号：奇点 8.0.0.101 (SP02logPatch25)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n历史版本：" +
                "\n\n版本号：奇点 8.0.0.101 (SP02Patch03)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP02Patch02)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP02)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch96)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch93)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch87)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch72)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch70)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch62)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch61)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01Patch58)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch39)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch35)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch23)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch20)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch03)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01logPatch01)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.101 (SP01)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch77)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch75)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch69)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch63)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch59)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch50)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch35)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch32)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch31)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch25)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch19)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch11)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch07)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP80Patch01)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch56)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch41)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch37)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch36)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch32)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch29)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45logPatch19)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch12)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch05)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45Patch02)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP45)" +
                "\n\n【更新日志】" +
                "\n您可以前往检测更新，点击当前版本直接查看更新内容" +
                "\n\n版本号：奇点 8.0.0.100 (SP40Patch09)" +
                "\n\n【更新日志】" +
                "\n\n【新增】" +
                "\n1.新增appkey过期下载新版本逻辑" +
                "\n2.新增用户昵称修改逻辑" +
                "\n3.新增多处昵称编辑字数显示" +
                "\n\n【优化】" +
                "\n1.优化部分界面加载动画" +
                "\n2.优化部分页面显示效果，强制竖屏显示" +
                "\n3.优化BETA测试申请页面" +
                "\n\n【修复】" +
                "\n1.修复部分已知bug" +
                "\n\n版本号：奇点 8.0.0.100 (SP40)---奇点 8.0.0.100 (SP40Patch05)" +
                "\n\n【更新日志】" +
                "\n\n【修复】" +
                "\n1.修复若干已知bug" +
                "\n2.修复release更新逻辑" +
                "\n\n【新增】" +
                "\n1.新增若干体验改进" +
                "\n\n【优化】" +
                "\n1.优化快捷反馈方式" +
                "\n\n【第四期测试说明】" +
                "\n1.本次测试为第四期第一轮，测试版本为：" +
                "\n奇点 8.0.0.100 (SP18log)" +
                "\n奇点 8.0.0.100 (SP19log)" +
                "\n奇点 8.0.0.100 (SP20log)" +
                "\n奇点 8.0.0.100 (SP23log)" +
                "\n奇点 8.0.0.100 (SP30log)" +
                "\n奇点 8.0.0.100 (SP35log)" +
                "\n奇点 8.0.0.100 (SP35logPatch02)" +
                "\n奇点 8.0.0.100 (SP35logPatch03)" +
                "\n奇点 8.0.0.100 (SP35logPatch04)" +
                "\n奇点 8.0.0.100 (SP35logPatch05)" +
                "\n奇点 8.0.0.100 (SP35logPatch06)" +
                "\n奇点 8.0.0.100 (SP35logPatch07)" +
                "\n奇点 8.0.0.100 (SP35logPatch08)" +
                "\n奇点 8.0.0.100 (SP35logPatch10)" +
                "\n奇点 8.0.0.100 (SP35logPatch17)" +
                "\n奇点 8.0.0.100 (SP35logPatch20)" +
                "\n奇点 8.0.0.100 (SP35logPatch21)" +
                "\n奇点 8.0.0.100 (SP35logPatch25)" +
                "\n2.测试前，请先前往设置,开启BETA-DEBUG模式以及自动上传crash异常日志" +
                "\n\n【测试内容】" +
                "\n\n版本号：奇点 8.0.0.100 (SP35logPatch02)---奇点 8.0.0.100 (SP35logPatch25)" +
                "\n\n【修复】" +
                "\n1.修复保护进程crash的问题" +
                "\n2.修复beta新版本无法下载的问题" +
                "\n3.修复带Patch版本检测问题，修复会检测到相同带Patch版本" +
                "\n4.修复主页无法成功加载公告的bug" +
                "\n5.修复意见反馈与动态补丁更新冲突问题" +
                "\n6.修复部分已知bug" +
                "\n\n【优化】" +
                "\n1.优化检测更新逻辑" +
                "\n2.优化用户等级显示" +
                "\n3.优化资料卡显示问题" +
                "\n4.优化部分页面布局" +
                "\n5.优化验证码加载逻辑" +
                "\n6.优化侧滑菜单昵称到期日期用户等级显示" +
                "\n7.调整侧滑菜单布局，防止出现异常的显示" +
                "\n\n【新增】" +
                "\n1.登录逻辑新增检测更新" +
                "\n\n版本号：奇点 8.0.0.100 (SP35log)" +
                "\n\n【修复】" +
                "\n1.修复部分已知bug" +
                "\n2.修复意见反馈，摇一摇无法弹出" +
                "\n\n版本号：奇点 8.0.0.100 (SP30log)" +
                "\n\n【修复】" +
                "\n1.修复部分已知bug" +
                "\n2.修复启动图无限倒计时" +
                "\n\n【优化】" +
                "\n1.优化进程保护机制，减少保护线程，唤醒App自动登录，防止部分crash" +
                "\n2.优化使用耗电" +
                "\n3.优化启动图逻辑" +
                "\n\n【新增】" +
                "\n1.新增Release检测更新，更新更方便" +
                "\n2.新增Bug上报系统" +
                "\n3.新增动态修复支持，严重bug及时修复" +
                "\n4.资料卡新增SVIP,VIP图标，到期日期显示进行处理" +
                "\n\n【已知问题】" +
                "\n1.由于资料卡正在试验测试更新阶段，用户等级暂时默认无法隐藏，后期将动态更新等级算法" +
                "\n\n版本号：奇点 8.0.0.100 (SP23log)" +
                "\n\n【修复】" +
                "\n1.修复部分已知bug" +
                "\n\n【新增】" +
                "\n1.新增首页轮番图（测试布局使用），请查看图片是否只加载一次，是否完全解决缓存图片模糊的问题，未正常加载时是否不是空白" +
                "\n2.重启测试页面（test页面），请测试所有按钮弹出是否有异常，以及上下滑动是否有异常" +
                "\n3.关于页面，硬件识别码新增长按识别功能，DEVICE_CODE仅供第五期及以后开通DEBUG模式使用" +
                "\n4.资料卡页面支持下拉刷新，下拉刷新重新添加特效，等你来体验" +
                "\n\n【优化】" +
                "\n1.优化图片缓存算法" +
                "\n2.优化侧滑菜单返回键样式，及总体布局" +
                "\n3.更新x5内核至V3.6.0.1249" +
                "\n4.调整部分生日填写方式" +
                "\n\n版本号：奇点 8.0.0.100 (SP20log)" +
                "\n\n【修复】" +
                "\n1.修复一个错误" +
                "\n\n版本号：奇点 8.0.0.100 (SP19log)" +
                "\n\n【新增】" +
                "\n1.新增摇一摇反馈等多种意见反馈方式，同时支持语音反馈" +
                "\n\n【优化】" +
                "\n1.侧滑菜单布局" +
                "\n2.去除不必要按钮" +
                "\n3.修复优化部分不合理布局" +
                "\n\n版本号：奇点 8.0.0.100 (SP18log)" +
                "\n\n【优化】" +
                "\n1.页面优化流畅度（主页滑动流畅度）" +
                "\n2.启动页首次启动新手引导滑动流畅度（请测试是否存在卡顿，内存溢出，或者无响应等情况）" +
                "\n3.注册登录账号找回等页面输入框显示优化，新增密码显示，以及错误提示，请着重测试是否有异常；注册页面邮箱自动补全是否正常；注册页面手动选择生日日期弹窗是否正常" +
                "\n4.优化大量状态栏，导航栏未沉浸问题，如果依旧存在未修改的，请及时通过意见反馈进行反馈" +
                "\n5.注册，账号找回等新增验证码，请测试验证过程是否存在问题，点击验证码可以重新生成，所填写验证码大小写不分" +
                "\n6.启动时对网络进行提醒及时告知用户" +
                "\n\n【新增】" +
                "\n1.新增检测更新，请在测试期间测试不同模式是否可以检测到新版本，以及没有新版本时检测是否有异常，以及检测到新版本以后，下载圆形进度条百分比是否正常，更新日志换行是否正常，下载成功以后，是否会自动打开提示安装，以及底部文字是否有异常（立即更新，正在下载，立即安装），且点击立即安装，也会跳转到安装页面，注意：本次测试所有地址皆为另外一个应用的地址，请知悉；请测试内测接口在内测结束后，是否可正常推送正式版本" +
                "\n2.新增公告系统，请在测试期间测试不同模式下是否可以收到新公告，以及公告换行是否有异常，是否会多次收到同一条公告" +
                "\n3.新增用户资料卡，请测试下拉刷新是否正常，签到与未签到按钮是否显示有异常，用户数据是否有异常，后四行数据展开箭头是否显示有异常（展开模式，箭头朝上，未展开时，箭头朝下），且点击那一行或者点击箭头都可以达到展开和关闭的目的" +
                "\n4.新增beta自助申请，请在测试期间，测试是否可以成功获取到正确beta活动激活码（激活码收到如若未及时使用，依旧会有可能被他人使用），获得激活码后跳转是否有异常，在自助平台上的自助授权是否可以正常用账号激活" +
                "\n5.新增x5内核支持，请测试自助授权web页面是否显示异常，以及该页面抽奖功能是否可以正常使用" +
                "\n6.新增关于页面，请及时查看信息是否有误，以及是否有数据为空（可能为启动时初始化未缓存到）" +
                "\n7.侧滑支持显示用户信息，请确保信息是否有误" +
                "\n8.新增账号注销（侧滑页面最下面的注销），一旦注销，请测试下次打开是否不会自动登录" +
                "\n\n【适配】" +
                "\n1.适配注册，登录，账号找回，以及检测更新和公告等接口，请测试所有相关逻辑是否存在异常" +
                "\n\n【已知问题】" +
                "\n1.启动图在部分机型无限死循环，请点击立即跳过进行跳过" +
                "\n2.软件自带多重进程保护，可能存在耗电异常，测试完成后，请及时杀去应用，以免出现大量耗电" +
                "\n\n\n\n注意：" +
                "\n测试过程中，请及时联系管理员qq:544901005开放测试权限，检测更新和公告需要单独开放权限，测试完毕可及时卸载本应用，或直接检测并更新正式版本，感谢参与！");

    }

    private void initData() {
        list_path.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1004138305,3574267912&fm=26&gp=0.jpg");
        list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=269417505,1119922751&fm=26&gp=0.jpg");
        list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1214742538,3795549008&fm=26&gp=0.jpg");
        list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=644298988,1798734310&fm=26&gp=0.jpg");
        list_path.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1565029830,4214558184&fm=26&gp=0.jpg");
        list_path.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=400872882,2453478033&fm=26&gp=0.jpg");
        list_path.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3167642956,1020780379&fm=26&gp=0.jpg");
        //list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530616822341&di=127291d0d27161f38da3da5a00ac14de&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01d881579dc3620000018c1b430c4b.JPG%403000w_1l_2o_100sh.jpg");
        list_title.add("标题-1");
        list_title.add("标题-2");
        list_title.add("标题-3");
        list_title.add("标题-4");
        list_title.add("标题-5");
        list_title.add("标题-6");
        list_title.add("标题-7");
        //list_title.add("标题-8");
    }

    private void initView() {
        list_path = new ArrayList<>();
        list_title = new ArrayList<>();
        initData();
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new MyLoader());
        banner.setBannerAnimation(Transformer.Default);
        banner.setBannerTitles(list_title);
        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(list_path)
                .setOnBannerListener(this)
                .start();
    }

    /**
     * 轮播监听
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        if (list_url.get(position) != null) {
            if (!list_url.get(position).equals("")) {
                Intent intent = new Intent(getActivity(), X5WebGameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", list_url.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        //FToastUtils.init().setRoundRadius(30).show( "你点了第" + (position + 1) + "张轮播图");
    }

    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            options.placeholder(R.drawable.aio_image_default);
            options.error(R.drawable.empty_photo);
            Glide.with(context.getApplicationContext())
                    .load((String) path)
                    .apply(options)
                    .into(imageView);
        }
    }


    //网略请求回调
    private class MyCallback implements okhttp3.Callback {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            int code2=response.code();
            if (code2==200){
                //得到服务器返回的具体内容
                String responseData=response.body().string();

                LogUtil.d("HomeFragement[json]" + responseData);

                BannerBean bannerBean = new Gson().fromJson(responseData, BannerBean.class);

                int banner_num = 0;

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String code=jsonObject.getString("code");

                    if (code.equals("0")){
                        //BannerBean bannerBean = new Gson().fromJson(responseData, BannerBean.class);
                        //Type type = new TypeToken<List<BannerBean.DataBean>>(){}.getType();
                        //bannerBean.setData(new Gson().fromJson(jsonObject.getString("data"), type));
                        bannerBean.setCode(code);
                        //parseNoHeaderJArray(responseData);
                        //parseNoHeaderJArray(jsonObject.getString("data"));
                        //LogUtil.d("HomeFragement[BannerBean]" + bannerBean.getCode());
                        //LogUtil.d("HomrFragement[dataFull]" + bannerBean.toString());
                        //int banner_num = 0;
                        try {
                            JsonParser parser = new JsonParser();
                            //将JSON的String 转成一个JsonArray对象
                            JsonArray jsonArray = parser.parse(jsonObject.getString("data")).getAsJsonArray();
                            LogUtil.d("HomeFragement[JsonArray]" + jsonArray);
                            Gson gson = new Gson();
                            ArrayList<BannerBean.DataBean> bannerBeans = new ArrayList<>();
                            //加强for循环遍历JsonArray
                            for (JsonElement banner_info : jsonArray) {
                                LogUtil.d("HomeFragement[banner_info]" + banner_info.toString());
                                //使用GSON，直接转成Bean对象
                                BannerBean.DataBean dataBean = gson.fromJson(banner_info.toString(), BannerBean.DataBean.class);
                                dataBean.setTitle(new JSONObject(banner_info.toString()).getString("title"));
                                dataBean.setPic(new JSONObject(banner_info.toString()).getString("pic"));
                                dataBean.setUrl(new JSONObject(banner_info.toString()).getString("url"));
                                //LogUtil.d("HomeFragement[bannerBeanTitle]" + bannerBean.getTitle());
                                //LogUtil.d("HomeFragement[bannerBeanPic]" + bannerBean.getPic());
                                //LogUtil.d("HomeFragement[bannerBeanUrl]" + bannerBean.getUrl());
                                bannerBeans.add(dataBean);
                                banner_num ++;
                            }
                            bannerBean.setData(bannerBeans);
                            //LogUtil.d("HomeFragement[bannerBeanTitle]" + bannerBean.getData().get(0).getTitle());
                            //LogUtil.d("HomeFragement[bannerBeanPic]" + bannerBean.getData().get(0).getPic());
                            //LogUtil.d("HomeFragement[bannerBeanUrl]" + bannerBean.getData().get(0).getUrl());
                            //LogUtil.d("HomeFragement[bannerBeanTitle]" + bannerBean.getData());

                        }catch (Exception e){
                            e.printStackTrace();
                            LogUtil.d("HomeFragement[Exception]" + e.toString());
                        }

                        if (tipDialog.isShowing())
                            tipDialog.dismiss();
                    }else {
                        //BannerBean bannerBean = new Gson().fromJson(responseData, BannerBean.class);
                        bannerBean.setCode(code);
                        //LogUtil.d("HomeFragement[BannerBean]" + bannerBean.getCode());

                        if (tipDialog.isShowing())
                            tipDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.d("HomeFragement[Exception]" + e.toString());
                    //BannerBean bannerBean = new Gson().fromJson(responseData, BannerBean.class);

                    //LogUtil.d("HomeFragement[BannerBean]" + bannerBean.getCode());

                    if (tipDialog.isShowing())
                        tipDialog.dismiss();
                }

                int finalBanner_num = banner_num;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        banner.setVisibility(View.VISIBLE);
                        //cardView_1.setVisibility(View.VISIBLE);
                        list_path = new ArrayList<>();
                        list_title = new ArrayList<>();
                        list_url = new ArrayList<>();
                        for (int i = 0; i < finalBanner_num; i ++){
                            list_path.add(bannerBean.getData().get(i).getPic());
                            list_title.add(bannerBean.getData().get(i).getTitle());
                            list_url.add(bannerBean.getData().get(i).getUrl());
                        }
                        //initData();
                        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                        banner.setImageLoader(new MyLoader());
                        banner.setBannerAnimation(Transformer.Default);
                        banner.setBannerTitles(list_title);
                        banner.setDelayTime(3000);
                        banner.isAutoPlay(true);
                        banner.setIndicatorGravity(BannerConfig.CENTER);
                        banner.setImages(list_path)
                                .setOnBannerListener(HomeFragment.this::OnBannerClick)
                                .start();
                        //tv_result.setText(result);
                    }
                });

                //LogUtil.d("HomeFragement[BannerBean]" + bannerBean.getCode());
                //LogUtil.d("HomeFragement[data_1_title]" + bannerBean.getData().get(1).getTitle());
                //LogUtil.d("HomeFragement[dataFull]" + bannerBean.toString());

                //获取数据
                /*try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    int code=jsonObject.getInt("code");

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

        }
        @Override
        public void onFailure(Call call, IOException e){
            if (e instanceof SocketTimeoutException) {
                //判断超时异常
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("访问超时");
                //sendMyMessage(-1);
            }
            if (e instanceof ConnectException) {
                //判断连接异常，
                if (tipDialog.isShowing())
                    tipDialog.dismiss();
                FToastUtils.init().setRoundRadius(30).show("连接异常");
                //sendMyMessage(-2);
            }
        }
    }

    Handler h1=new Handler()
    {

        private String content = null;

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

                    if (getActivity() == null){
                        //this.removeCallbacksAndMessages(null);
                        //break;
                    }else {

                        if (String.valueOf(msg.obj).equals(getActivity().getString(R.string.notice_new_version))) {

                            content = content_manager.get_app_content();

                            if (content == null){

                            }else if (content == ""){

                            }else {

                                client=new OkHttpClient();

                                new Thread(){
                                    @Override
                                    public void run() {

                                        String key = "";
                                        try {
                                            key = Encode.work(String.valueOf((int) (System.currentTimeMillis() / 1000)));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        HttpUtil.sendOkHttpRequest(content, new MyCallback(),key);

                                    }
                                }.start();

                            }

                        } else {
                            if (tipDialog.isShowing())
                                tipDialog.dismiss();
                        }

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

}
