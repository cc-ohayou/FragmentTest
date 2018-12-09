package space.cc.com.fragmenttest.domain;

public class UrlConstants {
    public static final String PREFIX="http://118.31.73.19:8002/";
    public static final String PREFIX02="http://118.31.73.19:3004/";
    public static final String PREFIX03="http://txt.janpn.com/to/txt/148/148372.txt";
    //下载源要选择好 否则DownLoadTask的Progress获取到的status一直是waiting状态 不是loading状态
    // 此时进度刷新的回调方法onProgress不会被执行也就不会更新下载进度
    public static final String PREFIX04="https://pkg.biligame.com/games/bh3_2.7.0_1_20181026_182528_d5aad5.apk";
}
