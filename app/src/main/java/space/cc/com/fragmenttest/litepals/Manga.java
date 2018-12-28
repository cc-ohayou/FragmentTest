package space.cc.com.fragmenttest.litepals;

import lombok.Data;

@Data
public class Manga {

    /**
     * 地区 中国大陆 china
     */
    private String area;
    //    新番与否
    private boolean isNew;
    //    封面  square_cover替换为 coverImage 方形封面图
    private String coverImage;
    //    封面图 点击新番后进入的作品图片
    private String cover;
    //    标题 名称
    private String title;
    //    播出日期
    private int weekday;
    //更新至第几话 bgmcount替换为   nowEpisode
    private String nowEpisode;
    //排名
    private int viewRank;
}
