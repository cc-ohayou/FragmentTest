package space.cc.com.fragmenttest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.List;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.QuickBookAdapter;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.litepals.Manga;

public class BrvahTestActivity extends AppCompatActivity {
    private static final String TAG = "BrvahTestActivity";
    private List<Manga> mangaList;
    private static List<Manga> defaultList;
    private RecyclerView mRecyclerView;
    private static String defaultData="[{\"area\":\"中国大陆\",\"arealimit\":317,\"attention\":352150,\"bangumi_id\":0,\"nowEpisode\":\"11\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/8f19c704a6516bab4716931db10621c1b6c6e4bf.jpg\",\"danmaku_count\":105072,\"ep_id\":258700,\"favorites\":352150,\"is_finish\":0,\"lastupdate\":1545883200,\"lastupdate_at\":\"2018-12-27 12:00:00\",\"isNew\":false,\"play_count\":8263731,\"pub_time\":\"\",\"season_id\":25823,\"season_status\":13,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/67a851a5cd87fef2ddd6f9bd6cf691781a1d8b95.jpg\",\"title\":\"画江湖之不良人 第三季\",\"viewRank\":0,\"weekday\":4},{\"area\":\"中国大陆\",\"arealimit\":316,\"attention\":12083,\"bangumi_id\":0,\"nowEpisode\":\"4\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/8df12fc092928ba57069eb16e077f9d719f345e5.jpg\",\"danmaku_count\":10022,\"ep_id\":258686,\"favorites\":12083,\"is_finish\":0,\"lastupdate\":1545876000,\"lastupdate_at\":\"2018-12-27 10:00:00\",\"isNew\":false,\"play_count\":486760,\"pub_time\":\"\",\"season_id\":26176,\"season_status\":2,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/c96b4daa6f3f1925184bc7835a6a26ee573d6970.jpg\",\"title\":\"通灵妃 河南话版\",\"viewRank\":0,\"weekday\":4},{\"area\":\"中国大陆\",\"arealimit\":316,\"attention\":151420,\"bangumi_id\":0,\"nowEpisode\":\"10\",\"cover\":\"http://i0.hdslb.com/bfs/bangumi/57dc8fdf8373f8c1facfc2911d50be1cfc6da18a.jpg\",\"danmaku_count\":26706,\"ep_id\":258689,\"favorites\":151420,\"is_finish\":0,\"lastupdate\":1545876000,\"lastupdate_at\":\"2018-12-27 10:00:00\",\"isNew\":false,\"play_count\":2023158,\"pub_time\":\"\",\"season_id\":24888,\"season_status\":2,\"spid\":0,\"coverImage\":\"http://i0.hdslb.com/bfs/bangumi/ef2f039b71564cc469b2cac9d8f2879e1259a74f.jpg\",\"title\":\"山河社稷图\",\"viewRank\":0,\"weekday\":4}]";
    static{
        try{

            defaultList= JSON.parseArray(defaultData,Manga.class);
        }catch (Exception e){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brvah_test);
//      远端加载数据
        initData();
//        初始化适配器
        initAdapter();
        initView();

    }

    private void initAdapter() {
        BaseQuickAdapter homeAdapter = new QuickBookAdapter(R.layout.book_item, mangaList);
        homeAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        homeAdapter.addHeaderView(top);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
//                startActivity(intent);
                ToastUtils.showDisplay("ItemClick:"+mangaList.get(position).getTitle());
            }
        });
        homeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showDisplay("ItemLongClick:"+mangaList.get(position).getTitle());
                return false;
            }
        });

        homeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showDisplay("ItemChildClick :"+mangaList.get(position).getTitle());
            }
        });

        homeAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showDisplay("ItemLongClick :"+mangaList.get(position).getTitle());
                return false;
            }

        });

        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        RequestParams params=new RequestParams(1);

        ClientUtlis.post(true, UrlConfig.TEST_DOWNLOAD.getValue(),params,
                this,new JsonCallback<List<Manga>>() {
                    @Override
                    public void onSuccess(List<Manga> list, String msg) {
                        if(list.isEmpty()){
                            mangaList= defaultList;

                        }else{
                            mangaList=list;
                        }
//                        ToastUtils.showDisplay(downloadUrl);
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Log.i(TAG,"获取列表失败，reason:"+msg);
                        ToastUtils.showDisplay("book列表加载异常");
                    }
                });
    }

    private void initView() {
        mRecyclerView =  findViewById(R.id.brvah_book_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
