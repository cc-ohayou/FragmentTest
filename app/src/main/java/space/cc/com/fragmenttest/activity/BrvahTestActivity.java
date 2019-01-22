package space.cc.com.fragmenttest.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.lzy.okgo.OkGo;
import com.nanchen.compresshelper.CompressHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.MyQuickAdapter;
import space.cc.com.fragmenttest.adapter.TabLayoutAdapter;
import space.cc.com.fragmenttest.adapter.base.BaseQuickAdapter;
import space.cc.com.fragmenttest.domain.ClientConfiguration;
import space.cc.com.fragmenttest.domain.GlobalSettings;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.bizobject.CustomProperties;
import space.cc.com.fragmenttest.domain.bizobject.UserInfo;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.StringUtil;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.fragment.OperationListFragment;
import space.cc.com.fragmenttest.litepals.Manga;
import space.cc.com.fragmenttest.service.DownLoadService;
import space.cc.com.fragmenttest.util.CustomScrollView;
import space.cc.com.fragmenttest.util.UtilBox;

import static android.view.View.FOCUSABLE;


public class BrvahTestActivity extends BaseActivity implements View.OnClickListener ,CustomScrollView.OnScrollChangeListener {


    private static final String TAG = "BrvahTestActivity";
    private static final String HEAD_CACHE_FILE_NAME ="cc_head_cache" ;
    private static final String SET_MENU_BACK_CACHE_FILE_NAME ="cc_set_menu_back_cache" ;
    private List<Manga> mangaList;
    private static List<Manga> defaultList;
    private RecyclerView mRecyclerView;
    MyQuickAdapter homeAdapter;
    private DrawerLayout drawerLayout;
    PopupWindow mPopupWindow;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    NavigationView navView;
    View topView;
    TextView popItem1TextView;
    TextView popItem2TextView;
    FloatingActionButton floatBackToTopBut;
    RecyclerView.LayoutManager layoutManager;
    private  CircleImageView headImageView;
    private TextView navLoginText;
    private TextView popBgChangeText;

    private  CircleImageView navTopLeftCircleImageView;

    @BindView(R.id.manga_toolbar_search_input)
    EditText searchText;
    int popMenuYoffset;
    //头像来源uri对象
    Uri sourceHeadImageUri;
//    头像目标存储uri
    TextView nickName;
    TextView mail;
    private UCrop uCrop;
    private String nickNameInput="";

    MenuItem loginItem;


    private static final String TAG_TAB_ONE = "dynamic";
    private static final String TAG_TAB_TWO = "operList";
    private TabLayoutAdapter mPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    //    private SmartRefreshLayout srl_home;
    private String[] tabTitles = {"列表", "动态"};

    private static String defaultData = "[{\"area\":\"中国大陆\",\"arealimit\":317,\"attention\":352150,\"bangumi_id\":0,\"nowEpisode\":\"11\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/8f19c704a6516bab4716931db10621c1b6c6e4bf.jpg\",\"danmaku_count\":105072,\"ep_id\":258700,\"favorites\":352150,\"is_finish\":0,\"lastupdate\":1545883200,\"lastupdate_at\":\"2018-12-27 12:00:00\",\"isNew\":false,\"play_count\":8263731,\"pub_time\":\"\",\"season_id\":25823,\"season_status\":13,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/67a851a5cd87fef2ddd6f9bd6cf691781a1d8b95.jpg\",\"title\":\"画江湖之不良人 第三季\",\"viewRank\":0,\"weekday\":4},{\"area\":\"中国大陆\",\"arealimit\":316,\"attention\":12083,\"bangumi_id\":0,\"nowEpisode\":\"4\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/8df12fc092928ba57069eb16e077f9d719f345e5.jpg\",\"danmaku_count\":10022,\"ep_id\":258686,\"favorites\":12083,\"is_finish\":0,\"lastupdate\":1545876000,\"lastupdate_at\":\"2018-12-27 10:00:00\",\"isNew\":false,\"play_count\":486760,\"pub_time\":\"\",\"season_id\":26176,\"season_status\":2,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/c96b4daa6f3f1925184bc7835a6a26ee573d6970.jpg\",\"title\":\"通灵妃 河南话版\",\"viewRank\":0,\"weekday\":4},{\"area\":\"中国大陆\",\"arealimit\":316,\"attention\":151420,\"bangumi_id\":0,\"nowEpisode\":\"10\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/57dc8fdf8373f8c1facfc2911d50be1cfc6da18a.jpg\",\"danmaku_count\":26706,\"ep_id\":258689,\"favorites\":151420,\"is_finish\":0,\"lastupdate\":1545876000,\"lastupdate_at\":\"2018-12-27 10:00:00\",\"isNew\":false,\"play_count\":2023158,\"pub_time\":\"\",\"season_id\":24888,\"season_status\":2,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/ef2f039b71564cc469b2cac9d8f2879e1259a74f.jpg\",\"title\":\"山河社稷图\",\"viewRank\":0,\"weekday\":4}]";

