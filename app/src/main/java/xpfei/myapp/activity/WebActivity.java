package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.ViewGroup;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityWebviewBinding;
import xpfei.myapp.util.ContentValue;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/23
 */
public class WebActivity extends MyBaseActivity {
    private ActivityWebviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        onSetTitle("详情");
        onSetLeft(true);
        String url = getIntent().getStringExtra(ContentValue.IntentKeyStr);
        binding.webview.loadUrl(url);
    }


    @Override
    public void onRequestData() {
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (binding.webview != null) {
            binding.webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            binding.webview.clearHistory();
            ((ViewGroup) binding.webview.getParent()).removeView(binding.webview);
            binding.webview.destroy();
        }
        super.onDestroy();
    }
}
