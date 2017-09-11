package xpfei.myapp.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.alibaba.fastjson.JSONObject;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityWelcomeBinding;
import xpfei.myapp.model.UserInfo;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.manager.ACache;
import xpfei.mylibrary.net.MyOkHttp;
import xpfei.mylibrary.net.response.FastJsonResponseHandler;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;


/**
 * 首页
 */
public class WelcomeActivity extends MyBaseActivity {
    private ActivityWelcomeBinding binding;
    private String strName, strPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        binding.llmain.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAlphaAnimation();
            }
        }, 500);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = binding.etUser.getText().toString();
                if (StringUtil.isEmpty(strName)) {
                    CommonUtil.showToast(WelcomeActivity.this, "请输入用户名");
                    return;
                }
                strPsw = binding.etPsw.getText().toString();
                if (StringUtil.isEmpty(strPsw)) {
                    CommonUtil.showToast(WelcomeActivity.this, "请输入密码");
                    return;
                }
                CommonUtil.hideInput(WelcomeActivity.this);
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//                startBaseReqTask(WelcomeActivity.this, null);
            }
        });
    }

    @Override
    public void onRequestData() {
        JSONObject params = new JSONObject();
        params.put("userName", strName);
        params.put("psw", strPsw);
        MyOkHttp.getInstance().postString(this, "http://192.168.10.20:8911/MyTest/MyTestService.svc/Login",
                params.toString(), null, new FastJsonResponseHandler<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo response) {
                        String message = null;
                        if (response != null) {
                            ACache aCache = ACache.get(WelcomeActivity.this);
                            aCache.put(ContentValue.AcacheKey.ACACHE_USER, response);
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            finish();
                        } else {
                            message = "登陆失败，请稍后再试~";
                        }
                        onDialogSuccess(message);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        onDialogFailure(error_msg);
                    }
                });
    }

    public void startAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(1000);
        //开始动画
        binding.llmain.setVisibility(View.VISIBLE);
        binding.llmain.startAnimation(alphaAnimation);
    }
}
