package space.cc.com.fragmenttest.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.BrvahTestActivity;
import space.cc.com.fragmenttest.activity.OperBizDetailActivity;
import space.cc.com.fragmenttest.adapter.OperationBizAdapter;
import space.cc.com.fragmenttest.adapter.base.BaseQuickAdapter;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.bizobject.IntentExtraKey;
import space.cc.com.fragmenttest.domain.bizobject.OperateBiz;
import space.cc.com.fragmenttest.domain.bizobject.PsPage;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.domain.util.Utils;
import space.cc.com.fragmenttest.util.UtilBox;

@SuppressLint("ValidFragment")
public class OperationListFragment extends BaseFragment {


    private static final boolean GRID_LAYOUT = false;
    private static final String TAG = "OperationListFragment";
    private RecyclerView mRecyclerView;

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    private Activity parentActivity;
    private PsPage<OperateBiz> operPage;
    private List<OperateBiz> operList;
    private OperationBizAdapter adapter;
    EditText searchText;
    private  String tabTitle;
    private  int currPage=1;
    private  int pageSize=10;
//    加载刷新后最新的position位置
    private  int lastPosition=10;

    private boolean firstReqDatListSucc=false;
    @SuppressLint("ValidFragment")
    public OperationListFragment(String tabTitle,Activity activity) {
        this.tabTitle = tabTitle;
        this.parentActivity = activity;
    }

    private static class  Instance{
        private static OperationListFragment fragment;
        public static OperationListFragment getInstance(String tabTitle,Activity activity){
            fragment=new OperationListFragment(tabTitle,activity);
            return fragment;
        }
    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static  OperationListFragment newInstance(String tabTitle,Activity activity) {

    /*        Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);*/
        return  Instance.getInstance(tabTitle,activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_test, container, false);
         mRecyclerView =  rootView.findViewById(R.id.oper_list);


        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(Utils.getApp(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
        }

//        mRecyclerView.setBackgroundResource(R.drawable.default_head);
        initAdapter();
        mRecyclerView.setAdapter(adapter);
        initSmartRefreshLayoutListener((RefreshLayout) rootView);

        loadOperationBizList(true);

        parentOtherWork();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(-1)){
//                  返回false表示不能往下滑动，即代表到顶部了
//                  隐藏到顶部的浮动按钮
//                    Log.e
                    hideView();

//                    ToastUtils.showDisplay("到顶部了");
                }else{
//                    显示浮动按钮
                    showView();
//                    ToastUtils.showDisplay("非顶部 显示按钮");

                }
//                recyclerView.computeVerticalScrollOffset()

//                判断是否滑动到底部， recyclerView.canScrollVertically(1);返回false表示不能往上滑动，即代表到底部了；
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return rootView;
    }

/**
     * @author  CF
     * @date   2019/1/22
     * @description
     *
     */
    private void parentOtherWork() {
        if(isBrvahtestActivity()) {
            ((BrvahTestActivity) parentActivity).setLayoutManager(mRecyclerView.getLayoutManager());
        }
    }

    private void showView() {
        if(isBrvahtestActivity()) {
            ((BrvahTestActivity) parentActivity).showViews();
        }
    }

    private void hideView() {
        if(isBrvahtestActivity()){
            ((BrvahTestActivity)parentActivity).hideViews();
        }

    }

