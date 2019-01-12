package space.cc.com.fragmenttest.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.TabTestActivity;
import space.cc.com.fragmenttest.adapter.MyQuickAdapter;
import space.cc.com.fragmenttest.adapter.OperationBizAdapter;
import space.cc.com.fragmenttest.adapter.base.BaseQuickAdapter;
import space.cc.com.fragmenttest.domain.GlobalSettings;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.bizobject.CustomProperties;
import space.cc.com.fragmenttest.domain.bizobject.OperateBiz;
import space.cc.com.fragmenttest.domain.bizobject.PsPage;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.domain.util.Utils;
import space.cc.com.fragmenttest.litepals.Manga;
import space.cc.com.fragmenttest.util.UtilBox;

@SuppressLint("ValidFragment")
public class OperationListFragment extends BaseFragment {


    private static final boolean GRID_LAYOUT = false;
    private static final String TAG = "OperationListFragment";
    private RecyclerView mRecyclerView;
    private Activity parentActivity;
    private PsPage<OperateBiz> operPage;
    private List<OperateBiz> operList;
    private OperationBizAdapter adapter;
    private  String tabTitle;
    private  int currPage=1;
    private  int pageSize=10;

    @SuppressLint("ValidFragment")
    public OperationListFragment(String tabTitle,Activity activity) {
        this.tabTitle = tabTitle;
        this.parentActivity = activity;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static synchronized OperationListFragment newInstance(String tabTitle,Activity activity) {

    /*        Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);*/
        return  new OperationListFragment(tabTitle,activity);
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
        mRecyclerView.setBackgroundResource(R.drawable.default_head);
        initAdapter();
        mRecyclerView.setAdapter(adapter);
        initOperationBizList();
        return rootView;
    }


    private void initOperationBizList() {

        RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
        params.put("envType","sit");
        if(operPage!=null&&currPage<operPage.getTotalPageCount()){
            currPage=operPage.getNextPage();
        }
        params.put("currPage",currPage);
        params.put("pageSize",pageSize);
//        mangaList=(new ClientUtlis<Manga>()).getBizList(params,UrlConfig.MANGA_LIST,this,"列表获取异常",mangaList);
        getBizList(params, UrlConfig.OPER_LIST, this, "列表获取异常");
    }
    private void initAdapter() {
        adapter = new OperationBizAdapter(R.layout.item_operation,operList,0,UtilBox.box().ui.getViewIemHeight(4));
        adapter.openLoadAnimation();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(HomeActivity.this, ACTIVITY[position]);
//                startActivity(intent);
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
        UtilBox.box().dialog.showMaterialDialogSimleDefault(Utils.getApp(), "操作请求",
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

    private void getBizList(RequestParams params, UrlConfig url, Object tag, final String errMsg) {
        ClientUtlis.post(true, url.getValue(), params,
                tag, new JsonCallback<PsPage<OperateBiz>>() {
                    @Override
                    public void onSuccess(PsPage<OperateBiz> page, String msg) {
                        operPage=page;
                        if (operPage.getItems().isEmpty()) {
                            operList = null;
                        } else {
                            operList = operPage.getItems();
                        }
                        adapter.setNewData(operList);
                    }

                    @Override
                    public void onError(String msg, int code) {
                        Log.i(TAG, "获取列表失败，reason:" + msg);
                        ToastUtils.showDisplay(errMsg);
                    }
                });
    }

}

