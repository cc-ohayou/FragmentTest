package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.ToastUtils;

public class OkGoTestActivity extends BaseActivity  implements View.OnClickListener   {


    TextView shouResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_go_test);
        shouResponse=findViewById(R.id.show_req_response);
        setButOnclickListenerByRid(R.id.send_request,this);
        setButOnclickListenerByRid(R.id.show_toast,this);
        setButOnclickListenerByRid(R.id.send_get,this);
    }

    @Override
    public void requestPermission() {

    }

    @Override
    public void onClick(View v) {
        RequestParams params=new RequestParams(1);
        switch(v.getId()){
            case R.id.send_request:
                ClientUtlis.post(true, UrlConfig.TEST_URL.getValue(),params,
                        this,new JsonCallback<List>() {
                            @Override
                            public void onSuccess(List result, String msg) {
                                shouResponse.setText(result.toString());

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
            case R.id.send_get:
                ClientUtlis.get(this, UrlConfig.TEST_URL.getValue(),
                        this,new JsonCallback<List>() {
                            @Override
                            public void onSuccess(List result, String msg) {
                                shouResponse.setText(result.toString());
                            }

                            @Override
                            public void onError(String msg, int code) {
                                shouResponse.setText(msg);
                            }
                        });
                break;
            default:
                break;

        }
    }
}
