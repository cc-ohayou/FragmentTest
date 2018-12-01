package space.cc.com.fragmenttest.domain;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;

import okhttp3.Call;
import space.cc.com.fragmenttest.MyApplication;
import space.cc.com.fragmenttest.domain.util.ToastUtils;

/**
 * Created by xuwenzheng on 2018/4/24 17:19
 */
public  abstract class JsonCallBack<T> extends NewSystemResponseCallback<T> {
    private boolean showPro = true;
    private Activity activity;

    private void initDialog(Activity activity, String msg) {
        this.activity = activity;

    }

    public JsonCallBack(Activity activity) {
        super();
        initDialog(activity,"");
    }

    public JsonCallBack(Activity activity, String msg) {
        super();
        initDialog(activity,msg);
    }

    public JsonCallBack(Activity activity, boolean showPro) {
        super();
        this.showPro = showPro;
        initDialog(activity,"");
    }

    public JsonCallBack(Activity activity, String msg, boolean showPro) {
        super();
        this.showPro = showPro;
        initDialog(activity,msg);
    }

    @Override
    public void onError(Call call, okhttp3.Response response, Exception e) {
        super.onError(call, response, e);
        if(activity == null || activity.isFinishing()) return;
        ToastUtils.showDisplay("当前系统繁忙，请稍后重试");
    }

    @Override
    public void onError(String msg, int code) {
        if(activity == null || activity.isFinishing()) return;
        ToastUtils.showDisplay(msg);
    }

    @Override
    public void onVersionControl(String msg, final String data) {
        OkGo.getInstance().cancelAll();
        MyApplication.isConnected = true;
        if(activity == null || activity.isFinishing()) return;
        /*PromptDialog promptDialog = new PromptDialog(activity);
        promptDialog.setMsg(msg)
                .setOkStr("立即更新")
                .setBanBack()
                .setOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VersionUpdate vp = new VersionUpdate(activity);
                        vp.downLoadApk(data);
                    }
                })
                .show();*/
    }

    @Override
    public void onLoginError(String msg, int code) {
        OkGo.getInstance().cancelAll();
        if(activity == null || activity.isFinishing()) return;
       /* UserInfo.getInstance().setmStrSid("");
        UserInfo.getInstance().setmStrUid("");
        ClientConfiguration.getInstance().setSid("");
        ClientConfiguration.getInstance().setUid("");*/
       /* Intent intent = new Intent(activity, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("backurl","main");
        intent.putExtras(bundle);
        activity.startActivity(intent);*/
    }

    @Override
    public void onBefore(BaseRequest request) {
        if(activity == null || activity.isFinishing()) return;
        /*if(activity instanceof YBaseActivity && showPro)
            ((YBaseActivity)activity).waitShow();*/
        super.onBefore(request);
    }

    @Override
    public void onAfter(String s, Exception e) {
        super.onAfter(s, e);
       /* if(activity == null || activity.isFinishing()) return;
        if(activity instanceof YBaseActivity && showPro)
            ((YBaseActivity)activity).waitDismiss();*/
    }

}
