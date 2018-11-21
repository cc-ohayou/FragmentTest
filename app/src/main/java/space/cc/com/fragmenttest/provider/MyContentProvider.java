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
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
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

    private Cursor getDirQueryCursor(String[] projection, String selection, String[] selectionArgs,
                                     String sortOrder, SQLiteDatabase db,String tableName) {
        return db.query(tableName,projection,selection,selectionArgs,
                null,null,sortOrder);
    }

    private Cursor getCursor(Uri uri, String[] projection, String sortOrder,
                           SQLiteDatabase db,String tableName) {
        String id=uri.getPathSegments().get(1);
        return db.query(tableName,projection,"id=?",
                new String[]{id},null,null,sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
