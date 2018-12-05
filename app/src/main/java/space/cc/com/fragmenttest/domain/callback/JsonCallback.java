/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package space.cc.com.fragmenttest.domain.callback;

import com.alibaba.fastjson.JSON;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
import space.cc.com.fragmenttest.domain.BaseResponse;
import space.cc.com.fragmenttest.domain.MyPublicParams;
import space.cc.com.fragmenttest.domain.util.Convert;
import space.cc.com.fragmenttest.domain.util.EncryptUtils;
import space.cc.com.fragmenttest.domain.util.TimeUtils;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;
    private BaseResponse baseResponse;
    public JsonCallback() {
    }
    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
        request.headers("header1", "HeaderValue1")//
                .params("params1", "ParamsValue1")//
                .params("token", "3215sdf13ad1f65asd4f3ads1f");

        MyPublicParams.onRefresh();
        String now = TimeUtils.getNowMills() + "";
        request.headers("Time", now);
        request.headers("systemtype", "ddy");
        request.headers("SignVersion", "1");//签名算法版本
        request.headers("Sign", EncryptUtils.encryptMD5ToString(now + "moc.89ddy"));//签名算法版本
//        request.headers("PushClientId", PushManager.getInstance().getClientid(Utils.getApp()));//cid
        request.headers("UserId", MyPublicParams.v_userId);
        request.headers("Token", MyPublicParams.v_sid);
        request.headers("sid", MyPublicParams.v_sid);
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

       /* if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        T t =convert.convertResponse(response);*/
       //只针对平常请求 回调这样转换
       String str= new String (response.body().bytes());
        try {
            baseResponse = JSON.parseObject(str, BaseResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) baseResponse;
    }

    @Override
    public void onCacheSuccess(com.lzy.okgo.model.Response<T> response) {
        super.onCacheSuccess(response);
    }



    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    public void uploadProgress(Progress progress) {
        super.uploadProgress(progress);
    }

    @Override
    public void downloadProgress(Progress progress) {
        super.downloadProgress(progress);
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {

        baseResponse= (BaseResponse) response.body();

        if (baseResponse == null) {
            onError("无数据", 00000000);
            return;
        }
        if (baseResponse == null) {
            onError("无数据", 00000000);
            return;
        }

        T data = null;
        if (baseResponse.getCode() == 0) {
            if (baseResponse.getData() == null) {
                data = (T) "";
                onSuccess(data, baseResponse.getMsg());
                return;
            }
            if (!baseResponse.getData().contains(":") && !baseResponse.getData().contains("[")) {
                data = (T) baseResponse.getData();
                onSuccess(data, baseResponse.getMsg());
                return;
            }
            //数组特殊处理直接强制转换
            if (!baseResponse.getData().contains("[") && !baseResponse.getData().contains("{")) {
                data = (T) baseResponse.getData();
                onSuccess(data, baseResponse.getMsg());
                return;
            }
            if (type == null) {
                if (clazz == null) {
                    Type genType = getClass().getGenericSuperclass();
                    type = ((ParameterizedType) genType).getActualTypeArguments()[0];
                } else {
                    try {
                        data = JSON.parseObject(baseResponse.getData(), clazz);
                        onSuccess(data, baseResponse.getMsg());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }
            if (type != null) {
                try {
                    data = JSON.parseObject(baseResponse.getData(), type);
                    onSuccess(data, baseResponse.getMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else if (baseResponse.getCode() == 900004) {
            onVersionControl(baseResponse.getMsg(), baseResponse.getData());
        } else if (baseResponse.getCode() == 3001) {
            onLoginError(baseResponse.getMsg(), baseResponse.getCode());
        } else
            onError(baseResponse.getMsg(), baseResponse.getCode());
    }

    public void onSuccess(T t, String msg) {
    }

    public void onError(String msg, int code) {
    }

    public void onLoginError(String msg, int code) {
    }

    public void onVersionControl(String msg, String data) {
    }
}
