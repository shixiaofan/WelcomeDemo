package com.example.administrator.welcomedemo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.example.administrator.welcomedemo.tab.Tab01;
import com.example.administrator.welcomedemo.tab.Tab02;
import com.example.administrator.welcomedemo.tab.Tab03;

public class MainActivity extends TabActivity {
    private TabHost tabHost;
    private int[] imgfalse = new int[] { R.drawable.skin_tab_icon_contact_normal,
            R.drawable.skin_tab_icon_msg_normal, R.drawable.skin_tab_icon_qzone_normal};
    private int[] imgtrue = new int[] { R.drawable.skin_tab_icon_contact_selected,
            R.drawable.skin_tab_icon_msg_selected,
            R.drawable.skin_tab_icon_qzone_selected };
//    private String[] textreg = new String[] { "联系人", "信息", "动态" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tabHost=getTabHost();
        addTabSpec(new int[]{0,1,2},null,null,getintent());
        tabHost.setCurrentTab(0);
        changeimg(0);
        // TabWidget改变监听
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int pos = Integer.parseInt(s);
                Log.d("xx", pos + "");
                // 给当前图片文字添加点击效果
                changeimg(pos);
            }
        } );


    }
    protected void changeimg(int pos) {
        TabWidget tabWidget = getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View v = tabWidget.getChildAt(i);
            ImageView img = (ImageView) v.findViewById(R.id.tabimg);
            if (i == pos) {
                         img.setBackgroundResource(imgtrue[i]);
            } else {
                img.setBackgroundResource(imgfalse[i]);
            }

        }
    }
    private void addTabSpec(int[] is, int[] imgfalse2, String[] textreg2,
                            Intent[] getintent) {
        for (int i = 0; i < is.length; i++) {
            View v=View.inflate(MainActivity.this,R.layout.select,null);
                     tabHost.addTab(tabHost.newTabSpec(is[i] + "").setIndicator(v)
                    .setContent(getintent[i]));
        }
    }
    private Intent[] getintent() {
        // TODO Auto-generated method stub
        Intent intent1 = new Intent(MainActivity.this, Tab01.class);
        Intent intent2 = new Intent(MainActivity.this, Tab02.class);
        Intent intent3 = new Intent(MainActivity.this, Tab03.class);
        return new Intent[] { intent1, intent2, intent3 };
    }
}
