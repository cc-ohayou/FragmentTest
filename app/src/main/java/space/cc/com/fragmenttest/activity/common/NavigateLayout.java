package space.cc.com.fragmenttest.activity.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.util.Utils;

public class NavigateLayout extends LinearLayout implements View.OnClickListener {

    ImageButton naviHome;
    ImageButton naviSearch;
    ImageButton naviMsg;

    DrawerLayout drawerLayout;

    public NavigateLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //在其他布局中引入标题栏时，动态加载导航栏的布局
        LayoutInflater.from(context).inflate(R.layout.navigate,this);
        naviHome=findViewById(R.id.navi_home);
        naviSearch=findViewById(R.id.navi_search);
        naviMsg=findViewById(R.id.navi_msg);
        naviHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
        naviSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
        naviMsg.setImageDrawable(getResources().getDrawable(R.drawable.navi_msg));

        naviHome.setOnClickListener(this);
        naviSearch.setOnClickListener(this);
        naviMsg.setOnClickListener(this);
        drawerLayout= (DrawerLayout) ((LayoutInflater) Utils.getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.activity_material_design,null);
        System.err.println("NavigateLayout="+drawerLayout);

    }
    @SuppressLint("WrongViewCast")
    @Override
    public void onClick(View v) {

         switch (v.getId()){
             case R.id.navi_home:
                 drawerLayout= findViewById(R.id.draw_layout);
                 System.err.println("NavigateLayout="+drawerLayout);

                  drawerLayout.openDrawer(GravityCompat.START);
                  break;
             default:
                 break;

         }

    }
}
