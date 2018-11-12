package space.cc.com.fragmenttest.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.dbhelper.MyDataBaseHelper;

public class DBTestActivity extends AppCompatActivity {
    private static final String TAG = "DBTestActivity";
    private MyDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
//        dbHelper = new MyDataBaseHelper(this,"book.db",null,1);
        //version改为2 表示数据库升级了 会调用 onUpgrade方法
        dbHelper = new MyDataBaseHelper(this,"bookStore.db",
                null,4);
        Button createDbBut=findViewById(R.id.create_db_but);
        createDbBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第一次检测到没有数据库 调用MyDataBaseHelper的onCreate方法
                //表也随即被创建了出来
                dbHelper.getWritableDatabase();
            }
        });

        Button addBut=findViewById(R.id.add_but);
        addBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",16.96);
               long cou=db.insert("Book",null,values);
                Log.d(TAG, "addData: over cou="+cou);
            }
        });

    }
}
