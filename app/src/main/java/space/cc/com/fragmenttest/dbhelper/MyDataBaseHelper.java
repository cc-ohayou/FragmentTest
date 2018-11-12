package space.cc.com.fragmenttest.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyDataBaseHelper";
    /**
     * real  对应double类型  浮点类型
     * text 字符
     */
    public static final String CREATE_SQL="create table Book ("
            +"id integer primary key autoincrement, "
            +" author text,"
            +" price real,"
            +" pages integer,"
            +" name text)";

    public static final String CREATE_CATEGORY="create table Category ("
            +"id integer primary key autoincrement, "
            +" category_name text,"
            +" category_code integer)";


    private Context  myContext;

    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        myContext=context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
         db.execSQL(CREATE_SQL);
         db.execSQL(CREATE_CATEGORY);
        Toast.makeText(myContext," create sql succ",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DB onUpgrade: exec");
           db.execSQL("drop table if exists Book");
           db.execSQL("drop table if exists Category");
           onCreate(db);
    }
}
