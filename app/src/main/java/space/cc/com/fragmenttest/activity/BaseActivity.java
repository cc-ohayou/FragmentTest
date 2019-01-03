package space.cc.com.fragmenttest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import space.cc.com.fragmenttest.activity.media.VideoTestActivity;
import space.cc.com.fragmenttest.broadcast.ForceOffLineReceiver;
import space.cc.com.fragmenttest.broadcast.MyBroadCast;
import space.cc.com.fragmenttest.domain.util.ActivityCollector;
import space.cc.com.fragmenttest.domain.util.CloseUtils;
import space.cc.com.fragmenttest.domain.util.StringUtil;
import space.cc.com.fragmenttest.domain.util.ToastUtils;

public  abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final Map EMPTY_MAP = new HashMap();
    private ForceOffLineReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "taskId is: " + getTaskId());
        Log.d(TAG, "create and add to collector activity is " + getClass().getSimpleName());
// 设置背景透明色
// this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//        this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//        首先要获取权限
        //设置背景为墙纸
//        requestPermission();
//        PermissinUtils.requestStoragePermission(Utils.getApp(),BaseActivity.this);

        //隐藏默认的actionBar
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

//        this.getWindow().setBackgroundDrawable(res);
        ActivityCollector.addActivity(this);

    }

    public abstract void requestPermission();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:fdgdf ");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyBroadCast.OFFLINE_BROADCAST);
        receiver = new ForceOffLineReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }


    /**
     * @author CF
     * created at 2018/10/21/021  21:42
     */
    public static void startAction(Context context, Map<String, String> map, Class<?> c) {
        Log.i(TAG, "startAction:context is :" + context.getClass().getSimpleName()
                + ",targetActivity is:" + c.getSimpleName());
        Intent intent = new Intent(context, c);
        if (isNotEmpty(map)) {
            Set<Map.Entry<String, String>> set = map.entrySet();
            for (Map.Entry<String, String> entry : set) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        context.startActivity(intent);
    }

    static boolean isNotEmpty(Map map) {
        return map != null && !map.isEmpty();
    }

    /**
     * @author CF
     * created at 2018/10/21/021  21:42
     */
    public void startAction(Context context, String bundlKkey, Bundle bundle) {
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra(bundlKkey, bundle);
        context.startActivity(intent);
    }

    void save(String inputText, String file) {
        if (!StringUtil.isEmpty(inputText)) {

        }
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            //写入文件默认目录  Android/Data/应用包名/files
            // Context.MODE_PRIVATE) 源文件存在则覆盖原文件模式
            // Context.MODE_APPEND) 源文件存在则追加内容到原文件模式
            out = openFileOutput(file, Context.MODE_PRIVATE);
            //字节流转换为字符流
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(writer);
        }

    }


    String load(String file) {
        FileInputStream in = null;
        BufferedReader bufferReader = null;
        StringBuilder content = new StringBuilder();
        try {
            in=openFileInput(file);
            bufferReader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = bufferReader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(bufferReader);
        }
        return content.toString();
    }

  /**
     * @description
     * @author CF
     * created at 2018/11/18/018  15:12
     */
    public  void setButOnclickListenerByRid(int id, View.OnClickListener listener) {
        Button addBut=findViewById(id);
        addBut.setOnClickListener(listener);
    }

    public  void setImageButOnclickListenerByRid(int id, View.OnClickListener listener) {
        ImageButton but=findViewById(id);
        but.setOnClickListener(listener);
    }
  /**
     * @description
     * @author CF
     * created at 2018/11/27/027  23:21
     */
    public  void setImageButOnTouchListener(int id, View.OnTouchListener listener) {
        ImageButton but=findViewById(id);
        but.setOnTouchListener(listener);
    }
    public  void setButOnTouchListener(int id, View.OnTouchListener listener) {
        Button addBut=findViewById(id);
        addBut.setOnTouchListener(listener);
    }

     int getViewIemWidth(int divide,Context context) {
        //获取屏幕高度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int mScreenWidth = dm.widthPixels;
        return (mScreenWidth)/divide;
    }

     int getViewIemHeight(int divide ,Context context) {
        //获取屏幕高度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeight = dm.heightPixels;//屏幕高度
        return (mScreenHeight)/divide;
    }



    public void toastSimple(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }




    public boolean hasNoPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context,permission )!= PackageManager.PERMISSION_GRANTED;
    }

    public void requirePermission(VideoTestActivity videoTestActivity, String ... permmissions) {
        ActivityCompat.requestPermissions(videoTestActivity,permmissions,1);
    }

    public boolean grantSucc(@NonNull int[] grantResults) {
        return grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED;
    }

    public Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }


  /**
     * @description  处理权限请求回调
     * @author CF
     * created at 2018/12/6/006  23:35
     */
    void dealRequestResult(int requestCode,int[] grantResults) {
//        ToastUtils.showDisplay(requestCode+",res="+grantResults.toString());
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!= PackageManager.PERMISSION_GRANTED
                        ){
                    ToastUtils.showDisplay("拒绝权限将无法使用程序");
                    finish();
                }
                break;
            default:
                break;
        }
    }


     void startServiceCustom(Context context, Intent startIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, startIntent);
        } else {
            this.startService(startIntent);
        }
    }

}

