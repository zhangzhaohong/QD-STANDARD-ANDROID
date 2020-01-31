package com.autumn.reptile.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.autumn.reptile.MyApplication;
import com.autumn.reptile.R;
import com.autumn.reptile.utils.PreferenceManager;
import com.autumn.reptile.utils.Theme;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by lao on 15/9/23.
 */
public class BaseActivity extends AppCompatActivity {

    protected void onPreCreate() {
        final Theme currentTheme = PreferenceManager.getCurrentTheme(this);

        switch (currentTheme) {
            case Blue:
                this.setTheme(R.style.BlueTheme);
                break;
            case Green:
                this.setTheme(R.style.GreenTheme);
                break;
            case Red:
                this.setTheme(R.style.RedTheme);
                break;
            case Indigo:
                this.setTheme(R.style.IndigoTheme);
                break;
            case BlueGrey:
                this.setTheme(R.style.BlueGreyTheme);
                break;
            case Black:
                this.setTheme(R.style.BlackTheme);
                break;
            case Orange:
                this.setTheme(R.style.OrangeTheme);
                break;
            case Purple:
                this.setTheme(R.style.PurpleTheme);
                break;
            case Pink:
                this.setTheme(R.style.PinkTheme);
                break;
            default:
                this.setTheme(R.style.BlueTheme);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onPreCreate();
        super.onCreate(savedInstanceState);
        //activity是否存在
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) { // 当前类不是该Task的根部，那么之前启动
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) { // 当前类是从桌面启动的
                    finish(); // finish掉该类，直接打开该Task中现存的Activity
                    return;
                }
            }
        }
        //内存泄漏
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // -------------------------------------隐藏输入法-----------------------------------------------------
    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
