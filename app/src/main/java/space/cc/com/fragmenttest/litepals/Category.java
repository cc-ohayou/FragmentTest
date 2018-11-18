package space.cc.com.fragmenttest.litepals;

import org.litepal.crud.LitePalSupport;

import lombok.Data;

  /**
     * @description  借用DataSupport 操作crud已经被遗弃
     * @author CF
     * created at 2018/11/18/018  15:32
     */
@Data
public class Category extends LitePalSupport{

    private int id;
    private String categoryName;
    private String categoryCode;

}
