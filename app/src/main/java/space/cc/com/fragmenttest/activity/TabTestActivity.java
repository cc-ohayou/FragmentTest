package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.TabLayoutAdapter;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.fragment.BaseFragment;
import space.cc.com.fragmenttest.fragment.DynamicFragment;
import space.cc.com.fragmenttest.fragment.OperationListFragment;

public class TabTestActivity extends AppCompatActivity {

    private static final String TAG_TAB_ONE = "dynamic";
    private static final String TAG_TAB_TWO = "operList";
    private TabLayoutAdapter mPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TabLayout tabLayout;
    private SmartRefreshLayout srl_home;

    private List<Fragment> tabFragments;
    private String[] tabTitles = {"列表", "动态"};
    FragmentManager fManager;
    BaseFragment dynamicFragment;
    BaseFragment operListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tab_test);
            Toolbar toolbar = findViewById(R.id.tab_toolbar);
            setSupportActionBar(toolbar);
            intiViewPagerAndAdapter();
//            mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());

            initTabLayout();
//           stateCheck(savedInstanceState);

            initFloatBut();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void initFloatBut() {
     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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
