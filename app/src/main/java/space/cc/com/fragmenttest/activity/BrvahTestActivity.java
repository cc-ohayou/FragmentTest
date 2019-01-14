package space.cc.com.fragmenttest.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.lzy.okgo.OkGo;
import com.nanchen.compresshelper.CompressHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
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
import space.cc.com.fragmenttest.domain.GlobalSettings;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.bizobject.CustomProperties;
import space.cc.com.fragmenttest.domain.bizobject.UserInfo;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.StringUtil;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.litepals.Manga;
import space.cc.com.fragmenttest.service.DownLoadService;
import space.cc.com.fragmenttest.util.CustomScrollView;
import space.cc.com.fragmenttest.util.UtilBox;


public class BrvahTestActivity extends BaseActivity implements View.OnClickListener ,CustomScrollView.OnScrollChangeListener {


    private static final String TAG = "BrvahTestActivity";
    private static final String HEAD_CACHE_FILE_NAME ="cc_head_cache" ;
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
    private  CircleImageView headImageView;
    private  CircleImageView navTopLeftCircleImageView;
    int popMenuYoffset;
    //头像来源uri对象
    Uri sourceHeadImageUri;
//    头像目标存储uri
    TextView nickName;
    TextView mail;
    private UCrop uCrop;
    private String nickNameInput="";

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
            initAllView();
            scrollerView=findViewById(R.id.scrollBar);
            toolbar = findViewById(R.id.manga_toolbar);
//        toolbar.setLogo(R.drawable.app_icon2);
            // 主标题,默认为app_label的名字  设置Title为空""则不显示  注意需要在setSupportActionBar之前
            toolbar.setTitle("");
            setToolbarStyle(toolbar);
            setSupportActionBar(toolbar);
            drawerLayout = findViewById(R.id.manga_drawer_lay_out);
            appBarToggleSet();
            //侧边栏菜单 监听
            setNavigationViewListener();
            login();

//            initActionBar();
            //初始化recycleView
            initRecycleView();
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

    private void initAllView() {
        navView = findViewById(R.id.nav_view);
        navTopLeftCircleImageView =  findViewById(R.id.nav_topLeft_image);
        headImageView = navView.getHeaderView(0).findViewById(R.id.head_image);
        nickName = navView.getHeaderView(0).findViewById(R.id.nav_nick_name);
        mail = navView.getHeaderView(0).findViewById(R.id.nav_mail);
        floatBackToTopBut = findViewById(R.id.float_but);

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
                    ToastUtils.showDisplay(String.valueOf(menuItem.getTitle()));
                }
                return true;
            }
        });
    }

    private void initNavHeaderViewValue() {
        UtilBox.box().picasso.loadUrlResIntoViewWithDefault(navTopLeftCircleImageView,GlobalSettings.userInfo.getHeadImage(), R.drawable.a2_profile);
        headImageView.setOnClickListener(this);
        UtilBox.box().picasso.loadUrlResIntoViewWithDefault(
                headImageView,GlobalSettings.userInfo.getHeadImage(),
                R.drawable.default_head);

        nickName.setText(GlobalSettings.userInfo.getNickName());
        nickName.setOnClickListener(this);

        mail.setText(GlobalSettings.userInfo.getMail());
        mail.setOnClickListener(this);
    }
    private void login() {
        RequestParams params=new RequestParams(RequestParams.PARAM_TYPE_FORM);
        params.put("userName","13758080693");
        params.put("pwd","123123ee");
        ClientUtlis.post(true, UrlConfig.USER_LOGIN.getValue(),params,
                this,new JsonCallback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo info, String msg) {
                        if(info!=null){
                            GlobalSettings.userInfo = info;
                            initNavHeaderViewValue();
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
                login();
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
                login();
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
                ToastUtils.showDisplay(String.valueOf(popItem1TextView.getText()));
                closePoupWindow();
                break;
            case R.id.ll_item2:
                ToastUtils.showDisplay(String.valueOf(popItem2TextView.getText()));
                if (CustomProperties.UPDATE.equals(settingProperties.getUpdateSign())) {
                    //下载更新
                    Intent startDownload = new Intent(this, DownLoadService.class);
                    startServiceCustom(this, startDownload);
                } else {
                    ToastUtils.showDisplay("暂未发现新的版本哦^^");
                }
                closePoupWindow();
                break;
            case R.id.head_image:
                openAlbum();
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
        ClientUtlis.post(true,UrlConfig.MODIFY_USER_INFO.getValue(),params,this,new JsonCallback<String>() {
            @Override
            public void onSuccess(String info, String msg) {
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

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*")
         .addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(intent, SELECT_LOCAL_IMAGE);
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

    private void initRecycleView() {
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
//                    ToastUtils.showDisplay(String.valueOf(toolbar.getTranslationY()));

                }else{
//                    显示浮动按钮
                    showViews();
//                    ToastUtils.showDisplay(String.valueOf(toolbar.getTranslationY()));

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
        float curY = toolbar.getTranslationY();
        ObjectAnimator oa = ObjectAnimator.ofFloat(toolbar,"translationY",curY,0f);//toolbar原位置是0
        oa.setDuration(2000);
        oa.start();

//        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        Log.d(TAG,String.valueOf(toolbar.getTranslationY()));
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) floatBackToTopBut.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        floatBackToTopBut.animate().translationY(floatBackToTopBut.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
//        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

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
                UtilBox.box().picasso.loadUriRes(
                        headImageView,resultUri);
                modifyHeadImageReq(files);
            } catch (Exception e) {
                Log.e(TAG,"修改头像失败",e);
                ToastUtils.showLong("修改头像失败!");

            }


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e(TAG,"修改头像失败 2 "+cropError.getMessage());
            ToastUtils.showLong("修改头像失败!");

        } else if(resultCode == RESULT_OK &&requestCode==SELECT_LOCAL_IMAGE){
            sourceHeadImageUri= data.getData();
            try {
//            Uri realDestUri=new Uri.Builder().path(realPath).build();
                cropImage();
            } catch (Exception e) {
                Log.e(TAG, " UCrop.of failed", e);
            }
        }
    }

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
     * @date   2019/1/14
     * @description
     *
     */
    private void modifyHeadImageReq(List<File> files) {
        RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
        OkGo.getInstance().getCommonHeaders().put("userid","1");
        ClientUtlis.uploadFiles(this, UrlConfig.MODIFY_HEAD_IMG.getValue(), params, "file", files, this, new JsonCallback<String>() {
            @Override
            public void onSuccess(String imagePath, String msg) {
                super.onSuccess(imagePath, msg);
                ToastUtils.showDisplay("修改成功！");

            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                ToastUtils.showDisplay(msg);

            }
        });
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
