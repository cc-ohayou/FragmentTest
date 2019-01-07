package space.cc.com.fragmenttest.domain.util;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import space.cc.com.fragmenttest.R;

public class NotificationUtil {
    private static final String sTAG = "NotificationUtil";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final int REQUEST_SETTING_NOTIFICATION = 1;
    private static final String DEAFULT_POSITIVE_TEXT ="ok" ;
    private static final String DEFAULT_NEGATIVE_TEXT ="cancel" ;

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
    public static void gotoOpenNotificationActivity(Context  context, final Activity activity){

       if(!isNotificationEnabled(context)){

           showMaterialDialogSimleDefault(activity,"Notification Settings","前往应用通知设置",new MaterialDialog.SingleButtonCallback() {
               @Override
               public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                   if (which == DialogAction.NEUTRAL) {
//                            Toast.makeText(MainActivity.this, "更多信息", Toast.LENGTH_LONG).show();
                   } else if (which == DialogAction.POSITIVE) {
//                            前往设置页面
                       gotoNotificationSetting(activity);

                   } else if (which == DialogAction.NEGATIVE) {
//                       应用通知权限不开启，可能会导致通知提醒服务异常，确定不设置吗？
                       ToastUtils.showDisplay("应用通知权限未开启，可能会导致通知提醒服务异常，" +
                               "若需要您可自行前往系统的通知设置中开启应用通知权限");
                       /*showMaterialDialogSimleDefault(activity,"warning!","应用通知权限不开启，可能会导致通知提醒服务异常，确定不设置吗？",new MaterialDialog.SingleButtonCallback() {
                           @Override
                           public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                               if (which == DialogAction.NEUTRAL) {
//                            Toast.makeText(MainActivity.this, "更多信息", Toast.LENGTH_LONG).show();
                               } else if (which == DialogAction.POSITIVE) {
//                            前往设置页面
                                   gotoNotificationSetting(activity);

                               } else if (which == DialogAction.NEGATIVE) {
                                   ToastUtils.showDisplay("应用没有通知权限将导致");

                               }

                           }
                       });*/


                   }

               }
           });
       }


      /*  摘要：4.4以下并没有提过从app跳转到应用通知设置页面的Action,可考虑跳转到应用详情页面,下面是直接跳转到应用通知设置的代码:if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){Intentintent=newIntent();intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");intent.p

            4.4以下并没有提过从app跳转到应用通知设置页面的Action,可考虑跳转到应用详情页面,下面是直接跳转到应用通知设置的代码:
*/




        }
    /**
         * @author  CF
         * @date   2019/1/7
         * @description 
         * 
         */
    private static void showMaterialDialogSimleDefault(Activity activity, String title, String content, MaterialDialog.SingleButtonCallback callbackAction) {
        
        showMaterialDialog(activity,title,content,DEAFULT_POSITIVE_TEXT,DEFAULT_NEGATIVE_TEXT,callbackAction);

    }


            /**
                 * @author  CF
                 * @date   2019/1/7
                 * @description 
                 * 
                 */
    private static void showMaterialDialog(final Activity activity,String title,String content,String positiveText,String negativeText,MaterialDialog.SingleButtonCallback callBackAction) {

//        简单的diaolog，同意和不同意的字体颜色是默认是R.color.colorAccent
        new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .iconRes(R.drawable.cc_dialog)
                .positiveText(positiveText)
                .negativeText(negativeText)
//                .neutralText("更多信息")
                .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色
                //CheckBox
               /* .checkBoxPrompt("不再提醒", false, new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Toast.makeText(MainActivity.this, "不再提醒", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "会再次提醒", Toast.LENGTH_LONG).show();
                        }
                    }
                })*/
                //嵌套recycleview，这个的点击事件可以先获取此Recycleview对象然后自己处理
//                .adapter(new RecycleviewAdapter(getData(), MainActivity.this), new LinearLayoutManager(MainActivity.this))


             /*   .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        dataChoose = "下标：" + position + " and 数据：" + mData.get(position);
                    }
                })*/

                //点击事件添加 方式1
                .onAny(callBackAction).show();

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

