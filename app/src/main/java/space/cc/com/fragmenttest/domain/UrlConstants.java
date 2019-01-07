package space.cc.com.fragmenttest.domain;

public class UrlConstants {
    public static  String PREFIX="http://118.31.73.19:8002/";
    public static final String PREFIX02="http://118.31.73.19:3004/";
    public static final String PREFIX03="http://txt.janpn.com/to/txt/148/148372.txt";
    //下载源要选择好 否则DownLoadTask的Progress获取到的status一直是waiting状态 不是loading状态
    // 此时进度刷新的回调方法onProgress不会被执行也就不会更新下载进度
//    public static final String PREFIX04="https://pkg.biligame.com/games/bh3_2.8.0_1_20181211_201637_b8c9b8.apk";
    public static final String PREFIX04="http://download.fc.xishisoft.com/app/p38/v1_0/juziqiquan-release.apk";
}