    static {
        try {
            defaultList = JSON.parseArray(defaultData, Manga.class);
        } catch (Exception e) {
            Log.e(TAG, "defaultData transfer error", e);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_brvah_test);
            ButterKnife.bind(this);
            initAllView();
            toolbar = findViewById(R.id.manga_toolbar);
//        toolbar.setLogo(R.drawable.app_icon2);
            // 主标题,默认为app_label的名字  设置Title为空""则不显示  注意需要在setSupportActionBar之前
            toolbar.setTitle("");
            setToolbarStyle(toolbar);
            setSupportActionBar(toolbar);
            appBarToggleSet();
            //侧边栏菜单 监听
            setNavigationViewListener();

            initUserInfoRelViewValueByLoginState();
//            initActionBar();
            //初始化recycleView
//            initRecycleView();
//            初始化homeAdapter
//            initAdapter();
//      远端加载数据
//            initData();

            intiViewPagerAndAdapter();
//          abLayout.setupWithViewPager(mViewPager);放在adapter之后 官方的一个坑爹bug 会导致tab不显示
//          详细说明见  https://blog.csdn.net/sundy_tu/article/details/52682246
            initTabLayout();
            initFloatBut();
            initSearchinputListener();
        } catch (Exception e) {
            Log.e(TAG, "onCreate error", e);

        }
//        int standardHeight=getViewIemWidth(4,BrvahTestActivity.this);


    }

    private void initSearchinputListener() {
//        searchText.setFocusable(false);
        searchText.setOnClickListener(this);

        // editText 离开监听
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // hasFocus 为false时表示点击了别的控件，离开当前editText控件
                if (!hasFocus) {
                    searchText.setFocusable(false);
                } else {
                    searchText.setFocusable(true);
                }
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent event) {
                searchText.setFocusable(true);
                //搜索框输入就绪点击回车换行时 刷新
                if (id == EditorInfo.IME_ACTION_SEND
                        || id == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    List<Fragment> fragments= getSupportFragmentManager().getFragments();
                    OperationListFragment fragment=(OperationListFragment)fragments.get(0);
                    fragment.loadOperationBizList(true);
                    return true;
                }
                return false;
            }
        });

    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.operList__tabs);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tab标题字体颜色
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.bg_gray), ContextCompat.getColor(this, R.color.white));
        //设置选中区域背景色
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        ViewCompat.setElevation(tabLayout, 10);
//            setupWithViewPager(mViewPager); 内部完成了下面注释的两行代码的功能 以及adapter监听器的添加
//             mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void intiViewPagerAndAdapter() {
        mPagerAdapter = new TabLayoutAdapter(getSupportFragmentManager(), tabTitles,BrvahTestActivity.this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.operList_vp);

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
        if(position==0){
            List<Fragment> fragments= getSupportFragmentManager().getFragments();

//            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.oper_list);
            Fragment fragment=fragments.get(0);
            layoutManager =((OperationListFragment)fragment).getmRecyclerView().getLayoutManager();
        }
        mViewPager.setCurrentItem(position);

        View  currentTab = tabLayout.getTabAt(position).getCustomView();

        currentTab.animate().alphaBy(0.8f);
