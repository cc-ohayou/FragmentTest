package space.cc.com.fragmenttest.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.MyQuickAdapter;
import space.cc.com.fragmenttest.adapter.OperationBizAdapter;
import space.cc.com.fragmenttest.adapter.base.BaseQuickAdapter;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.bizobject.OperateBiz;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.litepals.Manga;
import space.cc.com.fragmenttest.util.UtilBox;

public class OperationListFragment extends BaseFragment {


    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;

    @BindView(R.id.oper_list)
    RecyclerView mRecyclerView;

    private List<OperateBiz> operList;
    private OperationBizAdapter adapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OperationListFragment newInstance(String tabTitle) {
        OperationListFragment fragment = new OperationListFragment();
    /*        Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_test, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        initOperationBizList();
        initAdapter();
        mRecyclerView.setHasFixedSize(true);
//        传入mRecyclerView的子item布局id 和数据
        mRecyclerView.setAdapter();
    }

    private void initOperationBizList() {

        RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
//        mangaList=(new ClientUtlis<Manga>()).getBizList(params,UrlConfig.MANGA_LIST,this,"列表获取异常",mangaList);
        getBizList(params, UrlConfig.MANGA_LIST, this, "列表获取异常");
    }
    private void initAdapter() {
        adapter = new OperationBizAdapter(R.id.item_oper,operList,0,6);

        adapter.openLoadAnimation();


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
//                startActivity(intent);
                ToastUtils.showDisplay("ItemClick:" + operList.get(position).getOperId());
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showDisplay("ItemLongClick:" +operList.get(position).getOperId());
                return false;
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                String clickItem = getClickItemType(view);
                ToastUtils.showDisplay("ItemChildClick : " + clickItem);
            }
        });

        adapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
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
    private void initData() {

    }


    private void getBizList(RequestParams params, UrlConfig url, Object tag, final String errMsg) {
        ClientUtlis.post(true, url.getValue(), params,
                tag, new JsonCallback<List<OperateBiz>>() {
                    @Override
                    public void onSuccess(List<OperateBiz> list, String msg) {
                        if (list.isEmpty()) {
                            operList = null;
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

}
