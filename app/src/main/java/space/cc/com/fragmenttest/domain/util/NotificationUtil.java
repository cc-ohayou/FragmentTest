package space.cc.com.fragmenttest.domain.util;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static space.cc.com.fragmenttest.domain.util.ActivityUtils.startActivity;

public class NotificationUtil {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps =
                (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;

        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod =
                    appOpsClass.getMethod(CHECK_OP_NO_THROW,
                            Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);

            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) ==
                    AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void gotoOpenNotificationActivity(Context  context){




      /*  摘要：4.4以下并没有提过从app跳转到应用通知设置页面的Action,可考虑跳转到应用详情页面,下面是直接跳转到应用通知设置的代码:if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){Intentintent=newIntent();intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");intent.p

            4.4以下并没有提过从app跳转到应用通知设置页面的Action,可考虑跳转到应用详情页面,下面是直接跳转到应用通知设置的代码:
*/

            boolean enabled=  isNotificationEnabled(context);
           if(enabled){
                    return;
            }else{

           }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 进入设置系统应用权限界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                return;
                /*Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", context.getPackageName());
                ToastUtils.showDisplay(context.getApplicationInfo().uid+context.getApplicationInfo().packageName, Toast.LENGTH_LONG);
                intent.putExtra("app_uid", context.getApplicationInfo().uid);
                startActivity(intent);*/
            } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                startActivity(intent);
            }


        }

    private static String getPackageName(Context  context) {
        return context.getApplicationContext().getPackageName();
    }



}

