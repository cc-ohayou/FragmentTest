package space.cc.com.fragmenttest.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.dbhelper.MyDataBaseHelper;

public class DBTestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DBTestActivity";
    private MyDataBaseHelper dbHelper;
    private List list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
//        dbHelper = new MyDataBaseHelper(this,"book.db",null,1);
        //version改为2 表示数据库升级了 会调用 onUpgrade方法
        dbHelper = new MyDataBaseHelper(this,"bookStore.db",
                null,4);

        setOnclickListenerByRid(R.id.create_db_but);
        setOnclickListenerByRid(R.id.add_but);

    }

    private void setOnclickListenerByRid(int id) {
        Button addBut=findViewById(id);
        addBut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_but:
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                addSingleBookRecord(db,"The Da Vinci Code","Dan Brown",600,16.96);
                addSingleBookRecord(db,"lalala","hahaha",430,16.96);
                addSingleBookRecord(db,"ccccc","cc",546,16.96);

            case R.id.create_db_but:
                //第一次检测到没有数据库 调用MyDataBaseHelper的onCreate方法
                //表也随即被创建了出来
                dbHelper.getWritableDatabase();
            case R.id.update_but:
                SQLiteDatabase db2=dbHelper.getWritableDatabase();
                ContentValues params=new ContentValues();
                params.put("price",10.99);
                //?问号表示占位符 后面的是占位符对应的条件参数  params里面是要更新的字段
                int result=db2.update("Book",params,"name=?",
                        new String[]{"The Da Vinci Code"});
                Log.d(TAG, "updateData: result cou="+result);
            case R.id.del_but:
                SQLiteDatabase db3=dbHelper.getWritableDatabase();
                int cou=db3.delete(MyDataBaseHelper.TABLE_BOOK,"pages>?",new String[]{"500"});
                Log.d(TAG, "delData: result cou="+cou);
            case R.id.query_but:
                SQLiteDatabase queryDb=dbHelper.getWritableDatabase();
                //查询book中所有数据
                Cursor cursor=queryDb.query(MyDataBaseHelper.TABLE_BOOK,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                        );
                do{
                  String name=getDbStringProperty(cursor,"name");
                  String author=getDbStringProperty(cursor,"author");
                  int pages=getDbIntProperty(cursor,"pages");
                  double price=getDbDoubleProperty(cursor,"price");
                  Map<String,Object> book=new HashMap<>();
                  book.put("name",name);
                  book.put("author",author);
                  book.put("pages",pages);
                  book.put("price",price);
                  list.add(book);
                }while(cursor.moveToNext());
                cursor.close();
                Toast.makeText(this,list.toString(),Toast.LENGTH_LONG).show();
            default:
                break;
        }
    }

    private String getDbStringProperty(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }
    private int getDbIntProperty(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }
    private double getDbDoubleProperty(Cursor cursor, String name) {
        return cursor.getDouble(cursor.getColumnIndex(name));
    }

    private void addSingleBookRecord(SQLiteDatabase db,String name,String author,int pages,Double price) {
        ContentValues values=new ContentValues();
        values.put("name","The Da Vinci Code");
        values.put("author","Dan Brown");
        values.put("pages",454);
        values.put("price",16.96);
        long cou=db.insert(MyDataBaseHelper.TABLE_BOOK,null,values);
        Log.d(TAG, "addData: over cou="+cou);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
