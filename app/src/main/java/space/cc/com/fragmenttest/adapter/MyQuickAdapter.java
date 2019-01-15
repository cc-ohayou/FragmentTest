package space.cc.com.fragmenttest.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.base.BaseQuickAdapter;
import space.cc.com.fragmenttest.adapter.base.BaseViewHolder;
import space.cc.com.fragmenttest.litepals.Manga;
import space.cc.com.fragmenttest.util.UtilBox;

public class MyQuickAdapter extends BaseQuickAdapter<Manga, BaseViewHolder> {
    private int resId;
    private int height = 0;
    private int width = 0;

    public MyQuickAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        resId = layoutResId;
    }

    public MyQuickAdapter(int layoutResId, List data, int width, int height) {
        super(layoutResId, data);
        this.resId = layoutResId;
        this.width = width;
        this.height = height;


    }


    @SuppressLint("ResourceAsColor")
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateViewHolder(parent, viewType);
        try {
//        View view= LayoutInflater.from(parent.getContext())
//                .inflate(resId,parent,false) ;
      /*  ViewGroup.LayoutParams linearParams =view.getLayoutParams();
        //设置高度 一般纵向滚动时有此需求
        if(height>0){
            linearParams.height=height;
        }
//        一般最好宽高都自适应屏幕指定
        //设置宽度 一般横向滚动时有此需求
        if(width>0){
            linearParams.width=width;
        }*/
//        view.setLayoutParams(linearParams);
            ViewGroup.LayoutParams linearParams = holder.itemView.getLayoutParams();
            if (height > 0) {
                linearParams.height = height;
            }
            if (width > 0) {
                linearParams.width = width;
            }
            holder.getConvertView().setLayoutParams(linearParams);
            holder.itemView.setLayoutParams(linearParams);
//            holder.itemView.findViewById(R.id.manga_title);
//            Color.parseColor("#865955")  将string转化为 int 型color
//          int color=Color.rgb(red, green, blue)   将RGB转化为int 型colorcolor
// 如果需要透明度
// int color=Color.argb(a,red, green, blue)
//            holder.itemView.setBackgroundColor(Color.parseColor("#26000000"));
            holder.itemView.getBackground().mutate().setAlpha(60);
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
        return holder;

    }

    @Override
    protected void convert(BaseViewHolder holder, Manga item) {
        try {
            holder.setText(R.id.manga_title, item.getTitle());
            holder.setText(R.id.manga_episode, "第" + item.getNowEpisode() + "话");
            holder.setText(R.id.manga_area, item.getArea());
            holder.setText(R.id.manga_new_desc, item.isNew() ? "新番" : "老番");
            //子控件的点击事件除了 活动那里adapter需要设置点击监听 此处也需要对对应的子控件进行设置
            holder.addOnClickListener(R.id.manga_cover);
            holder.addOnLongClickListener(R.id.manga_title);
            UtilBox.box().picasso.loadUrlResIntoView((ImageView) holder.getView(R.id.manga_cover),
                    item.getCoverImage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



