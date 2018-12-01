package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.JsonCallBack;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.JsonUtil;
import space.cc.com.fragmenttest.domain.util.ToastUtils;

public class OkGoTestActivity extends BaseActivity  implements View.OnClickListener   {


    TextView shouResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_go_test);
        setButOnclickListenerByRid(R.id.send_request,this);
        setButOnclickListenerByRid(R.id.show_toast,this);
        setButOnclickListenerByRid(R.id.send_request,this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.send_request:
                RequestParams params=new RequestParams(1);
                ClientUtlis.post(true, UrlConfig.TEST_URL.getValue(),params,
                        this,new JsonCallBack<String>(this) {
                            @Override
                            public void onSuccess(String result, String msg) {
                                String data = JsonUtil.getJsontoString(result, "data");
                                shouResponse.setText(data);

                            }

                            @Override
                            public void onError(String msg, int code) {
                                shouResponse.setText(msg);
                            }
                        });

                break;
            case R.id.show_toast:
                ToastUtils.showDisplay("show_toast");
                break;
            default:
                break;

        }
    }
}
