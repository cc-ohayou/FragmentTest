package space.cc.com.fragmenttest.domain.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

/**
 * 屏幕相关工具类
 * Created by yaojian on 2017/9/20 19:21
 */

public class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽
     */
    public static int getScreenWidth() {
        return Utils.getApp().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高
     */
    public static int getScreenHeight() {
        return Utils.getApp().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 设置屏幕为全屏
     * <p>需在 {@code setContentView} 之前调用</p>
     *
     * @param activity activity
     */
    public static void setFullScreen(@NonNull final Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
