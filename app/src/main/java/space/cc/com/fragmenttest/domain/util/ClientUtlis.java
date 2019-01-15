package space.cc.com.fragmenttest.domain.util;

import android.app.Activity;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpHeaders;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import space.cc.com.fragmenttest.MyApplication;
import space.cc.com.fragmenttest.domain.ClientConfiguration;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;

/**
 * @author  CF
 * @date   2018/12/29
 * @description
 *
 */
public class ClientUtlis<Y> {





   /**
    * @author  CF
    * @date   2018/12/29
    * @description
    *
    */
    public  List<Y> getBizList(RequestParams params, UrlConfig url, Object tag, final String errMsg, final List<Y> defaultList) {
        final List<Y>[] listResult = null;
        ClientUtlis.post(true, url, params,
                tag, new JsonCallback<List<Y>>() {
                    @Override
                    public void onSuccess(List<Y> list, String msg) {
                        if (!list.isEmpty()) {
                            listResult[0] = list;
                        } else {
                            listResult[0] = defaultList;
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Log.i("", "获取列表失败，reason:" + msg);
                        ToastUtils.showDisplay(errMsg);
                    }
                });
        return listResult[0];
    }

/**
 * @author  CF
 * @date   2018/12/29
 * @description
 *
 */
    public static <Y> Y getSingleBiz(RequestParams params, UrlConfig url, Object obj, final String errMsg, final Y defaultBiz) {
        final Y[] result = null;
        ClientUtlis.post(true, url, params,
                obj, new JsonCallback<Y>() {
                    @Override
                    public void onSuccess(Y res, String msg) {
                        if (res != null) {
                            result[0] = res;
                        } else {
                            result[0] = defaultBiz;
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Log.i("", "获取列表失败，reason:" + msg);
                        ToastUtils.showDisplay(errMsg);
                    }
                });
        return result[0];
    }


    public static void post(boolean isLoop, String url, RequestParams params, Object tag, AbsCallback callback) {

        if (NetworkUtils.isConnected()) {
            MyApplication.canRequest = true;
        } else {
            if (isLoop) {
                if (MyApplication.canRequest) {
                    ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                    MyApplication.canRequest = false;
                }
            } else {
                ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                MyApplication.canRequest = false;
            }
            return;
        }

        if (MyApplication.isConnected) return;

        OkGo.post(url)
                .tag(tag)
                .upString(params.getParamsString(), MediaType.parse("application/x-www-form-urlencoded"))
                .execute(callback);
    }

  /**
     * @description
     * @author CF
     * created at 2019/1/15/015  23:27
     */
    public static void post(boolean isLoop, UrlConfig url, RequestParams params, Object tag, AbsCallback callback) {


        if(url.isNeedSession()){
            String sid=ClientConfiguration.getInstance().getSid();
        }
        OkGo.getInstance().getCommonHeaders().put("sid",ClientConfiguration.getInstance().getSid() );
        OkGo.getInstance().getCommonHeaders().put("userid", ClientConfiguration.getInstance().getUid());

        if (NetworkUtils.isConnected()) {
            MyApplication.canRequest = true;
        } else {
            if (isLoop) {
                if (MyApplication.canRequest) {
                    ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                    MyApplication.canRequest = false;
                }
            } else {
                ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                MyApplication.canRequest = false;
            }
            return;
        }

        if (MyApplication.isConnected) return;
        HttpHeaders headers= OkGo.getInstance().getCommonHeaders();
        OkGo.post(url.getValue())
                .tag(tag)
                .upString(params.getParamsString(), MediaType.parse("application/x-www-form-urlencoded"))
                .execute(callback);
    }


    public static void post(Activity activity, String url, RequestParams params, Object tag, AbsCallback callback) {
        if (NetworkUtils.isConnected()) {
            MyApplication.canRequest = true;
        } else {
            ToastUtils.showDisplay("当前网络较差，请检查网络设置");
            MyApplication.canRequest = false;
            return;
        }

        if (MyApplication.isConnected) return;

        OkGo.post(url)
                .tag(tag)
                .upString(params.getParamsString(), MediaType.parse("application/x-www-form-urlencoded"))
                .execute(callback);
    }

    public static void get(Activity activity, String url, Object tag, AbsCallback callback) {
        if (NetworkUtils.isConnected()) {
            MyApplication.canRequest = true;
        } else {
            if (MyApplication.canRequest) {
                ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                MyApplication.canRequest = false;
            }
            return;
        }

        if (MyApplication.isConnected) return;

        OkGo.get(url)
                .tag(tag)
                .execute(callback);
    }


    public static void uploadFiles(Activity activity, String url, RequestParams params, String fileKey, List<File> files, Object tag, AbsCallback callback) {
        if (NetworkUtils.isConnected()) {
            MyApplication.canRequest = true;

        } else {
            ToastUtils.showDisplay("当前网络较差，请检查网络设置");
            MyApplication.canRequest = false;
            return;
        }

        if (MyApplication.isConnected) return;

        OkGo.post(url)
                .tag(tag)
                .addFileParams(fileKey, files)
                .execute(callback);
    }


}
