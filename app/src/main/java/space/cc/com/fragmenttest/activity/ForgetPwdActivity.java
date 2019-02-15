package space.cc.com.fragmenttest.activity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import butterknife.BindView;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.RegexUtils;
import space.cc.com.fragmenttest.domain.util.StringUtils;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.util.SecurityUtil;


public class ForgetPwdActivity extends MyBaseNew {


    private static final String TAG = "ForgetPwdActivity";

    @BindView(R.id.forget_mail)
    EditText forgetMailText;
    @BindView(R.id.forget_pwd)
    EditText forgetPwdText;
    @BindView(R.id.forget_verify_code)
     EditText verifyCodeText;

    @BindView(R.id.forget_pwd_mail_del_icon)
    ImageView mailDelIcon;
    @BindView(R.id.forget_pwd_toggle)
     ToggleButton pwdToogleBtn;
    @BindView(R.id.forget_btn_verify_code)
     Button verifyCodeBtn;
    @BindView(R.id.forget_sub_btn)
     Button subButton;



    @Override
    protected int setContentLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {

        forgetMailText.addTextChangedListener(textWatcher);
        forgetPwdText.addTextChangedListener(textWatcher);
        verifyCodeText.addTextChangedListener(textWatcher);

        verifyCodeBtn.setOnClickListener(this);
        mailDelIcon.setOnClickListener(this);
        subButton.setOnClickListener(this);
        pwdToogleBtn.setOnCheckedChangeListener(pwdChange);
    }
    //密码可见切换
    private CompoundButton.OnCheckedChangeListener pwdChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            int pos = forgetPwdText.getSelectionStart();
            if (isChecked) {
                forgetPwdText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                forgetPwdText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            forgetPwdText.setSelection(pos);
        }
    };

    //注册按钮状态设置
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {

            if(forgetMailText.length() > 0){
                mailDelIcon.setVisibility(View.VISIBLE);
            }else{
                mailDelIcon.setVisibility(View.GONE);
            }

            if (forgetMailText.length() >  0 && forgetPwdText.length() >  0 && verifyCodeText.length() >  0 )
                subButton.setEnabled(true);
            else
                subButton.setEnabled(false);



        }
    };

    @Override
    protected void initViewClick(int id) {
         switch(id){
             case R.id.forget_pwd_mail_del_icon:
                 forgetMailText.setText("");
                 mailDelIcon.setVisibility(View.INVISIBLE);
                 break;
             case R.id.forget_btn_verify_code:
                 getVerifyCode();
                 break;
             case R.id.forget_sub_btn:
                 resetPwd();
             default:
                 break;

         }

    }

    @Override
    protected void initViewClick(View view) {

    }

    /**
     * @author  CF
     * @date   2019/1/28
     * @description
     *
     */
    private void resetPwd() {
       if(!verifyParamFailed()){

           final String mail = forgetMailText.getText().toString();
           final String pwd = forgetPwdText.getText().toString();
           final String verifyCode = verifyCodeText.getText().toString();
           RequestParams params=new RequestParams(RequestParams.PARAM_TYPE_FORM);
           params.put("mail",mail);
           params.put("pwd",SecurityUtil.MD5(pwd));
           params.put("verifyCode",verifyCode);
           ClientUtlis.post(true, UrlConfig.PWD_RESET, params, this, new JsonCallback<String>() {
               @Override
               public void onSuccess(String restMsg, String msg) {
                   super.onSuccess(restMsg, msg);
//                   ToastUtils.showDisplay("密码更改成功!");
                   Log.i(TAG,"密码更改成功!");
                   finish();
                   inValidateLoginState();
//                   intentStart(LoginActivity.class);
               }

               @Override
               public void onError(String msg, int code) {
                   super.onError(msg, code);
                   ToastUtils.showErrorDisplay("密码更改失败! "+msg);

               }
           });
       }


    }

    private CountDownTimer countDownTimer;
    private boolean canSent = true;
    private void getVerifyCode() {
        if (verifyMailTextFailed()){
            return;
        }

        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                String text = "已发送(" + ((int) l / 1000) + ")";
                verifyCodeBtn.setEnabled(false);
                verifyCodeBtn.setText(text);
                if(canSent){
                    canSent = false;
                    sendVerifyCodeReq();
                }
            }



            @Override
            public void onFinish() {
                verifyCodeBtn.setEnabled(true);
                verifyCodeBtn.setText("获取验证码");
                canSent = true;
            }
        };
        countDownTimer.start();

    }
/**
     * @author  CF
     * @date   2019/1/28
     * @description
     *
     */
    public void sendVerifyCodeReq() {
        if (verifyMailTextFailed()) {
            return ;
        }
        final String mail = forgetMailText.getText().toString();
        RequestParams params=new RequestParams(RequestParams.PARAM_TYPE_FORM);
        params.put("mail",mail);
        ClientUtlis.post(true, UrlConfig.FORGET_PWD_REQ_VERIFY_CODE, params, this, new JsonCallback<String>() {
            @Override
            public void onSuccess(String restMsg, String msg) {
                super.onSuccess(restMsg, msg);
                ToastUtils.showDisplay("验证码已发送到您的邮箱，请前往邮箱查看。");

            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                ToastUtils.showErrorDisplay("获取验证码失败 "+msg);

            }
        });

    }
/**
     * @author  CF
     * @date   2019/1/28
     * @description
     *
     */
    private boolean verifyParamFailed() {
        if (verifyMailTextFailed()) {
            return true;
        }
        final String pwd = forgetPwdText.getText().toString();
        final String verifyCode = verifyCodeText.getText().toString();

        if(StringUtils.isEmpty(pwd)){
            ToastUtils.showErrorDisplay("密码不能为空！");
            return true;
        }
        if(!RegexUtils.isPassword(pwd)){
            ToastUtils.showErrorDisplay("密码格式不正确！");
            return true;
         }
        if(StringUtils.isEmpty(verifyCode)){
            ToastUtils.showErrorDisplay("验证码不能为空！");
            return true;
        }
        if(verifyCode.length()!=6){
            ToastUtils.showErrorDisplay("验证码格式不正确！");
            return true;
        }



        return false;
    }

    private boolean verifyMailTextFailed() {
        final String mail = forgetMailText.getText().toString();
        if(StringUtils.isEmpty(mail)){
            ToastUtils.showErrorDisplay("邮箱不能为空！");
            return true;
        }
        if(!RegexUtils.isEmail(mail)){
            ToastUtils.showErrorDisplay("邮箱格式不正确！");
            return true;
        }
        return false;
    }
}
