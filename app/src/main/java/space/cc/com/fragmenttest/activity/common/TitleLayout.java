package space.cc.com.fragmenttest.activity.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import space.cc.com.fragmenttest.R;

public class TitleLayout extends RelativeLayout implements View.OnClickListener {
    ImageButton titleBack;
    public static TextView register;
    public static TextView titleText;
    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       /* LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.title,this);*/
//        findView();
        //在其他布局中引入标题栏时，动态加载标题栏的布局
        LayoutInflater.from(context).inflate(R.layout.title,this);
        titleBack=findViewById(R.id.common_title_back);
        titleText=findViewById(R.id.common_title_text);
        register=findViewById(R.id.login_reg);
//        titleEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit_b01));
        titleBack.setOnClickListener(this);

        initLayoutAndAttr(context, attrs);


    }

    private void initLayoutAndAttr(Context context,@Nullable AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout);
           /* int left_visibility = a.getInt(R.styleable.TitleBarView_left_visibility, 0);
            int right_visibility = a.getInt(R.styleable.TitleBarView_right_visibility, 4);
            Drawable left_img = a.getDrawable(R.styleable.TitleBarView_left_img);
            Drawable right_img = a.getDrawable(R.styleable.TitleBarView_right_img);
            String left_text = a.getString(R.styleable.TitleBarView_left_text);
            String right_text = a.getString(R.styleable.TitleBarView_right_text);*/
            String title_text = a.getString(R.styleable.TitleLayout_title_text);
            setTitleText(title_text);
        /*    int left_text_color = a.getColor(R.styleable.TitleBarView_left_text_color, Color.parseColor("#ffffff"));
            int right_text_color = a.getColor(R.styleable.TitleBarView_right_text_color, Color.parseColor("#ffffff"));
            int title_text_color = a.getColor(R.styleable.TitleBarView_title_text_color, Color.parseColor("#ffffff"));
            setLeftVisibility(left_visibility);
            setRightVisibility(right_visibility);
            if(left_img != null)
                setLeftImg(left_img);
            if(right_img != null)
                setRightImg(right_img);
            if(!StringUtils.isEmpty(left_text))
                setLeftText(left_text);
            if(!StringUtils.isEmpty(right_text))
                setRightText(right_text);*/

           /* setLeftTextColor(left_text_color);
            setRightTextColor(right_text_color);
            setTitleTextColor(title_text_color);*/

          /*  setLeftClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity)getContext()).finish();
                }
            });*/
            a.recycle();
        }

    }
        private void setTitleText (String title){
            titleText.setText(title);
        }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_title_back:
                ((Activity) getContext()).finish();
                break;
            case R.id.login_reg:
                break;
            default:
                break;

        }
    }
}