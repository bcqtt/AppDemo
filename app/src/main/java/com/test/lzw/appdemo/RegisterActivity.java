package com.test.lzw.appdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.test.lzw.appdemo.service.AccountService;

public class RegisterActivity extends AppCompatActivity {

    private Button submitBtn = null;

    private String username;
    private String password;

    private ProgressDialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("注册");

        submitBtn = (Button) findViewById(R.id.btn_register_submit_id);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText)findViewById(R.id.register_name)).getText().toString();
                password = ((EditText)findViewById(R.id.register_password)).getText().toString();
                new Thread(registerThread).start();
            }
        });

    }

    //创建一个handler，内部完成处理消息方法
    Handler registerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==2){
                Intent intent = new Intent();
                //intent.setClass(RegisterActivity.this,MainActivity.class);
                intent.putExtra("username",msg.obj.toString());
                setResult(RESULT_OK,intent);
                RegisterActivity.this.finish();
            }
        }
    };

    Runnable registerThread = new Runnable() {
        @Override
        public void run() {

            Message msg = registerHandler.obtainMessage();
            boolean flag = AccountService.doRegister(username, password);

            if(flag){
                msg.arg1=2;
                msg.obj = username;
                registerHandler.sendMessage(msg);
                registerHandler.removeCallbacks(registerThread);
            }
        }
    };
}
