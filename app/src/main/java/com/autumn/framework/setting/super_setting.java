package com.autumn.framework.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.autumn.framework.Log.CrashHandler;
import com.autumn.framework.Log.DumpUtil;
import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.Log.LogUtils;
import com.autumn.framework.X5WebView.ui.X5WebGameActivity;
import com.autumn.framework.data.Data_editer;
import com.autumn.framework.data.FileSizeUtil;
import com.autumn.framework.data.GlideCacheUtil;
import com.autumn.framework.data.LogServerManger;
import com.autumn.framework.music.CutAudioActivity;
import com.autumn.framework.update.utils.UpdateAppHttpUtil;
import com.autumn.reptile.MainActivity;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.reptile.ui.ThemeDialog;
import com.autumn.sdk.data.SpUtil;
import com.liyi.sutils.utils.AtyTransitionUtil;
import com.liyi.sutils.utils.io.FileUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.smtt.sdk.QbSdk;
import com.vector.update_app.UpdateAppManager;

import java.lang.reflect.Field;

import cn.hotapk.fastandrutils.utils.FLogUtils;
import cn.hotapk.fastandrutils.utils.FNetworkUtils;
import cn.hotapk.fastandrutils.utils.FToastUtils;

public class super_setting extends BaseActivity {

	private int width;

	private int height;

	private ToggleButton setting_1;

	private ToggleButton setting_2;

	private ToggleButton setting_3;

	private ToggleButton setting_4;

	private ToggleButton setting_5;

	private ToggleButton setting_6;

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

	private ToggleButton setting_7;

	private ToggleButton setting_8;

	private ToggleButton setting_9;

	private ToggleButton setting_10;

	private String ALLOW_ALL = "Allow_all";
	private LinearLayout dump;
	private TextView glide_temp_size;
	private LinearLayout clean_glide_temp;
	private CardView cardView_1;
	private CardView cardView_2;
	private CardView cardView_3;
	private CardView cardView_4;
	private TextView log_file_size;
	private TextView crash_file_size;
	private TextView dump_file_size;
	private LinearLayout clean_log_file;
	private LinearLayout clean_crash_file;
	private LinearLayout clean_dump_file;
	private TextView update_file_size;
	private LinearLayout clean_update_file;
	private TextView all_file_size;
	private LinearLayout clean_all_file;
	private TextView banner_file_size;
	private LinearLayout clean_banner_file;
	public static boolean dump_finished = false;
	private String ALLOW_SETTING = "Allow_setting";
	public static boolean is_auto = false;
	public static boolean is_savelog = false;
	public static boolean Allow_D = false;
	public static boolean Allow_E = false;
	public static boolean Allow_I = false;
	public static boolean Allow_V = false;
	public static boolean Allow_W = false;
	public static boolean Allow_WTF = false;
	public static boolean Allow_ALL = false;
	public static boolean Allow_DEBUG = false;
	public static boolean Allow_SETTING = false;
	private CardView cardView_5;
	private LinearLayout change_color;
	private CardView cardView_6;
	private TextView status_bar_height;
	private LinearLayout status_bar_height_refesh;
	private static final String TAG = "QD_SETTING";

	private ProgressBar progressBar;
	private TextView textview;
	private LinearLayout x5_setting;
	private LinearLayout core_info;
	private TextView core_name;
	private LinearLayout test_webview;
	private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
	private TextView kuwo_music_quality;
	private TextView kugou_music_quality;
	private TextView tencent_music_quality;
	private TextView netease_music_quality;
	private LinearLayout netease_music_info;
	private LinearLayout tencent_music_info;
	private LinearLayout kugou_music_info;
	private LinearLayout kuwo_music_info;
	private String MUSIC_CONFIG = "Music_config";
	private String NETEASE = "Netease";
	private String TENCENT = "Tencent";
	private String KUGOU = "Kugou";
	private String KUWO = "Kuwo";
	private String Netease_quality;
	private String Tencent_quality;
	private String Kugou_quality;
	private String Kuwo_quality;
	private LinearLayout migu_music_info;
	private LinearLayout baidu_music_info;
	private TextView migu_music_quality;
	private TextView baidu_music_quality;
	private String MIGU = "Migu";
	private String BAIDU = "Baidu";
	private String Migu_quality;
	private String Baidu_quality;
	private ToggleButton setting_11;
	private LinearLayout music_setting;
	private LinearLayout cut_audio;
	private LinearLayout pic_shower;
	private LinearLayout update_test;
	private TextView play_mode;
	private static final String PLAYER_PLAY_MODE = "Player_play_mode";
	private static final String FORGROUND = "Forground";
	private static final String BACKGROUND = "Background";
	private static final String PLAYER_SETTING = "Player_setting";
	private LinearLayout player_play_mode;

	//MV
	//@BindView(R.id.netease_mv_info)
	private LinearLayout netease_mv_info;
	//@BindView(R.id.netease_mv_quality)
	private TextView netease_mv_quality;
	//@BindView(R.id.tencent_mv_info)
	private LinearLayout tencent_mv_info;
	//@BindView(R.id.tencent_mv_quality)
	private TextView tencent_mv_quality;
	//@BindView(R.id.kugou_mv_info)
	private LinearLayout kugou_mv_info;
	//@BindView(R.id.kugou_mv_quality)
	private TextView kugou_mv_quality;
	//@BindView(R.id.kuwo_mv_info)
	private LinearLayout kuwo_mv_info;
	//@BindView(R.id.kuwo_mv_quality)
	private TextView kuwo_mv_quality;
	//@BindView(R.id.migu_mv_info)
	private LinearLayout migu_mv_info;
	//@BindView(R.id.migu_mv_quality)
	private TextView migu_mv_quality;
	//@BindView(R.id.baidu_mv_info)
	private LinearLayout baidu_mv_info;
	//@BindView(R.id.baidu_mv_quality)
	private TextView baidu_mv_quality;

