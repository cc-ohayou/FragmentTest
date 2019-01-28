package space.cc.com.fragmenttest.activity;

import butterknife.BindView;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.activity.common.TitleLayout;
import space.cc.com.fragmenttest.domain.GlobalSettings;
import space.cc.com.fragmenttest.domain.RequestParams;
import space.cc.com.fragmenttest.domain.UrlConfig;
import space.cc.com.fragmenttest.domain.callback.JsonCallback;
import space.cc.com.fragmenttest.domain.util.ClientUtlis;
import space.cc.com.fragmenttest.domain.util.RegexUtils;
import space.cc.com.fragmenttest.domain.util.StringUtils;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.util.SecurityUtil;
import space.cc.com.fragmenttest.util.UtilVerifyCode;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RegisterActivity extends MyBaseNew {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.reg_phone)
    EditText regPhone;

    @BindView(R.id.reg_nickName)
    EditText regNickName;

    @BindView(R.id.reg_pwd)
    EditText regPwd;

    @BindView(R.id.reg_pwd_toggle)
    ToggleButton regToggleBtn;


    @BindView(R.id.reg_verifyCode)
    EditText regVerifyCode;
    @BindView(R.id.reg_mail)
    EditText regMail;

    @BindView(R.id.reg_sub_btn)
    Button regSubBtn;
    @BindView(R.id.reg_custom_phone)
    TextView regCustomPhone;

    private boolean verifyCodeFlag=false;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        try {



            TitleLayout.titleText.setText("注册");

        regPhone.addTextChangedListener(textWatcher);
        regNickName.addTextChangedListener(textWatcher);
        regPwd.addTextChangedListener(textWatcher);
        regVerifyCode.addTextChangedListener(textWatcher);

        regVerifyCode.setOnClickListener(this);
        regSubBtn.setOnClickListener(this);
        regToggleBtn.setOnCheckedChangeListener(pwdChange);
        String text="温馨提示\n如有疑问请拨打";
        if(!GlobalSettings.settingProperties.getOtherProperties().isEmpty()){
            text=text+GlobalSettings.settingProperties.getOtherProperties().get("customPhone");
        }
        regCustomPhone.setText(text);
        } catch (Exception e) {
            Log.e(TAG, "defaultData transfer error", e);
        }

    }

    @Override
    protected void initViewClick(int id) {
           switch(id){
               case R.id.reg_sub_btn:
                   register();
                   break;
           }
    }



    //密码可见切换
    private CompoundButton.OnCheckedChangeListener pwdChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            int pos = regPwd.getSelectionStart();
            if (isChecked) {
                regPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                regPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            regPwd.setSelection(pos);
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
            verifyCodeFlag=UtilVerifyCode.isEqualsIgnoreCase(regVerifyCode.getText().toString());
            if (regPhone.length() != 0 && regPwd.length() != 0 && regVerifyCode.length() != 0 && verifyCodeFlag)
                regSubBtn.setEnabled(true);
            else
                regSubBtn.setEnabled(false);
        }
    };
    /**
     * 注册
     */
    public void register(){
        final String phone = regPhone.getText().toString();
        final String pwd = regPwd.getText().toString();
        final String nickName = regNickName.getText().toString();
        final String mail = regMail.getText().toString();

        if(StringUtils.isEmpty(phone)){
            ToastUtils.showDisplay("手机号不能为空！");
            return;
        }
        if(!RegexUtils.isMobileExact(phone)){
            ToastUtils.showDisplay("手机号格式不正确！");
            return;
        }
        if(StringUtils.isEmpty(mail)){
            ToastUtils.showDisplay("邮箱不可为空！");
            return;
        }
        if(!RegexUtils.isEmail(mail)){
            ToastUtils.showDisplay("邮箱格式非法！");
            return;
        }
       /* if(StringUtils.isEmpty(verifyCode) || verifyCode.length() < 6){
            ToastUtils.showDisplay("验证码格式不正确！");
            return;
        }*/
        if(StringUtils.isEmpty(pwd)){
            ToastUtils.showDisplay("密码不能为空！");
            return;
        }
        if(!RegexUtils.isPassword(pwd)){
            ToastUtils.showDisplay("密码由6到16位到数字与字母组成！");
            return;
        }
        if(!StringUtils.isEmpty(nickName)) {
            if (!RegexUtils.nickNameCheck(nickName)) {
                ToastUtils.showDisplay("昵称10位以内，不能有特殊字符！");
                return;
            }
        }
        if(!verifyCodeFlag){
            ToastUtils.showDisplay("验证码错误！");
            return;
        }


        RequestParams params = RequestParams.getFormDataParam(4);
        params.put("phone",phone);
        params.put("pwd", SecurityUtil.MD5(pwd));
        params.put("nickName",nickName);
        params.put("mail",mail);

        ClientUtlis.post(true, UrlConfig.user_register,params,this,new JsonCallback<String>(this) {
            @Override
            public void onSuccess(String result, String msg) {
                ToastUtils.showDisplay("注册成功！");
                finish();
                intentStart(LoginActivity.class);
             /*   final RegSuccessDialog regSuccessDialog = new RegSuccessDialog(RegisterActivity.this);
                regSuccessDialog.setOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regSuccessDialog.dismiss();
                        EventBusType eventBusType=new EventBusType();
                        eventBusType.setType(1);
                        EventBus.getDefault().post(eventBusType);
                        finish();

                    }
                }).show();*/
            }
            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                ToastUtils.showDisplay(msg);
            }
        });
    }

}
