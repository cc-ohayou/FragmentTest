package space.cc.com.fragmenttest.domain.bizobject;

import lombok.Data;

import java.util.Date;

/**
 * @AUTHOR CF
 * @DATE Created on 2019/1/10 10:40.
 */
@Data
public class OperateBiz {
    private String operId;
    private String type;
    private String desc;
    private String url;
    private String createTime;
    private String updateTime;

    public static void main(String[] args) {
        OperateBiz operateBiz=new OperateBiz();
        operateBiz.setOperId("001");
        operateBiz.setType("1");
        operateBiz.setDesc("递延操作");
        operateBiz.setUrl("");
        operateBiz.setCreateTime("");
        operateBiz.setUpdateTime(operateBiz.getCreateTime());


    }
}
