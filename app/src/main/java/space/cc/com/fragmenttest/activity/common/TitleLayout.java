package space.cc.com.fragmenttest.activity.common;

import android.app.Activity;
import android.content.Context;
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
        //在其他布局中引入标题栏时，动态加载标题栏的布局
        LayoutInflater.from(context).inflate(R.layout.title,this);
        titleBack=findViewById(R.id.common_title_back);
        titleText=findViewById(R.id.common_title_text);
        register=findViewById(R.id.login_reg);
//        titleEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit_b01));
        titleBack.setOnClickListener(this);

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