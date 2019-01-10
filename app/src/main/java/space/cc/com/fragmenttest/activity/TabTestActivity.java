package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import space.cc.com.fragmenttest.fragment.BaseFragment;
import space.cc.com.fragmenttest.fragment.OperationListFragment;
import space.cc.com.fragmenttest.fragment.OperationTabItemFragment;

public class TabTestActivity extends AppCompatActivity {

    private static final String TAG_TAB_ONE ="dynamic" ;
    private static final String TAG_TAB_TWO ="operList" ;
    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private TabLayoutAdapter mPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TabLayout tabLayout;
    private SmartRefreshLayout srl_home;

    private List<Fragment> tabFragments;
    private String[] tabTitles={"列表","动态"};
    FragmentManager fManager;
    BaseFragment dynamicFragment;
    BaseFragment operListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tab_test);
            ButterKnife.bind(this);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
//          初始化每个tab内部的fragment
            initFragmentList();

            intiViewPagerAndAdapter();
            mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());

            initTabLayout();
            stateCheck(savedInstanceState);
//            tabLayout.findViewById(R.id.tabItem);
//            setupWithViewPager(mViewPager); 内部完成了下面注释的两行代码的功能 以及adapter监听器的添加
//             mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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
        tabLayout=findViewById(R.id.tabs);
       /* tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tab标题字体颜色
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.bg_gray), ContextCompat.getColor(this, R.color.white));
        //设置选中区域背景色
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));*/
        ViewCompat.setElevation(tabLayout, 10);

        tabLayout.setupWithViewPager(mViewPager);
    }

    private void intiViewPagerAndAdapter() {
        mPagerAdapter = new TabLayoutAdapter(getSupportFragmentManager(),tabTitles,tabFragments);

        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initFragmentList() {
        tabFragments = new ArrayList<>();
        for (String title : tabTitles) {
            if("列表".equals(title)){

                tabFragments.add(OperationListFragment.newInstance(title));
            }
        }


    }



    /**
     * 状态检测 用于内存不足的时候保证fragment不会重叠
     */
    private void stateCheck(Bundle savedInstanceState) {

            if (null != savedInstanceState) {
                //页面意外终止后（如进程被杀）恢复原状态
                int position = savedInstanceState.getInt("position", 2);
                switchContent(position);
            } else {
                switchContent(0);
            }


    }

    private int mIntPosition = -1;

    /**
     * 切换Fragment
     */
    private void switchContent(int index) {
        if (mIntPosition == index) return;
        mIntPosition = index;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 获取fragment，同时防止页面意外终止后（如进程被杀）恢复时有重叠情况出现
        operListFragment = (OperationListFragment) fManager.findFragmentByTag(TAG_TAB_ONE);
        dynamicFragment = (OperationTabItemFragment) fManager.findFragmentByTag(TAG_TAB_TWO);

        hideFragments(transaction);

        switch (index) {
            case 0:
                if (operListFragment == null) {
                    operListFragment = new OperationListFragment();
                    transaction.add(R.id.tab_activity, operListFragment, TAG_TAB_ONE);
                } else {
                    transaction.show(operListFragment);
                }
                break;
            case 1:
                if (dynamicFragment == null) {
                    dynamicFragment = new OperationTabItemFragment();
                    transaction.add(R.id.tab_activity, dynamicFragment, TAG_TAB_TWO);
                } else {
                    transaction.show(dynamicFragment);
                }
                break;
            default:
                break;
        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.tab_activity,dynamicFragment).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().replace(R.id.tab_activity,dynamicFragment).commit();

        //commit 在onSaveInstanceState之后调用报错 java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        //transaction.commit();
        transaction.commitAllowingStateLoss();
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
