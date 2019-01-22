package space.cc.com.fragmenttest.domain.bizobject;

import java.util.Map;

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
    private String loginBgUrl;
    private String operBizDetailBgUrl;

    private Map otherProperties;
    public static final String UPDATE="1";
    public static final String NO_UPDATE="0";

    private static class Instance{
        private static CustomProperties customProperties;
        static{
            customProperties=new CustomProperties();
            customProperties.setUpdateSign(NO_UPDATE);
            customProperties.setEnv("sit");
            customProperties.setLoginBgUrl("https://ddy98.b0.upaiyun.com/user/bgImg/1547914325186.jpg");
            customProperties.setOperBizDetailBgUrl("https://ddy98.b0.upaiyun.com/user/headImages/1548152264892.jpg");
        }

    }
    public static CustomProperties   getInstance(){
        return Instance.customProperties;
    }


}
