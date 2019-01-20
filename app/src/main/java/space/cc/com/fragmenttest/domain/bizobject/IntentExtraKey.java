package space.cc.com.fragmenttest.domain.bizobject;

public enum IntentExtraKey {
    OPER_BIZ_DETAIL("operBizDetail","操作详情"),

    ;

    private String value;
    private String label;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    IntentExtraKey(String value, String label, String type) {
        this.value = value;
        this.label = label;
        this.type = type;
    }

    IntentExtraKey(String value, String label) {
        this.value = value;
        this.label = label;
        this.type = type;
    }
}
