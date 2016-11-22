package com.test.lzw.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.lzw.appdemo.extend.CircleImageView;

/**
 * Created by gionee on 2016/11/18.
 */
public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView imageView ;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        imageView = (CircleImageView)findViewById(R.id.id_person_header);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_person_header:
                Intent intent = new Intent();
                intent.setClass(PersonActivity.this,HeaderActivity.class);
                startActivity(intent);
                break;
        }

        }
}
