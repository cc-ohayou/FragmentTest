package space.cc.com.fragmenttest.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.GlobalSettings;
import space.cc.com.fragmenttest.domain.bizobject.IntentExtraKey;
import space.cc.com.fragmenttest.domain.bizobject.OperateBiz;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.util.UtilBox;

public class OperBizDetailActivity extends MyBaseNew {
    private TextView operBizDesc;
    private TextView operBizCreateTime;
    private TextView operBizUpdateTime;

   /* @BindView(R.id.operBiz_url)
    TextView operBiz_url;*/
    @BindView(R.id.operBiz_env)
    TextView operBiz_env;
    @BindView(R.id.operBiz_pro)
    TextView operBiz_pro;
    @BindView(R.id.oper_biz_detail_like_btn)
    FloatingActionButton floatingActionButton;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar oprBizToolBar;
    private ImageView imageView;



    @Override
    protected int setContentLayout() {
        return R.layout.activity_oper_biz_detail;
    }

    @Override
    public void initView() {
        operBizDesc = findViewById(R.id.operBiz_desc);
        operBizCreateTime = findViewById(R.id.operBiz_createTime);
        operBizUpdateTime = findViewById(R.id.operBiz_updateTime);

        collapsingToolbarLayout = findViewById(R.id.operBiz_collapingBar);
        oprBizToolBar = findViewById(R.id.operBiz_toolbar);
        imageView = findViewById(R.id.operBiz_image_view);
        floatingActionButton.setOnClickListener(this);
        Intent intent = getIntent();
        OperateBiz operateBiz = (OperateBiz) intent.getSerializableExtra(IntentExtraKey.OPER_BIZ_DETAIL.getValue());
//        initView();    java.lang.StackOverflowError: stack size 8MB  制造一个栈溢出错误
        initToolBar();
        initCollapseToolBar(operateBiz);
        UtilBox.box().picasso.loadUrlResIntoView(imageView,GlobalSettings.settingProperties.getOperBizDetailBgUrl());
        operBizDesc.setText("描述："+operateBiz.getDesc());
//        operBiz_url.setText("Url："+operateBiz.getUrl());
        operBiz_pro.setText("项目："+operateBiz.getProject());
        operBiz_env.setText("环境："+operateBiz.getEnvType());
        operBizCreateTime.setText("创建时间："+operateBiz.getCreateTime());
        operBizUpdateTime.setText("更新时间："+operateBiz.getUpdateTime());
    }

    private void initCollapseToolBar(OperateBiz operateBiz) {
        collapsingToolbarLayout.setTitle(operateBiz.getOperName());
        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(
                ContextCompat.getColor(getBaseContext(),R.color.colorAccent)));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ColorStateList.valueOf(
                ContextCompat.getColor(getBaseContext(),R.color.white)));
    }

    private void initToolBar() {
        //设置对应的图片
        oprBizToolBar.setNavigationIcon(R.drawable.common_title_back);
        //处理点击
        oprBizToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }





    @Override
    protected void initViewClick(int id) {
          switch (id){
              case R.id.oper_biz_detail_like_btn:
                  ToastUtils.showDisplay("此功能暂未开放");
                  break;
          }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
