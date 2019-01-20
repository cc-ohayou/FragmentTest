package space.cc.com.fragmenttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.bizobject.IntentExtraKey;
import space.cc.com.fragmenttest.domain.bizobject.OperateBiz;
import space.cc.com.fragmenttest.util.UtilBox;

public class OperBizDetailActivity extends BaseActivity {
    private TextView operBizDesc;
    private FloatingActionButton floatingActionButton;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar oprBizToolBar;
    private ImageView imageView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oper_biz_detail);
        Intent intent=getIntent();
        OperateBiz operateBiz= (OperateBiz) intent.getSerializableExtra(IntentExtraKey.OPER_BIZ_DETAIL.getValue());
        initView();
        actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(operateBiz.getOperName());
        UtilBox.box().picasso.loadDrawResIntoView(imageView,R.drawable.default_head);
        operBizDesc.setText(generateOperBizDesc(operateBiz.getDesc()));
    }
private String generateOperBizDesc(String desc){
        StringBuilder sb=new StringBuilder(desc);
        for (int i=0;i<500;i++){
            sb.append(desc);
        }
        return sb.toString();
}
    private void initView() {
        operBizDesc=findViewById(R.id.operBiz_desc);
        collapsingToolbarLayout=findViewById(R.id.operBiz_collapingBar);
        oprBizToolBar=findViewById(R.id.operBiz_toolbar);
        imageView=findViewById(R.id.operBiz_image_view);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
