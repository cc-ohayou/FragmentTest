package space.cc.com.fragmenttest.listener;

public interface DownLoadListener {
  /**
     * @description 通知当前的下载进度
     * @author CF
     * created at 2018/12/3/003  23:19
     */
    void onProgress(int progress);
  /**
     * @description 下载成功的回调 可以进行安装或者通知提醒
     * @author CF
     * created at 2018/12/3/003  23:19
     */
    void onSuccess();
  /**
     * @description 下载失败事件通知
     * @author CF
     * created at 2018/12/3/003  23:19
     */
    void onFailed();

    void onPaused();

    void onCanceled();
}
