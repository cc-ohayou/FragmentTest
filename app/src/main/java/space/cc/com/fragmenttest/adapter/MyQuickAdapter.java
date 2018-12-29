package space.cc.com.fragmenttest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import space.cc.com.fragmenttest.R;

import space.cc.com.fragmenttest.litepals.Manga;

public class MyQuickAdapter extends BaseQuickAdapter<Manga, BaseViewHolder> {
       private int resId;
       private int height=0;
       private int width=0;

       public MyQuickAdapter(int layoutResId, List data) {
            super(layoutResId, data);
            resId=layoutResId;
        }

    public MyQuickAdapter(int layoutResId, List data,int width,int height) {
        super(layoutResId, data);
        this.resId=layoutResId;
        this.width=width;
        this.height=height;


    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(resId,parent,false) ;
        ViewGroup.LayoutParams linearParams =view.getLayoutParams();

        //设置高度 一般纵向滚动时有此需求
        if(height>0){
            linearParams.height=height;
        }
//        一般最好宽高都自适应屏幕指定
        //设置宽度 一般横向滚动时有此需求
        if(width>0){
            linearParams.width=width;
        }
        view.setLayoutParams(linearParams);
        final BaseViewHolder holder=new BaseViewHolder(view);
        return holder;

    }

    @Override
        protected void convert(BaseViewHolder helper, Manga item) {
            helper.setText(R.id.manga_title, item.getTitle());
            helper.setText(R.id.manga_episode,"第"+ item.getNowEpisode()+"话");
            helper.setText(R.id.manga_area,  item.getArea());
            helper.setText(R.id.manga_new_desc,  item.isNew()?"新番":"老番");
            //http://square.github.io/picasso/    Picasso使用说明
//            helper.setImageBitmap()
//            Picasso.get().load(R.drawable.landing_screen).into(imageView1);
//            Picasso.get().load("file:///android_asset/DvpvklR.png").into(imageView2);
//            Picasso.get().load(new File(...)).into(imageView3);
//            获取单例实例
            Picasso instance=  Picasso.get();
//            debug模式 方便看出图片的来源  网络来源红色  绿色 内存 磁盘蓝色 左上角三角标识
            instance.setIndicatorsEnabled(true);
            instance.load(item.getCoverImage())
//                     .resize(50, 50)
//                     中心修剪
//                     .centerCrop()
                     .placeholder(R.drawable.manga_default)
                     .error(R.drawable.manga_default_error)
//                     此处使用 helper.getView获取对应位置的view对象
                     .into((ImageView) helper.getView(R.id.manga_cover));


//            helper.setImageBitmap()
//           .
        }
    }



