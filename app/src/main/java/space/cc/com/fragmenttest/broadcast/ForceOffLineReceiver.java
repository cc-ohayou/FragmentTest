package space.cc.com.fragmenttest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import space.cc.com.fragmenttest.activity.LoginActivity;
import space.cc.com.fragmenttest.domain.util.ActivityCollector;

public class ForceOffLineReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("you are forced tobe offline.Please try to login again");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //重新登陆之前销毁所有活动
                ActivityCollector.finishAll();
                //重新启动登录界面
                LoginActivity.actionStart(context,null);
            }
        });
        builder.show();
    }
}
