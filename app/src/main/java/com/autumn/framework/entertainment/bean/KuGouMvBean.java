package com.autumn.framework.entertainment.bean;

import java.util.List;

public class KuGouMvBean {
    private List<KugouMvBean> KugouMv;

    public List<KugouMvBean> getKugouMv() {
        return KugouMv;
    }

    public void setKugouMv(List<KugouMvBean> KugouMv) {
        this.KugouMv = KugouMv;
    }

    public static class KugouMvBean {
        /**
         * mvhash : Google
         */

        private String mvhash;

        public String getMvhash() {
            return mvhash;
        }

        public void setMvhash(String mvhash) {
            this.mvhash = mvhash;
        }
    }
}
