package space.cc.com.fragmenttest.litepals;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Album extends LitePalSupport {

    @Column(unique=true,defaultValue="unknown")
    private String name;

    private float price;
    private byte[] cover;
    private List<Song> songs=new ArrayList<>();

}