//                .setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.white));
//        tabLayout.getTabAt(position).setCustomView()
        //        ToastUtils.showDisplay("position selected "+position);

    }
    /**
         * @author  CF
         * @date   2019/1/15
         * @description
     *
     *    在线状态获取远端用户信息
     *    离线状态使用默认布局 和默认的头像
     * 1、登录状态   隐藏点击登录view    获取用户最新信息 显示昵称 显示电话 载入头像  背景加载 显示登出按钮
     * 2、退出状态  显示点击登录view   隐藏昵称和电话 使用默认头像 默认背景 隐藏登出按钮
     *
     */
    private void initUserInfoRelViewValueByLoginState() {
        if(ClientConfiguration.getInstance().getLoginState()){
            getUserInfo();
        }else{
            initUserInfoRelViewValueWithNotLogin();

        }


    }
  /**
     * @author  CF
     * @date   2019/1/15
     * @description 未登录状态 初始化用户相关信息
     *
     */
    private void initUserInfoRelViewValueWithNotLogin() {
        //           未登录 显示 点击登录
        navLoginText.setVisibility(View.VISIBLE);
//        导航头像更换
        UtilBox.box().picasso.loadDrawResIntoView(navTopLeftCircleImageView,
                R.drawable.default_head);
//        侧边栏头像更换
        UtilBox.box().picasso.loadDrawResIntoView(
                headImageView,
                R.drawable.default_head);
//        昵称
        nickName.setVisibility(View.INVISIBLE);
//        手机
        mail.setVisibility(View.INVISIBLE);
//        登出item隐藏
        loginItem.setVisible(false);
//      初始化背景图片
        initMainBgImg();

    }

    private void initMainBgImg() {
        new DownloadImageTask().execute(ClientConfiguration.getInstance().getMainBgUrl()) ;
    }

    private void initAllView() {
        drawerLayout = findViewById(R.id.manga_drawer_lay_out);
        appBarLayout=findViewById(R.id.brvah_app_bar);
        navView = findViewById(R.id.nav_view);
        navTopLeftCircleImageView =  findViewById(R.id.nav_topLeft_image);
        headImageView = navView.getHeaderView(0).findViewById(R.id.head_image);
        nickName = navView.getHeaderView(0).findViewById(R.id.nav_nick_name);
        mail = navView.getHeaderView(0).findViewById(R.id.nav_mail);
        navLoginText=navView.getHeaderView(0).findViewById(R.id.nav_login_text);

        nickName.setOnClickListener(this);
        headImageView.setOnClickListener(this);
        mail.setOnClickListener(this);
        navLoginText.setOnClickListener(this);

        floatBackToTopBut = findViewById(R.id.float_but);



        loginItem= navView.getMenu().findItem(R.id.nav_login_out);

    }

    private void appBarToggleSet() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {

               initUserInfoRelViewValueByLoginState();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }



    @Override
    public void onScrollToStart() {
       ToastUtils.showDisplay("滑动到顶部了");
    }

    @Override
    public void onScrollToEnd() {
        ToastUtils.showDisplay("滑动到底部了");

    }


    private void initFloatBut() {
//        layoutManager = mRecyclerView.getLayoutManager();
        floatBackToTopBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilBox.box().ui.MoveToPosition((LinearLayoutManager) layoutManager,0);
//                UIUtils.MoveToPosition(new LinearLayoutManager(mContext), recyclerview, 0);
                hideViews();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action",

                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtils.showDisplay("undo action");
                                    }
                                }).show();*/
            }
        });
    }

    private void setNavigationViewListener() {

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                drawerLayout.closeDrawers();

                if(menuItem.getItemId()==R.id.nav_home){
                    drawerLayout.closeDrawers();
                }else if(menuItem.getItemId()==R.id.nav_history){
                    startAction(BrvahTestActivity.this,null,DrawerDemoActivity.class);
                }else if(menuItem.getItemId()==R.id.nav_collect){
                    startAction(BrvahTestActivity.this,null,TabTestActivity.class);
                }else if(menuItem.getItemId()==R.id.nav_login_out){
                    clearLoginStateAndUserInfo();
                    startAction(BrvahTestActivity.this,null,LoginActivity.class);
                }else{
                    ToastUtils.showDisplay(String.valueOf(menuItem.getTitle()));
                }
                return true;
            }
        });
    }

    private void clearLoginStateAndUserInfo() {
        GlobalSettings.userInfo=null;
        ClientConfiguration.getInstance().setUid("");
        ClientConfiguration.getInstance().setSid("");
        ClientConfiguration.getInstance().setLoginState(false);
    }

    private void initNavHeaderViewValueWithLoginState() {
//        隐藏 点击登录提示语
        navLoginText.setVisibility(View.GONE);
//        导航头像更换
        UtilBox.box().picasso.loadUrlResIntoViewWithDefault(navTopLeftCircleImageView,GlobalSettings.userInfo.getHeadImage(),
                R.drawable.vector_drawable_nav_profile_grey___);
//        侧边栏头像更换
        UtilBox.box().picasso.loadUrlResIntoViewWithDefault(
                headImageView,GlobalSettings.userInfo.getHeadImage(),
                R.drawable.vector_drawable_nav_profile_grey___);
//        昵称
        nickName.setText(GlobalSettings.userInfo.getNickName());
        nickName.setVisibility(View.VISIBLE);
//        手机
        mail.setText(GlobalSettings.userInfo.getPhone());
        mail.setVisibility(View.VISIBLE);

        loginItem.setVisible(true);

        initMainBgImg();

    }

    private void getUserInfo() {

        RequestParams params=RequestParams.getFormDataParam(2);
        ClientUtlis.post(true, UrlConfig.GET_USER_INFO,params,
                this,new JsonCallback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo info, String msg) {
                        if(info!=null){
                            GlobalSettings.userInfo = info;
                            ClientConfiguration.getInstance().setMainBgUrl(info.getMainBgUrl());
                            initNavHeaderViewValueWithLoginState();
                        }else{
                            Log.d(TAG,"");
                        }

                    }

                    @Override
                    public void onError(String msg, int code) {
                        ToastUtils.showDisplay(msg);
                    }
                });
    }
    private void setToolbarStyle(Toolbar toolbar) {


//        toolbar.setTitle("Title");
//        toolbar.setTitleTextColor(Color.YELLOW);
        // 副标题
//        toolbar.setSubtitle("Sub title");
//        toolbar.setSubtitleTextColor(Color.parseColor("#FFF0D0"));


        //设置toolBar上的MenuItem点击事件
//        注意：需要將toolbar.setOnMenuItemClickListener()设定在 setSupportActionBar 之后才有作用
//       （setNavigationIcon也需要放在 setSupportActionBar之后才会生效）
      /*  toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_share:
                        ToastUtils.showDisplay("click share");
                        break;
                    case R.id.action_overflow:
                        //弹出自定义overflow
//                        popUpMyOverflow();
                        break;
                    case R.id.nav_topLeft_image:
                        drawerLayout.openDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });*/
        //ToolBar里面还可以包含子控件
      /*  toolbar.findViewById(R.id.btn_diy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showDisplay("点击自定义按钮");

            }
        });*/
      /*  toolbar.findViewById(R.id.manga_toolbar_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showDisplay("点击自定义标题");
            }
        });*/

        toolbar.findViewById(R.id.nav_topLeft_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
//                ToastUtils.showDisplay("点击自定义图标");
            }
        });
       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showDisplay("click NavigationIcon");
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });*/

    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initUserInfoRelViewValueByLoginState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_my, menu);
