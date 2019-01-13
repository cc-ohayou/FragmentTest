package space.cc.com.fragmenttest.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.BuildConfig;
import space.cc.com.fragmenttest.domain.util.Utils;

/**
 * 必需的小工具
 * <p>
 * Created by Bamboy on 2017/3/24.
 */
public class UtilWant {

    /**
     * 判断字符串是否为空
     *
     * @param str 需要判断的字符串
     * @return true即为空；false不为空
     */
    public boolean isNull(String str) {
        if (str == null
                || "".equals(str)
                || "null".equals(str)
                || "[null]".equals(str)
                || "{null}".equals(str)
                || "[]".equals(str)
                || "{}".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断TextView或其内容是否为空
     *
     * @param tv 需要判断的TextView
     * @return true即为空；false不为空
     */
    public boolean isNull(TextView tv) {
        if (tv == null
                || tv.getText() == null
                || isNull(tv.getText().toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断list或其内容是否为空
     *
     * @param list 需要判断的list
     * @return true即为空；false不为空
     */
    public boolean isNull(List list) {
        if (list == null
                || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 清空List
     *
     * @param list 要清空的List
     */
    public void clearList(List list) {
        if (!isNull(list)) {
            for (int i = list.size() - 1; i >= 0; i--) {
                list.remove(i);
            }
        }
    }


    public void showInput(EditText et) {
        InputMethodManager inputManager =
                (InputMethodManager) et.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(et, 0);
    }

    /**
     * 收起输入法
     *
     * @param activity 上下文
     */
    @SuppressWarnings("static-access")
    public void hideInput(Activity activity) {
        try {
            ((InputMethodManager) activity
                    .getSystemService(activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    /**
     * 打印错误日志【非打包模式下才会打印】
     *
     * @param e
     */
    public void showException(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }

    /**
     * 打印错误日志
     *
     * @param e
     */
    public void showException(JSONException e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }


    private final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    /**
     * 用来判断是否开启通知权限
     * */
    public boolean isNotificationEnabled(Context context){
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            boolean isOpened = manager.areNotificationsEnabled();
            return isOpened;
        } else if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOps =
                    (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE,
                        Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg)
                        == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                    | InvocationTargetException | IllegalAccessException | RuntimeException e) {
                return true;
            }
        } else {
            return true;
        }
    }


    public static void main(String[] args) {
        double transparentRatio=0.75;
        String  db=String.valueOf(255*(1-transparentRatio));
        db.subSequence(0,db.indexOf("."));
        Integer x = Integer.parseInt(db.substring(0,db.indexOf(".")));
        String hex = x.toHexString(x);
        System.out.println(hex);
    }
  /**
     * @description 获取本地缓存Uri对象通过名称 如存在先删除 再创建心新的ile 内容后续自行写入
     * @author CF
     * created at 2019/1/13/013  22:13
     */
    public Uri getFileUriByName(String picName) {
        Uri imageUri;
    /*此处使用应用关联缓存存放图片 android6.0开始 读写SD卡被列为了危险权限
   任何直接存储到sd卡的操作都需要申请运行时权限  使用应用关联目录则可以跳过这一步

     * */
        File outPutImage = new File(Utils.getApp().getExternalCacheDir(), picName);
        try {
            if (outPutImage.exists()) {
                outPutImage.delete();
            }
            outPutImage.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
/*
            低于android7.0的设备需要用Uri的fromFile方法将File对象转换为Uri对象
            Uri对象标识着output_image.jpg这张图片的本地真实路径
            7.0以后直接使用本地真实路径被认为是不安全的 所以通过getUriForFile
            获取一个转换过的不是本地真实路径但可以关联到真实路径的Uri对象
            总之一切为了更加安全
*/
            imageUri = FileProvider.getUriForFile(Utils.getApp(),
                    Utils.getApp().getString(R.string.fileProvider), outPutImage);
        } else {
            imageUri = Uri.fromFile(outPutImage);
        }
        return imageUri;
    }

    @TargetApi(19)
    public String handleImageOnKitKat(Intent data) {
        String imagePath = "";
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(Utils.getApp(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //contnet类型普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //file类型直接获取图片路径
            imagePath = uri.getPath();
        }
        return imagePath;

    }


    /**
     * @description  获取图片路径
     * @author CF
     * created at 2018/11/26/026  0:11
     */
    public String getImagePath(Uri uri, String selection) {
        String path = "";
        //通过Uri和selection来获取真实图片路径
        Cursor cursor=Utils.getApp().getContentResolver().query(uri,null,selection,
                null,null);
        if(cursor!=null){
            if(cursor.moveToNext()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }
        return path;
    }


    /**
     * @description
     * @author CF
     * created at 2018/11/26/026  0:15
     */
    public String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        return imagePath;
    }

}
