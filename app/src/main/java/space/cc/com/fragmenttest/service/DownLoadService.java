package space.cc.com.fragmenttest.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
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
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.util.StringUtils;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.domain.util.Utils;

/**
 * @author CF
 * created at 2018/12/3/003  23:11
 * @description 注意一定要在manifest中配置一下service声明否则不会创建的
 */
public class DownLoadService extends Service {
    private static final String TAG = "testTag";
    private DownloadTask downloadTask;
    private int notifyId = 1;
    private int preProgress = 1;
    private Context mContext;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private static String downloadUrl = UrlConfig.DOWN_LOAD04.getValue();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.d(TAG, "MyService onStartCommand");
            mContext = this;
            //显示服务前台通知
            initNotification();
            if (downloadTask == null) {
                initDownLoadTask();
            }
            downloadTask.start();

            ToastUtils.showDisplay("Downloading");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        Log.d(TAG, " onCreate");
        super.onCreate();
        mContext = this;
        startForeground(notifyId,initNotification());
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, " destory");
        super.onDestroy();
        if (downloadTask != null) {
            downloadTask.unRegister(TAG);
            downloadTask = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * @description 取消下载
     * @author CF
     * created at 2018/12/6/006  23:36
     */
    /*public  void pauseDownLoad(){
        if(downloadTask!=null){
            downloadTask.pause();
        }
    }*/

    /*public void cancelDownLoad(){
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
    }*/
   /* public void reDownLoad(String url) {
        if (downloadTask == null) {
            initDownLoadTask(url);
        }
        downloadTask.restart();
    }*/


    private DownloadListener MyDownLoadListener = new DownloadListener(TAG) {

        @Override
        public void onStart(Progress progress) {
            Log.d(TAG, "onStart: progress.filePath=" + progress.filePath);
        }

        @Override
        public void onProgress(Progress progress) {
            Log.e(TAG, "onProgress: progress=" + progress);
            //下载文件源要选择好 否则DownLoadTask的Progress获取到的status一直是waiting状态 不是loading状态
            // 此时进度刷新的回调方法onProgress不会被执行也就不会更新下载进度
            refresh(progress);
        }

        public void refresh(Progress progress) {
            String currentSize = Formatter.formatFileSize(getBaseContext(), progress.currentSize);
            Log.e(TAG, "refresh: currentSize=" + currentSize);
            String totalSize = Formatter.formatFileSize(getBaseContext(), progress.totalSize);
            Log.e(TAG, "refresh: totalSize=" + totalSize);

            switch (progress.status) {
                case Progress.NONE:
                    updateNotification("停止", progress);
                    break;
                case Progress.PAUSE:
                    updateNotification("暂停中", progress);

                    break;
                case Progress.ERROR:
                    updateNotification("下载出错", progress);

                    break;
                case Progress.WAITING:
                    updateNotification("等地中", progress);

                    break;
                case Progress.FINISH:
                    updateNotification("下载完成", progress);
                    break;
                case Progress.LOADING:
                    String speed = Formatter.formatFileSize(getBaseContext(), progress.speed);
                    Log.e(TAG, "LOADING speed= " + speed);
                    updateNotification("下载中", progress);
                    break;
            }
//            updateNotification("", progressFraction);
        }

        @Override
        public void onError(Progress progress) {
            Log.d(TAG, "onError: " + progress);
            downloadTask = null;
//            stopForeground(true);
            progress.fraction = -1;
            updateNotification("Downlaod Failed", progress);
            ToastUtils.showDisplay("DownLoad Failed");
            progress.exception.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            downloadTask = null;
            //关闭前台服务
            stopForeground(true);
            //下载成功的通知
//            progress.fraction = -1;
            updateNotification("下载完成", progress);
            //           ToastUtils.showDisplay("DownLoad Success");
            installApk(file);// 安装apk

        }

        @Override
        public void onRemove(Progress progress) {
            downloadTask = null;
            stopForeground(true);
            ToastUtils.showDisplay("DownLoad Canceled");

        }
    };


    /**
     * 初始化Notification通知
     */
    private Notification initNotification() {


//        int progressFraction= (int) (progress.fraction*10000);
//        if(progress.fraction<0){progressFraction=-1;}
//        String speedDesc= Formatter.formatFileSize(getBaseContext(), progress.speed)+"/s";
        String title = "CCの更新";
        int importanceLevel = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getNotification(title, importanceLevel);
        } else {

            Intent intent = new Intent(this, MyServiceActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
            builder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText("")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.msg_32)
                    .setOnlyAlertOnce(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                    .setContentIntent(pi);
            getNotificationManager().notify(notifyId, builder.build());
            return builder.build();
        }


    }


    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;

    }

    /**
     * 更新通知
     */
    public void updateNotification(String text, Progress progress) {
        int progressFraction = (int) (progress.fraction * 10000);
        String speed = Formatter.formatFileSize(getBaseContext(), progress.speed);

        if (progress.fraction < 0) {
            progressFraction = -1;
        }
        if (preProgress < progressFraction) {
            if (StringUtils.isEmpty(text)) {
                builder.setContentText(text);
            } else {
                builder.setContentText(progress + "%");
            }
            builder.setSubText(speed + "/s");
//          builder.setProgress(100, (int) progress, false);
            builder.setProgress(10000, progressFraction, true);
            notificationManager.notify(notifyId, builder.build());
        }
        preProgress = progressFraction;
    }


    private Notification getNotification(String title, int importanceLevel) {
        NotificationChannel channel = new NotificationChannel("fore_service", "前台服务", importanceLevel);
        getNotificationManager().createNotificationChannel(channel);
        Intent intentForeSerive = new Intent(Utils.getApp(), MyServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Utils.getApp(), 0, intentForeSerive, 0);
        builder = new NotificationCompat.Builder(Utils.getApp(), "fore_service")
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.msg_32)
                .setOnlyAlertOnce(true)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                .setContentIntent(pendingIntent);
        getNotificationManager().notify(notifyId, builder.build());
        return builder.build();
    }


    private void initDownLoadTask() {

        downloadUrl = MyServiceActivity.downloadUrl;
        GetRequest<File> request = OkGo.<File>get(downloadUrl)
                .headers("aaa", "111")
                .params("bbb", "222");
        downloadTask = OkDownload.request(TAG, request)
                .folder(DownLoadActivity.path)
                .fileName(downloadUrl.substring(downloadUrl.lastIndexOf("/")))
                //save必须调用 任务数据存储到数据库中
                .save()
                //注册监听器可注册多个
                .register(MyDownLoadListener);
    }

    /**
     * 安装软件
     *
     * @param file
     */
    /*
     * 安装apk
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //兼容android N
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.cc.space.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());//如果不加，最后不会提示完成、打开。
    }


}
