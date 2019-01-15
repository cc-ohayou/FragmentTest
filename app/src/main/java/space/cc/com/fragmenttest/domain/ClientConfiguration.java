package space.cc.com.fragmenttest.domain;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

import space.cc.com.fragmenttest.MyApplication;
import space.cc.com.fragmenttest.domain.util.StringUtils;

/**
 * 客户端配置
 * Created by yaojian on 2017/9/18 16:08
 */

public class ClientConfiguration {

    public static final String PREFERENCE_NAME = "cc_config";
    private volatile static SharedPreferences mSharedPreferences;
    private static ClientConfiguration clientConfiguration;
    private static SharedPreferences.Editor editor;

    /**
     * 登录账号
     */
    private String SHARED_LOGIN_ACCOUNT = "shared_login_account";
    /**
     * 登录密码
     */
    private String SHARED_LOGIN_PASSWORD = "shared_login_password";
    private String SHARED_SID = "shared_sid";
    private String SHARED_UID = "shared_uid";
    private String order_auth="OrderAuth";
    /**
     * 登录状态
     */
    private String SHARED_LOGIN_STATE = "shared_login_state";


    /**
     * 记录的版本号
     */
    private String SHARED_VERSION = "shared_version";
    /**
     * user数据
     */
    private String SHARED_USERINFO = "shared_userinfo";

    private String MsgPullPeriod = "msg_pull_period";

    private ClientConfiguration() {
        mSharedPreferences = MyApplication.getAppContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static ClientConfiguration getInstance() {
        if (clientConfiguration == null) {
            synchronized (ClientConfiguration.class) {
                if (clientConfiguration == null) {
                    clientConfiguration = new ClientConfiguration();
                    editor = mSharedPreferences.edit();
                }
            }
        }
        return clientConfiguration;
    }



    public int getMsgPullPeriod() {
        int second = mSharedPreferences.getInt(MsgPullPeriod, 300);
        return second;
    }

    public void setMsgPullPeriod(int msgPullPeriod) {
        editor.putInt(MsgPullPeriod, msgPullPeriod);
        editor.commit();
    }

    public void setLoginAccount(String account) {
        editor.putString(SHARED_LOGIN_ACCOUNT, ConcealUtils.encrypt(account));
        editor.commit();
    }

    public String getLoginAccount() {
        String str = mSharedPreferences.getString(SHARED_LOGIN_ACCOUNT, "");
        return ConcealUtils.decrypt(str);
    }

    public void setLoginPassword(String password) {
        editor.putString(SHARED_LOGIN_PASSWORD, ConcealUtils.encrypt(password));
        editor.commit();
    }

    public String getLoginPassword() {
        String str = mSharedPreferences.getString(SHARED_LOGIN_PASSWORD, "");
        return StringUtils.isEmpty(str) ? str : ConcealUtils.decrypt(str);
    }

    public void setSid(String sid) {
        editor.putString(SHARED_SID, ConcealUtils.encrypt(sid));
        editor.commit();
    }
    public String getSid() {
        String str = mSharedPreferences.getString(SHARED_SID, "");
        return StringUtils.isEmpty(str) ? str : ConcealUtils.decrypt(str);
    }

    public void setUid(String uid) {
        editor.putString(SHARED_UID, ConcealUtils.encrypt(uid));
        editor.commit();
    }

    public String getUid() {
        String str = mSharedPreferences.getString(SHARED_UID, "");
        return StringUtils.isEmpty(str) ? str : ConcealUtils.decrypt(str);
    }


    public void setLoginState(boolean state) {
        editor.putBoolean(SHARED_LOGIN_STATE, state);
        editor.commit();
    }

    public boolean getLoginState() {
        return mSharedPreferences.getBoolean(SHARED_LOGIN_STATE, false);
    }






    public void setVersion(String version) {
        editor.putString(SHARED_VERSION, version);
        editor.commit();
    }

    public String getVersion() {
        return mSharedPreferences.getString(SHARED_VERSION, "1.0.0");
    }

    public void setUserInfo(String account) {
        editor.putString(SHARED_USERINFO, ConcealUtils.encrypt(account));
        editor.commit();
    }

    public String getUserInfo() {
        String str = mSharedPreferences.getString(SHARED_USERINFO, "");

        return ConcealUtils.decrypt(str);
    }





    /**最后一次提醒日 */
    private String REMIND_THISDAY = "remind_thisday";

    /**
     * 检查是否应该提醒更新
     */
    public boolean isShowThisDay(){
        Calendar cal=Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m=cal.get(Calendar.MONTH);
        int d=cal.get(Calendar.DATE);

        String str = mSharedPreferences.getString(REMIND_THISDAY, "");
        if (!StringUtils.isEmpty(str)&&str.equals(y+""+m+""+d)){
            return false;//不提醒 因为今日已经提醒过了
        }
        setRemindDay();//保留一下
        return true;
    }
    public  void setRemindDay(){
        Calendar cal=Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m=cal.get(Calendar.MONTH);
        int d=cal.get(Calendar.DATE);

        editor.putString(REMIND_THISDAY, y+""+m+""+d);
        editor.commit();
    }



}