//        menu.getItem(0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
           /* case R.id.action_share:
                ToastUtils.showDisplay("click share");
                break;*/
            case R.id.action_overflow:
                //弹出自定义overflow
                popUpMyOverflow();
                break;
           /* case R.id.nav_topLeft_image:
                ToastUtils.showDisplay("onOptionsItemSelected 点击自定义图标");
                drawerLayout.openDrawer(GravityCompat.START);
                break;*/

        }
        return true;
    }

    /**
     * 弹出自定义的popWindow
     */
    public void popUpMyOverflow() {

        if (null == mPopupWindow) {
            //获取状态栏高度
            getToolBarHeight();
            //初始化PopupWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.action_overflow_popwindow, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让它显示在右上角。
            mPopupWindow.showAtLocation(toolbar, Gravity.RIGHT | Gravity.TOP, 0, popMenuYoffset);
            //设置item的点击监听
            popView.findViewById(R.id.ll_item1).setOnClickListener(this);
            popView.findViewById(R.id.ll_item2).setOnClickListener(this);

            popItem1TextView= popView.findViewById(R.id.ll_item1_text);
            popItem2TextView= popView.findViewById(R.id.ll_item2_text);
            popBgChangeText = popView.findViewById(R.id.setting_menu_bg_change_text);
            popBgChangeText.setOnClickListener(this);

        } else {
            mPopupWindow.showAtLocation(toolbar, Gravity.RIGHT | Gravity.TOP, 0, popMenuYoffset);
        }

    }

    private void getToolBarHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度+toolbar的高度
        popMenuYoffset= frame.top + toolbar.getHeight();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_item1:
                ToastUtils.showDisplay(String.valueOf(popItem1TextView.getText()));
                closePoupWindow();
                break;
            case R.id.ll_item2:
                ToastUtils.showDisplay(String.valueOf(popItem2TextView.getText()));
                if (CustomProperties.UPDATE.equals( GlobalSettings.settingProperties.getUpdateSign())) {
                    //下载更新
                    Intent startDownload = new Intent(this, DownLoadService.class);
                    startServiceCustom(this, startDownload);
                } else {
                    ToastUtils.showDisplay("暂未发现新的版本哦^^");
                }
                closePoupWindow();
                break;
            case R.id.setting_menu_bg_change_text:
               openAlbum(CHANGE_BACK_OPEN_LOCAL_ALBUM);
                break;
            case R.id.head_image:
                clickHeadScopeWork();
                break;
            case R.id.manga_toolbar_search_input:
                searchText.setFocusable(true);
                break;

            case R.id.nav_login_text:
                clickHeadScopeWork();
                break;
            case R.id.nav_nick_name:
                UtilBox.box().dialog.showInputMaterialDialogSimple(BrvahTestActivity.this,
                        "修改昵称", "请输入昵称：", "", String.valueOf(nickName.getText())
                        , new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (which == DialogAction.POSITIVE) {
//                            前往设置页面
                                    changeNickName();
                                }
                            }
                        }, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                nickNameInput = String.valueOf(input);
                                Log.i(TAG, "输入的是：" + input);
                            }
                        });
