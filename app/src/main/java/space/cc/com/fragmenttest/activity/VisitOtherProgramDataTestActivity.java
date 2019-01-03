package space.cc.com.fragmenttest.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import space.cc.com.fragmenttest.R;

public class VisitOtherProgramDataTestActivity extends AppCompatActivity {


    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_other_program_data_test);
        ListView contactView = findViewById(R.id.contact_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        contactView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
        }
    }

    private void readContacts() {
        Cursor cursor = null;
        try{
             cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            while(cursor.moveToNext()){
                //联系人姓名
                String  displayName=cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //手机号
                String number=cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(displayName+"\n"+number);
            }
            adapter.notifyDataSetChanged();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

           switch(requestCode){
               case 1:
                   if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                       readContacts();
                   }else{
                       Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                   }
               break;
                   default:

        }
    }

    private void addDataToOtherProgramDemo() {
        ContentValues values = new ContentValues();
        values.put("column1", "text");
        values.put("column2", 1);
        Uri uri = Uri.parse("content://com.example.app.provider/table1");
        getContentResolver().insert(uri, values);
    }

    private void updateDataToOtherProgramDemo() {
        ContentValues values = new ContentValues();
        values.put("column1", "updateText");
        Uri uri = Uri.parse("content://com.example.app.provider/table1");
        getContentResolver().update(uri, values, "column1=? and column2=?",
                new String[]{"text", "1"
                });
        getContentResolver().delete(uri, "coloumn2=?", new String[]{"1"});
    }

    private void queryOtherProgramDataDemo() {
        //uri指向某个程序的具体表的路径  对应sql的from table1
        Uri uri = Uri.parse("content://com.example.app.provider/table1");
        // 对应sql中的 select  param1,param2 指定查询的列名
        String[] projection = {};
        // 对应sql中的 where  column=value
        String selection = "";
        //对应where占位符的具体值
        String[] selectionArgs = {"", ""};
        //对应sql中的  order  by  column1 指定查询结果的排序方式
        String sortOrder = "";
        //查询的返回结果是Cursor对象
        Cursor cursor = getContentResolver().query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String column1 = cursor.getString(cursor.getColumnIndex("column1"));
                int column2 = cursor.getInt(cursor.getColumnIndex("column2"));

            }
            cursor.close();
        }
    }


}
