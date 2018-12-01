package space.cc.com.fragmenttest.domain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import space.cc.com.fragmenttest.domain.util.StringUtil;

@TargetApi(11)
public class XPhoneInfo {
    /** 页面Log_Title */
    private static String PAGETAG = "XPhoneInfo";

    /** 状态栏高度 */
    public static int mIntZhuangTaiLanGaoDu = -1;

    /** 屏幕宽度 */
    public static int mIntKuangDu = -1;

    /** 屏幕高度 */
    public static int mIntGaoDu = -1;

    /** 系统版本号 */
    public static String mStrXiTongBanBenHao = "";

    /** 手机型号 */
    public static String mStrShouJiXingHao = "";

    /** 本地软件版本号 */
    public static String mStrRuanJianBanBenHao = "";

    /** 屏幕分辨率 */
    public static String mStrFenBianLv = "";

    /** 手机设备ID */
    public static String mStrDeviceId = "100000000000000";

    /** 手机类型，0:ios，1:android	 */
    public static String mIntMobileType = "1";

    /** 获取手机信息 */
    public static void getMyPhoneInfo(Context context, Activity activity) {
        if (!StringUtil.isEmpty(mStrShouJiXingHao)) {
            return ;
        }
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        XPhoneInfo.mIntZhuangTaiLanGaoDu = rect.top;// 状态栏高度

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        XPhoneInfo.mIntKuangDu = wm.getDefaultDisplay().getWidth();// 屏幕宽度
        XPhoneInfo.mIntGaoDu = wm.getDefaultDisplay().getHeight();// 屏幕高度

        XPhoneInfo.mStrXiTongBanBenHao = android.os.Build.VERSION.RELEASE;// 系统版本号
        XPhoneInfo.mStrShouJiXingHao = android.os.Build.MODEL;
        XPhoneInfo.mStrRuanJianBanBenHao = getAppVersionName(context);// 本地软件版本号
        // 屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        XPhoneInfo.mStrFenBianLv = dm.heightPixels * dm.widthPixels + "";// 屏幕分辨率

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        //仅能拿到有效设备号才更新，否则用默认设备号
        if (!StringUtil.isEmpty(tm.getDeviceId())){
            XPhoneInfo.mStrDeviceId = tm.getDeviceId();
        }
        Log.d(PAGETAG, "XPhoneInfo = " + "手机信息" +
                ": 状态栏高度 = " + XPhoneInfo.mIntZhuangTaiLanGaoDu +
                "、屏幕宽度 = " + XPhoneInfo.mIntKuangDu +
                "、屏幕高度 = " + XPhoneInfo.mIntGaoDu +
                "、系统版本号 = " + XPhoneInfo.mStrXiTongBanBenHao +
                "、手机型号 = " + XPhoneInfo.mStrShouJiXingHao +
                "、本地软件版本号= " + XPhoneInfo.mStrRuanJianBanBenHao +
                "、屏幕分辨率= " + XPhoneInfo.mStrFenBianLv +
                "、手机设备ID= " + XPhoneInfo.mStrDeviceId + ";") ;
    }

    /** 软件版本号 */
    private static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取本机IP
     * @return 本机IP
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    String tempIP = inetAddress.getHostAddress().toString();
                    Log.d("WifiPreference IpAddress", "tempIP = {" + tempIP + "} ;");
                    if (!inetAddress.isLoopbackAddress()) {
                        String mIP = inetAddress.getHostAddress().toString();
                        if (!mIP.contains("::")) {
                            return mIP;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机MAC
     *
     * @return 本机MAC
     */
    public static String getLocalMacAddress(Context mContext) {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /** 显示内存 */
    public static ActivityManager.MemoryInfo getDisplayBriefMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        Log.d(PAGETAG, "系统剩余内存:" + (info.availMem >> 10) + "k");
        Log.d(PAGETAG, "系统是否处于低内存运行：" + info.lowMemory);
        Log.d(PAGETAG, "当系统剩余内存低于" + info.threshold + "时就看成低内存运行");
        return info;
    }

    /** 回收变量 */
    public void myHuiShou() {

        /** 状态栏高度 */
        mIntZhuangTaiLanGaoDu = -1;

        /** 屏幕宽度 */
        mIntKuangDu = -1;

        /** 屏幕高度 */
        mIntGaoDu = -1;

        /** 系统版本号 */
        mStrXiTongBanBenHao = "";

        /** 本地软件版本号 */
        mStrRuanJianBanBenHao = "";

        /** 屏幕分辨率 */
        mStrFenBianLv = "";

        /** 手机设备ID */
        mStrDeviceId = "";
    }
}
