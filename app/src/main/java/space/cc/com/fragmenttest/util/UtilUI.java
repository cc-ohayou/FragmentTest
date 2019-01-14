package space.cc.com.fragmenttest.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yalantis.ucrop.UCrop;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import space.cc.com.fragmenttest.domain.util.Utils;

/**
 * 关于UI的工具类
 * <p/>
 * Created by Bamboy on 2017/3/24.
 */
public class UtilUI {
    private int barHeight = -1;

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return 状态栏高度
     */
    public int getBarHeight(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            barHeight = 0;
        }

        if (barHeight == -1) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;

            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                barHeight = context.getResources().getDimensionPixelSize(x);

            } catch (Exception e1) {
                e1.printStackTrace();
                return 0;
            }
        }
        return barHeight;
    }

    /**
     * 获取屏幕截屏 【不包含状态栏】
     *
     * @param activity
     * @param containTopBar 是否包含状态栏
     * @return
     */
    public Bitmap getScreenshot(Activity activity, boolean containTopBar) {
        try {
            Window window = activity.getWindow();
            View view = window.getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap bmp1 = view.getDrawingCache();
            /**
             * 除去状态栏和标题栏
             **/
            int height = containTopBar ? 0 : getBarHeight(activity);
            return Bitmap.createBitmap(bmp1, 0, height, bmp1.getWidth(), bmp1.getHeight() - height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Activity截图
     *
     * @param activity
     * @return bitmap 截图
     */
    public Bitmap getDrawing(Activity activity) {
        View view = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        return getDrawing(view);
    }

    /**
     * 获取View截图
     *
     * @param view
     * @return 截图
     */
    public Bitmap getDrawing(View view) {
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap tBitmap = view.getDrawingCache();
            // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
            tBitmap = tBitmap.createBitmap(tBitmap);
            view.setDrawingCacheEnabled(false);
            return tBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
            * RecyclerView 移动到当前位置，
            *
            * @param manager
     * @param mRecyclerView
     * @param n
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager
     * @param n
     */
    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }


    /**
         * @author  CF
         * @date   2019/1/11
         * @description
         *
         */
    public int getViewIemWidth(int divide,Context context) {
        //获取屏幕高度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int mScreenWidth = dm.widthPixels;
        return (mScreenWidth)/divide;
    }
    /**
     * @author  CF
     * @date   2019/1/11
     * @description
     *
     */
    public int getViewIemHeight(int divide ,Context context) {
        //获取屏幕高度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeight = dm.heightPixels;//屏幕高度
        return (mScreenHeight)/divide;
    }

    public int getViewIemHeight(int divide ) {
        //获取屏幕高度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeight = dm.heightPixels;//屏幕高度
        return (mScreenHeight)/divide;
    }


    /**
     * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    public UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
//        压缩格式 默认jpeg
//         options.setCompressionFormat(Bitmap.CompressFormat.PNG);
         options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        设置压缩质量 0-100 可用一个进度条控制
//        options.setCompressionQuality(mSeekBarQuality.getProgress());
//        隐藏底部选项只有一个选择框 简洁
//        options.setHideBottomControls(mCheckBoxHideBottomControls.isChecked());
//        自由模式 选择框大小自由可调节 地步选项全有
         options.setFreeStyleCropEnabled(true);
//         设置这个参数 则裁剪区域为16:9长方形  其它选项不再显示 一般不用设置
        options.withAspectRatio(1, 1);

        /*
        If you want to configure how gestures work for all UCropActivity tabs

        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */


       /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */

        return uCrop.withOptions(options);
    }
}
