package space.cc.com.fragmenttest.listener;


import android.util.Log;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;

public class MyDownLoadListener extends DownloadListener {
  private static final String TAG = "MyDownLoadListener";
  public MyDownLoadListener(Object tag) {
    super(tag);
  }

  @Override
  public void onStart(Progress progress) {
    Log.d(TAG, "onStart: "+progress);
  }

  @Override
  public void onProgress(Progress progress) {
    Log.d(TAG, "onProgress: "+progress);
    publishProgress(progress.fraction);
  }
  /**
     * @description 刷新前台服务的进度条
     * @author CF
     * created at 2018/12/6/006  0:28
     */
  private void publishProgress(float fraction) {

  }

  @Override
  public void onError(Progress progress) {
    Log.d(TAG, "onError: "+progress);
    progress.exception.printStackTrace();
  }

  @Override
  public void onFinish(File file, Progress progress) {
    Log.d(TAG, "onFinish: "+progress);
  }

  @Override
  public void onRemove(Progress progress) {
    Log.d(TAG, "onRemove: "+progress);
  }
}
