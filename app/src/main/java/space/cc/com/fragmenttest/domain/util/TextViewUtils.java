package space.cc.com.fragmenttest.domain.util;

import android.content.Context;
import android.widget.TextView;

import space.cc.com.fragmenttest.R;

/**
 * TextView常用样式
 *
 * Created by yaojian on 2017/10/18 14:01
 */

public class TextViewUtils {

    public static void setStockTextColor(double price,TextView tv){
        Context context = Utils.getApp();
        if(price > 0)
            tv.setTextColor(context.getResources().getColor(R.color.red));
        else if(price < 0)
            tv.setTextColor(context.getResources().getColor(R.color.green));
        else
            tv.setTextColor(context.getResources().getColor(R.color.second_text_color));
    }

    public static void setStockTextColor(String priceStr, TextView tv){
        double price = 0;
        if(!StringUtils.isEmpty(priceStr))
            price = Double.parseDouble(priceStr);
        Context context = Utils.getApp();
        if(price > 0)
            tv.setTextColor(context.getResources().getColor(R.color.red));
        else if(price < 0)
            tv.setTextColor(context.getResources().getColor(R.color.green));
        else
            tv.setTextColor(context.getResources().getColor(R.color.second_text_color));
    }

    public static String setStockTextColor(double price){
        String colorStr;
        if(price>0)
            colorStr = "#fa3e3e";
        else if(price<0)
            colorStr = "#4ca92a";
        else
            colorStr = "#333333";
        return colorStr;
    }

}