	private String MV_CONFIG = "Mv_config";
	private String neteaseMvQuality;
	private String tencentMvQuality;
	private String kugouMvQuality;
	private String kuwoMvQuality;
	private String miguMvQuality;
	private String baiduMvQuality;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*if (!isTaskRoot()) {
            finish();
            return;
        }*/
		setContentView(R.layout.activity_super_setting);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
			//getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
			//getWindow().setNavigationBarColor(Color.BLUE);
		}

		//init();

		initState();

		init_file_path();

		// 每次加入stack
		ActivtyStack.getScreenManager().pushActivity(this);

		initX5();

		DisplayMetrics dm = getResources().getDisplayMetrics();
		width = dm.widthPixels;
		height = dm.heightPixels;

		setting_1 = (ToggleButton) findViewById(R.id.supersettingToggleButton1);
		setting_2 = (ToggleButton) findViewById(R.id.supersettingToggleButton2);
		setting_3 = (ToggleButton) findViewById(R.id.supersettingToggleButton3);
		setting_4 = (ToggleButton) findViewById(R.id.supersettingToggleButton4);
		setting_5 = (ToggleButton) findViewById(R.id.supersettingToggleButton5);
		setting_6 = (ToggleButton) findViewById(R.id.supersettingToggleButton6);
		setting_7 = (ToggleButton) findViewById(R.id.supersettingToggleButton7);
		setting_8 = (ToggleButton) findViewById(R.id.supersettingToggleButton8);
		setting_9 = (ToggleButton) findViewById(R.id.supersettingToggleButton9);
		setting_10 = (ToggleButton) findViewById(R.id.supersettingToggleButton10);
		dump = (LinearLayout) findViewById(R.id.dump_manual);
		glide_temp_size = (TextView) findViewById(R.id.glide_temp_size);
		clean_glide_temp = (LinearLayout) findViewById(R.id.clean_glide_temp);
		glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
		log_file_size = (TextView) findViewById(R.id.log_file_size);
		log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
		crash_file_size = (TextView) findViewById(R.id.crash_file_size);
		crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
		dump_file_size = (TextView) findViewById(R.id.dump_file_size);
		dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
		clean_log_file = (LinearLayout) findViewById(R.id.clean_log_file);
		clean_crash_file = (LinearLayout) findViewById(R.id.clean_crash_file);
		clean_dump_file = (LinearLayout) findViewById(R.id.clean_dump_file);
		clean_update_file = (LinearLayout) findViewById(R.id.clean_update_file);
		update_file_size = (TextView) findViewById(R.id.update_file_size);
		update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
		all_file_size = (TextView) findViewById(R.id.all_file_size);
		all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
		clean_all_file = (LinearLayout) findViewById(R.id.clean_all_file);
		banner_file_size = (TextView) findViewById(R.id.banner_file_size);
		clean_banner_file = (LinearLayout) findViewById(R.id.clean_banner_file);
		banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
		change_color = (LinearLayout) findViewById(R.id.change_color);
		status_bar_height = (TextView) findViewById(R.id.status_bar_height);
		status_bar_height.setText(String.valueOf(QMUIStatusBarHelper.getStatusbarHeight(super_setting.this)));
		status_bar_height_refesh = (LinearLayout) findViewById(R.id.status_bar_height_refresh);
		x5_setting = (LinearLayout) findViewById(R.id.x5_setting);
		core_info = (LinearLayout) findViewById(R.id.core_info);
		test_webview = (LinearLayout) findViewById(R.id.test_webview);
		netease_music_info = (LinearLayout)findViewById(R.id.netease_music_info);
		tencent_music_info = (LinearLayout)findViewById(R.id.tencent_music_info);
		kugou_music_info = (LinearLayout)findViewById(R.id.kugou_music_info);
		kuwo_music_info = (LinearLayout)findViewById(R.id.kuwo_music_info);
		netease_music_quality = (TextView)findViewById(R.id.netease_music_quality);
		tencent_music_quality = (TextView)findViewById(R.id.tencent_music_quality);
		kugou_music_quality = (TextView)findViewById(R.id.kugou_music_quality);
		kuwo_music_quality = (TextView)findViewById(R.id.kuwo_music_quality);
		migu_music_info = (LinearLayout)findViewById(R.id.migu_music_info);
		baidu_music_info = (LinearLayout)findViewById(R.id.baidu_music_info);
		migu_music_quality = (TextView)findViewById(R.id.migu_music_quality);
		baidu_music_quality = (TextView)findViewById(R.id.baidu_music_quality);
		setting_11 = (ToggleButton) findViewById(R.id.supersettingToggleButton11);
		music_setting = (LinearLayout)findViewById(R.id.music_setting);
		cut_audio = (LinearLayout)findViewById(R.id.cut_audio);
		update_test = (LinearLayout)findViewById(R.id.update_test);
		pic_shower = (LinearLayout)findViewById(R.id.pic_shower);
		play_mode = (TextView)findViewById(R.id.play_mode);
		player_play_mode = (LinearLayout)findViewById(R.id.player_play_mode);
		netease_mv_info = (LinearLayout)findViewById(R.id.netease_mv_info);
		netease_mv_quality = (TextView)findViewById(R.id.netease_mv_quality);
		tencent_mv_info = (LinearLayout)findViewById(R.id.tencent_mv_info);
		tencent_mv_quality = (TextView)findViewById(R.id.tencent_mv_quality);
		kugou_mv_info = (LinearLayout)findViewById(R.id.kugou_mv_info);
		kugou_mv_quality = (TextView)findViewById(R.id.kugou_mv_quality);
		kuwo_mv_info = (LinearLayout)findViewById(R.id.kuwo_mv_info);
		kuwo_mv_quality = (TextView)findViewById(R.id.kuwo_mv_quality);
		migu_mv_info = (LinearLayout)findViewById(R.id.migu_mv_info);
		migu_mv_quality = (TextView)findViewById(R.id.migu_mv_quality);
		baidu_mv_info = (LinearLayout)findViewById(R.id.baidu_mv_info);
		baidu_mv_quality = (TextView)findViewById(R.id.baidu_mv_quality);

		String play_mode_TYPE = new SpUtil().getValue(super_setting.this, PLAYER_SETTING, PLAYER_PLAY_MODE);
		if (play_mode_TYPE == null)
			play_mode.setText("每次询问");
		else if (play_mode_TYPE.equals(""))
			play_mode.setText("每次询问");
		else if (play_mode_TYPE.equals(FORGROUND))
			play_mode.setText("仅允许前台播放");
		else if (play_mode_TYPE.equals(BACKGROUND))
			play_mode.setText("允许后台播放");

		player_play_mode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				set_player_mode();
			}
		});

		pic_shower.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(super_setting.this, com.autumn.framework.picEngine.MainActivity.class);
				startActivity(intent);
				AtyTransitionUtil.exitToRight(super_setting.this);
			}
		});

		update_test.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTestUpateDialog();
			}
		});

		cut_audio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(super_setting.this);
				FToastUtils.init().showLong("在此输入您的测试文件名称（请先将文件放入内部存储/com.autumn.reptile/shared/Other目录下）");
				builder.setTitle("请输入测试文件名称")
						.setPlaceholder("在此输入您的测试文件名称（请先将文件放入内部存储/com.autumn.reptile/shared/Other目录下）")
						.setInputType(InputType.TYPE_CLASS_TEXT)
						.addAction("取消", new QMUIDialogAction.ActionListener() {
							@Override
							public void onClick(QMUIDialog dialog, int index) {
								dialog.dismiss();
							}
						})
						.addAction("确定", new QMUIDialogAction.ActionListener() {
							@Override
							public void onClick(QMUIDialog dialog, int index) {
								CharSequence text = builder.getEditText().getText();
								if (text != null && text.length() > 0) {
									dialog.dismiss();
									Intent intent = new Intent(super_setting.this, CutAudioActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("key", text.toString());
									intent.putExtras(bundle);
									startActivity(intent);
								}else {
									FToastUtils.init().setRoundRadius(30).show("未输入测试文件名称");
								}
								/*if (text != null && text.length() > 0) {
									//Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT).show();
									dialog.dismiss();
									Intent intent = new Intent(super_setting.this, com.autumn.framework.music.MainActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("key", text.toString());
									intent.putExtras(bundle);
									startActivity(intent);
								} else {
									//Toast.makeText(getActivity(), "请填入昵称", Toast.LENGTH_SHORT).show();
									FToastUtils.init().setRoundRadius(30).show("未输入测试音乐URL");
								}*/
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		music_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(super_setting.this);
				builder.setTitle("请输入测试音乐URL")
						.setPlaceholder("在此输入您的测试音乐URL")
						.setInputType(InputType.TYPE_CLASS_TEXT)
						.addAction("取消", new QMUIDialogAction.ActionListener() {
							@Override
							public void onClick(QMUIDialog dialog, int index) {
								dialog.dismiss();
							}
						})
						.addAction("确定", new QMUIDialogAction.ActionListener() {
							@Override
							public void onClick(QMUIDialog dialog, int index) {
								CharSequence text = builder.getEditText().getText();
								dialog.dismiss();
								Intent intent = new Intent(super_setting.this, com.autumn.framework.music.MainActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("key", text.toString());
								intent.putExtras(bundle);
								startActivity(intent);
								/*if (text != null && text.length() > 0) {
									//Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT).show();
									dialog.dismiss();
									Intent intent = new Intent(super_setting.this, com.autumn.framework.music.MainActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("key", text.toString());
									intent.putExtras(bundle);
									startActivity(intent);
								} else {
									//Toast.makeText(getActivity(), "请填入昵称", Toast.LENGTH_SHORT).show();
									FToastUtils.init().setRoundRadius(30).show("未输入测试音乐URL");
								}*/
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		if (LogServerManger.getLogServerStatus()){
			setting_11.setChecked(true);
		}else {
			setting_11.setChecked(false);
		}

		setting_11.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//rw.writeBoolean(super_setting.this, SETTING, AUTO_POST, true);
					//CrashHandler crashHandler = CrashHandler.getInstance();
					//crashHandler.init(getApplicationContext());
					//crashHandler.Auto_post = true;
					int port = 9090;
					FLogUtils.getInstance().startLogServer(port);
					FToastUtils.init().setRoundRadius(30).showLong("已打开log内网服务" + "\n请访问：http://" + FNetworkUtils.getIPAddress() + ":" + port);
					LogServerManger.setLogServerStatus(true);
				} else {
					//rw.writeBoolean(super_setting.this, SETTING, AUTO_POST, false);
					//CrashHandler crashHandler = CrashHandler.getInstance();
					//crashHandler.init(getApplicationContext());
					//crashHandler.Auto_post = false;
					FLogUtils.getInstance().stopLogServer();
					FToastUtils.init().setRoundRadius(30).show("已关闭log内网服务");
					LogServerManger.setLogServerStatus(false);
				}
			}
		});

		test_webview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showEditTextDialog();
			}
		});

		core_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(super_setting.this, X5WebGameActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("key", "http://soft.imtt.qq.com/browser/tes/feedback.html");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		x5_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(super_setting.this, X5WebGameActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("key", "http://debugtbs.qq.com/");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		status_bar_height_refesh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				status_bar_height.setText(String.valueOf(QMUIStatusBarHelper.getStatusbarHeight(super_setting.this)));
			}
		});

		change_color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ThemeDialog dialog = new ThemeDialog();
				dialog.show(getSupportFragmentManager(), "theme");
			}
		});

		clean_log_file.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clean_file("logs");
				log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
				crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
				dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
				update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
				banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
				all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
				FToastUtils.init().setRoundRadius(30).show("删除成功！");
			}
		});

		clean_crash_file.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clean_file("crash");
				log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
				crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
				dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
				update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
				banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
				all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
				FToastUtils.init().setRoundRadius(30).show("删除成功！");
			}
		});

		clean_dump_file.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clean_file("dump");
				log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
				crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
				dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
				update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
				banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
				all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
				FToastUtils.init().setRoundRadius(30).show("删除成功！");
			}
		});

		clean_update_file.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clean_file("download");
				log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
				crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
				dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
				update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
				banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
				all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
				FToastUtils.init().setRoundRadius(30).show("删除成功！");
			}
		});

		clean_banner_file.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clean_file("cache");
				log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
				crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
				dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
				update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
				banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
				all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
				FToastUtils.init().setRoundRadius(30).show("删除成功！");
			}
		});

		clean_all_file.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clean_file("logs");
				clean_file("crash");
				clean_file("dump");
				clean_file("download");
				clean_file("cache");
				log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
				crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
				dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
				update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
				banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
				all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
				FToastUtils.init().setRoundRadius(30).show("删除成功！");
			}
		});

		cardView_1 = (CardView) findViewById(R.id.cardView_1);
		cardView_1.setRadius(30);//设置图片圆角的半径大小
		cardView_1.setCardElevation(8);//设置阴影部分大小
		cardView_1.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
		cardView_2 = (CardView) findViewById(R.id.cardView_2);
		cardView_2.setRadius(30);//设置图片圆角的半径大小
		cardView_2.setCardElevation(8);//设置阴影部分大小
		cardView_2.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
		cardView_3 = (CardView) findViewById(R.id.cardView_3);
		cardView_3.setRadius(30);//设置图片圆角的半径大小
		cardView_3.setCardElevation(8);//设置阴影部分大小
		cardView_3.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
		cardView_4 = (CardView) findViewById(R.id.cardView_4);
		cardView_4.setRadius(30);//设置图片圆角的半径大小
		cardView_4.setCardElevation(8);//设置阴影部分大小
		cardView_4.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
		cardView_5 = (CardView) findViewById(R.id.cardView_5);
		cardView_5.setRadius(30);//设置图片圆角的半径大小
		cardView_5.setCardElevation(8);//设置阴影部分大小
		cardView_5.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
		cardView_6 = (CardView) findViewById(R.id.cardView_6);
		cardView_6.setRadius(30);//设置图片圆角的半径大小
		cardView_6.setCardElevation(8);//设置阴影部分大小
		cardView_6.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小

		clean_glide_temp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
				crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
				dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
				update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
				banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
				GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
				GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
				glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
				all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
				FToastUtils.init().setRoundRadius(30).show("Glide缓存清理成功！");
			}
		});

		dump.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView dump_now = (ImageView) findViewById(R.id.dump_now);
				dump_now.setVisibility(View.GONE);
				TextView dump_doing = (TextView) findViewById(R.id.dump_doing);
				dump_doing.setVisibility(View.VISIBLE);
				dump.setEnabled(false);
				DumpUtil.createDumpFile(super_setting.this);
				while (dump_finished) {
					dump_finished = false;
					log_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs"));
					crash_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash"));
					dump_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump"));
					update_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download"));
					banner_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache"));
					GlideCacheUtil.getInstance().clearImageAllCache(super_setting.this);
					GlideCacheUtil.getInstance().clearImageDiskCache(super_setting.this);
					GlideCacheUtil.getInstance().clearImageMemoryCache(super_setting.this);
					glide_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(super_setting.this));
					all_file_size.setText(FileSizeUtil.getAutoFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path)));
					dump_doing.setVisibility(View.GONE);
					dump_now.setVisibility(View.VISIBLE);
					dump.setEnabled(true);
				}
			}
		});

		final SpUtil rw = new SpUtil();

		//is_auto = rw.getBoolean(super_setting.this,SETTING,AUTO_POST);
		//is_savelog = rw.getBoolean(super_setting.this,SETTING,AUTO_SAVELOG);
		//Allow_D = rw.getBoolean(super_setting.this,SETTING,ALLOW_D);
		//Allow_E = rw.getBoolean(super_setting.this,SETTING,ALLOW_E);
		//Allow_I = rw.getBoolean(super_setting.this,SETTING,ALLOW_I);
		//Allow_V = rw.getBoolean(super_setting.this,SETTING,ALLOW_V);
		//Allow_W = rw.getBoolean(super_setting.this,SETTING,ALLOW_W);
		//Allow_WTF = rw.getBoolean(super_setting.this,SETTING,ALLOW_WTF);
		//Allow_ALL = rw.getBoolean(super_setting.this,SETTING,ALLOW_ALL);
		//Allow_DEBUG = rw.getBoolean(super_setting.this,SETTING,ALLOW_DEBUG);
		//Allow_SETTING = rw.getBoolean(super_setting.this,SETTING,ALLOW_SETTING);

		//setting_1.setWidth(width/8);

		if (is_auto) {
			setting_1.setChecked(true);
		}
		if (is_savelog) {
			setting_2.setChecked(true);
		}
		if (Allow_D) {
			setting_3.setChecked(true);
		}
		if (Allow_E) {
			setting_4.setChecked(true);
		}
		if (Allow_I) {
			setting_5.setChecked(true);
		}
		if (Allow_V) {
			setting_6.setChecked(true);
		}
		if (Allow_W) {
			setting_7.setChecked(true);
		}
		if (Allow_WTF) {
			setting_8.setChecked(true);
		}
		if (Allow_ALL) {
			setting_2.setChecked(true);
			setting_3.setChecked(true);
			setting_4.setChecked(true);
			setting_5.setChecked(true);
			setting_6.setChecked(true);
			setting_7.setChecked(true);
			setting_8.setChecked(true);
			setting_9.setChecked(true);
		}
		if (Allow_DEBUG) {
			setting_10.setChecked(true);
		}

		if (Allow_SETTING) {
			setting_1.setEnabled(true);
			setting_2.setEnabled(true);
			setting_3.setEnabled(true);
			setting_4.setEnabled(true);
			setting_5.setEnabled(true);
			setting_6.setEnabled(true);
			setting_7.setEnabled(true);
			setting_8.setEnabled(true);
			setting_9.setEnabled(true);
			setting_10.setEnabled(true);
		} else {
			setting_1.setEnabled(false);
			setting_2.setEnabled(false);
			setting_3.setEnabled(false);
			setting_4.setEnabled(false);
			setting_5.setEnabled(false);
			setting_6.setEnabled(false);
			setting_7.setEnabled(false);
			setting_8.setEnabled(false);
			setting_9.setEnabled(false);
			setting_10.setEnabled(false);
		}

		setting_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, AUTO_POST, true);
					CrashHandler crashHandler = CrashHandler.getInstance();
					crashHandler.init(getApplicationContext());
					crashHandler.Auto_post = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, AUTO_POST, false);
					CrashHandler crashHandler = CrashHandler.getInstance();
					crashHandler.init(getApplicationContext());
					crashHandler.Auto_post = false;
				}
			}
		});

		setting_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, AUTO_SAVELOG, true);
					LogUtil.isSaveLog = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, AUTO_SAVELOG, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					setting_9.setChecked(false);
					LogUtil.isSaveLog = false;
				}
			}
		});

		setting_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_D, true);
					LogUtil.allowD = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_D, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					setting_9.setChecked(false);
					LogUtil.allowD = false;
				}
			}
		});

		setting_4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_E, true);
					LogUtil.allowE = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_E, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					setting_9.setChecked(false);
					LogUtil.allowE = false;
				}
			}
		});

		setting_5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_I, true);
					LogUtil.allowI = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_I, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					setting_9.setChecked(false);
					LogUtil.allowI = false;
				}
			}
		});

		setting_6.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_V, true);
					LogUtil.allowV = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_V, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					setting_9.setChecked(false);
					LogUtil.allowV = false;

				}
			}
		});

		setting_7.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_W, true);
					LogUtil.allowW = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_W, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					setting_9.setChecked(false);
					LogUtil.allowW = false;
				}
			}
		});

		setting_8.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_WTF, true);
					LogUtil.allowWtf = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_WTF, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					setting_9.setChecked(false);
					LogUtil.allowWtf = false;
				}
			}
		});

		setting_9.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, true);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_D, true);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_E, true);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_I, true);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_V, true);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_W, true);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_WTF, true);
					LogUtil.isSaveLog = true;
					LogUtil.allowD = true;
					LogUtil.allowE = true;
					LogUtil.allowI = true;
					LogUtil.allowV = true;
					LogUtil.allowW = true;
					LogUtil.allowWtf = true;
					setting_2.setChecked(true);
					setting_3.setChecked(true);
					setting_4.setChecked(true);
					setting_5.setChecked(true);
					setting_6.setChecked(true);
					setting_7.setChecked(true);
					setting_8.setChecked(true);
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_ALL, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_D, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_E, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_I, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_V, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_W, false);
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_WTF, false);
					LogUtil.isSaveLog = false;
					LogUtil.allowD = false;
					LogUtil.allowE = false;
					LogUtil.allowI = false;
					LogUtil.allowV = false;
					LogUtil.allowW = false;
					LogUtil.allowWtf = false;
					setting_2.setChecked(false);
					setting_3.setChecked(false);
					setting_4.setChecked(false);
					setting_5.setChecked(false);
					setting_6.setChecked(false);
					setting_7.setChecked(false);
					setting_8.setChecked(false);
				}
			}
		});

		setting_10.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_DEBUG, true);
					LogUtils.isDebug = true;
				} else {
					rw.writeBoolean(super_setting.this, SETTING, ALLOW_DEBUG, false);
					LogUtils.isDebug = false;
				}
			}
		});

		Netease_quality = rw.getValue(super_setting.this, MUSIC_CONFIG, NETEASE);
		Tencent_quality = rw.getValue(super_setting.this, MUSIC_CONFIG, TENCENT);
		Kugou_quality = rw.getValue(super_setting.this, MUSIC_CONFIG, KUGOU);
		Kuwo_quality = rw.getValue(super_setting.this, MUSIC_CONFIG, KUWO);
		Migu_quality = rw.getValue(super_setting.this, MUSIC_CONFIG, MIGU);
		Baidu_quality = rw.getValue(super_setting.this, MUSIC_CONFIG, BAIDU);

		if (Netease_quality == null){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
			netease_music_quality.setText("320000");
			Netease_quality = "320000";
		}else if (Netease_quality.equals("")){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
			netease_music_quality.setText("320000");
			Netease_quality = "320000";
		}else {
			netease_music_quality.setText(Netease_quality);
		}

		if (Tencent_quality == null){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
			tencent_music_quality.setText("192");
			Tencent_quality = "192";
		}else if (Tencent_quality.equals("")){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
			tencent_music_quality.setText("192");
			Tencent_quality = "192";
		}else {
			tencent_music_quality.setText(Tencent_quality);
		}

		if (Kugou_quality == null){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
			kugou_music_quality.setText("128");
			Kugou_quality = "128";
		}else if (Kugou_quality.equals("")){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
			kugou_music_quality.setText("128");
			Kugou_quality = "128";
		}else {
			kugou_music_quality.setText(Kugou_quality);
		}

		if (Kuwo_quality == null){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
			kuwo_music_quality.setText("128");
			Kuwo_quality = "128";
		}else if (Kuwo_quality.equals("")){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
			kuwo_music_quality.setText("128");
			Kuwo_quality = "128";
		}else {
			kuwo_music_quality.setText(Kuwo_quality);
		}

		if (Migu_quality == null){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, MIGU, "128");
			migu_music_quality.setText("128");
			Migu_quality = "128";
		}else if (Migu_quality.equals("")){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, MIGU, "128");
			migu_music_quality.setText("128");
			Migu_quality = "128";
		}else {
			migu_music_quality.setText(Migu_quality);
		}

		if (Baidu_quality == null){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
			baidu_music_quality.setText("128");
			Baidu_quality = "128";
		}else if (Baidu_quality.equals("")){
			rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
			baidu_music_quality.setText("128");
			Baidu_quality = "128";
		}else {
			baidu_music_quality.setText(Baidu_quality);
		}

		netease_music_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"128000", "192000", "320000", "999000"};
				int checkedIndex = 2;
				if (Netease_quality == null){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
					netease_music_quality.setText("320000");
					Netease_quality = "320000";
				}else if (Netease_quality.equals("")){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, "320000");
					netease_music_quality.setText("320000");
					Netease_quality = "320000";
				}
				switch (Netease_quality){
					case "128000":
						checkedIndex = 0;
						break;
					case "192000":
						checkedIndex = 1;
						break;
					case "320000":
						checkedIndex = 2;
						break;
					case "999000":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MUSIC_CONFIG, NETEASE, items[which]);
								netease_music_quality.setText(items[which]);
								Netease_quality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		tencent_music_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"24", "96", "128", "192", "320", "ape", "flac"};
				int checkedIndex = 3;
				if (Tencent_quality == null){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
					tencent_music_quality.setText("192");
					Tencent_quality = "192";
				}else if (Tencent_quality.equals("")){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, "192");
					tencent_music_quality.setText("192");
					Tencent_quality = "192";
				}
				switch (Tencent_quality){
					case "24":
						checkedIndex = 0;
						break;
					case "96":
						checkedIndex = 1;
						break;
					case "128":
						checkedIndex = 2;
						break;
					case "192":
						checkedIndex = 3;
						break;
					case "320":
						checkedIndex = 4;
						break;
					case "ape":
						checkedIndex = 5;
						break;
					case "flac":
						checkedIndex = 6;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MUSIC_CONFIG, TENCENT, items[which]);
								tencent_music_quality.setText(items[which]);
								Tencent_quality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		kugou_music_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"128", "320", "flac", "hr", "dsd"};
				int checkedIndex = 0;
				if (Kugou_quality == null){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
					kugou_music_quality.setText("128");
					Kugou_quality = "128";
				}else if (Kugou_quality.equals("")){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, "128");
					kugou_music_quality.setText("128");
					Kugou_quality = "128";
				}
				switch (Kugou_quality){
					case "128":
						checkedIndex = 0;
						break;
					case "320":
						checkedIndex = 1;
						break;
					case "flac":
						checkedIndex = 2;
						break;
					case "hr":
						checkedIndex = 3;
						break;
					case "dsd":
						checkedIndex = 4;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MUSIC_CONFIG, KUGOU, items[which]);
								kugou_music_quality.setText(items[which]);
								Kugou_quality = items[which];
								dialog.dismiss();
								if (which > 2)
									FToastUtils.init().setRoundRadius(30).showLong("修改成功，由于当前音质音乐源较少，不建议选择！");
								else
									FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		kuwo_music_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"128", "192", "320", "ape", "flac"};
				int checkedIndex = 0;
				if (Kuwo_quality == null){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
					kuwo_music_quality.setText("128");
					Kuwo_quality = "128";
				}else if (Kuwo_quality.equals("")){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, "128");
					kuwo_music_quality.setText("128");
					Kuwo_quality = "128";
				}
				switch (Kuwo_quality){
					case "128":
						checkedIndex = 0;
						break;
					case "192":
						checkedIndex = 1;
						break;
					case "320":
						checkedIndex = 2;
						break;
					case "ape":
						checkedIndex = 3;
						break;
					case "flac":
						checkedIndex = 4;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MUSIC_CONFIG, KUWO, items[which]);
								kuwo_music_quality.setText(items[which]);
								Kuwo_quality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		migu_music_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"128", "192", "320", "flac"};
				int checkedIndex = 0;
				if (Migu_quality == null){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, MIGU, "128");
					migu_music_quality.setText("128");
					Migu_quality = "128";
				}else if (Migu_quality.equals("")){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, MIGU, "128");
					migu_music_quality.setText("128");
					Migu_quality = "128";
				}
				switch (Migu_quality){
					case "128":
						checkedIndex = 0;
						break;
					case "192":
						checkedIndex = 1;
						break;
					case "320":
						checkedIndex = 2;
						break;
					case "flac":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MUSIC_CONFIG, MIGU, items[which]);
								migu_music_quality.setText(items[which]);
								Migu_quality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		baidu_music_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"64", "128", "320", "flac"};
				int checkedIndex = 0;
				if (Baidu_quality == null){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
					baidu_music_quality.setText("128");
					Baidu_quality = "128";
				}else if (Baidu_quality.equals("")){
					rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, "128");
					baidu_music_quality.setText("128");
					Baidu_quality = "128";
				}
				switch (Baidu_quality){
					case "64":
						checkedIndex = 0;
						break;
					case "128":
						checkedIndex = 1;
						break;
					case "320":
						checkedIndex = 2;
						break;
					case "flac":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MUSIC_CONFIG, BAIDU, items[which]);
								baidu_music_quality.setText(items[which]);
								Baidu_quality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		neteaseMvQuality = rw.getValue(super_setting.this, MV_CONFIG, NETEASE);
		tencentMvQuality = rw.getValue(super_setting.this, MV_CONFIG, TENCENT);
		kugouMvQuality = rw.getValue(super_setting.this, MV_CONFIG, KUGOU);
		kuwoMvQuality = rw.getValue(super_setting.this, MV_CONFIG, KUWO);
		miguMvQuality = rw.getValue(super_setting.this, MV_CONFIG, MIGU);
		baiduMvQuality = rw.getValue(super_setting.this, MV_CONFIG, BAIDU);

		if (neteaseMvQuality == null){
			neteaseMvQuality = "480";
			netease_mv_quality.setText(neteaseMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, NETEASE, neteaseMvQuality);
		}else if (neteaseMvQuality.equals("")){
			neteaseMvQuality = "480";
			netease_mv_quality.setText(neteaseMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, NETEASE, neteaseMvQuality);
		}else {
			netease_mv_quality.setText(neteaseMvQuality);
		}

		if (tencentMvQuality == null){
			tencentMvQuality = "480";
			tencent_mv_quality.setText(tencentMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, TENCENT, tencentMvQuality);
		}else if (tencentMvQuality.equals("")){
			tencentMvQuality = "480";
			tencent_mv_quality.setText(tencentMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, TENCENT, tencentMvQuality);
		}else {
			tencent_mv_quality.setText(tencentMvQuality);
		}

		if (kugouMvQuality == null){
			kugouMvQuality = "480";
			kugou_mv_quality.setText(kugouMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, KUGOU, kugouMvQuality);
		}else if (kugouMvQuality.equals("")){
			kugouMvQuality = "480";
			kugou_mv_quality.setText(kugouMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, KUGOU, kugouMvQuality);
		}else {
			kugou_mv_quality.setText(kugouMvQuality);
		}

		if (kuwoMvQuality == null){
			kuwoMvQuality = "480";
			kuwo_mv_quality.setText(kuwoMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, KUWO, kuwoMvQuality);
		}else if (kuwoMvQuality.equals("")){
			kuwoMvQuality = "480";
			kuwo_mv_quality.setText(kuwoMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, KUWO, kuwoMvQuality);
		}else {
			kuwo_mv_quality.setText(kuwoMvQuality);
		}

		if (miguMvQuality == null){
			miguMvQuality = "480";
			migu_mv_quality.setText(miguMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, MIGU, miguMvQuality);
		}else if (miguMvQuality.equals("")){
			miguMvQuality = "480";
			migu_mv_quality.setText(miguMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, MIGU, miguMvQuality);
		}else {
			migu_mv_quality.setText(miguMvQuality);
		}

		if (baiduMvQuality == null){
			baiduMvQuality = "480";
			baidu_mv_quality.setText(baiduMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, BAIDU, baiduMvQuality);
		}else if (baiduMvQuality.equals("")){
			baiduMvQuality = "480";
			baidu_mv_quality.setText(baiduMvQuality);
			rw.writeValue(super_setting.this, MV_CONFIG, BAIDU, baiduMvQuality);
		}else {
			baidu_mv_quality.setText(baiduMvQuality);
		}

		netease_mv_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"240", "480", "720", "1080"};
				int checkedIndex = 0;
				if (neteaseMvQuality == null){
					neteaseMvQuality = "480";
					netease_mv_quality.setText(neteaseMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, NETEASE, neteaseMvQuality);
				}else if (neteaseMvQuality.equals("")){
					neteaseMvQuality = "480";
					netease_mv_quality.setText(neteaseMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, NETEASE, neteaseMvQuality);
				}
				switch (neteaseMvQuality){
					case "240":
						checkedIndex = 0;
						break;
					case "480":
						checkedIndex = 1;
						break;
					case "720":
						checkedIndex = 2;
						break;
					case "1080":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MV_CONFIG, NETEASE, items[which]);
								netease_mv_quality.setText(items[which]);
								neteaseMvQuality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		tencent_mv_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"240", "480", "720", "1080"};
				int checkedIndex = 0;
				if (tencentMvQuality == null){
					tencentMvQuality = "480";
					tencent_mv_quality.setText(tencentMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, TENCENT, tencentMvQuality);
				}else if (tencentMvQuality.equals("")){
					tencentMvQuality = "480";
					tencent_mv_quality.setText(tencentMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, TENCENT, tencentMvQuality);
				}
				switch (tencentMvQuality){
					case "240":
						checkedIndex = 0;
						break;
					case "480":
						checkedIndex = 1;
						break;
					case "720":
						checkedIndex = 2;
						break;
					case "1080":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MV_CONFIG, TENCENT, items[which]);
								tencent_mv_quality.setText(items[which]);
								tencentMvQuality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		kugou_mv_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"240", "480", "720", "1080"};
				int checkedIndex = 0;
				if (kugouMvQuality == null){
					kugouMvQuality = "480";
					kugou_mv_quality.setText(kugouMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, KUGOU, kugouMvQuality);
				}else if (kugouMvQuality.equals("")){
					kugouMvQuality = "480";
					kugou_mv_quality.setText(kugouMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, KUGOU, kugouMvQuality);
				}
				switch (kugouMvQuality){
					case "240":
						checkedIndex = 0;
						break;
					case "480":
						checkedIndex = 1;
						break;
					case "720":
						checkedIndex = 2;
						break;
					case "1080":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MV_CONFIG, KUGOU, items[which]);
								kugou_mv_quality.setText(items[which]);
								kugouMvQuality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		kuwo_mv_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"240", "480", "720", "1080"};
				int checkedIndex = 0;
				if (kuwoMvQuality == null){
					kuwoMvQuality = "480";
					kuwo_mv_quality.setText(kuwoMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, KUWO, kuwoMvQuality);
				}else if (kuwoMvQuality.equals("")){
					kuwoMvQuality = "480";
					kuwo_mv_quality.setText(kuwoMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, KUWO, kuwoMvQuality);
				}
				switch (kuwoMvQuality){
					case "240":
						checkedIndex = 0;
						break;
					case "480":
						checkedIndex = 1;
						break;
					case "720":
						checkedIndex = 2;
						break;
					case "1080":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MV_CONFIG, KUWO, items[which]);
								kuwo_mv_quality.setText(items[which]);
								kuwoMvQuality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		migu_mv_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"480", "720", "1080"};
				int checkedIndex = 0;
				if (miguMvQuality == null){
					miguMvQuality = "480";
					migu_mv_quality.setText(miguMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, MIGU, miguMvQuality);
				}else if (miguMvQuality.equals("")){
					miguMvQuality = "480";
					migu_mv_quality.setText(miguMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, MIGU, miguMvQuality);
				}
				switch (miguMvQuality){
					case "480":
						checkedIndex = 0;
						break;
					case "720":
						checkedIndex = 1;
						break;
					case "1080":
						checkedIndex = 2;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MV_CONFIG, MIGU, items[which]);
								migu_mv_quality.setText(items[which]);
								miguMvQuality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		baidu_mv_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[]{"240", "480", "720", "1080"};
				int checkedIndex = 0;
				if (baiduMvQuality == null){
					baiduMvQuality = "480";
					baidu_mv_quality.setText(baiduMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, BAIDU, baiduMvQuality);
				}else if (baiduMvQuality.equals("")){
					baiduMvQuality = "480";
					baidu_mv_quality.setText(baiduMvQuality);
					rw.writeValue(super_setting.this, MV_CONFIG, BAIDU, baiduMvQuality);
				}
				switch (baiduMvQuality){
					case "240":
						checkedIndex = 0;
						break;
					case "480":
						checkedIndex = 1;
						break;
					case "720":
						checkedIndex = 2;
						break;
					case "1080":
						checkedIndex = 3;
						break;
				}

				new QMUIDialog.CheckableDialogBuilder(super_setting.this)
						.setCheckedIndex(checkedIndex)
						.addItems(items, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
								rw.writeValue(super_setting.this, MV_CONFIG, BAIDU, items[which]);
								baidu_mv_quality.setText(items[which]);
								baiduMvQuality = items[which];
								dialog.dismiss();
								FToastUtils.init().setRoundRadius(30).show("修改成功！");
							}
						})
						.create(mCurrentDialogStyle).show();
			}
		});

		ImageButton back = (ImageButton) findViewById(R.id.imageButton1);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                /*Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);*/

				Intent intent = new Intent(super_setting.this, MainActivity.class);
				startActivity(intent);
				AtyTransitionUtil.exitToRight(super_setting.this);


			}
		});

	}

	private void set_player_mode() {
		SpUtil spUtil = new SpUtil();
		final String[] items = new String[]{"每次询问", "仅允许前台播放", "允许后台播放"};
		int checkedIndex = 0;
		String player_play_mode_type = spUtil.getValue(super_setting.this, PLAYER_SETTING, PLAYER_PLAY_MODE);
		if (player_play_mode_type == null){
			player_play_mode_type = "";
		}else if (player_play_mode_type.equals("")){
			player_play_mode_type = "";
		}
		switch (player_play_mode_type){
			case "":
				checkedIndex = 0;
				break;
			case FORGROUND:
				checkedIndex = 1;
				break;
			case BACKGROUND:
				checkedIndex = 2;
				break;
		}

		new QMUIDialog.CheckableDialogBuilder(super_setting.this)
				.setCheckedIndex(checkedIndex)
				.addItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
						switch (which){
							case 0:
								spUtil.writeValue(super_setting.this, PLAYER_SETTING, PLAYER_PLAY_MODE, "");
								play_mode.setText(items[which]);
								break;
							case 1:
								spUtil.writeValue(super_setting.this, PLAYER_SETTING, PLAYER_PLAY_MODE, FORGROUND);
								play_mode.setText(items[which]);
								break;
							case 2:
								spUtil.writeValue(super_setting.this, PLAYER_SETTING, PLAYER_PLAY_MODE, BACKGROUND);
								play_mode.setText(items[which]);
								break;
						}
						dialog.dismiss();
						FToastUtils.init().setRoundRadius(30).show("修改成功！");
					}
				})
				.create(mCurrentDialogStyle).show();
	}

	private void showTestUpateDialog() {
		final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(super_setting.this);
		builder.setTitle("请输入测试JSON")
				.setPlaceholder("在此输入您的测试JSON")
				.setInputType(InputType.TYPE_CLASS_TEXT)
				.addAction("取消", new QMUIDialogAction.ActionListener() {
					@Override
					public void onClick(QMUIDialog dialog, int index) {
						dialog.dismiss();
					}
				})
				.addAction("确定", new QMUIDialogAction.ActionListener() {
					@Override
					public void onClick(QMUIDialog dialog, int index) {
						CharSequence text = builder.getEditText().getText();
						if (text != null && text.length() > 0 && text.toString().endsWith(".json")){
							new UpdateAppManager
									.Builder()
									//当前Activity
									.setActivity(super_setting.this)
									//更新地址
									.setUpdateUrl(text.toString())
									//实现httpManager接口的对象
									.setHttpManager(new UpdateAppHttpUtil())
									.build()
									.update();
						}else {
							FToastUtils.init().setRoundRadius(30).show("JSON格式不支持");
						}
						/*if (text != null && text.length() > 0) {
							//Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							Intent intent = new Intent(super_setting.this, X5WebGameActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("key", text.toString());
							intent.putExtras(bundle);
							startActivity(intent);
						} else {
							//Toast.makeText(getActivity(), "请填入昵称", Toast.LENGTH_SHORT).show();
							FToastUtils.init().setRoundRadius(30).show("未输入测试URL");
						}*/
					}
				})
				.create(mCurrentDialogStyle).show();
	}

	private void showEditTextDialog() {
		final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(super_setting.this);
		builder.setTitle("请输入测试URL")
				.setPlaceholder("在此输入您的测试URL")
				.setInputType(InputType.TYPE_CLASS_TEXT)
				.addAction("取消", new QMUIDialogAction.ActionListener() {
					@Override
					public void onClick(QMUIDialog dialog, int index) {
						dialog.dismiss();
					}
				})
				.addAction("确定", new QMUIDialogAction.ActionListener() {
					@Override
					public void onClick(QMUIDialog dialog, int index) {
						CharSequence text = builder.getEditText().getText();
						if (text != null && text.length() > 0) {
							//Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							Intent intent = new Intent(super_setting.this, X5WebGameActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("key", text.toString());
							intent.putExtras(bundle);
							startActivity(intent);
						} else {
							//Toast.makeText(getActivity(), "请填入昵称", Toast.LENGTH_SHORT).show();
							FToastUtils.init().setRoundRadius(30).show("未输入测试URL");
						}
					}
				})
				.create(mCurrentDialogStyle).show();
	}

	private void clean_file(String file_type) {
		// 获取SD卡路径
		String path = Environment.getExternalStorageDirectory()
				+ getString(R.string.files_path) + getString(R.string.logs_path) + "/" + file_type;
		if (file_type.equals("logs")) {

			Data_editer.deleteDirectory(path);
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs");

		} else if (file_type.equals("crash")) {

			Data_editer.deleteDirectory(path);
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash");

		} else if (file_type.equals("dump")) {

			Data_editer.deleteDirectory(path);
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump");

		} else if (file_type.equals("download")) {

			Data_editer.deleteDirectory(path);
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download");

		} else if (file_type.equals("cache")) {

			Data_editer.deleteDirectory(path);
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
			FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache");

		}

	}

	/**
	 * 初始化x5内核并加载
	 */
	private void initX5() {

		//非wifi网络下是否允许下载内核，默认是false
		QbSdk.setDownloadWithoutWifi(true);

		progressBar = (ProgressBar) findViewById(R.id.x5_progressBar);
		textview = (TextView) findViewById(R.id.x5_status_text);
		core_name = (TextView) findViewById(R.id.core_name);

		//QbSdk.isTbsCoreInited()用来判断x5内核是否已经加载了
		if (QbSdk.isTbsCoreInited()) {
			//如果已经加载
			Log.d(TAG, "QbSdk.isTbsCoreInited: true 已经加载x5内核");
			progressBar.setVisibility(View.INVISIBLE);
			textview.setText("x5内核初始化成功");
			core_name.setText("X5 Core");
			//initListener();
		} else {
			//还没加载,就要初始化内核并加载
			Log.d(TAG, "QbSdk.isTbsCoreInited: false 还没加载x5内核");
			//初始化x5内核
			QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {

				@Override
				public void onCoreInitFinished() {

				}

				@Override
				public void onViewInitFinished(boolean b) {
					progressBar.setVisibility(View.INVISIBLE);
					Log.d(TAG, "onViewInitFinished: x5内核初始化:" + b);
					if (b == true) {
						if (QbSdk.isTbsCoreInited()) {
							textview.setText("x5内核初始化成功");
							core_name.setText("X5 Core");
						}else{
							textview.setText("x5内核初始化失败");
							core_name.setText("Sys Core");
						}
						//initListener();
					} else {
						textview.setText("x5内核初始化失败");
						core_name.setText("Sys Core");
					}
				}
			});
		}
	}

	private void init_file_path() {

		FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path));
		FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/logs");
		FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/crash");
		FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/dump");
		FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/download");
		FileUtil.getInstance().createDir(Environment.getExternalStorageDirectory().getPath() + getString(R.string.files_path) + getString(R.string.logs_path) + "/cache");

	}

	/**
	 * 动态的设置状态栏  实现沉浸式状态栏
	 */
	private void initState() {

		//当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			//
			LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
			linear_bar.setVisibility(View.VISIBLE);
			//获取到状态栏的高度
			int statusHeight = getStatusBarHeight();
			//动态的设置隐藏布局的高度
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
			params.height = statusHeight;
			linear_bar.setLayoutParams(params);
		}
	}

	/**
	 * 通过反射的方式获取状态栏高度
	 *
	 * @return
	 */
	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected void onDestroy() {
		// 退出时弹出stack
		//LogUtil.i("Stack弹出AppUpdateActivity");
		ActivtyStack.getScreenManager().popActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		//ActivtyStack.getScreenManager().popActivity(this);
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(super_setting.this, MainActivity.class);
			startActivity(intent);
			AtyTransitionUtil.exitToRight(super_setting.this);

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
