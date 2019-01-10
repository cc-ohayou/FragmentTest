package space.cc.com.fragmenttest.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.MyQuickAdapter;
import space.cc.com.fragmenttest.adapter.base.BaseQuickAdapter;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.bizobject.CustomProperties;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.listener.HidingScrollListener;
import space.cc.com.fragmenttest.litepals.Manga;
import space.cc.com.fragmenttest.service.DownLoadService;
import space.cc.com.fragmenttest.util.CustomScrollView;
import space.cc.com.fragmenttest.util.UtilBox;


public class BrvahTestActivity extends BaseActivity implements View.OnClickListener ,CustomScrollView.OnScrollChangeListener {
    private static final String TAG = "BrvahTestActivity";
    private List<Manga> mangaList;
    private static List<Manga> defaultList;
    private RecyclerView mRecyclerView;
    MyQuickAdapter homeAdapter;
    private DrawerLayout drawerLayout;
    PopupWindow mPopupWindow;
    Toolbar toolbar;
    NavigationView navView;
    View topView;
    TextView popItem1TextView;
    TextView popItem2TextView;
    FloatingActionButton floatBackToTopBut;
    private ScrollView scrollerView;
    RecyclerView.LayoutManager layoutManager;
    
    int popMenuYoffset;
    
    private static String defaultData = "[{\"area\":\"中国大陆\",\"arealimit\":317,\"attention\":352150,\"bangumi_id\":0,\"nowEpisode\":\"11\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/8f19c704a6516bab4716931db10621c1b6c6e4bf.jpg\",\"danmaku_count\":105072,\"ep_id\":258700,\"favorites\":352150,\"is_finish\":0,\"lastupdate\":1545883200,\"lastupdate_at\":\"2018-12-27 12:00:00\",\"isNew\":false,\"play_count\":8263731,\"pub_time\":\"\",\"season_id\":25823,\"season_status\":13,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/67a851a5cd87fef2ddd6f9bd6cf691781a1d8b95.jpg\",\"title\":\"画江湖之不良人 第三季\",\"viewRank\":0,\"weekday\":4},{\"area\":\"中国大陆\",\"arealimit\":316,\"attention\":12083,\"bangumi_id\":0,\"nowEpisode\":\"4\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/8df12fc092928ba57069eb16e077f9d719f345e5.jpg\",\"danmaku_count\":10022,\"ep_id\":258686,\"favorites\":12083,\"is_finish\":0,\"lastupdate\":1545876000,\"lastupdate_at\":\"2018-12-27 10:00:00\",\"isNew\":false,\"play_count\":486760,\"pub_time\":\"\",\"season_id\":26176,\"season_status\":2,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/c96b4daa6f3f1925184bc7835a6a26ee573d6970.jpg\",\"title\":\"通灵妃 河南话版\",\"viewRank\":0,\"weekday\":4},{\"area\":\"中国大陆\",\"arealimit\":316,\"attention\":151420,\"bangumi_id\":0,\"nowEpisode\":\"10\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/57dc8fdf8373f8c1facfc2911d50be1cfc6da18a.jpg\",\"danmaku_count\":26706,\"ep_id\":258689,\"favorites\":151420,\"is_finish\":0,\"lastupdate\":1545876000,\"lastupdate_at\":\"2018-12-27 10:00:00\",\"isNew\":false,\"play_count\":2023158,\"pub_time\":\"\",\"season_id\":24888,\"season_status\":2,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/ef2f039b71564cc469b2cac9d8f2879e1259a74f.jpg\",\"title\":\"山河社稷图\",\"viewRank\":0,\"weekday\":4}]";

