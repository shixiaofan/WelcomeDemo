package com.example.administrator.welcomedemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.administrator.welcomedemo.R;

import java.io.ByteArrayOutputStream;
public class UserDataManager {             //用户数据管理类
    private static final String TAG = "UserDataManager";
    private static final String DB_NAME = "user_data";
    private static final String TABLE_NAME = "users";
    public static final String ID = "_id";
    private static final String INFO_NAME="info_name";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_pwd";
    private static final int DB_VERSION = 2;
    private Context mContext=null ;
    public static class PictureColumns implements BaseColumns {
        public static final String INFO_IMAGE = "info_image";
    }

    //创建用户book表
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + USER_NAME + " varchar,"
            + USER_PWD + " varchar," + INFO_NAME+ " varchar,"+PictureColumns.INFO_IMAGE+"BLOB);";
    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;
    //DataBaseManagementHelper继承自SQLiteOpenHelper

    private static class DataBaseManagementHelper extends SQLiteOpenHelper {
    private Context mContect;
        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {//创建数据库
            Log.i(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);
            Log.i(TAG, "db.execSQL(DB_CREATE)");
            Log.e(TAG, DB_CREATE);
            initDataBase(db,mContect);
        }
        //将转换后的图片存入到数据库中
        private void initDataBase (SQLiteDatabase db, Context context) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.one);
            User user=new User(INFO_NAME);
            String info_name=user.getInfo_Name();
            ContentValues cv = new ContentValues();
            cv.put(PictureColumns.INFO_IMAGE, getPicture(drawable));
            cv.put(INFO_NAME,info_name);
            db.insert(TABLE_NAME, null, cv);
        }
        //将drawable转换成可以用来存储的byte[]类型
        private byte[] getPicture(Drawable drawable) {
            if(drawable == null) {
                return null;
            }
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bitmap = bd.getBitmap();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, os);
            return os.toByteArray();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//数据库升级
            Log.i(TAG, "DataBaseManagementHelper onUpgrade");
            onCreate(db);
        }
    }

    public UserDataManager(Context context) {
        mContext = context;
        Log.i(TAG, "UserDataManager construction!");
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }
    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }
    //添加新用户，即注册
    public long insertUserData(User user) {
        String userName= user.getUserName();
        String userPwd= user.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_PWD, userPwd);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }
    //更新用户信息，如修改密码
    public boolean updateUserData(User user) {
        String userName = user.getUserName();
        String userPwd = user.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_PWD, userPwd);
        String where = USER_NAME + "=" + "=\"" + userName + "\"";
        return mSQLiteDatabase.update(TABLE_NAME, values,where, null) > 0;
    }
    //
    public Cursor fetchUserData(int id) throws SQLException {
        Cursor mCursor = mSQLiteDatabase.query(false, TABLE_NAME, null, ID
                + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //
    public Cursor fetchAllUserDatas() {
        return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null,
                null);
    }
    //根据id删除用户
    public boolean deleteUserData(int id) {
        return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
    }
    //根据用户名注销
    public boolean deleteUserDatabyname(String name) {
        return mSQLiteDatabase.delete(TABLE_NAME, USER_NAME+"=?", new String[]{name}) > 0;
    }
    //删除所有用户
    public boolean deleteAllUserDatas() {
        return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    //
    public String getStringByColumnName(String columnName, int id) {
        Cursor mCursor = fetchUserData(id);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }
    //
    public boolean updateUserDataById(String columnName, int id,
                                      String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }
    //根据用户名找用户，可以判断注册时用户名是否已经存在
    public int findUserByName(String userName){
        Log.i(TAG,"findUserByName , userName="+userName);
        int result=0;
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME+"=?", new String[]{userName}, null, null, null);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
            Log.i(TAG,"findUserByName , result="+result);
        }
        return result;
    }

   /* public ArrayList<Drawable> getDrawables() {

        ArrayList<Drawable> drawables = new ArrayList<Drawable>();
        //查询数据库
        Cursor c = mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        //遍历数据
        if(c != null) {

                //获取数据
                byte[] b = c.getBlob(c.getColumnIndexOrThrow(UserDataManager.PictureColumns.INFO_IMAGE));
                //将获取的数据转换成drawable
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                Drawable drawable = bitmapDrawable;
                drawables.add(drawable);
            }

        return drawables;
    }
*/
    //根据用户名和密码找用户，用于登录
    public int findUserByNameAndPwd(String userName, String pwd){
        Log.i(TAG,"findUserByNameAndPwd");
        int result=0;
     Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, USER_NAME+"=?"+" and "+USER_PWD+"=?",
             new String[]{userName,pwd}, null, null, null);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
            Log.i(TAG,"findUserByNameAndPwd , result="+result);
        }
        return result;
    }

}
