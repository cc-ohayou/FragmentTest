package space.cc.com.fragmenttest.service;

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
import android.text.format.Formatter;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.DownLoadActivity;
import space.cc.com.fragmenttest.activity.MyServiceActivity;
import space.cc.com.fragmenttest.domain.util.StringUtils;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.domain.util.Utils;

/**
   * @description  注意一定要在manifest中配置一下service声明否则不会创建的
   * @author CF
   * created at 2018/12/3/003  23:11
   */
public class DownLoadService extends Service {
  private static final String TAG="testTag";
  private DownloadTask downloadTask;
  private int notifyId=1;
    private String  downloadUrl;

  private DownloadBinder mbinder=new DownloadBinder();

  public class DownloadBinder extends Binder {

        /**
           * @description  开始下载
           * @author CF
           * created at 2018/12/6/006  23:07
           */
    public void startDownLoad(String url){
        if(downloadTask==null){
            initDownLoadTask(url);

        }
        downloadTask.start();
        //显示服务前台通知
        startForeground(notifyId,getNotification("Downloading...",downloadTask.progress));
//        refresh(downloadTask.progress);
        ToastUtils.showDisplay("Downloading");
    }

      private void initDownLoadTask(String url) {
          downloadUrl=url;
          GetRequest<File> request= OkGo.<File>get(downloadUrl)
                  .headers("aaa","111")
                  .params("bbb","222");
          downloadTask= OkDownload.request(TAG,request)
                  .folder(DownLoadActivity.path)
                  .fileName(downloadUrl.substring(downloadUrl.lastIndexOf("/")))
                  //save必须调用 任务数据存储到数据库中
                  .save()
                  //注册监听器可注册多个
                  .register(MyDownLoadListener);
      }

      /**
     * @description  取消下载
     * @author CF
     * created at 2018/12/6/006  23:36
     */
    public  void pauseDownLoad(){
        if(downloadTask!=null){
            downloadTask.pause();
        }
    }

    public void cancelDownLoad(){
        if(downloadTask!=null){
            downloadTask.remove();
        }else if(!StringUtils.isEmpty(downloadUrl)){
            //取消下载时需要删除文件，并关闭通知
            String  fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            Log.d(TAG, "cancelDownLoad: progress.filePath="+DownLoadActivity.path+",fileName="+fileName);

            String directory=DownLoadActivity.path;
            File file=new File(directory+fileName);
            if(file.exists()){
                file.delete();
            }
            getNotificationManager().cancel(notifyId);
            stopForeground(true);
            ToastUtils.showDisplay("cancled download");
        }
    }

      public void reDownLoad(String url) {
        if(downloadTask==null){
            initDownLoadTask(url);
        }
          downloadTask.restart();
      }

  }
   private  DownloadListener MyDownLoadListener =new  DownloadListener(TAG){

       @Override
       public void onStart(Progress progress) {
           Log.d(TAG, "onStart: progress.filePath="+progress.filePath);

           getNotificationManager().notify(notifyId,getNotification("Download start", progress));

       }

       @Override
       public void onProgress(Progress progress) {
           Log.e(TAG, "onProgress: progress="+progress );
           Log.e(TAG, "progress.sttatus= "+progress.status );
           System.err.println("refresh going"+progress.status);
           refresh(progress);
//          getNotificationManager().notify(notifyId,getNotification("Downloading...", progress));
       }
       public void refresh(Progress progress) {
           String currentSize = Formatter.formatFileSize(getBaseContext(), progress.currentSize);
           Log.e(TAG, "refresh: currentSize="+currentSize );
           String totalSize = Formatter.formatFileSize(getBaseContext(), progress.totalSize);
           Log.e(TAG, "refresh: totalSize="+totalSize );

           switch (progress.status) {
               case Progress.NONE:
                   getNotification("停止", progress);
                   break;
               case Progress.PAUSE:
                   getNotification("暂停中", progress);

                   break;
               case Progress.ERROR:
                   getNotification("下载出错", progress);

                   break;
               case Progress.WAITING:
                   getNotification("等地中", progress);

                   break;
               case Progress.FINISH:
                   getNotification("下载完成", progress);
                   break;
               case Progress.LOADING:
                   String speed = Formatter.formatFileSize(getBaseContext(), progress.speed);
                   Log.e(TAG, "LOADING speed= "+speed );
                   break;
           }
           getNotification("DownLoading", progress);

       }
       @Override
       public void onError(Progress progress) {
           Log.d(TAG, "onError: "+progress);
           downloadTask=null;
           stopForeground(true);
           progress.fraction=-1;
           getNotificationManager().notify(notifyId,getNotification("Downlaod Failed",progress));
           ToastUtils.showDisplay("DownLoad Failed");
           progress.exception.printStackTrace();
       }

       @Override
       public void onFinish(File file, Progress progress) {
          downloadTask=null;
          //关闭前台服务
          stopForeground(true);
          //下载成功的通知
           progress.fraction=-1;
          getNotificationManager().notify(notifyId,getNotification("Downlaod Success",progress));
           ToastUtils.showDisplay("DownLoad Success");
       }

       @Override
       public void onRemove(Progress progress) {
            downloadTask=null;
            stopForeground(true);
           ToastUtils.showDisplay("DownLoad Canceled");

       }
   };


    public NotificationManager  getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    Notification getNotification(String title, Progress progress) {

        int progressFraction= (int) (progress.fraction*10000);
        if(progress.fraction<0){progressFraction=-1;}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "getNotification: CODES.O"+",progressFraction="+progressFraction+" progress="+progress);
            NotificationChannel channel = new NotificationChannel("fore_service", "前台服务", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            Intent intentForeSerive = new Intent(Utils.getApp(), MyServiceActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(Utils.getApp(), 0, intentForeSerive, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp(), "fore_service")
                    .setContentTitle(title)
                    .setContentText(progress.speed/1024+"KB/s")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                    .setContentIntent(pendingIntent);
            //进度条
            builder.setProgress(10000, progressFraction, false);

           /* if (progressFraction > 0) {

                builder.setProgress(10000, progressFraction, true);
//                builder.setContentText(progress + "%");
            }*/
            return builder.build();
        } else {
            Log.d(TAG, "getNotification: progress"+progress);

            Intent intent = new Intent(this, MyServiceActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                    .setContentIntent(pi);
            if (progressFraction > 0) {
                builder.setProgress(10000, progressFraction, false);
                builder.setContentText(progress + "%");
            }
            return builder.build();
        }
    }



  @Override
  public void onCreate() {
      Log.d(TAG, " onCreate");
      super.onCreate();

  }
  @Override
  public void onDestroy() {
      Log.d(TAG," destory");
      super.onDestroy();
      if(downloadTask!=null){
          downloadTask.unRegister(TAG);
          downloadTask=null;
      }
  }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }


    @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      Log.d(TAG,"MyService onStartCommand");
      return super.onStartCommand(intent, flags, startId);
  }
}
