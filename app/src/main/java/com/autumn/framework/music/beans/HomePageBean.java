package com.autumn.framework.music.beans;

import com.autumn.framework.music.base.BaseBean;

import java.util.List;

/**
 * Created by ywl on 2017/7/21.
 */

public class HomePageBean extends BaseBean {

    private List<ScrollImgBean> scrollImg;

    public List<ScrollImgBean> getScrollImg() {
        return scrollImg;
    }

    public void setScrollImg(List<ScrollImgBean> scrollImg) {
        this.scrollImg = scrollImg;
    }
}
