package com.test.lzw.appdemo.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gionee on 2016/11/9.
 */
public class HttpUtil {
    //创建HttpClient对象
    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL = "http://192.168.23.1:8080/article/acc/login/bcqtt/123456";
    public static final String BASE_URL2 = "http://192.168.23.1:8080/article/acc/login2";
    public static final String REGISTER_URL = "http://192.168.23.1:8080/article/acc/register";

    /**
     * @param url 发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String getRequest(String url) throws Exception{
        HttpGet get = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(get);
        if (httpResponse.getStatusLine().getStatusCode() == 200){
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }else{
            Log.d("服务器响应代码",(new Integer(httpResponse.getStatusLine().getStatusCode())).toString());
            return null;
        }
    }

    public static String postRequest(String url, Map<String ,String> rawParams) throws Exception{
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(String key : rawParams.keySet()){
            //封装请求参数
            params.add(new BasicNameValuePair(key , rawParams.get(key)));
            Log.d("params", key + ":" + rawParams.get(key));
        }
        // 设置请求参数
        post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
        // 发送POST请求
        HttpResponse httpResponse = httpClient.execute(post);

        if(httpResponse.getStatusLine().getStatusCode()==200){
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }
        return null;
    }

}
