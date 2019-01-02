package space.cc.com.fragmenttest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.MyQuickAdapter;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.litepals.Manga;


public class BrvahTestActivity extends BaseActivity {
    private static final String TAG = "BrvahTestActivity";
    private List<Manga> mangaList;
    private static List<Manga> defaultList;
    private RecyclerView mRecyclerView;
    MyQuickAdapter homeAdapter;
    private DrawerLayout drawerLayout;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brvah_test);
        Toolbar toolbar = findViewById(R.id.manga_toolbar);
//        toolbar.setLogo(R.drawable.app_icon2);
        // 主标题,默认为app_label的名字
        setSupportActionBar(toolbar);
//        setToobarStyle(toolbar);
        drawerLayout = findViewById(R.id.manga_drawer_lay_out);
        NavigationView navView = findViewById(R.id.nav_view);
        //侧边栏菜单 监听
        setNavigationViewListener(navView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            setHomeButtonEnabled(true) //设置返回键可用
//            setDisplayHomeAsUpEnabled(true) //设置返回键显示
            actionBar.setHomeAsUpIndicator(R.drawable.home_32_1);
        }

        initView();
        initAdapter();
//      远端加载数据
        initData();

//        int standardHeight=getViewIemWidth(4,BrvahTestActivity.this);


    }

    private void setNavigationViewListener(NavigationView navView) {
//        navView.setCheckedItem(R.id.nav_collect);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                drawerLayout.closeDrawers();
                ToastUtils.showDisplay(menuItem.getTitle());
                return true;
            }
        });
    }

    private void setToobarStyle(Toolbar toolbar) {
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
        toolbar.findViewById(R.id.manga_toolbar_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showDisplay("点击自定义标题");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_my, menu);
        menu.getItem(0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_share:
                ToastUtils.showDisplay("click share");
                break;
            case R.id.action_overflow:
                //弹出自定义overflow
//                        popUpMyOverflow();
                ToastUtils.showDisplay("click action_overflow");

                break;
            case R.id.nav_topLeft_image:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return true;
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
    private void initAdapter() {
        homeAdapter = new MyQuickAdapter(R.layout.manga_item, mangaList, 0, getViewIemHeight(5, this));
//         homeAdapter = new MyQuickAdapter(R.layout.manga_item, mangaList);
//        homeAdapter.openLoadAnimation();
        //放大动画
        homeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        BaseQuickAdapter.SLIDEIN_LEFT 左边划入动画
        View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        homeAdapter.addHeaderView(top);
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
        RequestParams params = new RequestParams(1);
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
    }


}
