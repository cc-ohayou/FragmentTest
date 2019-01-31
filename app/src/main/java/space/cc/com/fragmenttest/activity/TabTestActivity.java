package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.TabLayoutAdapter;
import space.cc.com.fragmenttest.util.UtilBox;

public class TabTestActivity extends AppCompatActivity {

    private static final String TAG_TAB_ONE = "dynamic";
    private static final String TAG_TAB_TWO = "operList";
    private TabLayoutAdapter mPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
//    private SmartRefreshLayout srl_home;
    private String[] tabTitles = {"列表", "动态"};
    FloatingActionButton floatBackToTopBut;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tab_test);
            toolbar = findViewById(R.id.tab_toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            intiViewPagerAndAdapter();
//            mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());

            initTabLayout();
//           stateCheck(savedInstanceState);
            layoutManager=new LinearLayoutManager(this);

            initFloatBut();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void initFloatBut() {
        floatBackToTopBut = findViewById(R.id.tab_fab);

        floatBackToTopBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilBox.box().ui.MoveToPosition((LinearLayoutManager) layoutManager,0);
//               显示toolbar
//                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
//                hideViews();

            }
        });
    }
    private void hideViews() {
//        显示toolbar
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) floatBackToTopBut.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        floatBackToTopBut.animate().translationY(floatBackToTopBut.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tab标题字体颜色
//        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.bg_gray), ContextCompat.getColor(this, R.color.white));
        //设置选中区域背景色
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(tabLayout, 10);
//            setupWithViewPager(mViewPager); 内部完成了下面注释的两行代码的功能 以及adapter监听器的添加
//             mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void intiViewPagerAndAdapter() {
        mPagerAdapter = new TabLayoutAdapter(getSupportFragmentManager(), tabTitles,TabTestActivity.this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);

        mViewPager.setAdapter(mPagerAdapter);
        //设置ViewPager的切换监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected ( int position){
//                ToastUtils.showDisplay("switch to "+position);
                switch (position) {
                    case 0:
                        selectTab(position);
                        break;
                    case 1:
                        selectTab(position);

                        break;
                    case 2:
                        selectTab(position);
                        break;
                }
            }
            //页面滚动事件
            @Override
            public void onPageScrolled ( int arg0, float arg1, int arg2){
            }
            //页面滚动状态改变事件
            @Override
            public void onPageScrollStateChanged ( int arg0){
            }
        });

    }
    /**
         * @author  CF
         * @date   2019/1/11
         * @description   选中tab  切换图标选中状态和viewPager的当前item
         *
         */
    private void selectTab(int position) {
        mViewPager.setCurrentItem(position);

    }



    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
      /*  if (mHomeFragment != null)
            transaction.hide(mHomeFragment);
        if (mNewsFragment != null)
            transaction.hide(mNewsFragment);
        if (mStrategyFragment != null)
            transaction.hide(mStrategyFragment);
        if (mUserFragment != null)
            transaction.hide(mUserFragment);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
