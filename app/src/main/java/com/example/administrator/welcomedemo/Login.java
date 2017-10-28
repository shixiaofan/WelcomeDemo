package com.example.administrator.welcomedemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.welcomedemo.database.UserDataManager;
import com.example.administrator.welcomedemo.sina.Constants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.text.SimpleDateFormat;

public class Login extends Activity {
    private EditText login_name;
    private EditText login_pass;
    private Button login_btn;
    private ImageView sina_login;
    private TextView login_register;
    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;
    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    private UserDataManager mUserDataManager;
    private static final String TAG = "weibosdk";
    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        WbSdk.install(this,new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));//创建微博API接口类对象
        login_name = (EditText) findViewById(R.id.login_name);
        login_pass = (EditText) findViewById(R.id.login_pass);
        login_btn = (Button) findViewById(R.id.login_btn);
        sina_login=(ImageView)findViewById(R.id.sina_login);
        login_register=(TextView)findViewById(R.id.login_register) ;
        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        login_register.setOnClickListener(mClick);
        login_btn.setOnClickListener(mClick);
        sina_login.setOnClickListener(mClick);
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
                case R.id.sina_login:
                    mSsoHandler.authorize(new SelfWbAuthListener());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

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
    private class SelfWbAuthListener implements WbAuthListener{

        @Override
        public void onSuccess(final Oauth2AccessToken oauth2AccessToken) {
            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken=oauth2AccessToken;
                    if (mAccessToken.isSessionValid()){
                        updateTokenView(false);
                        AccessTokenKeeper.writeAccessToken(Login.this,mAccessToken);
                        Intent intent=new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(Login.this,
                    "取消授权", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            Toast.makeText(Login.this, "授权失败！", Toast.LENGTH_LONG).show();
        }
    }
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = "qqqqqq";
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = "sssssss" + "\n" + message;
        }
        mTokenText.setText(message);
    }

}
