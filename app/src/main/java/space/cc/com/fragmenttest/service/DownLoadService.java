package space.cc.com.fragmenttest.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.util.Log;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.MyServiceActivity;
import space.cc.com.fragmenttest.domain.util.NotificationUtil;
import space.cc.com.fragmenttest.domain.util.Utils;
import space.cc.com.fragmenttest.listener.DownLoadListener;

/**
   * @description  注意一定要在manifest中配置一下service声明否则不会创建的
   * @author CF
   * created at 2018/12/3/003  23:11
   */
public class DownLoadService extends Service {
  private static final String TAG = "DownLoadService";

//  private DownloadTask downloadTask;
  private String  downloadUrl;

  public  DownLoadListener listener=new DownLoadListener(){
      @Override
      public void onProgress(int progress) {
          getNotificationManager().notify(1,getNotification());
      }

      @Override
      public void onSuccess() {

      }

      @Override
      public void onFailed() {

      }

      @Override
      public void onPaused() {

      }

      @Override
      public void onCanceled() {

      }
  };

    public NotificationManager  getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    Notification getNotification(){
        Notification notification=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("fore_service", "前台服务", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            Intent intentForeSerive = new Intent(Utils.getApp(), MyServiceActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(Utils.getApp(), 0, intentForeSerive, 0);
            notification = new NotificationCompat.Builder(Utils.getApp(), "fore_service")
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setLargeIcon(BitmapFactory.decodeResource(IconCompat.getResources(), R.mipmap.msg_64))
                    .setContentIntent(pendingIntent)
                    .build();
        }else{
            Intent intent=new Intent(this, MyServiceActivity.class);
            PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
            notification=new NotificationCompat.Builder(this)
                    .setContentTitle("service create")
                    .setContentText("stfysdjfskldgldkglk k sklflskdflksdf")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.msg_64))
                    .setContentIntent(pi)
                    .build();
        }
        return notification;
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
