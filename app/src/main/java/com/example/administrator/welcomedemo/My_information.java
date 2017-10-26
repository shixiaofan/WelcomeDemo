package com.example.administrator.welcomedemo;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.welcomedemo.database.PictureDatabase;
import com.example.administrator.welcomedemo.database.User;
import com.example.administrator.welcomedemo.myview.CircleImageView;

public class My_information extends Activity {
    private CircleImageView info_image;
    private EditText info_name;
    private Button info_ok;
    private PictureDatabase mUserDataManager;
    private SQLiteDatabase mSQLiteDatabase;
    PictureDatabase pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        info_image=(CircleImageView)findViewById(R.id.image_information);
        info_name=(EditText)findViewById(R.id.name_info) ;
        info_ok=(Button)findViewById(R.id.info_ok);
        if (mUserDataManager == null) {
            mUserDataManager = new PictureDatabase(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
        info_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info_names=info_name.getText().toString().trim();
                User user=new User(info_names);
                Long flag=mUserDataManager.insertUserData(user);
                if (flag==1){
                    Toast.makeText(My_information.this,"修改成功。",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(My_information.this,"未成功。",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
  /*      ImageView iv = new ImageView(this);
        if(getDrawable().size() != 0) {
            iv.setImageDrawable(getDrawable().get(0));
        }
        setContentView(iv);
    }

    private ArrayList<Drawable> getDrawable() {
        PictureDatabase pd = new PictureDatabase(this);
        SQLiteDatabase sd = pd.getWritableDatabase();
        ArrayList<Drawable> drawables = new ArrayList<Drawable>();
        //查询数据库
        Cursor c = sd.query("picture", null, null, null, null, null, null);
        //遍历数据
        if(c != null) {
            while(c.moveToNext()) {
                //获取数据
                byte[] b = c.getBlob(c.getColumnIndexOrThrow(PictureDatabase.PictureColumns.PICTURE));
                //将获取的数据转换成drawable
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                Drawable drawable = bitmapDrawable;
                drawables.add(drawable);
                c.close();
            }
        }
        return drawables;}*/
        }




