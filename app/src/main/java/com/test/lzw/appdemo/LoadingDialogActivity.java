package com.test.lzw.appdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by gionee on 2016/11/4.
 */
public class LoadingDialogActivity extends Activity {

    private WebView webView;
    private long exitTime = 0;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

                LoadingDialogActivity.this.finish();
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
            }
        }, 8000);
    }
}
