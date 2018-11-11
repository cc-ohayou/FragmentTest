package space.cc.com.fragmenttest.domain.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 解决float和double计算精度丢失问题
 * Created by yaojian on 2017/9/24 20:37
 */

public class ArithUtils {

    private static final int DEF_DIV_SCALE = 10;

    public static double retainDecimal(double d){
        DecimalFormat df = new DecimalFormat("#.00");
        String ds = df.format(d);
        return Double.valueOf(ds);
    }

    public static double cutOutN(double d,int n){
        if(d == Double.NEGATIVE_INFINITY || d == -Double.NEGATIVE_INFINITY || d == Double.NaN || d == -Double.NaN)
            return 0;
        double xs = Math.pow(10,n);
        d=((int)(d*xs))/xs;
        return d;
    }

    public static double ceilN(double d,int n){
        if(d == Double.NEGATIVE_INFINITY || d == -Double.NEGATIVE_INFINITY || d == Double.NaN || d == -Double.NaN)
            return 0;
        double bd = d * Math.pow(10,n);
        bd = Math.ceil(bd) /  Math.pow(10,n);
        return bd;
    }
    public static double floorN(double d,int n){
        if(d == Double.NEGATIVE_INFINITY || d == -Double.NEGATIVE_INFINITY || d == Double.NaN || d == -Double.NaN)
            return 0;
        double bd = d * Math.pow(10,n);
        bd = Math.floor(bd) /  Math.pow(10,n);
        return bd;
    }

    /**
     * 四舍五入 保留两位小数
     */
    public static double scale(double d){
        if(d == Double.NEGATIVE_INFINITY || d == -Double.NEGATIVE_INFINITY || d == Double.NaN || d == -Double.NaN)
            return 0;
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double scale2(double d){
        if(d == Double.NEGATIVE_INFINITY || d == -Double.NEGATIVE_INFINITY)
            return 0;
        return new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /** 加 */
    public static double add(double d1,double d2){
        return add(Double.toString(d1), Double.toString(d2));
    }
    /** 加 */
    public static double add(String d1, String d2){
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.add(b2).doubleValue();
    }
    /** 减 */
    public static double sub(double d1,double d2){
        return sub(Double.toString(d1), Double.toString(d2));
    }
    /** 减 */
    public static double sub(String d1, String d2){
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).doubleValue();
    }
    /** 乘 */
    public static double mul(double d1,double d2){
        return mul(Double.toString(d1), Double.toString(d2));
    }
    /** 乘 */
    public static double mul(String d1, String d2){
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }
    /** 除 */
    public static double div(double d1,double d2){
        return div(d1, d2, DEF_DIV_SCALE);
    }
    /** 除 */
    public static double div(double d1, double d2, boolean up){
        return div(Double.toString(d1), Double.toString(d2), DEF_DIV_SCALE, up? BigDecimal.ROUND_HALF_UP: BigDecimal.ROUND_DOWN);
    }
    /** 除 */
    public static double div(double d1,double d2,int scale){
        return div(Double.toString(d1), Double.toString(d2), scale, BigDecimal.ROUND_HALF_UP);
    }
    /** 除 */
    public static double div(String d1, String d2, int scale, int roundingMode){
        if(scale < 0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1=new BigDecimal(d1);
        BigDecimal b2=new BigDecimal(d2);
        if (b2.doubleValue() == 0){
            b2 = new BigDecimal("1");
        }
        return b1.divide(b2, scale, roundingMode).doubleValue();
    }

}
