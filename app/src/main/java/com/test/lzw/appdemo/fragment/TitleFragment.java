package com.test.lzw.appdemo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.test.lzw.appdemo.PersonActivity;
import com.test.lzw.appdemo.R;

/**
 * Created by gionee on 2016/11/16.
 */
public class TitleFragment extends Fragment {

    private ImageButton mLeftMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_tittle,container,false);
        mLeftMenu = (ImageButton)view.findViewById(R.id.id_title_left_btn);
        mLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
