package space.cc.com.fragmenttest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import space.cc.com.fragmenttest.R;


/**
 * Created by xuwenzheng on 2018/5/29 14:12
 */
public class EmptyView extends LinearLayout {
    private ImageView img;
    private TextView textView;
    public EmptyView(Context context) {
        super(context);
        init();
    }



    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view,this);
         img =  findViewById(R.id.iv_no_img);
         textView = findViewById(R.id.tv_no_notice);
    }
    public void creatView(int resId,String warning){
        img.setImageResource(resId);
        textView.setText(warning);
    }
    public void creatView(String warning){
        textView.setText(warning);
    }
}
