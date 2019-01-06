package space.cc.com.fragmenttest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import space.cc.com.fragmenttest.service.DownLoadService;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: receive mybroadcast");
        DownLoadService.controlDownloadStateByAction(intent.getAction());


        Toast.makeText(context,"receive mybroadcast "+intent.getAction(),Toast.LENGTH_SHORT).show();
    }
}
