package com.test.lzw.appdemo.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.test.lzw.appdemo.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gionee on 2016/11/9.
 */
public class AccountService {

    public static boolean doLogin(String username, String password){
        String strFlag = "";
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<String, String>();
        map.put("un", username);
        map.put("pw", password);

        String url = HttpUtil.BASE_URL2; //POST方式
        Log.d("url", url);
        Log.d("username", username);
        Log.d("password", password);
        try {
            // 发送请求
            strFlag = HttpUtil.postRequest(url, map);  //POST方式
//          strFlag = HttpUtil.getRequest(url);  //GET方式
            //strFlag = "true";
            Log.d("服务器返回值", strFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(strFlag.trim().equals("true")){
            return true;
        }else{
            return false;
        }
    }

    public static boolean doRegister(String username, String password){
        String strFlag = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("un", username);
        map.put("pw", password);
        try {
            // 发送请求，POST方式提交注册信息
            strFlag = HttpUtil.postRequest(HttpUtil.REGISTER_URL, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(strFlag.trim().equals("true")){
            return true;
        }else{
            return false;
        }
    }
}