//            case R.id.nav_nick_name:
           /* case R.id.:
                break;*/
        }

    }

    private void clickHeadScopeWork() {
        if(ClientConfiguration.getInstance().getLoginState()){
//            登录状态 修改头像
            openAlbum(CHANGE_HEAD_OPEN_LOCAL_ALBUM);
        }else{
//            前往登录
            LoginActivity.actionStart(this,null);
        }
    }

    private void closePoupWindow() {
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     *更改昵称
     *
     */
    private void changeNickName() {
        RequestParams params=new RequestParams(RequestParams.PARAM_TYPE_FORM);
        if(StringUtil.isEmpty(nickNameInput)){
            ToastUtils.showDisplay("昵称不可为空");
        }
//        校验 非法字符这些
        nickNameCheck();
        OkGo.getInstance().getCommonHeaders().put("userid",GlobalSettings.userInfo.getUid());
        params.put("userid",GlobalSettings.userInfo.getUid());
        params.put("nickName",nickNameInput);
        ClientUtlis.post(true,UrlConfig.MODIFY_USER_INFO,params,this,new JsonCallback<String>() {
            @Override
            public void onSuccess(String info, String msg) {
                nickName.setText(nickNameInput);
               ToastUtils.showDisplay("修改成功!");
            }
            @Override
            public void onError(String msg, int code) {
                ToastUtils.showDisplay("昵称修改失败!");
            }
            });
    }

    private void nickNameCheck() {

    }

    private void openAlbum(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*")
         .addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void requestPermission() {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    /**
     * @author CF
     * @date 2018/12/29
     * @description
     */
    @SuppressLint("ResourceAsColor")
    private void initAdapter() {
        homeAdapter = new MyQuickAdapter(R.layout.manga_item, mangaList, 0, getViewIemHeight(6, this));
        //放大动画
//        homeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        homeAdapter.openLoadAnimation();
//        homeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        BaseQuickAdapter.SLIDEIN_LEFT 左边划入动画
        topView = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        UtilBox.box().picasso.loadDrawResIntoView(((ImageView) topView.findViewById(R.id.top_header_view_bac_image)),
                R.drawable.manga_top_view_bg);
        homeAdapter.addHeaderView(topView);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
//                startActivity(intent);
                ToastUtils.showDisplay("ItemClick:" + mangaList.get(position).getTitle());
            }
        });
        homeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showDisplay("ItemLongClick:" + mangaList.get(position).getTitle());
                return false;
            }
        });

        homeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                String clickItem = getClickItemType(view);
                ToastUtils.showDisplay("ItemChildClick : " + clickItem);
            }
        });

        homeAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                String clickItem = getClickItemType(view);
                ToastUtils.showDisplay("ItemChildLongClick : " + clickItem);
                return false;
            }


        });
        //默认动画只是第一次加载 所以设置下每次都加载动画
        homeAdapter.isFirstOnly(false);
        mRecyclerView.setAdapter(homeAdapter);
    }

    @NonNull
    private String getClickItemType(View view) {
        String clickItem = "";
        switch (view.getId()) {
            case R.id.manga_cover:
                clickItem = "manga-cover";
                break;
            case R.id.manga_title:
                clickItem = "manga-title";
                break;
            default:
                break;
        }
        return clickItem;
    }

    private void initData() {
        RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
//        mangaList=(new ClientUtlis<Manga>()).getBizList(params,UrlConfig.MANGA_LIST,this,"列表获取异常",mangaList);
        getBizList(params, UrlConfig.MANGA_LIST, this, "列表获取异常");

    }


    private void getBizList(RequestParams params, UrlConfig url, Object tag, final String errMsg) {
        ClientUtlis.post(true, url, params,
                tag, new JsonCallback<List<Manga>>() {
                    @Override
                    public void onSuccess(List<Manga> list, String msg) {
                        if (list.isEmpty()) {
                            mangaList = defaultList;
                        } else {
                            mangaList = list;
                        }
                        homeAdapter.setNewData(mangaList);
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Log.i(TAG, "获取列表失败，reason:" + msg);
                        ToastUtils.showDisplay(errMsg);
                    }
                });
    }

    /*private void initRecycleView() {
        mRecyclerView = findViewById(R.id.brvah_book_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setBackgroundResource(R.drawable.default_head);



        //setting up our OnScrollListener
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(-1)){
//                  返回false表示不能往下滑动，即代表到顶部了
//                  隐藏到顶部的浮动按钮
                    hideViews();
                }else{
//                    显示浮动按钮
                    showViews();
                }
//                recyclerView.computeVerticalScrollOffset()

//                判断是否滑动到底部， recyclerView.canScrollVertically(1);返回false表示不能往上滑动，即代表到底部了；
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }*/


    public  void hideViews() {
//      显示toolbar
        appBarLayout.setExpanded(true,true);
              /*  a.getBoolean(com.google.android.material.R.styleable.AppBarLayout_expanded, false),
                false, *//* animate *//*
                false *//* force *//*);*/
//        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        Log.d(TAG,String.valueOf(toolbar.getTranslationY()));
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) floatBackToTopBut.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        floatBackToTopBut.setVisibility(View.INVISIBLE);
        floatBackToTopBut.animate().translationY(floatBackToTopBut.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void showViews() {
//        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        floatBackToTopBut.setVisibility(View.VISIBLE);
        floatBackToTopBut.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            String  path= resultUri.getPath();
            try {
                List<File> files = new ArrayList<>();
                files.add(CompressHelper.getDefault(this).compressToFile(new File(path)));
                Log.d(TAG, "changeHead: " + files.get(0).length());
                Uri destUri=data.getExtras().getParcelable(UCrop.EXTRA_OUTPUT_URI);
                doAlterReqByDestUriPath(files, destUri);
            } catch (Exception e) {
                Log.e(TAG,"修改头像失败",e);
                ToastUtils.showLong("修改头像失败!");

            }


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e(TAG,"修改头像失败 2 "+cropError.getMessage());
            ToastUtils.showLong("修改头像失败!");

        } else if(resultCode == RESULT_OK &&requestCode==CHANGE_HEAD_OPEN_LOCAL_ALBUM){
            sourceHeadImageUri= data.getData();
            try {
                cropImage();
            } catch (Exception e) {
                Log.e(TAG, " UCrop.of failed", e);
            }
        } else if(resultCode == RESULT_OK &&requestCode==CHANGE_BACK_OPEN_LOCAL_ALBUM){
            try {
                cropImage(data.getData(), SET_MENU_BACK_CACHE_FILE_NAME,true);
            } catch (Exception e) {
                Log.e(TAG, " UCrop.of failed", e);
            }
        }
    }
