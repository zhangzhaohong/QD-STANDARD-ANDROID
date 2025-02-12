package com.autumn.framework.entity;

import com.autumn.framework.Log.LogUtil;
import com.autumn.framework.update.data.Update_url_manager;
import com.autumn.sdk.manager.update.update_manager;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Aspsine on 2015/7/8.
 */
public class AppInfo implements Serializable {
    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECT_ERROR = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_INSTALLED = 7;

    private String name;
    private String packageName;
    private String id;
    private String image;
    private String url;
    private int progress;
    private String downloadPerSize;
    private int status;
    private File FilePath;

    public AppInfo() {
    }

    public AppInfo(String id, String name, String image, String url) {
        //this.name = name;
        this.id = id;
        this.image = image;
        this.url = url;
    }

    public String getName() {
        this.name = update_manager.get_software_version();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        if (url == null) {
            if (Update_url_manager.getUrl() == null){
                if (update_manager.get_download_url() == null)
                    this.url = "";
                else if (!update_manager.get_download_url().equals(""))
                    this.url = update_manager.get_download_url();
                else
                    this.url = "";
            }else if (update_manager.get_download_url() == null){
                this.url = "";
            }else if (!Update_url_manager.getUrl().equals("") && !update_manager.get_download_url().equals("") && update_manager.get_download_url().contains("meternity.cn/api/LanZouApi.php?url="))
                this.url = Update_url_manager.getUrl();
            else if (!update_manager.get_download_url().equals(""))
                this.url = update_manager.get_download_url();
            else
                this.url = "";
        }else if (url.equals("")){
            if (Update_url_manager.getUrl() == null){
                if (update_manager.get_download_url() == null)
                    this.url = "";
                else if (!update_manager.get_download_url().equals(""))
                    this.url = update_manager.get_download_url();
                else
                    this.url = "";
            }else if (update_manager.get_download_url() == null){
                this.url = "";
            }else if (!Update_url_manager.getUrl().equals("") && !update_manager.get_download_url().equals("") && update_manager.get_download_url().contains("meternity.cn/api/LanZouApi.php?url="))
                this.url = Update_url_manager.getUrl();
            else if (!update_manager.get_download_url().equals(""))
                this.url = update_manager.get_download_url();
            else
                this.url = "";
        }
        return url;
    }

    public void setUrl(String url) {
        LogUtil.d("SetUrlInfo:" + url);
        this.url = url;
    }

    public String getDownloadPerSize() {
        return downloadPerSize;
    }

    public void setDownloadPerSize(String downloadPerSize) {
        this.downloadPerSize = downloadPerSize;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Not Download";
            case STATUS_CONNECTING:
                return "Connecting";
            case STATUS_CONNECT_ERROR:
                return "Connect Error";
            case STATUS_DOWNLOADING:
                return "Downloading";
            case STATUS_PAUSED:
                return "Pause";
            case STATUS_DOWNLOAD_ERROR:
                return "Download Error";
            case STATUS_COMPLETE:
                return "Complete";
            case STATUS_INSTALLED:
                return "Installed";
            default:
                return "Not Download";
        }
    }

    public String getButtonText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Download";
            case STATUS_CONNECTING:
                return "Cancel";
            case STATUS_CONNECT_ERROR:
                return "Try Again";
            case STATUS_DOWNLOADING:
                return "Pause";
            case STATUS_PAUSED:
                return "Resume";
            case STATUS_DOWNLOAD_ERROR:
                return "Try Again";
            case STATUS_COMPLETE:
                return "Install";
            case STATUS_INSTALLED:
                return "UnInstall";
            default:
                return "Download";
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public File getFilePath() {
        return FilePath;
    }
}
