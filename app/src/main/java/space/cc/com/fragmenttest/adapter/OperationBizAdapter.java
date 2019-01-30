package space.cc.com.fragmenttest.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.base.BaseQuickAdapter;
import space.cc.com.fragmenttest.adapter.base.BaseViewHolder;
import space.cc.com.fragmenttest.domain.bizobject.OperateBiz;

public class OperationBizAdapter extends BaseQuickAdapter<OperateBiz, BaseViewHolder> {
    private int resId;
    private int height = 0;
    private int width = 0;

    public OperationBizAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        resId = layoutResId;
    }

    public OperationBizAdapter(int layoutResId, List data, int width, int height) {
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
            ViewGroup.LayoutParams linearParams = holder.getConvertView().getLayoutParams();
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
//            holder.itemView.setBackgroundColor(R.color.mangaItemBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;

    }

    @Override
    protected void convert(BaseViewHolder holder, OperateBiz item) {
        try {
            holder.setText(R.id.item_oper_name, "名称： " + item.getOperName() );
            holder.setText(R.id.item_oper_content, "描述："+item.getDesc());
//            holder.setText(R.id.item_oper_url, "url："+item.getUrl());
            holder.setText(R.id.item_oper_pro, "项目: " + item.getProject() );
            holder.setText(R.id.item_oper_env, "环境: " + item.getEnvType() );
//            holder.setText(R.id.item_oper_create_time, "创建时间: " + item.getCreateTime() );
//            holder.setImageResource(R.id.item_oper_but,R.drawable.press_but);
            holder.addOnClickListener(R.id.item_oper_but);
            holder.addOnLongClickListener(R.id.item_oper_but);
            /*UtilBox.box().picasso.loadUrlResIntoView((ImageView) holder.getView(R.id.manga_cover),
                    item.getCoverImage());

*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}