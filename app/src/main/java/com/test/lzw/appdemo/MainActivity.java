package com.test.lzw.appdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.lzw.appdemo.service.AccountService;
import com.test.lzw.appdemo.utils.MyDbOpenHelper;

public class MainActivity extends AppCompatActivity {

    public static final String SETTING_INFO = "SETTING_INFOs";
    public static final String NAME = "NAME";
    public static final String PASS = "PASS";
    private static final String ACTION_RECV_MSG = "com.test.lzw.testapp.intent.action.RECEIVE_MESSAGE";

    private MyDbOpenHelper myDBHelper;

    EditText et_username ;
    EditText et_password;
    String username;
    String password;

    CheckBox saveAccount;

    TextView register;


    Button loginBtn = null;

    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        //获取输入框
        et_username = (EditText)findViewById(R.id.userName);
        et_password = (EditText)findViewById(R.id.password);

        SharedPreferences spre = getSharedPreferences(SETTING_INFO,0);
        String name = spre.getString(NAME,"");
        String pass = spre.getString(PASS,"");
        et_username.setText(name);
        et_password.setText(pass);


        //获取按钮
        Button submit = (Button)findViewById(R.id.btn_login);

        //为按钮绑定事件监听器
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                saveAccount = (CheckBox) findViewById(R.id.save_name_pass);
                if(saveAccount.isChecked()){
                    //保存用户名密码到SharedPreference,并在下一次启动时读取并自动登录
                    SharedPreferences sharePre = getSharedPreferences(SETTING_INFO,0);
                    sharePre.edit()
                            .putString(NAME,username)
                            .putString(PASS,password)
                            .commit();

                }

                new Thread(login_thread).start();

                selectData();
            }
        });

        register = (TextView) findViewById(R.id.register_Id);
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"点击了注册",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(mContext,RegisterActivity.class);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("resultCode","resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            String usernameResult = data.getStringExtra("username");
            Log.d("usernameResult","注册返回结果:" + usernameResult);
            et_username.setText(usernameResult);
        }
        Toast.makeText(this,"注册完成",Toast.LENGTH_LONG).show();
    }

    //创建一个handler，内部完成处理消息方法
    Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==1){
                Intent intent = new Intent();
                intent.setClass(mContext,OneActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }
    };

    Runnable login_thread = new Runnable() {

        public void run() {
            //首先获得一个消息结构
            Message msg = loginHandler.obtainMessage();
            //给消息结构的arg1参数赋值
            boolean flag = AccountService.doLogin(username, password);

            //如果登录成功
            if(flag) {
                msg.arg1=1;
                loginHandler.sendMessage(msg);
                //把线程从线程队列中移除
                loginHandler.removeCallbacks(login_thread);
            }
        }
    };


    /**
     * 查询并输出person中的全部数据,然后清空表里的数据
     */
    private void selectData(){
        myDBHelper = new MyDbOpenHelper(mContext, "my.db", null, 1);
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM person;", null);

        if( cursor != null && cursor.moveToFirst() ){
            cursor.moveToFirst();
            do{
                int personid = cursor.getInt(cursor.getColumnIndex("personid"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("Person-Info", personid + " : " + name);
            }while (cursor.moveToNext());
            cursor.close();

            db.close();
        }

    }


}
