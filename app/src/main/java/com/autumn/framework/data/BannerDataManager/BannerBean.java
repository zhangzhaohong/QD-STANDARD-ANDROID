package com.autumn.framework.data.BannerDataManager;

import java.util.List;

public class BannerBean {
    /**
     * code : 0
     * data : [{"title":"Google","pic":"https://www.baidu.com/img/bd_logo1.png","url":"https://www.baidu.com"},{"title":"Google","pic":"https://www.baidu.com/img/bd_logo1.png","url":"https://www.baidu.com"},{"title":"Google","pic":"https://www.baidu.com/img/bd_logo1.png","url":"https://www.baidu.com"},{"title":"Google","pic":"https://www.baidu.com/img/bd_logo1.png","url":"https://www.baidu.com"},{"title":"Google","pic":"https://www.baidu.com/img/bd_logo1.png","url":"https://www.baidu.com"},{"title":"Google","pic":"https://www.baidu.com/img/bd_logo1.png","url":"https://www.baidu.com"},{"title":"Google","pic":"https://www.baidu.com/img/bd_logo1.png","url":"https://www.baidu.com"}]
     */

    private String code;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : Google
         * pic : https://www.baidu.com/img/bd_logo1.png
         * url : https://www.baidu.com
         */

        private String title;
        private String pic;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
