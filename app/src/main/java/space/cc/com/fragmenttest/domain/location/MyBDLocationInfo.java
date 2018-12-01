package space.cc.com.fragmenttest.domain.location;

/**
 * 存储百度定位信息
 */
public class MyBDLocationInfo {

    public static int mIntErrCode;
    public static String mStrTime = "";
    public static String mStrProvince = "";
    public static String mStrCity = "";
    public static String mStrAddress = "";
    public static double mLatitude;
    public static double mLongitude;

    public static String toStrings() {
        return "Location : "
                + "\nerror code : " + mIntErrCode
                + "\ntime : " + mStrTime
                + "\nprovince : " + mStrProvince
                + "\ncity : " + mStrCity
                + "\naddr : " + mStrAddress
                + "\nlatitude : " + mLatitude
                + "\nlongitude : " + mLongitude;
    }
}