/**
     * @author  CF
     * @date   2019/1/16
     * @description
     * 根据发起请求的目标Uri包含的path分别来进行不同的请求
     * 要求每个裁剪图片都需要有不同的临时文件名称
     *
     */
    private void doAlterReqByDestUriPath(List<File> files, Uri destUri) {
        if(destUri.getPath().contains(SET_MENU_BACK_CACHE_FILE_NAME)){
//                    修改背景图片
            modifyBackgroundImage(files);
        }else  if(destUri.getPath().contains(HEAD_CACHE_FILE_NAME)){
//                    修改头像
            modifyHeadImageReq(files);
        }
    }

    private void modifyBackgroundImage(List<File> files) {
        RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
        ClientUtlis.uploadFiles(this, UrlConfig.MODIFY_BACKGROUND_IMG, params, "file", files, this, new JsonCallback<String>() {
            @Override
            public void onSuccess(String imagePath, String msg) {
                super.onSuccess(imagePath, msg);
                Log.i(TAG,"change head succ imagePath="+imagePath);
                if(!StringUtil.isEmpty(imagePath)&&imagePath.contains("http")){
                    refreshBackgroundRelView(imagePath);
                }
                ToastUtils.showDisplay("背景修改成功！");

            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                Log.i(TAG,"背景修改失败,"+msg);
                ToastUtils.showDisplay("背景修改失败");

            }
        });
    }

    private void refreshBackgroundRelView(String imagePath) {
        ClientConfiguration.getInstance().setMainBgUrl(imagePath);
        initMainBgImg();


    }


    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        View view ;
        @Override
        protected Drawable doInBackground(String... urls) {
            return loadImageFromNetwork(urls[0]);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            drawerLayout.setBackground(result);
        }
    }

    private Drawable loadImageFromNetwork(String imageUrl) {
            Drawable drawable = null;
            try {
                // 可以在这里通过文件名来判断，是否本地有此图片
                drawable = Drawable.createFromStream(
                        new URL(imageUrl).openStream(), "main_bg.jpg");
            } catch (IOException e) {
                Log.d("test", e.getMessage());
            }
            if (drawable == null) {
                Log.d("test", "null drawable");
            } else {
                Log.d("test", "not null drawable");
            }

            return drawable;
    }
    /**
     *
     *
     */
    private void cropImage() {
        uCrop= UCrop.of(sourceHeadImageUri, Uri.fromFile(new File(getCacheDir(), HEAD_CACHE_FILE_NAME)));
        uCrop = UtilBox.box().ui.advancedConfig (uCrop);
        uCrop.start(BrvahTestActivity.this);

     /*   if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
            setupFragment(uCrop);
        } else {                                                        // else start uCrop Activity
            uCrop.start(BrvahTestActivity.this);
        }*/


    }
