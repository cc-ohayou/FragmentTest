package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import space.cc.com.fragmenttest.R;

public class MaterialDesignActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design);
        DrawerLayout drawerLayout=findViewById(R.id.draw_layout);
        System.err.println("MaterialDesignActivity="+drawerLayout);
    }

    @Override
    public void requestPermission() {

    }
}
