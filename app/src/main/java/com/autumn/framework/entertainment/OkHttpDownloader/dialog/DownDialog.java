package com.autumn.framework.entertainment.OkHttpDownloader.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.autumn.framework.entertainment.OkHttpDownloader.view.RopeProgressBar;
import com.autumn.reptile.R;

public class DownDialog {
    private Dialog dialog;
    DownDialogListener dialogClick;
    public RopeProgressBar view_progress;
    public TextView tv_title;
    public TextView tv_speed;
    Context context;
    private TextView tv_desc_update;

    /***
     * 刷新dialog
     *
     * @param progress
     *            进度
     * @param desc
     *            下载秒速
     * @param speed
     *            下载速度
     */
    public void updateView(int progress, String desc, long speed) {
        tv_speed.setVisibility(View.VISIBLE);
        view_progress.setProgress(progress);
        tv_title.setTextColor(0xff1fbaf3);
        tv_title.setText("文件下载(" + desc + ")");
        tv_speed.setText(speed + " kb/s");
        if (desc.contains("异常")) {
            tv_speed.setVisibility(View.GONE);
            tv_title.setTextColor(Color.RED);
            tv_title.setText("下载异常，请重新启动程序");
        }
    }

    public DownDialog(final Context context) {
        this.context = context;
        if (dialog == null) {
            dialog = new Dialog(context, R.style.MyDialog);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = 800;
        int height = 1280;
        LayoutParams params = new LayoutParams(width, height);
        View dialog_view = View.inflate(context, R.layout.dialog_down, null);
        dialog.setContentView(dialog_view, params);

        dialog.setCancelable(false); // true点击屏幕以外关闭dialog
        tv_title = (TextView) dialog_view.findViewById(R.id.dialog_title);
        tv_desc_update = (TextView) dialog_view.findViewById(R.id.tv_desc_update);
        tv_speed = (TextView) dialog_view.findViewById(R.id.tv_speed);
        btn_no = (Button) dialog_view.findViewById(R.id.btn_dialog_no);
        btn_dialog_yes = (Button) dialog_view.findViewById(R.id.btn_dialog_yes);

        view_progress = (RopeProgressBar) dialog_view.findViewById(R.id.update_progress);
        btn_dialog_yes.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                view_progress.setVisibility(View.VISIBLE);
                tv_desc_update.setVisibility(View.INVISIBLE);
                btn_dialog_yes.setVisibility(View.GONE);
                dialogClick.sure();
            }
        });

        btn_no.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.noSure();
                    dissmiss();
                }
            }
        });

        btn_no.setBackgroundColor(0xffB3B3B3);
        btn_dialog_yes.setBackgroundColor(0xffB3B3B3);
        btn_no.setOnFocusChangeListener(onFocusChangeListener);
        btn_dialog_yes.setOnFocusChangeListener(onFocusChangeListener);
    }

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundColor(Color.BLUE);
            } else {
                v.setBackgroundColor(0xffB3B3B3);
            }
        }
    };


    Button btn_no;
    final Button btn_dialog_yes;

    public void show(String desc) {
        view_progress.setVisibility(View.INVISIBLE);
        tv_desc_update.setVisibility(View.VISIBLE);
        if (desc != null && desc.length() > 4) {
            tv_desc_update.setText(desc);
        }
        tv_speed.setText("0kb/s");
        dialog.show();
    }

    public void dissmiss() {
        if (dialog == null) {
            return;
        }
        btn_dialog_yes.setVisibility(View.VISIBLE);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setOnDialogClickListener(DownDialogListener dc) {
        dialogClick = dc;
    }

    public interface DownDialogListener {
        void noSure();

        void sure();
    }

}
