package space.cc.com.fragmenttest.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.lzy.okserver.OkDownload;
import com.lzy.okserver.task.XExecutor;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.util.SDCardUtils;
import space.cc.com.fragmenttest.domain.util.StringUtils;
import space.cc.com.fragmenttest.domain.util.ToastUtils;


public class DownLoadActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener{

    private OkDownload okDownload;
    public static String path;
    static{
        path = SDCardUtils.getSDCardPath();
        if(StringUtils.isEmpty(path)){
            ToastUtils.showShort("无存储空间");

        }
        path +="/cc/download";
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ok_download_test);
            okDownload=OkDownload.getInstance();
            okDownload.setFolder(path);
            //设置同时下载数量
            okDownload.getThreadPool().setCorePoolSize(3);
            okDownload.addOnAllTaskEndListener(this);

    }

    @Override
    public void requestPermission() {

    }

    @Override
    public void onAllTaskEnd() {
         toastSimple("所有下载任务已结束");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除所有任务监听
        okDownload.removeOnAllTaskEndListener(this);
    }
}
