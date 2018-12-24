package space.cc.com.fragmenttest.domain.util;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotificationUtil {
    private static final String sTAG = "NotificationUtil";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final int REQUEST_SETTING_NOTIFICATION = 1;
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return isEnableV26(context);
        } else {
            return isEnableV19(context);
        }
    }
    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
     * 19及以上
     *
     * @param context
     * @return
     */
    public static boolean isEnableV19(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            Log.e(sTAG,"", e);
        } catch (NoSuchMethodException e) {
            Log.e(sTAG,"", e);
        } catch (NoSuchFieldException e) {
            Log.e(sTAG,"", e);
        } catch (InvocationTargetException e) {
            Log.e(sTAG,"", e);

        } catch (IllegalAccessException e) {
            Log.e(sTAG,"", e);

        }
        return false;
    }

    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
     * 针对8.0及以上设备
     *
     * @param context
     * @return
     */
    public static boolean isEnableV26(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {
            Log.d(sTAG, "isEnableV26: ");
        }
        return false;
    }
    public static void gotoOpenNotificationActivity(Context  context,Activity activity){

       if(!isNotificationEnabled(context)){
           gotoNotificationSetting(activity);
       }


      /*  摘要：4.4以下并没有提过从app跳转到应用通知设置页面的Action,可考虑跳转到应用详情页面,下面是直接跳转到应用通知设置的代码:if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){Intentintent=newIntent();intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");intent.p

            4.4以下并没有提过从app跳转到应用通知设置页面的Action,可考虑跳转到应用详情页面,下面是直接跳转到应用通知设置的代码:
*/




        }
    public static void gotoNotificationSetting(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" +pkg));
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            Log.e(sTAG, "",e);
        }

    }
    private static String getPackageName(Context  context) {
        return context.getApplicationContext().getPackageName();
    }



}

