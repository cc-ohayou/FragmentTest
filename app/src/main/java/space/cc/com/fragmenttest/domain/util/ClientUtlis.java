package space.cc.com.fragmenttest.domain.util;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import space.cc.com.fragmenttest.MyApplication;
import space.cc.com.fragmenttest.domain.RequestParams;

/**
 * Client工具
 *
 * Created by yaojian on 2017/10/11 17:55
 */

public class ClientUtlis {

    public static void post(boolean isLoop, String url, RequestParams params, Object tag, AbsCallback callback){
        if(NetworkUtils.isConnected()){
            MyApplication.canRequest = true;
        }else{
            if(isLoop){
                if(MyApplication.canRequest){
                    ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                    MyApplication.canRequest = false;
                }
            }else{
                ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                MyApplication.canRequest = false;
            }
            return;
        }

        if(MyApplication.isConnected) return;

        OkGo.post(url)
                .tag(tag)
                .upString(params.getParamsString(), MediaType.parse("application/x-www-form-urlencoded"))
                .execute(callback);
    }

    public static void post(Activity activity, String url, RequestParams params, Object tag, AbsCallback callback){
        if(NetworkUtils.isConnected()){
            MyApplication.canRequest = true;
        }else{
            ToastUtils.showDisplay("当前网络较差，请检查网络设置");
            MyApplication.canRequest = false;
            return;
        }

        if(MyApplication.isConnected) return;

        OkGo.post(url)
                .tag(tag)
                .upString(params.getParamsString(), MediaType.parse("application/x-www-form-urlencoded"))
                .execute(callback);
    }

    public static void get(Activity activity, String url, Object tag, AbsCallback callback){
        if(NetworkUtils.isConnected()){
            MyApplication.canRequest = true;
        }else{
            if(MyApplication.canRequest){
                ToastUtils.showDisplay("当前网络较差，请检查网络设置");
                MyApplication.canRequest = false;
            }
            return;
        }

        if(MyApplication.isConnected) return;

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
