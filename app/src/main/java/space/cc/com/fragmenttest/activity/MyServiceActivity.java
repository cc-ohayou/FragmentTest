package space.cc.com.fragmenttest.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import space.cc.com.fragmenttest.MyService;
import space.cc.com.fragmenttest.R;

public class MyServiceActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "MyServiceActivity";
    private MyService.DownloadBinder downloadBinder;

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            downloadBinder=(MyService.DownloadBinder)service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_service);
        setButOnclickListenerByRid(R.id.start_service,this);
        setButOnclickListenerByRid(R.id.stop_service,this);
        setButOnclickListenerByRid(R.id.bind_service,this);
        setButOnclickListenerByRid(R.id.unbind_service,this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start_service:
                Intent startIntent=new Intent(this, MyService.class);
                //兼容android8.0
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    ContextCompat.startForegroundService(this,startIntent);
                }
                else{
                    this.startService(startIntent);
                }
                toastSimple("startService");
                break;
             case R.id.stop_service:
                 Intent stopIntent=new Intent(this, MyService.class);

                     this.stopService(stopIntent);

                 toastSimple("stopService");
                break;
            case R.id.bind_service:
                Intent bindIntent=new Intent(this, MyService.class);
                //绑定服务 此处是自定义的模拟下载服务 实际场景 会真正启动一个下载后台或前台服务
                this.bindService(bindIntent,connection,BIND_AUTO_CREATE);
                toastSimple("bind_service");
                break;
            case R.id.unbind_service:
                //解绑服务 譬如取消下载 停止导航服务等等 停止音乐服务前台显示等等
                this.unbindService(connection);
                toastSimple("unbind_service");
                break;
            default:
                break;

        }
    }
}
