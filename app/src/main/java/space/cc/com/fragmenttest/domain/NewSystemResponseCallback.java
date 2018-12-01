package space.cc.com.fragmenttest.domain;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.BaseRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;
import space.cc.com.fragmenttest.domain.util.EncryptUtils;
import space.cc.com.fragmenttest.domain.util.TimeUtils;



/**
 * Created by xuwenzheng on 2018/4/24 17:16
 */
public abstract class NewSystemResponseCallback<T> extends AbsCallback<String> {

    private Type type;
    private Class<T> clazz;
    private BaseResponse baseResponse;

    public NewSystemResponseCallback() {
    }

    public NewSystemResponseCallback(Type type) {
        this.type = type;
    }

    public NewSystemResponseCallback(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
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

    @Override
    public void onSuccess(String s, Call call, Response response) {
        //可以在这里添加报文解密

        //统一处理flag
        try {
            baseResponse = JSON.parseObject(s, BaseResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public String convertSuccess(Response response) throws Exception {
        String s = StringConvert.create().convertSuccess(response);
        response.close();
        return s;
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