package space.cc.com.fragmenttest.activity.media;

import android.Manifest;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BaseActivity;
import space.cc.com.fragmenttest.domain.Ring;

public class VideoTestActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ImageButton play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);
        play=findViewById(R.id.play);
        setImageButOnclickListenerByRid(R.id.play, this);
        setImageButOnclickListenerByRid(R.id.stop, this);
        setImageButOnclickListenerByRid(R.id.pause, this);
        setImageButOnclickListenerByRid(R.id.showDir, this);
        setImageButOnTouchListener(R.id.play,this);
        //首先申请存储访问权限  音视频文件都在sd卡内
        if (hasNoPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requirePermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            //初始化播放器
            initMediaPlayer();
        }
    }

    private void initMediaPlayer() {
        try {
            File file = new File("/external/audio/media","1332.mp3");
            toastSimple(file.canRead()+"");
            mediaPlayer.setDataSource(this,getSystemDefultRingtoneUri());
          //获取系统默认铃声
//            MediaPlayer.create(this,getSystemDefultRingtoneUri());
            //media进入准备状态
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            toastSimple("initMediaPlayer failed "+e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case 1:
                if(grantSucc(grantResults)){
                    initMediaPlayer();
                }else{
                    toastSimple("拒绝权限将无法使用该程序");
                    finish();
                }
                break;
            default:
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                    toastSimple("start play mediaPlayer.isPlaying()="+mediaPlayer.isPlaying());
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                    toastSimple("pause play now");

                }else{
                    toastSimple("not playing now");
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()) {
                    //停止播放
                    mediaPlayer.reset();
                    initMediaPlayer();
                    play.setImageResource(R.drawable.play);
                    toastSimple("stop play now");
                }else{
                    toastSimple("not playing now");
                }
                break;
            case R.id.showDir:
                RingtoneManager ringtoneManager = new RingtoneManager(this); // 铃声管理器
                Cursor cursor = ringtoneManager.getCursor(); //获取铃声表,根据表名取值
                List<Ring> list=new ArrayList<>();
                while (cursor != null && cursor.moveToNext()) {
                    Ring ring=new Ring();
                    ring.setName(cursor.getString(cursor.getColumnIndex("title")));
                    ring.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    list.add(ring);
                }

                toastSimple(Environment.getExternalStorageDirectory().getPath()+" \n"+getSystemDefultRingtoneUri().getPath());
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       /* switch (v.getId()){
            case R.id.play:
                if( event.getAction() == MotionEvent.ACTION_DOWN) {
                    play.setImageResource(R.drawable.pause);
                } else {
                    play.setImageResource(R.drawable.play);
                }
                break;
            default:
                break;
        }*/


        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
