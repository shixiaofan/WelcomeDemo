package com.example.administrator.welcomedemo;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.administrator.welcomedemo.myview.CircleImageView;
import com.example.administrator.welcomedemo.tab.Tab01;
import com.example.administrator.welcomedemo.tab.Tab02;
import com.example.administrator.welcomedemo.tab.Tab03;

public class MainActivity extends TabActivity implements View.OnClickListener {
    public final static int SMALL_CAPTURE = 0;
    private CircleImageView head_image;
    private TextView head_text;
    private TabHost tabHost;
    private int[] imgfalse = new int[]{R.drawable.skin_tab_icon_contact_normal,
            R.drawable.skin_tab_icon_msg_normal, R.drawable.skin_tab_icon_qzone_normal};
    private int[] imgtrue = new int[]{R.drawable.skin_tab_icon_contact_selected,
            R.drawable.skin_tab_icon_msg_selected,
            R.drawable.skin_tab_icon_qzone_selected};
    private String[] textreg = new String[]{"联系人", "消息", "动态"};
    /* 头像文件 */

    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";


/* 请求识别码 */

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        head_image = (CircleImageView) findViewById(R.id.head_image_button);
        head_text = (TextView) findViewById(R.id.head_text);
        head_image.setOnClickListener(this);
        tabHost = getTabHost();
        addTabSpec(new int[]{0, 1, 2}, null, null, getintent());
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
        });


    }

    protected void changeimg(int pos) {
        TabWidget tabWidget = getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View v = tabWidget.getChildAt(i);

            ImageView img = (ImageView) v.findViewById(R.id.tabimg);
            if (i == pos) {
                head_text.setText(textreg[i]);
                img.setBackgroundResource(imgtrue[i]);
            } else {
                img.setBackgroundResource(imgfalse[i]);
            }

        }
    }

    private void addTabSpec(int[] is, int[] imgfalse2, String[] textreg2,
                            Intent[] getintent) {
        for (int i = 0; i < is.length; i++) {
            View v = View.inflate(MainActivity.this, R.layout.select, null);
            tabHost.addTab(tabHost.newTabSpec(is[i] + "").setIndicator(v)
                    .setContent(getintent[i]));
        }
    }

    private Intent[] getintent() {
        // TODO Auto-generated method stub
        Intent intent1 = new Intent(MainActivity.this, Tab01.class);
        Intent intent2 = new Intent(MainActivity.this, Tab02.class);
        Intent intent3 = new Intent(MainActivity.this, Tab03.class);
        return new Intent[]{intent1, intent2, intent3};
    }

    @Override
    public void onClick(View view) {
        showDialog();


    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.activity_my_dialog, null);//获取自定义布局
        builder.setView(layout);
        builder.setTitle("qqqqqqq");//设置标题内容
        Button see_big_image = (Button) layout.findViewById(R.id.see_big_image);
        Button camara = (Button) layout.findViewById(R.id.canara);
        final AlertDialog dlg = builder.create();

        see_big_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,My_information.class);
                startActivity(intent);
                dlg.dismiss();

            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SMALL_CAPTURE);
                    dlg.dismiss();
                }
            }
        });

        dlg.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SMALL_CAPTURE) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            head_image.setImageBitmap(imageBitmap);
        }
    }

    }




