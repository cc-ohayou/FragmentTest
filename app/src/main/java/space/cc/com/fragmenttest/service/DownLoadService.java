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
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.DownLoadActivity;
import space.cc.com.fragmenttest.activity.MyServiceActivity;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.util.Utils;
import space.cc.com.fragmenttest.listener.MyDownLoadListener;

/**
   * @description  注意一定要在manifest中配置一下service声明否则不会创建的
   * @author CF
   * created at 2018/12/3/003  23:11
   */
public class DownLoadService extends Service {
  private static final String TAG="testTag";
    private DownloadTask downloadTask;
  private String  downloadUrl= UrlConfig.DOWN_LOAD.getValue();


    public NotificationManager  getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    Notification getNotification(String title, int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("fore_service", "前台服务", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            Intent intentForeSerive = new Intent(Utils.getApp(), MyServiceActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(Utils.getApp(), 0, intentForeSerive, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp(), "fore_service")
                    .setContentTitle(title)
                    .setContentText("This is content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                    .setContentIntent(pendingIntent);
            //进度条
            if (progress > 0) {
                builder.setProgress(100, progress, false);
                builder.setContentText(progress + "%");
            }
            return builder.build();
        } else {
            Intent intent = new Intent(this, MyServiceActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("service create")
                    .setContentText("stfysdjfskldgldkglk k sklflskdflksdf")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                    .setContentIntent(pi);
            if (progress > 0) {
                builder.setProgress(100, progress, false);
                builder.setContentText(progress + "%");
            }
            return builder.build();
        }
    }

    @Nullable
  @Override
  public IBinder onBind(Intent intent) {
      return null;
  }

  @Override
  public void onCreate() {
      Log.d(TAG, "MyService onCreate");
      super.onCreate();
      GetRequest<File> request= OkGo.<File>get(downloadUrl)
              .headers("aaa","111")
              .params("bbb","222");
      downloadTask= OkDownload.request(TAG,request)
               .folder(DownLoadActivity.path)
              .fileName("hahaha.apk")
              //save必须调用 任务数据存储到数据库中
              .save()
              //注册监听器可注册多个
              .register(new MyDownLoadListener(TAG));
      downloadTask.start();
      Notification notification = getNotification("下载开始",0);
      startForeground(1, notification);


  }
  @Override
  public void onDestroy() {
      Log.d(TAG,"MyService destory");
      super.onDestroy();
      if(downloadTask!=null){
          downloadTask.unRegister(TAG);
//          downloadTask.remove();
      }
  }


    @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      Log.d(TAG,"MyService onStartCommand");
      return super.onStartCommand(intent, flags, startId);
  }
}