/**
     * @author  CF
     * @date   2019/1/16
     * @description
     *
     */
    private void cropImage(Uri  sourceImageUri,String destCacheFileName,boolean freeStyle) {
        uCrop= UCrop.of(sourceImageUri, Uri.fromFile(new File(getCacheDir(), destCacheFileName)));
        if(freeStyle){
            uCrop = UtilBox.box().ui.advancedConfigWithFreeStyle (uCrop);
        }else{
            uCrop = UtilBox.box().ui.advancedConfig (uCrop);
        }
        uCrop.start(BrvahTestActivity.this);

    }




    /**
     * @author  CF
     * @date   2019/1/14
     * @description
     *
     */
    private void modifyHeadImageReq(List<File> files) {
        RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
        ClientUtlis.uploadFiles(this, UrlConfig.MODIFY_HEAD_IMG, params, "file", files, this, new JsonCallback<String>() {
            @Override
            public void onSuccess(String imagePath, String msg) {
                super.onSuccess(imagePath, msg);
                Log.i(TAG,"change head succ imagePath="+imagePath);
                if(!StringUtil.isEmpty(imagePath)&&imagePath.contains("http")){
                    refreshHeadImageRelView(imagePath);
                }

                ToastUtils.showDisplay("修改成功！");

            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                ToastUtils.showDisplay(msg);

            }
        });

    }
    /**
         * @author  CF
         * @date   2019/1/15
         * @description 更新头像相关的view
         *
         */
    private void refreshHeadImageRelView(String imagePath) {
        UtilBox.box().picasso.loadUrlResIntoViewWithDefault(
                headImageView,imagePath,R.drawable.default_head);
        UtilBox.box().picasso.loadUrlResIntoViewWithDefault(
                navTopLeftCircleImageView,imagePath,R.drawable.default_head);
        GlobalSettings.userInfo.setHeadImage(imagePath);
    }
    public  static void actionStart(Context context, Map<String,String> map){
        startAction(context, map,BrvahTestActivity.class);
    }
    public static void main(String[] args) {
        double transparentRatio=0.85;
        String  db=String.valueOf(255*(1-transparentRatio));
        db.subSequence(0,db.indexOf("."));
        Integer x = Integer.parseInt(db.substring(0,db.indexOf(".")));
        String hex = x.toHexString(x);
        System.out.println(hex);

       /* String hex = "0xfff";
        Integer x = Integer.parseInt(hex.substring(2),16);//从第2个字符开始截取
        System.out.println(x);*/
    }
}
