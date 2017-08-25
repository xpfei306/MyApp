package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.RankingAdapter;
import xpfei.myapp.databinding.ActivityListBinding;
import xpfei.myapp.model.RankingInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.view.reclyview.CustomGifHeader;
import xpfei.mylibrary.view.reclyview.XRefreshView;

/**
 * Description: 榜单列表
 * Author: xpfei
 * Date:   2017/08/25
 */
public class RankingActivity extends MyBaseActivity {
    private String title;//标题
    private ActivityListBinding binding;
    private RankingAdapter adapter;
    private List<RankingInfo> rankingInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        title = getIntent().getStringExtra(ContentValue.IntentKeyStr);
        onSetTitle(title);
        onSetLeft(true);
        initView();
    }

    private void initView() {
        CustomGifHeader header = new CustomGifHeader(this);
        binding.xrefreshview.setCustomHeaderView(header);
        binding.xrefreshview.setPullRefreshEnable(true);
        binding.recvclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.xrefreshview.setPullLoadEnable(false);
        adapter = new RankingAdapter(this, rankingInfos, R.layout.item_recyclerview_ranking);
        binding.recvclerview.setAdapter(adapter);
        binding.xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                rankingInfos.clear();
                startBaseReqTask(RankingActivity.this, null);
            }
        });
        startBaseReqTask(this, null);
    }

    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Billboard.billCategory(), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    try {
                        JSONArray json = jsonObject.optJSONArray(ContentValue.Json.Content);
                        List<RankingInfo> list = JSON.parseArray(json.toString(), RankingInfo.class);
                        adapter.setData(list);
                    } catch (Exception e) {
                        AppLog.Loge("Error:" + e.getMessage());
                        onFailure("服务器繁忙，请稍后再试！");
                    }
                } else {
                    onFailure("未查询到相关歌单！");
                }
                onDialogSuccess(null);
                binding.xrefreshview.stopRefresh();
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
                binding.xrefreshview.stopRefresh();
            }
        });
    }
}
