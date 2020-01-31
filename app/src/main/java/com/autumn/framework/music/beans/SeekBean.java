package com.autumn.framework.music.beans;

import com.autumn.framework.music.base.BaseBean;

/**
 * Created by yangw on 2018-3-30.
 */

public class SeekBean extends BaseBean{

    private boolean seekingfinished;
    private boolean showTime;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSeekingfinished() {
        return seekingfinished;
    }

    public void setSeekingfinished(boolean seekingfinished) {
        this.seekingfinished = seekingfinished;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }
}
