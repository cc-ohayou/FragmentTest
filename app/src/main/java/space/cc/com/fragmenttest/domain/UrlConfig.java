package space.cc.com.fragmenttest.domain;

public enum UrlConfig {
    TEST_URL(UrlConstants.PREFIX+"v1/cc/get/userList","测试url"),
    TEST_DOWNLOAD(UrlConstants.PREFIX+"v1/cc/getDownloadUrl","测试下载url"),
    DOWN_LOAD04(UrlConstants.PREFIX04+"","下载测试url"),
    MANGA_LIST(UrlConstants.PREFIX+"v1/cc/getMangaList","manga列表url"),
    OPER_LIST(UrlConstants.PREFIX+"v1/cc/getOperateList","操作列表url"),
    CUSTOM_PROPERTIES(UrlConstants.PREFIX+"v1/cc/getCustomProperties","获取全局自定义属性"),
    MODIFY_HEAD_IMG(UrlConstants.PREFIX+"v1/cc/modify/headImg","修改头像",true),
    USER_LOGIN(UrlConstants.PREFIX+"v1/cc/user/login","登录",false),
    MODIFY_USER_INFO(UrlConstants.PREFIX+"v1/cc/update/userInfo","更新用户信息",true),
    GET_USER_INFO(UrlConstants.PREFIX+"v1/cc/user/info","获取用户信息",true),
    MODIFY_BACKGROUND_IMG(UrlConstants.PREFIX+"v1/cc/modify/bgImg", "更改背景图"),
    user_register(UrlConstants.PREFIX+"v1/cc/register", "注册"),
    FORGET_PWD_REQ_VERIFY_CODE(UrlConstants.PREFIX+"v1/cc/forget/pwd", "忘记密码获取验证码"),
    PWD_RESET(UrlConstants.PREFIX+"v1/cc/reset/pwd", "重置密码"),
    GATEWAY_REQ(UrlConstants.PREFIX+"v1/support/gateWay/req", "网关请求"),
    LOGIN_OUT(UrlConstants.PREFIX+"v1/cc/user/loginOut","退出" );

    private String value;
    private String label;
    private String parentType;
    private boolean needSession=true;


    UrlConfig(String value, String label) {
        this.value = value;
        this.label = label;
    }

    UrlConfig(String value, String label,boolean needSession) {
        this.value = value;
        this.label = label;
        this.needSession = needSession;
    }

    UrlConfig(String value, String label, String parentType) {
        this.value = value;
        this.label = label;
        this.parentType = parentType;
    }

    public boolean isNeedSession() {
        return needSession;
    }

    public void setNeedSession(boolean needSession) {
        this.needSession = needSession;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
