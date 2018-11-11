package space.cc.com.fragmenttest.domain.util;

/**
 * Created by yaojian on 2017/9/24 18:31
 */

public class NumUtils {

    /**
     * 获取整手股票
     */
    public static int boardLot(double num) {
        return floorN(num, 2);
    }

    /**
     * N位向下取整
     * @param num 入参
     * @param n   位数
     */
    public static int floorN(double num,int n) {
        if(n == 0 || num == 0)
            return  (int) Math.floor(num);
        int coefficient = (int) Math.pow( 10, n);
        if( coefficient > num)
            return  (int) Math.floor(num);
        int param = (int) Math.floor(num / coefficient);
        return param * coefficient;
    }

}
