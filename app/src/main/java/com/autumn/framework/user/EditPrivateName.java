package com.autumn.framework.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.autumn.framework.View.ClearEditText;
import com.autumn.reptile.R;
import com.autumn.reptile.activity_manager.ActivtyStack;
import com.autumn.reptile.ui.BaseActivity;
import com.autumn.sdk.data.DataUtil;
import com.autumn.sdk.runtime.user.edit_private_name_runtime;
import com.liyi.sutils.utils.AtyTransitionUtil;

import cn.hotapk.fastandrutils.utils.FToastUtils;

public class EditPrivateName extends BaseActivity {

    public static String private_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (!isTaskRoot()) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_edit_private_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }

        // 每次加入stack
        ActivtyStack.getScreenManager().pushActivity(this);

        final ClearEditText et_content;//定义一个文本输入框
        final TextView tv_num;// 用来显示剩余字数
        final int num = 20;//限制的最大字数

        et_content = (ClearEditText) findViewById(R.id.et_content);
        tv_num = (TextView) findViewById(R.id.tv_num);

        tv_num.setVisibility(View.GONE);
        tv_num.setText("20");

        et_content.setText(private_name);

        et_content.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                System.out.println("s=" + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_num.setVisibility(View.VISIBLE);
                int number = num - s.length();
                tv_num.setText("" + number);
                selectionStart = et_content.getSelectionStart();
                selectionEnd = et_content.getSelectionEnd();
                //System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    et_content.setText(s);
                    et_content.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        Button cancel_edit_private_name = (Button)findViewById(R.id.user_cancel_edit_private_name);
        cancel_edit_private_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditPrivateName.this, UserInfoActivity.class);
                startActivity(intent);

                AtyTransitionUtil.exitToRight(EditPrivateName.this);
            }
        });

        Button edit_private_name = (Button) findViewById(R.id.user_edit_private_name);
        edit_private_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int private_name_disabled_1 = et_content.getText().toString().indexOf("sb");
                //int private_name_disabled_2 = et_content.getText().toString().indexOf("傻逼");
                //int private_name_disabled_3 = et_content.getText().toString().indexOf("操你妈");
                //int private_name_disabled_4 = et_content.getText().toString().indexOf("草泥马");
                //int private_name_disabled_5 = et_content.getText().toString().indexOf("脑残");

                String new_private_name = et_content.getText().toString();

                if (DataUtil.find_words(new_private_name,"sb")|| DataUtil.find_words(new_private_name,"傻逼") || DataUtil.find_words(new_private_name,"操你妈") || DataUtil.find_words(new_private_name,"草泥马") || DataUtil.find_words(new_private_name,"脑残")) {

                    et_content.setText(private_name);
                    et_content.setError("昵称含有非法字符，请重新输入！");
                    //et_content.setText("");
                    //FToastUtils.init().setRoundRadius(30).show("昵称含有非法字符，请重新输入！");

                } else {
                    new Thread(new edit_private_name_runtime(h1,EditPrivateName.this,et_content.getText().toString())).start();
                }
            }

        });
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){

        Intent intent = new Intent(EditPrivateName.this, UserInfoActivity.class);
        startActivity(intent);

        AtyTransitionUtil.exitToRight(EditPrivateName.this);

        return super.onKeyDown(keyCode, event);
    }


    Handler h1=new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO: Implement this method
            switch (msg.what)
            {
                case edit_private_name_runtime.FINDER_IMAGE_1:
                    //inderout1.setTextSize(15);
                    //finderout1.setGravity(Gravity.CENTER);
                    //finderout1.setText(msg.obj.toString());
                    //username = (EditText)view1.findViewById(R.id.register_username);
                    //username.setText(String.valueOf(msg.obj));

                    if (String.valueOf(msg.obj).equals(getString(R.string.edit_success))){
                        FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        Intent intent = new Intent(EditPrivateName.this, UserInfoActivity.class);
                        startActivity(intent);

                        AtyTransitionUtil.exitToRight(EditPrivateName.this);
                    }else{
                        //FToastUtils.init().setRoundRadius(30).show(String.valueOf(msg.obj));
                        ClearEditText et_content = (ClearEditText) findViewById(R.id.et_content);

                        et_content.setError(String.valueOf(msg.obj));

                    }

                    this.removeCallbacksAndMessages(null);
                    break;
            }
        }

    };

    @Override
    protected void onDestroy(){
        // 退出时弹出stack
        //LogUtil.i("Stack弹出AppUpdateActivity");
        ActivtyStack.getScreenManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        //ActivtyStack.getScreenManager().popActivity(this);
        super.onStop();
    }

}
