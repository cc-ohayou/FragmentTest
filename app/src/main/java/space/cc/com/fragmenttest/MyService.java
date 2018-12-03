package space.cc.com.fragmenttest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import space.cc.com.fragmenttest.activity.MyServiceActivity;
  /**
     * @description  注意一定要在manifest中配置一下service声明否则不会创建的
     * @author CF
     * created at 2018/12/3/003  23:11
     */
public class MyService extends Service {
    private static final String TAG = "MyService";

    private DownloadBinder mBinder=new DownloadBinder();

    public class DownloadBinder extends Binder {
        public void startDownload(){
            Log.d(TAG," start download executed");
        }
        public void getProgress(){
            Log.d(TAG, "getProgress: ");
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "MyService onCreate");
        super.onCreate();

       /* Intent intent=new Intent(this, MyServiceActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        Notification notice=new NotificationCompat.Builder(this)
                .setContentTitle("service create")
                .setContentText("stfysdjfskldgldkglk k sklflskdflksdf")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.msg_32)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.msg_64))
                .setContentIntent(pi)
                .build();
        //启动前台服务
        startForeground(1,notice);*/
        //兼容android8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("fore_service", "前台服务", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            Intent intentForeSerive = new Intent(this, MyServiceActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentForeSerive, 0);
            Notification notification = new NotificationCompat.Builder(this, "fore_service")
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);

//        notificationManager.notify();
        }else{
              Intent intent=new Intent(this, MyServiceActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        Notification notice=new NotificationCompat.Builder(this)
                .setContentTitle("service create")
                .setContentText("stfysdjfskldgldkglk k sklflskdflksdf")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.msg_32)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.msg_64))
                .setContentIntent(pi)
                .build();
        //启动前台服务
        startForeground(1,notice);
        }
    }
    @Override
    public void onDestroy() {
        Log.d(TAG,"MyService destory");
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"MyService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
