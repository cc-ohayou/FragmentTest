package space.cc.com.fragmenttest.domain.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘控制
 *
 * Created by yaojian on 2017/11/22 14:31
 */

public class KeyboardUtil {

    /**控制键盘隐藏*/
    public static void hideKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive() && null != activity.getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 控制键盘显示  表示隐式显示输入窗口，非用户直接要求。窗口可能不显示。
     * @param activity
     * @param view - 接受软键盘输入的视图View
     */
    public static void showKeyboard(Activity activity, View view){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != view) {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 如果输入法打开则关闭，如果没打开则打开
     * @param activity
     */
    public static void toggleKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
