package com.autumn.framework.X5WebView.mvp.mvp_use;

import com.autumn.framework.X5WebView.model.MVPLoginBean;
import com.autumn.framework.X5WebView.mvp.mvpbase.BasePresenter;

/**
 * Created by tzw on 2018/3/13.
 */

public interface MVPLoginPresenter extends BasePresenter<MVPLoginView>{
    void login(MVPLoginBean bean);
}
