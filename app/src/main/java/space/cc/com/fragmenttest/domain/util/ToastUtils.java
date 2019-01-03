package space.cc.com.fragmenttest.domain.util;

import android.app.Application;
import android.content.Context;

import android.widget.Toast;

import space.cc.com.fragmenttest.util.BToast;
import space.cc.com.fragmenttest.util.EToast;
import space.cc.com.fragmenttest.util.UtilBox;

import static space.cc.com.fragmenttest.util.BToast.ICONTYPE_ERROR;
import static space.cc.com.fragmenttest.util.BToast.ICONTYPE_NONE;
import static space.cc.com.fragmenttest.util.BToast.ICONTYPE_SUCCEED;

/**
 * Created by yaojian on 2017/9/21 20:25
 */

public class ToastUtils {

    private static int checkNotification = 0;
    private static Object mToast;

    private ToastUtils(Context context, CharSequence text, int time,
                     int iconType) {
        if (mToast != null) {
            cancel();
        }

        if (context instanceof Application)
            checkNotification = 0;
        else
            checkNotification = UtilBox.getBox().want.isNotificationEnabled(context) ? 0 : 1;

        if (checkNotification == 1)
            mToast = new EToast(context, text, time, iconType);
        else
            mToast = BToast.getToast(context, text, time, iconType);

    }

    /**
     * 显示一个纯文本吐司
     *
     * @param context  上下文
     * @param stringId 显示的文本的Id
     */
    public static void showText(Context context, int stringId) {
        new ToastUtils(context, context.getString(stringId), 0, ICONTYPE_NONE).show();
    }

    /**
     * 显示一个纯文本吐司
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    public static void showText(Context context, CharSequence text) {
        new ToastUtils(context, text, 0, ICONTYPE_NONE).show();
    }

    /**
     * 显示一个带图标的吐司
     *
     * @param context   上下文
     * @param stringId  显示的文本的Id
     * @param isSucceed 显示【对号图标】还是【叉号图标】
     */
    public static void showText(Context context, int stringId, boolean isSucceed) {
        new ToastUtils(context, context.getString(stringId), 0, isSucceed ? ICONTYPE_SUCCEED : ICONTYPE_ERROR).show();
    }

    /**
     * 显示一个带图标的吐司
     *
     * @param context   上下文
     * @param text      显示的文本
     * @param isSucceed 显示【对号图标】还是【叉号图标】
     */
    public static void showText(Context context, CharSequence text,
                                boolean isSucceed) {
        new ToastUtils(context, text, 0, isSucceed ? ICONTYPE_SUCCEED : ICONTYPE_ERROR).show();
    }

    /**
     * 显示一个纯文本吐司
     *
     * @param context  上下文
     * @param stringId 显示的文本的Id
     * @param time     持续的时间
     */
    public static void showText(Context context, int stringId, int time) {
        new ToastUtils(context, context.getString(stringId), time, ICONTYPE_NONE).show();
    }

    /**
     * 显示一个纯文本吐司
     *
     * @param context 上下文
     * @param text    显示的文本
     * @param time    持续的时间
     */
    public static void showText(Context context, CharSequence text, int time) {
        new ToastUtils(context, text, time, ICONTYPE_NONE).show();
    }

    /**
     * 显示一个带图标的吐司
     *
     * @param context   上下文
     * @param stringId  显示的文本的Id
     * @param time      持续的时间
     * @param isSucceed 显示【对号图标】还是【叉号图标】
     */
    public static void showText(Context context, int stringId, int time,
                                boolean isSucceed) {
        new ToastUtils(context, context.getString(stringId), time, isSucceed ? ICONTYPE_SUCCEED : ICONTYPE_ERROR).show();
    }

    /**
     * 显示一个带图标的吐司
     *
     * @param context   上下文
     * @param text      显示的文本
     * @param time      持续的时间
     * @param isSucceed 显示【对号图标】还是【叉号图标】
     */
    public static void showText(Context context, CharSequence text, int time,
                                boolean isSucceed) {
        new ToastUtils(context, text, time, isSucceed ? ICONTYPE_SUCCEED : ICONTYPE_ERROR).show();
    }

    public void show() {
        if (mToast instanceof EToast) {
            ((EToast) mToast).show();
        } else if (mToast instanceof Toast) {
            ((Toast) mToast).show();
        }
    }

    public void cancel() {
        if (mToast instanceof EToast) {
            ((EToast) mToast).cancel();
        } else if (mToast instanceof Toast) {
            ((Toast) mToast).cancel();
        }
    }

    public static void showDisplay(CharSequence errMsg) {


        showText(Utils.getApp(),errMsg);
    }
}
