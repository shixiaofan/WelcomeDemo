package com.example.administrator.welcomedemo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
/**
 * 通过使用ViewPager实现多个页面滑动切换
 */
public class NavigationActivity extends Activity {
    private ViewPager vPager;
    private List<View> vList; //装载各页面的视图
    private ImageView[] pointImgViews; //装载导航小圆点
    private ViewGroup pointGroup; //与布局文件的小黑点位置ID相关联

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initView();
        setViewPager();
        addPoints();
        setAdapterForViewPager();
    }

    private void initView(){
        vPager = (ViewPager)findViewById(R.id.navigation_vp);
        pointGroup = (ViewGroup)findViewById(R.id.viewPoints);
//        getSupportActionBar().hide();
    }

    //实现基本滑动切换界面
    private void setViewPager(){
        LayoutInflater inflater = getLayoutInflater();
        vList = new ArrayList<View>();
        vList.add(inflater.inflate(R.layout.navigation_page1,null));
        vList.add(inflater.inflate(R.layout.navigation_page2,null));
        vList.add(inflater.inflate(R.layout.navigation_page3,null));
        vList.add(inflater.inflate(R.layout.navigation_page4,null));
        vList.add(inflater.inflate(R.layout.navigation_page5,null));

        PagerAdapter myAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return vList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view==o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(vList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(vList.get(position));
                return vList.get(position);
            }
        };

        vPager.setAdapter(myAdapter);
    }

    private void addPoints(){
        ImageView pointImgView;
        pointImgViews = new ImageView[vList.size()]; //确定小圆点的个数

        //动态添加小圆点
        for(int i=0; i<vList.size(); i++){
            pointImgView = new ImageView(NavigationActivity.this);
            pointImgView.setLayoutParams(new ViewGroup.LayoutParams(25,25)); //设置圆点大小
            pointImgView.setPadding(5, 0, 5, 0);
            pointImgViews[i] = pointImgView;

            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0)
                pointImgViews[i].setImageDrawable(getResources().getDrawable(
                        R.drawable.focus));
            else
                pointImgViews[i].setImageDrawable(getResources().getDrawable(
                        R.drawable.unfocus));
            // 将imageviews添加到小圆点视图组
            pointGroup.addView(pointImgViews[i]);
        }
    }

    //添加监听器，将相应页面的小圆点设置为选中状态
    private void setAdapterForViewPager(){
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pointImgViews.length; i++) {
                    // 当前view下设置小圆点为未选中状态
                    pointImgViews[i].setImageDrawable(getResources().getDrawable(
                            R.drawable.unfocus));
                    //设置小圆点为选中状态
                    if(position == i)
                        pointImgViews[i].setImageDrawable(getResources().getDrawable(
                                R.drawable.focus));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //点击button后调用，跳转到主界面，勿忘设置参数
    public void shipToFrame(View v){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        this.finish();
    }
}
