package space.cc.com.fragmenttest.domain;

public enum UrlConfig {
    TEST_URL(UrlConstants.PREFIX+"v1/cc/get/userList","测试url"),
    TEST_DOWNLOAD(UrlConstants.PREFIX+"v1/cc/getDownloadUrl","测试下载url"),
    DOWN_LOAD04(UrlConstants.PREFIX04+"","下载测试url"),
    MANGA_LIST(UrlConstants.PREFIX+"/v1/cc/getMangaList","manga列表url"),
    CUSTOM_PROPERTIES(UrlConstants.PREFIX+"/v1/cc/getCustomProperties","获取全局自定义属性"),
    ;

    private String value;
    private String label;
    private String parentType;

    UrlConfig(String value, String label) {
        this.value = value;
        this.label = label;
    }

    UrlConfig(String value, String label, String parentType) {
        this.value = value;
        this.label = label;
        this.parentType = parentType;
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
