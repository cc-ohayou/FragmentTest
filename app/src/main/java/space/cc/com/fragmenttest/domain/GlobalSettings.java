package space.cc.com.fragmenttest.domain;

import space.cc.com.fragmenttest.domain.bizobject.CustomProperties;
import space.cc.com.fragmenttest.domain.bizobject.UserInfo;

public class GlobalSettings {

    public static CustomProperties settingProperties=CustomProperties.getInstance();
    public static UserInfo userInfo;
}
