package space.cc.com.fragmenttest;

import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import androidx.multidex.MultiDexApplication;
import space.cc.com.fragmenttest.domain.ClientConfiguration;
import space.cc.com.fragmenttest.domain.GlobalSettings;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.bizobject.CustomProperties;
import space.cc.com.fragmenttest.domain.bizobject.UserInfo;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.domain.util.Utils;

public class MyApplication extends MultiDexApplication {
    private static final String TAG = "MyApplication";
   private static Context context;
    public static boolean canRequest=true;
    public static boolean isConnected;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initOkGo();
        Utils.init(this);
        Log.d(TAG, "MyApplication onCreate ok ");
//        LitePalApplication.initialize(context);
        LitePal.initialize(this);
        //数据库初始化准备
        Connector.getDatabase();
        Log.d(TAG, "LitePal.initialize( ok ");

        /**
         * 百度map sdk使用初始化
         */
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
//
        initCustomProperties();

    }



    private void initCustomProperties() {
        RequestParams params=new RequestParams(RequestParams.PARAM_TYPE_FORM);
        ClientUtlis.post(true, UrlConfig.CUSTOM_PROPERTIES,params,
                this,new JsonCallback<CustomProperties>() {
                    @Override
                    public void onSuccess(CustomProperties customProperties, String msg) {
                        ClientConfiguration.getInstance().setCustomProperties(customProperties);
                        if(customProperties!=null){
                            GlobalSettings.settingProperties = ClientConfiguration.getInstance().getCustomProperties();
                            if(CustomProperties.UPDATE.equals( GlobalSettings.settingProperties.getUpdateSign())){
                                ToastUtils.showLong("软件有更新哦，可在右上角菜单中自行更新");
                            }
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        ToastUtils.showDisplay(msg);
                    }
                });
    }
      /**
         * @description 提供一个全局的获取context的方法
       * 应为很多地方用到Context但却不方便获取
       * 需要在manifes文件中顶一下
         * @author CF
         * created at 2018/11/29/029  22:53
         */
    public static Context  getContext (){
        return context;
    }


    /** OkGo初始化 */
    private void initOkGo(){
        //配置统一固定头部
        HttpHeaders headers = new HttpHeaders();
//        headers.put("ClientType", DeviceInfo.mMobileType);    //header不支持中文，不允许有特殊字符
//        headers.put("AppIdentifier", BuildConfig.APPLICATION_ID);
        headers.put("AppIdentifier", "com.ddtg");
        headers.put("Address", "");
        headers.put("Longitude", "");
        headers.put("Latitude", "");
    /*    headers.put("SoftVersion", DeviceInfo.mAppVer);
        headers.put("SystemVersion", DeviceInfo.mSysVer);
        headers.put("DeviceId", DeviceInfo.mDeviceId);
        headers.put("PhoneVersion", DeviceInfo.mPhoneModel);*/

        //OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //OkGo必须调用初始化
        OkGo.getInstance().init(this);
        OkGo.getInstance()
                //.setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                //.setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers);
    }
}
