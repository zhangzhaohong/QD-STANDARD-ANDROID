package com.autumn.framework.entertainment.OkHttpDownloader;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.autumn.framework.entertainment.OkHttpDownloader.dialog.DownDialog;

public class DownManager implements DownListener, DownDialog.DownDialogListener {

    Context context;
    DownDialog downDialog;
    DownUtil downUtil;

    public DownManager(Context context) {
        this.context = context;
        downDialog = new DownDialog(context);
        downDialog.setOnDialogClickListener(this);
        downUtil = new DownUtil(this);
    }

    public static final int DOWN_START = 0;
    public static final int DOWN_PROGRESS = DOWN_START + 1;
    public static final int DOWN_SUCCESS = DOWN_PROGRESS + 1;
    public static final int DOWN_FAILED = DOWN_SUCCESS + 1;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWN_START:
                    downDialog.updateView(0, "准备下载", 0);
                    break;
                case DOWN_PROGRESS:
                    int progress = msg.arg1;
                    long speed = (long) msg.obj;
                    downDialog.updateView(progress, "下载中", speed);
                    break;
                case DOWN_SUCCESS:
                    downDialog.updateView(100, "下载完成", 0);
                    downDialog.dissmiss();
                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_FAILED:
                    downDialog.updateView(0, "下载异常", 0);
                    break;
            }
        }
    };

    /***
     * 开始下载
     * @param downUrl
     * 下载地址
     * @param downPath
     * 保存的地址
     * @param desc
     * dialog显示的描述
     */
    String downUrl;
    String downPath;

    public void downSatrt(String downUrl, String downPath, String desc) {
        this.downUrl = downUrl;
        this.downPath = downPath;
        downDialog.show(desc);
    }

    @Override
    public void downStart() {
        handler.sendEmptyMessage(DOWN_START);
    }

    @Override
    public void downProgress(int progress, long speed) {
        Message msg = new Message();
        msg.what = DOWN_PROGRESS;
        msg.arg1 = progress;
        msg.obj = speed;
        handler.sendMessage(msg);
    }

    @Override
    public void downSuccess(String downUrl) {
        handler.sendEmptyMessage(DOWN_SUCCESS);

    }

    @Override
    public void downFailed(String failedDesc) {
        handler.sendEmptyMessage(DOWN_FAILED);
    }

    @Override
    public void noSure() {
        downUtil.cacleDown();
    }

    @Override
    public void sure() {
        downUtil.downFile(downUrl, downPath);
    }
}
