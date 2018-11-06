package space.cc.com.fragmenttest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import space.cc.com.fragmenttest.R;

public class BroadcastTestActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_test);
      /*  intentFilter=new IntentFilter();
        //android系统网络发生变化时 发出的广播值为 android.net.conn.CONNECTIVITY_CHANGE
        //
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        //注册一个广播
        registerReceiver(networkChangeReceiver,intentFilter);*/
        Button but01=findViewById(R.id.but01);
        but01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("com.cc.ccspace.MY_BROADCAST");
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //动态注册的广播一定要取消住蹙额
        unregisterReceiver(networkChangeReceiver);
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
}
