package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.ArtAdapter;
import xpfei.myapp.databinding.ActivityArtlistBinding;
import xpfei.myapp.model.ArtInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.view.reclyview.CustomFooterView;
import xpfei.mylibrary.view.reclyview.XRefreshView;

/**
 * Description: 歌手列表
 * Author: xpfei
 * Date:   2017/08/21
 */
public class SingerListActivity extends MyBaseActivity {
    private String title;//标题
    private ActivityArtlistBinding binding;
    private int page = 0, area, sex;
    private ArtAdapter adapter;
    private boolean isMore = true;
    private List<ArtInfo> artInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_artlist);
        title = getIntent().getStringExtra(ContentValue.IntentKeyStr);
        onSetTitle(title);
        onSetLeft(true);
        initView();
        initEvent();
    }

    private void initView() {
        binding.xrefreshview.setPullRefreshEnable(false);
        binding.xrefreshview.setPullLoadEnable(true);
        binding.recvclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtAdapter(this, artInfos);
        binding.recvclerview.setAdapter(adapter);
        adapter.setCustomLoadMoreView(new CustomFooterView(this));
    }

    private void initEvent() {
        binding.rgArea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbAllArea:
                        area = 0;
                        break;
                    case R.id.rbHy:
                        area = 6;
                        break;
                    case R.id.rbHg:
                        area = 7;
                        break;
                    case R.id.rbOm:
                        area = 3;
                        break;
                }
                artInfos.clear();
                startBaseReqTask(SingerListActivity.this, null);
            }
        });
        binding.rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rbAll:
                        sex = 0;
                        break;
                    case R.id.rbMan:
                        sex = 1;
                        break;
                    case R.id.rbWoman:
                        sex = 2;
                        break;
                    case R.id.rbZh:
                        sex = 3;
                        break;
                }
                artInfos.clear();
                startBaseReqTask(SingerListActivity.this, null);
            }
        });
        binding.xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onLoadMore(boolean isSilence) {
                if (isMore) {
                    loadmore();
                } else {
                    binding.xrefreshview.setPullLoadEnable(false);
                    CommonUtil.showToast(SingerListActivity.this, "已经是最后一页了！");
                }
            }

            @Override
            public void onRefresh(boolean isPullDown) {

            }
        });
        startBaseReqTask(this, null);
    }


    private void loadmore() {
        page++;
        startBaseReqTask(this, null);
    }

    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Artist.artistList(page, 50, area, sex, 1, null), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray json = jsonObject.optJSONArray(ContentValue.Json.ArtList);
                    if (json != null) {
                        List<ArtInfo> tempList = JSON.parseArray(json.toString(), ArtInfo.class);
                        artInfos.addAll(tempList);
                        adapter.setData(artInfos);
                    } else {
                        onFailure("未查询到相关歌手信息！");
                        isMore = false;
                    }
                } catch (Exception e) {
                    AppLog.Loge("Error:" + e.getMessage());
                    onFailure("服务器繁忙，请稍后再试！");
                }
                binding.xrefreshview.stopLoadMore();
                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
                binding.xrefreshview.stopLoadMore();
            }
        });
    }
}
