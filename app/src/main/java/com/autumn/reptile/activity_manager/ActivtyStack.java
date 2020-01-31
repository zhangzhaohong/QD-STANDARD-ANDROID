package com.autumn.reptile.activity_manager;

import android.app.Activity;

import java.util.Stack;

public class ActivtyStack {
    private static Stack<Activity> mActivityStack = new Stack<Activity>();
    private static ActivtyStack instance = new ActivtyStack();

    private ActivtyStack() {
    }

    public static ActivtyStack getScreenManager() {
        return instance;
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            mActivityStack.remove(activity);
            activity = null;
        }
    }

    // 将当前Activity推入栈中
    public void pushActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    // 退出栈中所有Activity
    public void popAllActivityExceptOne() {
        while (mActivityStack.size() > 0) {
            Activity activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }

}