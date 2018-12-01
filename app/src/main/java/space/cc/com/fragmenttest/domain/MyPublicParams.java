package space.cc.com.fragmenttest.domain;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import space.cc.com.fragmenttest.domain.location.MyBDLocationInfo;
import space.cc.com.fragmenttest.domain.util.XMd5;

/**
 * 通用参数设置
 */
public class MyPublicParams {

    /**	1:浏览器 2:iOS 3:Android */
    public static String c_clientType = "cClientType";
    /**	地址 */
    public static String c_address = "cAddress";
    /**	经度 */
    public static String c_longitude = "cLongitude";
    /**	纬度 */
    public static String c_latitude = "cLatitude";
    /**	软件版本号	"1.0"	*/
    public static String c_softVersion = "cSoftVersion";
    /**	手机系统版号	"4.2.2"	*/
    public static String c_systemVersion = "cSystemVersion";
    /**	手机型号	MI 2	*/
    public static String c_phoneVersion = "cPhoneVersion";
    /**	客户端时间 */
    public static String c_time = "cTime";
    /**	签名算法版本 “1”，“2”，“3”，“4” */
    public static String c_signVersion = "cSignVersion";
    /**	签名 “1” ： 计算MD5(c_time+"moc.89ddy")值同c_sign比较，若相等则校验成功 */
    public static String c_sign = "cSign";
    /**	设备id */
    public static String c_deviceId = "cDeviceId";
    /**	用户id */
    public static String c_userId = "cUserId";
    /**	会话id	*/
    public static String c_sid = "cSid";
    /**	渠道	*/
    public static String c_channel = "cChannel";

    /**	1:浏览器 2:iOS 3:Android */
    public static String v_clientType = "3";
    /**	地址 */
    public static String v_address = "";
    /**	经度 */
    public static String v_longitude = "";
    /**	纬度 */
    public static String v_latitude = "";
    /**	软件版本号	"1.0"	*/
    public static String v_softVersion = "";
    /**	手机系统版号	"4.2.2"	*/
    public static String v_systemVersion = "";
    /**	手机型号	MI 2	*/
    public static String v_phoneVersion = "";
    /**	客户端时间 */
    public static String v_time = "";
    /**	签名算法版本 “1”，“2”，“3”，“4” */
    public static String v_signVersion = "";
    /**	签名 “1” ： 计算MD5(c_time+"moc.89ddy")值同c_sign比较，若相等则校验成功 */
    public static String v_sign = "";
    /**	设备id */
    public static String v_deviceId = "";
    /**	用户id */
    public static String v_userId = "";
    /**	会话id	*/
    public static String v_sid = "";
    /**	渠道	*/
    public static String v_channel = "";

    /**刷新数据*/
    public static void onRefresh() {
        // System
        MyPublicParams.v_clientType = "3";
        MyPublicParams.v_systemVersion = XPhoneInfo.mStrXiTongBanBenHao;
        MyPublicParams.v_softVersion = XPhoneInfo.mStrRuanJianBanBenHao;
        MyPublicParams.v_deviceId = XPhoneInfo.mStrDeviceId;
        MyPublicParams.v_phoneVersion = XPhoneInfo.mStrShouJiXingHao;
//        MyPublicParams.v_channel = BuildConfig.FLAVOR;
//        MyPublicParams.v_channel = MyApplication.getChannal();

                // Location
        MyPublicParams.v_address = MyBDLocationInfo.mStrAddress;
        MyPublicParams.v_longitude = MyBDLocationInfo.mLongitude + "";
        MyPublicParams.v_latitude = MyBDLocationInfo.mLatitude + "";

        // Sign
        MyPublicParams.v_time = "" + System.currentTimeMillis();
        MyPublicParams.v_signVersion = "1";
        MyPublicParams.v_sign = XMd5.encode(v_time + "moc.89ddy");

        // User
//        MyPublicParams.v_userId = MyApplication.getUid();
//        MyPublicParams.v_sid = MyApplication.getSid();

    }

    /**获取参数键值对*/
    public static Map<String, Object> getMapParams(){
        MyPublicParams.onRefresh();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MyPublicParams.c_clientType, MyPublicParams.v_clientType);
        map.put(MyPublicParams.c_systemVersion, MyPublicParams.v_systemVersion);
        map.put(MyPublicParams.c_softVersion, MyPublicParams.v_softVersion);
        map.put(MyPublicParams.c_phoneVersion, MyPublicParams.v_phoneVersion);
        map.put(MyPublicParams.c_deviceId, MyPublicParams.v_deviceId);
        map.put(MyPublicParams.c_address, MyPublicParams.v_address);
        map.put(MyPublicParams.c_longitude, MyPublicParams.v_longitude);
        map.put(MyPublicParams.c_latitude, MyPublicParams.v_latitude);
        map.put(MyPublicParams.c_userId, MyPublicParams.v_userId);
        map.put(MyPublicParams.c_sid, MyPublicParams.v_sid);
        map.put(MyPublicParams.c_time, MyPublicParams.v_time);
        map.put(MyPublicParams.c_signVersion, MyPublicParams.v_signVersion);
        map.put(MyPublicParams.c_sign, MyPublicParams.v_sign);
        map.put(MyPublicParams.c_channel, MyPublicParams.v_channel);
        return map ;
    }

    public static void getLog(){
        Log.d("公共请求头", "{ cClientType = " + v_clientType
                + ",cSystemVersion = " + v_systemVersion
                + ",cSoftVersion = " + v_softVersion
                + ",cPhoneVersion = " + v_phoneVersion
                + ",cDeviceId = " + v_deviceId
                + ",cAddress = " + v_address
                + ",cLongitude = " + v_longitude
                + ",cLatitude = " + v_latitude
                + ",cUserId = " + v_userId
                + ",cSid = " + v_sid
                + ",cTime = " + v_time
                + ",cSignVersion = " + v_signVersion
                + ",cChannel = " + v_channel
                + ",cSign = " + v_sign + " }");
    }
}
