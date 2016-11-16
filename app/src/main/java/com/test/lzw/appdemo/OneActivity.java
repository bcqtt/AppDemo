package com.test.lzw.appdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.lzw.appdemo.adapter.MyPagerAdapter;
import com.test.lzw.appdemo.utils.MyDbOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gionee on 2016/11/11.
 */
public class OneActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private ViewPager vpaperOne;
    private ArrayList<View> vList;
    private MyPagerAdapter mAdapter;

    private LinearLayout mGallery;
    private String[] mTags;
    private List<String> names = new ArrayList<>();
    private LayoutInflater mInflater;

    private ScrollView scrollView;
    private GridLayout gridLayout;
    private SwipeRefreshLayout swipeLayout;

    private MyDbOpenHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        //setTitle("ViewPager练习");

        vpaperOne =(ViewPager)findViewById(R.id.vpager_one);
        mInflater = LayoutInflater.from(this);
        vList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        swipeLayout = (SwipeRefreshLayout) li.inflate(R.layout.view_one,null,false);
        scrollView = (ScrollView) swipeLayout.findViewById(R.id.grid_scroll_view);
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (swipeLayout != null) {
                        swipeLayout.setEnabled(scrollView.getScrollY() == 0);
                    }
                }
            });
        }

        mGallery = (LinearLayout) swipeLayout.findViewById(R.id.id_gallery);
        vList.add(swipeLayout);
        vList.add(li.inflate(R.layout.view_two,null,false));
        vList.add(li.inflate(R.layout.view_three,null,false));
        mAdapter = new MyPagerAdapter(vList);
        vpaperOne.setAdapter(mAdapter);

        initData();
        initView();

        //下拉刷新
        //swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //读取sqlite的数据
                new Thread(reflashThread).start();
            }
        });
    }

    Handler reflashHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("names-size", String.valueOf(names.size()));
            if(msg.arg1==2){
                //更新界面
                gridLayout.removeAllViews();
                for(int i = names.size();i>=1;i--){
                    View gridView = mInflater.inflate(R.layout.view_grid_item, gridLayout, false);
                    ImageView imgV = (ImageView)gridView.findViewById(R.id.dog1);
                    imgV.setImageResource(R.drawable.dog);
                    TextView tittle = (TextView) gridView.findViewById(R.id.dog_tittle);
                    tittle.setText(names.get(i-1));
                    imgV.setOnClickListener(onClickListener);
                    gridLayout.addView(gridView);
                    Log.d("Refalsh-Do",names.get(i-1));
                }
                names.clear();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                swipeLayout.setRefreshing(false);
            }
        }
    };

    Runnable reflashThread = new Runnable() {
        @Override
        public void run() {
            Message msg = reflashHandler.obtainMessage();

            myDBHelper = new MyDbOpenHelper(OneActivity.this, "my.db", null, 1);
            SQLiteDatabase db = myDBHelper.getReadableDatabase();
            Cursor cursor =  db.rawQuery("SELECT * FROM person;", null);
            if( cursor != null && cursor.moveToFirst() ){
                cursor.moveToFirst();
                do{
                    int personid = cursor.getInt(cursor.getColumnIndex("personid"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    names.add(name);
                    Log.d("Person-Info", personid + " : " + name);
                }while (cursor.moveToNext());
                cursor.close();
                db.close();

                //读取数据库等耗时操作
                msg.arg1=2;
                reflashHandler.sendMessage(msg);
                //把线程从线程队列中移除
                reflashHandler.removeCallbacks(reflashThread);
            }else{
                swipeLayout.setRefreshing(false);
            }
        }
    };

    private void initData() {
        mTags = new String[] { "删除Person","林志玲","Angelababy","黄晓明","猪八戒","孙悟空","天马龙","扬子","海贼王","路飞","Shanji","程序猿","购物中心" };
    }

    private void initView() {
        //mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        myDBHelper = new MyDbOpenHelper(OneActivity.this, "my.db", null, 1);
        db = myDBHelper.getWritableDatabase();
        for (int i = 0; i < mTags.length; i++) {
            db.execSQL("INSERT INTO person(name) values(?)", new String[]{mTags[i]});
            Log.d("SQL-Execute",mTags[i]);

            //填充横向滚动view
            View view = mInflater.inflate(R.layout.activity_tag_item, mGallery, false);
            TextView tag = (TextView) view.findViewById(R.id.id_tag_item_text);
            tag.setText(mTags[i]);
            mGallery.addView(view);

            //填充GridView
            gridLayout = (GridLayout) swipeLayout.findViewById(R.id.my_grid_layout);
            View gridView = mInflater.inflate(R.layout.view_grid_item, gridLayout, false);
            ImageView imgV = (ImageView)gridView.findViewById(R.id.dog1);
            imgV.setImageResource(R.drawable.dog);
            TextView tittle = (TextView) gridView.findViewById(R.id.dog_tittle);
            tittle.setText(mTags[i]);

            imgV.setClickable(true);
            imgV.setOnClickListener(onClickListener);

            gridLayout.addView(gridView);

        }
    }

    android.view.View.OnClickListener onClickListener =new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(db.isOpen()){
                db.execSQL("delete from person");
                db.close();
            }

            Toast.makeText(OneActivity.this,"点击了图片,清空了Person",Toast.LENGTH_LONG).show();
        }
    };

}
