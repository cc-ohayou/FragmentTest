package space.cc.com.fragmenttest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.QuickBookAdapter;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.litepals.Book;

public class BrvahTestActivity extends AppCompatActivity {
    private static final String TAG = "BrvahTestActivity";
    private List<Book> bookList;
    private RecyclerView mRecyclerView;

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
        BaseQuickAdapter homeAdapter = new QuickBookAdapter(R.layout.book_item, bookList);
        homeAdapter.openLoadAnimation();
        View top = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) mRecyclerView.getParent(), false);
        homeAdapter.addHeaderView(top);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
//                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        RequestParams params=new RequestParams(1);

        ClientUtlis.post(true, UrlConfig.TEST_DOWNLOAD.getValue(),params,
                this,new JsonCallback<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> list, String msg) {
                        if(list.isEmpty()){
                            bookList= new ArrayList<>();

                        }else{
                            bookList=list;
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
