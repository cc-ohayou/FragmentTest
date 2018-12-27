package space.cc.com.fragmenttest.litepals;

import org.litepal.crud.LitePalSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Book extends LitePalSupport {
    private long id;
    private String author;
    private String name;
    private int pages;
    private double price;
    private int imageId;

}
