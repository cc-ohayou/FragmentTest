package space.cc.com.fragmenttest.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import androidx.annotation.NonNull;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.util.ToastUtils;

public class UtilDialog {

    private static final String DEAFULT_POSITIVE_TEXT ="ok" ;
    private static final String DEFAULT_NEGATIVE_TEXT ="cancel" ;

   /*
   示例
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
                       *//*showMaterialDialogSimleDefault(activity,"warning!","应用通知权限不开启，可能会导致通知提醒服务异常，确定不设置吗？",new MaterialDialog.SingleButtonCallback() {
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
                       });*//*


            }

        }
    });*/


    /**
     * @author  CF
     * @date   2019/1/7
     * @description
     *
     */
    public  void showMaterialDialogSimleDefault(Context activity, String title, String content, MaterialDialog.SingleButtonCallback callbackAction) {

        showMaterialDialog(activity,title,content,DEAFULT_POSITIVE_TEXT,DEFAULT_NEGATIVE_TEXT,callbackAction);

    }


    /**
     * @author  CF
     * @date   2019/1/7
     * @description
     *
     */
    public  void showMaterialDialog(final Context activity, String title, String content, String positiveText, String negativeText, MaterialDialog.SingleButtonCallback callBackAction) {

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

    /**
         * @author  CF
         * @date   2019/1/14
         * @description
         *
         */
 public  void showInputMaterialDialogSimple(final Context activity, String title,String content,String inputHint,
                                  String inputPrefill, MaterialDialog.SingleButtonCallback callBackAction,MaterialDialog.InputCallback inputCallback){
        showInputMaterialDialog(activity,title,content,inputHint,inputPrefill,"ok","cancel",callBackAction,inputCallback);
    }
    /**
         * @author  CF
         * @date   2019/1/14
         * @description
         *
         */
@SuppressLint("ResourceAsColor")
public  void showInputMaterialDialog(final Context activity, String title, String content, String inputHint,
                                     String inputPrefill, String positiveText, String negativeText,
                                     MaterialDialog.SingleButtonCallback callBackAction, MaterialDialog.InputCallback inputCallBack){
    new MaterialDialog.Builder(activity)
            .title(title)
            .iconRes(R.drawable.cc_dialog)
            .content(content)
//                                .widgetColor(Color.BLUE)//输入框光标的颜色
            .positiveText(positiveText)
            .negativeText(negativeText)
            //可以输入的类型-电话号码-人名 邮箱等等
            .inputType(InputType.TYPE_CLASS_TEXT)
            //前2个一个是hint 一个是预输入的文字
            .input(inputHint, inputPrefill,inputCallBack)
            .inputRange(3, 40, R.color.colorAccent)
            //点击事件添加 方式1
            .onAny(callBackAction)
            .show();
}



}
