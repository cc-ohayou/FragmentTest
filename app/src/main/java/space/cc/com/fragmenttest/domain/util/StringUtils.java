package space.cc.com.fragmenttest.domain.util;

import android.support.v4.util.ArrayMap;

import java.text.DecimalFormat;

/**
 * 字符串相关工具类
 * Created by yaojian on 2017/9/18 17:04
 */

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(final String a, final String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(final String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    public static String moneyCeil(String ds) {
        if(StringUtils.isEmpty(ds)) return "0.00";
        return String.valueOf(ArithUtils.ceilN(Double.parseDouble(ds),2));
    }
    public static String moneyCeil(double d) {
        return String.valueOf(ArithUtils.ceilN(d,2));
    }
    public static String moneyFloor(String ds) {
        if(StringUtils.isEmpty(ds)) return "0.00";
        return String.valueOf(ArithUtils.floorN(Double.parseDouble(ds),2));
    }
    public static String moneyFloor(double d) {
        return String.valueOf(ArithUtils.floorN(d,2));
    }

    public static String moneyCutOut(String ds) {
        if(StringUtils.isEmpty(ds)) return "0.00";
        if(ds.equals("--")) return "--";
        if(ds.contains(".")){
            String[] dss = ds.split("\\.");
            if(dss.length <= 1)
                ds = dss[0] + ".00";
            else if(dss[1].length()>=2)
                ds = dss[0] + "." + dss[1].substring(0,2);
            else if(dss[1].length() == 1)
                ds = dss[0] + "." + dss[1] + "0";
            else if(dss[1].length() == 0)
                ds = dss[0] + ".00";
        } else
            ds = ds + ".00";
        return ds;
    }
    public static String moneyCutOut(double d) {
        return moneyCutOut(String.valueOf(d));
    }

    public static String money(String ds) {
        return nDecimal(ds,2);
    }
    public static String money(double d) {
        return nDecimal(d,2);
    }

    /**
     * 小数点后位数
     */
    public static String nDecimal(String ds, int n) {
        if(StringUtils.isEmpty(ds))
            return "0.00";
        if(ds.equals("--"))
            return "--";
        double d = Double.parseDouble(ds);
        return nDecimal(d,n);
    }

    /**
     * 小数点后位数
     */
    public static String nDecimal(double d, int n) {
        String rex = "###0.";
        if(n==0)
            rex = "###0";
        for (int i = 0; i < n; i++)
            rex = rex + "0";
        DecimalFormat df = new DecimalFormat(rex);
        return df.format(d);
    }

    /**
     * 返回url结尾string
     */
    public static String urlEnd(String url){
        String end = url;
        if(url.contains("?")){
            String content[] = url.split("\\?");
            if(content.length > 1)
                end = content[0];
        }
        if(end.contains("/")){
            String[] s = end.split("/");
            if(s.length>0)
                end = s[s.length - 1];
        }
        return end;
    }
    /**
     * 兼容返回url结尾string
     */
    public static String realUrlEnd(String url){
        String end = url;
        if(url.contains("?")){
            String content[] = url.split("\\?");
            if(content.length > 1)
                end = content[1];
        }

        return end;
    }
    /**
     * 把url参数转换为map
     */
    public static ArrayMap<String,Object> urlToParam(String url){
        ArrayMap<String,Object> map = new ArrayMap<>();
        if(!url.contains("?"))
            return map;
        String content[] = url.split("\\?");
        if(content.length < 1)
            return map;
        String param = content[1];
        if(!param.contains("&"))
            return map;
        String[] kvs = param.split("&");
        if(kvs.length < 1)
            return map;
        for(String kv: kvs){
            if(kv.contains("=")){
                String[] s = kv.split("=");
                if(s.length == 2){
                    map.put(s[0], s[1]);
                }
            }
        }
        return map;
    }

}
