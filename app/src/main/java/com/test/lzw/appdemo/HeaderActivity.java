package com.test.lzw.appdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.test.lzw.appdemo.extend.ZoomImageView;


/**
 * Created by gionee on 2016/11/21.
 */
public class HeaderActivity extends Activity {

    private RelativeLayout relativeLayout;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);

        relativeLayout = (RelativeLayout) findViewById(R.id.header_img);
        ZoomImageView imageView = new ZoomImageView(getApplicationContext());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.header);
        imageView.setImageBitmap(bitmap);
        relativeLayout.addView(imageView);
    }
}
