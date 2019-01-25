package space.cc.com.fragmenttest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import space.cc.com.fragmenttest.domain.util.ToastUtils;


public abstract class MyBaseNew extends BasePermissionsActivity implements View.OnClickListener {


    public Context mContext = null;
    public Activity mActivity = null;
//    private WaitDialog mWaitDialog;

    public Bundle mBunble;
    public boolean isShowgetShare=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;
        registerEventBus();
        mBunble = getIntent().getExtras();
//      mWaitDialog = initDialog("请求网络中...");
        if (setContentLayout() != 0){
            setContentView(setContentLayout());
        }
        ButterKnife.bind(this);
        initView();
    }

    protected  void registerEventBus(){
//            EventBus.getDefault().register(this);
    }
/**
     * @author  CF
     * @date   2019/1/23
     * @description 初始化布局View
     *
     */
    protected abstract int setContentLayout();
/**
 *
 * 初始化 布局中的组件   butterknife大量使用注解 所以initView可能不需要做什么
 * 但是万一碰到升级不兼容的问题时需要把注解转换为findViewById 网上有这么一种插件可以完成这个工作
 */
    protected abstract void initView();

    @Override
    protected void onResume() {
        super.onResume();
        //百度数据统计
      /*  StatService.onResume(this);
        if (isShowgetShare){
            readPaste();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //百度数据统计
//        StatService.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterEventBus();
        ToastUtils.cancel();//关闭toast
    }

    protected  void unRegisterEventBus(){
//        EventBus.getDefault().unregister(this);
    }


    public void intentStart(Class<? extends Activity> ativity) {
        Intent intent = new Intent(this, ativity);
        startActivity(intent);
    }

    public void intentStart(Class<? extends Activity> ativity, Bundle bundle) {
        Intent intent = new Intent(this, ativity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /************************************************ 防止连续点击 开始**********************************************************/

    private long lastClickTime;

    /**
     * 判断事件出发时间间隔是否超过预定值
     */
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500)
            return true;
        lastClickTime = time;
        return false;
    }

    @Override
    public void onClick(View view) {
        // 防止连续点击
        if (isFastDoubleClick())
            return;
        initViewClick(view.getId());
    }

    protected abstract void initViewClick(int id);

    /************************************************ 防止连续点击 结束**********************************************************/


   /* private void readPaste() {
        try{

            ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData data = cm.getPrimaryClip();
            ClipData.Item item = data.getItemAt(0);
            String content1 = item.getText().toString();
            String content2 = content1.replaceAll("¥", "");
            LogUtils.e("content1", content1);
            LogUtils.e("content2", content2);
            int num = content1.length() - content2.length();
            LogUtils.e("num", num);
            if (num == 2) {
                String codeStr = content1.substring(content1.indexOf("¥") + 1, content1.lastIndexOf("¥"));
                LogUtils.e("content1", codeStr);
                if (!StringUtils.isEmpty(codeStr)) {
                    RequestParams params = new RequestParams(RequestParams.PARAM_TYPE_FORM);
                    params.put("bonusCode", codeStr);
                    ClientUtlis.post(mActivity, UrlsConfig.getPathByBonusCode, params, this, new DialogCallback<PathByBonusCodeEntity>(this) {
                        @Override
                        public void onSuccess(PathByBonusCodeEntity result, String msg) {
                            LogUtils.e("result",result.getStrategySharePath());
                            ReceiveShareRedPackageDialog receiveShareRedPackageDialog = new ReceiveShareRedPackageDialog(mActivity, result.getStrategySharePath());
                            receiveShareRedPackageDialog.show();
                        }
                    });
                }
            }
        }catch (Exception e) {
            LogUtils.e("ddj",e.toString());
        }
    }*/

}
