package space.cc.com.fragmenttest.domain.bizobject;

import java.io.Serializable;

import lombok.Data;

/**
 * @AUTHOR CF
 * @DATE Created on 2019/1/10 10:40.
 */
@Data
public class OperateBiz implements Serializable {
    private String operId;
    //操作简称 比如 递延
    private String operName;
    //类型
    private String type;
    private String desc;
    // 定时任务
    private String label;
    //环境类型
    private String envType;
    /**
     * 项目归属
     */
    private String project;
    private String url;
    private String createTime;
    private String updateTime;
    private String roleCode;


}
