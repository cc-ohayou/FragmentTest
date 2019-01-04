package space.cc.com.fragmenttest.util;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import space.cc.com.fragmenttest.BuildConfig;
import space.cc.com.fragmenttest.R;

public class PicassoUtil {
    private static Picasso picassoInst = Picasso.get();

    static {
        if (BuildConfig.DEBUG) {
            picassoInst.setIndicatorsEnabled(true);
        }

    }
    //http://square.github.io/picasso/    Picasso使用说明

//            Picasso.get().load(R.drawable.landing_screen).into(imageView1);
//            Picasso.get().load("file:///android_asset/DvpvklR.png").into(imageView2);
//            Picasso.get().load(new File(...)).into(imageView3);
//            获取单例实例
/*    Picasso instance = Picasso.get();
    //            debug模式 方便看出图片的来源  网络来源红色  绿色 内存 磁盘蓝色 左上角三角标识
            instance.setIndicatorsEnabled(true);
            instance.load(item.getCoverImage())
//                     .resize(50, 50)
//                     中心修剪
//                     .centerCrop()
                    .placeholder(R.drawable.manga_default)
                    .error(R.drawable.manga_default_error)
//                     此处使用 helper.getView获取对应位置的view对象
                    .into((ImageView) holder.getView(R.id.manga_cover));*/

//            helper.setImageBitmap()

    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    public void loadDrawResIntoView(ImageView targetView, int drawableResId) {
//        picassoInst.setIndicatorsEnabled(true);
        loadWork(targetView, drawableResId, R.drawable.manga_default);

    }

    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    public void loadUrlResIntoView(ImageView targetView, String imageUrl) {

        loadUrlResWork(targetView, imageUrl);


    }

    private void loadUrlResWork(ImageView targetView, String imageUrl) {

        picassoInst.load(imageUrl)
//                .resize和centerCrop()需要一起使用
//                     .resize(50, 50)
//                     中心修剪
//                .centerCrop()
                .placeholder(R.drawable.manga_default)
                .error(R.drawable.manga_default_error)
//                     此处使用 helper.getView获取对应位置的view对象
                .into(targetView);
    }

    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    public void loadUrlResIntoViewWithDefault(ImageView targetView, String imageUrl, Integer defaultRes) {
        int defaultResId = R.drawable.manga_default;
        if (defaultRes != null) {
            defaultResId = defaultRes;
        }
        loadUrlResWork(targetView, imageUrl);


    }


    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    public void loadDrawResIntoViewWithDefault(ImageView targetView, int drawableResId, Integer defaultId) {
//        picassoInst.setIndicatorsEnabled(true);
        int defaultResId = R.drawable.manga_default;
        if (defaultId != null) {
            defaultResId = defaultId;
        }
        loadWork(targetView, drawableResId, defaultResId);


    }

    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    private void loadWork(ImageView targetView, int drawableResId, int defaultResId) {
        picassoInst.load(drawableResId)
//                .resize和centerCrop()需要一起使用
//                     .resize(50, 50)
//                     中心修剪
//                .centerCrop()
                .placeholder(defaultResId)
                .error(R.drawable.manga_default_error)
//                     此处使用 helper.getView获取对应位置的view对象
                .into(targetView);
    }

}
