package space.cc.com.fragmenttest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //关闭模拟器 再打开观察消息
        Toast.makeText(context,"boot started",Toast.LENGTH_LONG).show();
    }
}
