package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.RadiaoAdapter;
import xpfei.myapp.databinding.ActivityListBinding;
import xpfei.myapp.model.RadioInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.view.MyStaggerGrildLayoutManger;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.AppLog;

/**
 * Description: 电台列表
 * Author: xpfei
 * Date:   2017/08/21
 */
public class RadioListActivity extends MyBaseActivity {
    private String title;//标题
    private ActivityListBinding binding;
    private RadiaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        title = getIntent().getStringExtra(ContentValue.IntentKey.IntentKeyStr);
        onSetTitle(title);
        onSetLeft(true);
        initView();
    }

    private void initView() {
        binding.xrefreshview.setPullRefreshEnable(false);
        binding.recvclerview.setLayoutManager(new MyStaggerGrildLayoutManger(this, 3, StaggeredGridLayoutManager.VERTICAL));
        binding.xrefreshview.setPullLoadEnable(false);
        adapter = new RadiaoAdapter(this, new ArrayList<RadioInfo>());
        binding.recvclerview.setAdapter(adapter);
        startBaseMSVReqTask(this, null);
    }


    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Radio.getRadioList(), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    try {
                        JSONArray list = jsonObject.optJSONArray(ContentValue.Json.Result);
                        List<RadioInfo> tempList = JSON.parseArray(list.toString(), RadioInfo.class);
                        adapter.setData(tempList);
                    } catch (Exception e) {
                        AppLog.Loge("Error:" + e.getMessage());
                        onMSVFailure("服务器繁忙，请稍后再试！");
                    }
                } else {
                    onMSVFailure("未查询到相关电台！");
                }
                onMSVSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                onMSVFailure(msg);
            }
        });
    }
}
