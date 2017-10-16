package com.example.administrator.welcomedemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.welcomedemo.database.UserDataManager;

public class Login extends Activity {
    private EditText login_name;
    private EditText login_pass;
    private Button login_btn;
    private TextView login_register;
    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;
    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    private UserDataManager mUserDataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login_name = (EditText) findViewById(R.id.login_name);
        login_pass = (EditText) findViewById(R.id.login_pass);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_register=(TextView)findViewById(R.id.login_register) ;
        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        login_register.setOnClickListener(mClick);
        login_btn.setOnClickListener(mClick);
    }

    private View.OnClickListener mClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.login_register:
                    Intent intent_Login_to_Register = new Intent(Login.this,Register.class) ;    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn:
                    login();
                    break;
            }
        }

        public void login() {                                              //登录按钮监听事件
            if (isUserNameAndPwdValid()) {
                String userName = login_name.getText().toString().trim();    //获取当前输入的用户名和密码信息
                String userPwd = login_pass.getText().toString().trim();
                SharedPreferences.Editor editor = login_sp.edit();
                int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
                if (result == 1) {                                             //返回1说明用户名和密码均正确
                    Intent intent = new Intent(Login.this, MainActivity.class);    //切换Login Activity至登录成功页面
                    startActivity(intent);
                    finish();
                } else if (result == 0) {
                    Toast.makeText(Login.this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        public boolean isUserNameAndPwdValid() {
            if (login_name.getText().toString().trim().equals("")) {
                Toast.makeText(Login.this, getString(R.string.account_empty),
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (login_pass.getText().toString().trim().equals("")) {
                Toast.makeText(Login.this, getString(R.string.pwd_empty),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    };
    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }

}