    /**
 * @description
 * @author CF
 * created at 2019/1/13/013  19:52
 */
    private void initSmartRefreshLayoutListener(RefreshLayout rootView) {
        final RefreshLayout refreshLayout = rootView;
        refreshLayout.setEnableRefresh(true);
//        是否在加载完成之后滚动内容显示新数据（默认-true）
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        //触发自动刷新 进入页面就开始刷新 不需要手动发起请求数据  setOnRefreshListener监听到请求自会加载数据
//        refreshLayout.autoRefresh();
//        属性设置参考文档 https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
        //设置 Header 为 贝塞尔雷达 样式
        BezierRadarHeader refreshHeader=new BezierRadarHeader(parentActivity).setEnableHorizontalDrag(true);
        refreshHeader.setAccentColorId(R.color.white);//设置强调颜色
        refreshHeader.setPrimaryColorId(R.color.refreshHeaderBack);//设置主题颜色 刷新布局的背景色
        refreshLayout.setRefreshHeader(refreshHeader);

//      设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(parentActivity).setSpinnerStyle(SpinnerStyle.Scale));
        //是否启用上拉加载功能
        refreshLayout.setEnableLoadMore(true);
//        adapter.setEmptyView();
        //        刷新监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadOperationBizList(true);
                        //延迟2000毫秒后结束刷新 这期间可用于更新recycleView的adapter数据 刷新列表
                        refreshLayout.finishRefresh(/*2000,false*/);//传入false表示刷新失败
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });
//        加载更多监听
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() >=operPage.getTotalCount()) {
                            ToastUtils.showDisplay( "没有更多了");
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            loadOperationBizList(false);
                            refreshLayout.finishLoadMore();
                            //延迟2000毫秒后结束加载
//                            refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                        }
                    }
                }, 2000);
            }
        });


    }

  /**
     * @description 刷新当前页数据或者加载下一页数据
     * @author CF
     * created at 2019/1/13/013  20:42
     */
    public void loadOperationBizList(boolean refreshFlag) {
        RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
        params.put("envType","sit");
        if(isBrvahtestActivity()){
            searchText = parentActivity.findViewById(R.id.manga_toolbar_search_input);
            params.put("operName",searchText.getText().toString().trim());
        }
        if(!refreshFlag) {
//            加载下一页数据
            if (operPage != null && currPage < operPage.getTotalPageCount()) {
                currPage = operPage.getNextPage();
            }
        }
        Log.d(TAG,"加载数据 currPage="+currPage+",refreshFlag="+refreshFlag);
        //刷新当前页数据 一般首页动态会采取刷新措施永远显示 最新动态 下拉时才加载刷新新的数据
        params.put("currPage",currPage);
        params.put("pageSize",pageSize);
//        mangaList=(new ClientUtlis<Manga>()).getBizList(params,UrlConfig.MANGA_LIST,this,"列表获取异常",mangaList);
        getBizList(params, UrlConfig.OPER_LIST, this, "列表获取异常",refreshFlag);
    }

    private boolean isBrvahtestActivity() {
        return parentActivity instanceof BrvahTestActivity;
    }

    private void initAdapter() {
        adapter = new OperationBizAdapter(R.layout.item_operation,operList,0,UtilBox.box().ui.getViewIemHeight(4));
        adapter.openLoadAnimation();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(),OperBizDetailActivity.class);
                intent.putExtra(IntentExtraKey.OPER_BIZ_DETAIL.getValue(),operList.get(position));
                startActivity(intent);
//                ToastUtils.showDisplay("ItemClick:" + operList.get(position).getOperId());
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                doChildClickWorkById(view, position);
            }
        });

        adapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showDisplay("ItemChildLongClick : " + operList.get(position).getOperId());
                return false;
            }


        });

        //默认动画只是第一次加载 所以设置下每次都加载动画
        adapter.isFirstOnly(false);


    }
    /**
         * @author  CF
         * @date   2019/1/11
         * @description
         *
         */
    private void doChildClickWorkById(View view, int position) {
        switch (view.getId()) {
            case R.id.item_oper_but:
                String content = "确认发起" + operList.get(position).getOperName() +
                        ", (环境："+ operList.get(position).getEnvType() + ") 请求吗?";
                requestDialogShow(content, operList.get(position));
        }
    }

    private void requestDialogShow(String content, final OperateBiz operateBiz) {
        UtilBox.box().dialog.showMaterialDialogSimleDefault(getActivity(), "操作请求",
                content, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.NEUTRAL) {
//                            Toast.makeText(MainActivity.this, "更多信息", Toast.LENGTH_LONG).show();
                        } else if (which == DialogAction.POSITIVE) {
                            sendPostByUrl(operateBiz.getUrl());
                        } else if (which == DialogAction.NEGATIVE) {
                            ToastUtils.showLong("请求已取消");
                        }

                    }


                });
    }

  /**
     * @description
     * @author CF
     * created at 2019/1/13/013  20:29
     */
    private void sendPostByUrl(String url) {
    RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
    ClientUtlis.post(true, url, params,
            this, new JsonCallback<String>() {
                @Override
                public void onSuccess(String returnMsg, String msg) {
                   ToastUtils.showDisplay("请求成功");

                }

                @Override
                public void onError(String msg, int code) {
                    ToastUtils.showDisplay(msg);
                }
            });
}
  /**
     * @description
     * @author CF
     * created at 2019/1/13/013  20:29
     */
    private void getBizList(RequestParams params, UrlConfig url, Object tag, final String errMsg, final boolean refreshFlag) {
        ClientUtlis.post(true, url, params,
                tag, new JsonCallback<PsPage<OperateBiz>>() {
                    @Override
                    public void onSuccess(PsPage<OperateBiz> page, String msg) {
                        operPage=page;
                        if (operPage.getItems().isEmpty()) {
                            operList = Collections.emptyList();
                            adapter.setNewData(operList);
                        } else {
                            operList = operPage.getItems();
                            if(firstReqDatListSucc&&!refreshFlag){
//                                加载更多 不是刷新操作 刷新操作用add会不断添加同一批数据到data中
                                adapter.addData(operList);
                            }else if(refreshFlag){
//                                刷新 永远是只有一页数据且是最新数据
                                adapter.setNewData(operList);
                                firstReqDatListSucc=true;
                            }
                            lastPosition=adapter.getItemCount()-1;
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Log.i(TAG, "获取列表失败，reason:" + msg);
                        ToastUtils.showDisplay(errMsg);
                    }
                });
    }




}

