package com.example.administrator.welcomedemo.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.welcomedemo.friend_list_item.ExpandableList_friend_item;
import com.example.administrator.welcomedemo.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class Tab01 extends AppCompatActivity implements View.OnClickListener{
    private Button http_request,list;
    private HttpResponse httpResponse;
    private HttpEntity httpEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab01);
        http_request=(Button)findViewById(R.id.http_request);
        list=(Button)findViewById(R.id.list);
        list.setOnClickListener(this);
        http_request.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //记得添加依赖org.apache.http.client
        //添加联网权限
        //生成一个请求对象
        switch (view.getId()) {
            case R.id.http_request:
                httpRequest();
                break;
            case R.id.list:
                Intent intent=new Intent(this, ExpandableList_friend_item.class);
                startActivity(intent);
                break;
        }
    }




    public void httpRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGet httpGet = new HttpGet("http://www.sogou.com");
                InputStream inputStream = null;
                HttpClient httpClient = new DefaultHttpClient();//生成一个http客户端对象

                try {
                    httpResponse = httpClient.execute(httpGet);//客户端发送请求并生成一个HttpResponse的回应
                    httpEntity = httpResponse.getEntity();//获得一个httpEntity数据
                    inputStream = httpEntity.getContent();//将数据存放入l流中

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String result = " ";
                    String line = " ";
                    while ((line = bufferedReader.readLine()) != null) {
                        result = result + line;
                    }
                    System.out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