    static {
        try {
            defaultList = JSON.parseArray(defaultData, Manga.class);
        } catch (Exception e) {
            Log.e(TAG, "defaultData transfer error", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_brvah_test);
            scrollerView=findViewById(R.id.scrollBar);
            toolbar = findViewById(R.id.manga_toolbar);
//        toolbar.setLogo(R.drawable.app_icon2);
            // 主标题,默认为app_label的名字  设置Title为空""则不显示  注意需要在setSupportActionBar之前
            toolbar.setTitle("");
            setToolbarStyle(toolbar);
            setSupportActionBar(toolbar);
            appBarToggleSet();
            drawerLayout = findViewById(R.id.manga_drawer_lay_out);
            navView = findViewById(R.id.nav_view);
            //侧边栏菜单 监听
            setNavigationViewListener();
//            initActionBar();
            //初始化recycleView
            initView();
//            初始化homeAdapter
            initAdapter();
//      远端加载数据
            initData();
            initFloatBut();
//            scrollerView.onSc


        } catch (Exception e) {
            Log.e(TAG, "onCreate error", e);

        }
//        int standardHeight=getViewIemWidth(4,BrvahTestActivity.this);


    }

    private void appBarToggleSet() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            setHomeButtonEnabled(true) //设置返回键可用
//            setDisplayHomeAsUpEnabled(true) //设置返回键显示
//            actionBar.setHomeAsUpIndicator(R.drawable.nav_left_white_16);
        }
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
       floatBackToTopBut = findViewById(R.id.float_but);

        floatBackToTopBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mRecyclerView.getF
                UtilBox.box().ui.MoveToPosition((LinearLayoutManager) layoutManager,0);
//                UIUtils.MoveToPosition(new LinearLayoutManager(mContext), recyclerview, 0);
//               显示toolbar
//                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
                hideViews();
                /* scrollerView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollerView.post(new Runnable() {
                            public void run() {
                                // 滚动至顶部
                                scrollerView.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                });*/
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
//        navView.setCheckedItem(R.id.nav_collect);
        UtilBox.box().picasso.loadDrawResReSize(
                (CircleImageView) navView.getHeaderView(0).findViewById(R.id.head_image),
                R.drawable.default_head,0,0);
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
                }else{
                    ToastUtils.showDisplay(menuItem.getTitle());
                }
                return true;
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

        UtilBox.box().picasso.loadDrawResIntoView((CircleImageView) findViewById(R.id.nav_topLeft_image), R.drawable.a2_profile);
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
                ToastUtils.showDisplay(popItem1TextView.getText());
                break;
            case R.id.ll_item2:
                ToastUtils.showDisplay(popItem2TextView.getText());

               if(CustomProperties.UPDATE.equals(settingProperties.getUpdateSign())){
                   //下载更新
                   Intent startDownload = new Intent(this, DownLoadService.class);
                   startServiceCustom(this,startDownload);
               }else{
                   ToastUtils.showDisplay("暂未发现新的版本哦^^");
               }
                break;

        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
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
        homeAdapter = new MyQuickAdapter(R.layout.manga_item, mangaList, 0, getViewIemHeight(5, this));
//         homeAdapter = new MyQuickAdapter(R.layout.manga_item, mangaList);
//        homeAdapter.openLoadAnimation();
        //放大动画
//        homeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        homeAdapter.openLoadAnimation();
//        homeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        BaseQuickAdapter.SLIDEIN_LEFT 左边划入动画
//        CardView
        topView = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        UtilBox.box().picasso.loadDrawResIntoView(((ImageView) topView.findViewById(R.id.top_header_view_bac_image)),
                R.drawable.manga_top_view_bg);
        topView.setBackgroundColor(R.color.mangaTopViewBack);
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
        ClientUtlis.post(true, url.getValue(), params,
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

    private void initView() {
        mRecyclerView = findViewById(R.id.brvah_book_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setBackgroundResource(R.drawable.default_head);

       layoutManager = mRecyclerView.getLayoutManager();

        //setting up our OnScrollListener
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }


    private void hideViews() {
//        显示toolbar
//        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) floatBackToTopBut.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        floatBackToTopBut.animate().translationY(floatBackToTopBut.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
//        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        floatBackToTopBut.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
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
