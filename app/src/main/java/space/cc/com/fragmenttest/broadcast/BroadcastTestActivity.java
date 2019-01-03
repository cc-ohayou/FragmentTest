package space.cc.com.fragmenttest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BaseActivity;


public class BroadcastTestActivity extends BaseActivity {

    private static final String TAG = "BroadcastTestActivity";
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private MyBroadcastReceiver myBroadcastReceiver;
    private ForceOffLineReceiver forceOffLineReceiver;


    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_test);
//        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        Button but01=findViewById(R.id.but01);
        but01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyBroadCast.OFFLINE_BROADCAST);
               /* intent.setComponent(new ComponentName("space.cc.com.fragmenttest.broadcast",
                        "MyBroadcastReceiver"));*/
                Log.d(TAG, "onClick: ");
//                localBroadcastManager.sendBroadcast(intent);
                sendBroadcast(intent);
            }
        });

    /*    intentFilter=new IntentFilter();
        //android系统网络发生变化时 发出的广播值为 android.net.conn.CONNECTIVITY_CHANGE
        //
        intentFilter.addAction(MyBroadCast.OFFLINE_BROADCAST);
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        networkChangeReceiver=new NetworkChangeReceiver();
        forceOffLineReceiver=new ForceOffLineReceiver();
        //注册一个广播
//        registerReceiver(myBroadcastReceiver,intentFilter);
//        localBroadcastManager.registerReceiver(myBroadcastReceiver,intentFilter);
        registerReceiver(forceOffLineReceiver,intentFilter);*/
    }

    @Override
    public void requestPermission() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //动态注册的广播一定要取消住蹙额
//        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
//       unregisterReceiver(myBroadcastReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager= (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            //点击settings中的Network&Internet选项中的 mobile network->mobile data
            // 开关切换可以观察到网络变化的广播接收到了（前提app没有关闭）
            if(info!=null&&info.isAvailable()){
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT)
                        .show();
            }else{
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    public  static void actionStart(Context context, Map<String,String> map){
        startAction(context, map,BroadcastTestActivity.class);
    }
}
