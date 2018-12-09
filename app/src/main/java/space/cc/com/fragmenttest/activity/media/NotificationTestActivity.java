package space.cc.com.fragmenttest.activity.media;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BaseActivity;
import space.cc.com.fragmenttest.domain.util.NotificationUtil;

public class NotificationTestActivity extends BaseActivity implements View.OnClickListener {
    int notifyID = 1;
    // The id of the channel.
    String CHANNEL_ID = "my_channel_01";
    String CHANNEL_NAME = "testChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        setButOnclickListenerByRid(R.id.sendNotifyBut, this);
        NotificationUtil.gotoOpenNotificationActivity(this);
    }

    @Override
    public void requestPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendNotifyBut:
                toastSimple("send notify start");
//                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                Intent intent = new Intent(this, NotificationDisplayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("notifyMsg", "this is msg from notify");
//                intent.setAction(ACTION_BIG_PICTURE_STYLE);

                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

                builder.setContentTitle("test title")
                        .setContentText("test content test content" +
                                "test content" +
                                "test content" +
                                "test content" +
                                "test content" +
                                "test contenttest contenttest content" +
                                "test content" +
                                "test content test content" +
                                "test content" +
                                "test content" +
                                "test content" )
                        //时间用于排序
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.msg_32)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.msg_64))
                        //兼容sdk28 也即安卓8.0以后通知栏显示
                        // channelId非常重要，不设置通知栏不展示
                        .setChannelId(CHANNEL_ID)
                        //设置点击后的跳转事件
                        .setContentIntent(pi)
//                        .setPriority(Notification.PRIORITY_HIGH)
//                        .setStyle(getBigText())
                        .setFullScreenIntent(pi,true)
                        .setStyle(getBigPicture())
                        //设置通知点击后自动消失
                        .setAutoCancel(true);
                //兼容sdk28 也即安卓8.0以后通知栏显示
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    toastSimple("send notify newest version="+Build.VERSION.SDK_INT);
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);

                }
                Notification notification = builder.build();
                //此处传入notify的id  可以用于在后面显式的取消通知状态栏  notificationManager.cancel(1);
                notificationManager.notify(notifyID, notification);
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
    private NotificationCompat.BigPictureStyle getBigPicture() {

        return new NotificationCompat.BigPictureStyle()
                .setBigContentTitle("Big picture style notification ~ Expand title")
                .setSummaryText("Demo for big picture style notification ! ~ Expand summery")
                .bigPicture(BitmapFactory.decodeResource(getResources(),R.mipmap.robort));


    }

    private NotificationCompat.BigTextStyle getBigText() {

    return    new NotificationCompat.BigTextStyle()
            .bigText("this is for test longText this is for test longText" +
            "this is for test longText" +
            "this is for test longText" +
            "this is for test longText" +
            "this is for test longText" +
            "this is for test longText" +
            "this is for test longText" +
            "this is for test longText" +
            "this is for test longText");
    }
}
