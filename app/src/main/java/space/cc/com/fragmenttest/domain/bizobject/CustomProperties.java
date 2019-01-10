package space.cc.com.fragmenttest.domain.bizobject;

import lombok.Data;
/**
     * @author  CF
     * @date   2019/1/7
     * @description  自定义属性 集合体 用于app全局配置的一些公用属性
     *
     */
@Data
public class CustomProperties {
    private String downLoadUrl;
    private String updateSign;
    // dev  sit prod 三个环境  用于切换 请求url
    private String env;
    public static final String UPDATE="1";
    public static final String NO_UPDATE="0";

    private static class Instance{
        private static CustomProperties customProperties;
        static{
            customProperties=new CustomProperties();
            customProperties.setUpdateSign(NO_UPDATE);
        }

    }
    public static CustomProperties   getInstance(){
        return Instance.customProperties;
    }


}
