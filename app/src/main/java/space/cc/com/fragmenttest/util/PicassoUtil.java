package space.cc.com.fragmenttest.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import space.cc.com.fragmenttest.BuildConfig;
import space.cc.com.fragmenttest.R;

public class PicassoUtil {
    private static Picasso picassoInst = Picasso.get();
    private static final int DEF_WIDTH = 50;
    private static int DEF_HEIGHT = 50;
    int defaultResId = R.drawable.manga_default;

    /* 对于不透明的图片可以使用RGB_565来优化内存。

             Picasso.with( imageView.getContext() )
             .load(url)
 .config(Bitmap.Config.RGB_565)
 .into(imageView);
 默认情况下，Android使用ARGB_8888

 Android中有四种，分别是：
 ALPHA_8：每个像素占用1byte内存
 ARGB_4444:每个像素占用2byte内存
 ARGB_8888:每个像素占用4byte内存
 RGB_565:每个像素占用2byte内存
 */
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
     * @description
     * @author CF
     * created at 2019/1/5/005  21:50
     */
    public void loadDrawResReSize(ImageView targetView, int drawableResId, int width, int height) {

        picassoInst.load(drawableResId)
//                .resize和centerCrop()需要一起使用
//                .resize(width <= 0 ? DEF_WIDTH : width, height <= 0 ? DEF_HEIGHT : height)
//               中心修剪
                .centerInside()
                .fit()
                .config(Bitmap.Config.RGB_565)
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
    public void loadUrlResIntoView(ImageView targetView, String imageUrl) {

        loadUrlResWork(targetView, imageUrl,R.drawable.manga_default);

    }

    public void loadUrlResIntoView(ImageView targetView, String imageUrl, String tag) {

        picassoInst.load(imageUrl)
//                .resize和centerCrop()需要一起使用
//                     .resize(50, 50)
//                     中心修剪
//                .centerCrop()
                .placeholder(R.drawable.manga_default)
                .error(R.drawable.manga_default_error)
                .config(Bitmap.Config.RGB_565)
                .tag(tag)
                .centerInside()
//                     此处使用 helper.getView获取对应位置的view对象
                .into(targetView);

    }

    private void loadUrlResWork(ImageView targetView, String imageUrl,int defaultResId) {

        picassoInst.load(imageUrl)
//                .resize和centerCrop()需要一起使用
//                     .resize(50, 50)
//                     中心修剪
                .placeholder(defaultResId)
                .error(R.drawable.manga_default_error)
                .config(Bitmap.Config.RGB_565)
//                上面的centerCrop是可能看不到全部图片的，如果你想让View将图片展示完全，可以用centerInside，
//               但是如果图片尺寸小于View尺寸的话，是不能充满View边界的。
                .centerInside()
                .fit()
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
        loadUrlResWork(targetView, imageUrl,defaultResId);


    }


    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    public void loadDrawResIntoViewWithDefault(ImageView targetView, int drawableResId, Integer defaultId) {
//        picassoInst.setIndicatorsEnabled(true);

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
                .fit()
                .placeholder(defaultResId)
                .config(Bitmap.Config.RGB_565)
                .centerInside()
                .error(R.drawable.manga_default_error)
//                     此处使用 helper.getView获取对应位置的view对象
                .into(targetView);

    }


    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    public void loadUriResWithDefault(ImageView targetView, Uri resUri, int defaultResId) {

        picassoInst.load(resUri)
//                .resize和centerCrop()需要一起使用
//                     .resize(50, 50)
//                     中心修剪 和centerInside 不兼容
//                .centerCrop()
                .placeholder(defaultResId)
                .config(Bitmap.Config.RGB_565)
                .centerInside()
                .fit()
                .error(R.drawable.manga_default_error)
//                     此处使用 helper.getView获取对应位置的view对象
                .into(targetView);

    }

    /**
     * @description
     * @author CF
     * created at 2019/1/13/013  22:54
     */
    public void loadUriRes(ImageView targetView, Uri resUri) {
        loadUriResWithDefault(targetView, resUri, defaultResId);
    }


    /**
     * @author CF
     * @date 2019/1/4
     * @description
     */
    public void loadBigUrlImg(ImageView targetView, String url, int defaultResId) {
//参考文章 https://blog.csdn.net/mawei7510/article/details/80580097
        picassoInst.load(url)
//                .resize和centerCrop()需要一起使用
//                     .resize(50, 50)
//                     中心修剪
//                .centerCrop()
                .placeholder(defaultResId)
                .config(Bitmap.Config.RGB_565)
                //静止内存缓存  针对大图 不要在内存中占用空间  放到磁盘缓存中
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)//跳过磁盘缓存

                .error(R.drawable.manga_default_error)
//                     此处使用 helper.getView获取对应位置的view对象
                .into(targetView);

    }
}
