package com.example.administrator.welcomedemo.database;
public class User {
    private String userName;                  //用户名
    private String userPwd;                   //用户密码
    private int userId;                       //用户ID号
    public int pwdresetFlag=0;
    public String getUserName() {             //获取用户名
        return userName;
    }
    public void setUserName(String userName) {  //输入用户名
        this.userName = userName;
    }
    public String getUserPwd() {                //获取用户密码
        return userPwd;
    }
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
    public int getUserId() {                   //获取用户ID号
        return userId;
    }
    public void setUserId(int userId) {       //设置用户ID号
        this.userId = userId;
    }

    public User(String userName, String userPwd) {  //这里只采用用户名和密码
        super();
        this.userName = userName;
        this.userPwd = userPwd;
    }

}
