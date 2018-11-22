package space.cc.com.fragmenttest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

public class MyContentProvider extends ContentProvider {
    private static final String TAG = "MyContentProvider";
    //dir对应 根据条件查询某表中复合条件的所有数据
    //item根据条件获取表中单条数据
    public  static final int BOOK_DIR=0;
    public  static final int BOOK_ITEM=1;
    public  static final int CATEGORY_DIR=2;
    public  static final int CATEGORY_ITEM=3;
    public  static final String TABLE_BOOK="Book";
    public  static final String TABLE_CATEGORY="Category";

    public  static final String AUTHORITY="cc.com.databasetest.provider";
    private static UriMatcher uriMatcher;
    static{
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }


    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=LitePal.getDatabase();
        int deleteRows=0;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows=db.delete(TABLE_BOOK,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                deleteRows=db.delete(TABLE_BOOK,"id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows=db.delete(TABLE_CATEGORY,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                deleteRows=db.delete(TABLE_CATEGORY,"id=?",new String[]{categoryId});
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.cc.com.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.cc.com.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.cc.com.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.cc.com.databasetest.provider.category";
        }
        return  null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db=LitePal.getDatabase();
        Uri  uriReturn=null;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId=db.insert(TABLE_BOOK,null,values);
                uriReturn =Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId=db.insert(TABLE_CATEGORY,null,values);
                uriReturn =Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        Connector.getDatabase();
        Log.e(TAG, "onCreate: ");
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=LitePal.getDatabase();
        Cursor cursor=null;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor= getDirQueryCursor(projection, selection, selectionArgs, sortOrder, db,TABLE_BOOK);
                break;
            case BOOK_ITEM:
                cursor=getCursor(uri, projection, sortOrder, db,TABLE_BOOK);
                break;
            case CATEGORY_DIR:
                cursor= getDirQueryCursor(projection, selection, selectionArgs, sortOrder, db,TABLE_CATEGORY);
                break;
            case CATEGORY_ITEM:
                cursor=getCursor(uri, projection, sortOrder, db,TABLE_CATEGORY);
                break;
            default:
                break;
        }
         return  cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db=LitePal.getDatabase();
        int updateRows=0;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows=db.update(TABLE_BOOK,values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                updateRows=db.update(TABLE_BOOK,values,"id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows=db.update(TABLE_CATEGORY,values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                updateRows=db.update(TABLE_CATEGORY,values,"id=?",new String[]{categoryId});
                break;
        }
        return updateRows;
    }
  /**
     * @description
     * @author CF
     * created at 2018/11/22/022  22:09
     */
    private Cursor getDirQueryCursor(String[] projection, String selection, String[] selectionArgs,
                                     String sortOrder, SQLiteDatabase db,String tableName) {
        return db.query(tableName,projection,selection,selectionArgs,
                null,null,sortOrder);
    }
  /**
     * @description
     * @author CF
     * created at 2018/11/22/022  22:09
     */
    private Cursor getCursor(Uri uri, String[] projection, String sortOrder,
                             SQLiteDatabase db,String tableName) {
        String id=uri.getPathSegments().get(1);
        return db.query(tableName,projection,"id=?",
                new String[]{id},null,null,sortOrder);
    }
}
