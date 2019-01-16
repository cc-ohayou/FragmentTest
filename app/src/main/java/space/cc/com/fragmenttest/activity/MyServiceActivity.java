package space.cc.com.fragmenttest.activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.ClientConfiguration;
import space.cc.com.fragmenttest.domain.GlobalSettings;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.LogUtil;
import space.cc.com.fragmenttest.domain.util.NotificationUtil;
import space.cc.com.fragmenttest.domain.util.PermissinUtils;
import space.cc.com.fragmenttest.domain.util.StringUtils;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.service.DownLoadService;
import space.cc.com.fragmenttest.service.MyService;

public class MyServiceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MyServiceActivity";
    private static  int progress = 0;
    //通过downLoadBinder进行活动和服务之间的通信  依赖于Ibinder接口的特性
//    private DownLoadService.DownloadBinder downloadBinder;



    //connection用于绑定服务 绑定成功后有一个回调
    // 在这里建立活动和服务的链接 IBinder类型 这样达到在活动里操纵服务的效果
   /* private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            downloadBinder = (DownLoadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.my_service);
            setButOnclickListenerByRid(R.id.start_service, this);
            setButOnclickListenerByRid(R.id.stop_service, this);
            setButOnclickListenerByRid(R.id.bind_service, this);
            setButOnclickListenerByRid(R.id.unbind_service, this);
            setButOnclickListenerByRid(R.id.startDownload, this);
            setButOnclickListenerByRid(R.id.pauseDownload, this);
            setButOnclickListenerByRid(R.id.cancelDownload, this);
            setButOnclickListenerByRid(R.id.reDownLoad, this);
            setButOnclickListenerByRid(R.id.addProgress, this);

//            绑定服务 下载后台或前台服务 必须在activity完成
//            boolean  flag= getApplicationContext().bindService(startDownload, connection, BIND_AUTO_CREATE);
//            Log.d(TAG, "onCreate: flag="+flag);
        } catch (Exception e) {
            ToastUtils.showDisplay(e.getMessage());
            LogUtil.getInstance().writeEvent(TAG, "onCreate: failed" + e.getMessage(), e);
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                //兼容android8.0
                startServiceCustom(this,startIntent);
                toastSimple("startService");
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                this.stopService(stopIntent);
                toastSimple("stopService");
                break;
            case R.id.bind_service:
//                Intent bindIntent = new Intent(this, MyService.class);
                //绑定服务 此处是自定义的模拟下载服务 实际场景 会真正启动一个下载后台或前台服务
//                this.bindService(bindIntent, connection, BIND_AUTO_CREATE);
                toastSimple("bind_service");
                break;
            case R.id.unbind_service:
                //解绑服务 譬如取消下载 停止导航服务等等 停止音乐服务前台显示等等
//                this.unbindService(connection);
                toastSimple("unbind_service");
                break;
            case R.id.startDownload:
                //启动下载任务
                Intent startDownload = new Intent(this, DownLoadService.class);
                startServiceCustom(this,startDownload);
//                downloadBinder.startDownLoad(downloadUrl);
                break;
            case R.id.pauseDownload:
//                downloadBinder.pauseDownLoad();
                break;
            case R.id.cancelDownload:
//                downloadBinder.cancelDownLoad();
                break;
            case R.id.reDownLoad:
//                downloadBinder.reDownLoad(downloadUrl);
                break;
            case R.id.addProgress:
                    ToastUtils.showDisplay(GlobalSettings.settingProperties.getDownLoadUrl());
                    break;
            default:
                break;

        }
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(connection);
    }
}
