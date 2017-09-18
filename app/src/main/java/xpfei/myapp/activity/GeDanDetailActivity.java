package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.GeDanDetailAdapter;
import xpfei.myapp.databinding.ActivityAlbumdetailBinding;
import xpfei.myapp.model.GeDanDetailInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.CommonUtil;

/**
 * Description: 专辑详情
 * Author: xpfei
 * Date:   2017/09/13
 */
public class GeDanDetailActivity extends MyBaseActivity {
    private ActivityAlbumdetailBinding binding;
    private String album_id;
    private GeDanDetailAdapter adapter;
    private Drawable drawable;
    private LinearLayoutManager layoutManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_albumdetail);
        album_id = getIntent().getStringExtra(ContentValue.IntentKey.IntentKeyStr);
        initView();
    }

    private void initView() {
        onSetLeft(true);
        drawable = binding.llAlbumDeatailTop.getBackground();
        if (drawable != null) {
            drawable.setAlpha(0);
        }
        adapter = new GeDanDetailAdapter(GeDanDetailActivity.this, new GeDanDetailInfo());
        layoutManger = new LinearLayoutManager(this);
        binding.MyRv.setLayoutManager(layoutManger);
        binding.MyRv.setAdapter(adapter);
        final int height = CommonUtil.getScreenSize(this)[0] / 2;
        binding.MyRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = layoutManger.findFirstVisibleItemPosition();
                if (position == 0) {
                    int alpha = 0;
                    if (recyclerView.getChildCount() > 0 && recyclerView.getChildAt(1) != null) {
                        int top = recyclerView.getChildAt(1).getTop();
                        if (top < height) {
                            double a = (height - top) * 255.0 / height;
                            alpha = (int) a;
                        } else {
                            alpha = 0;
                        }
                    }
                    if (drawable != null) {
                        drawable.setAlpha(alpha);
                    }
                } else {
                    if (drawable != null) {
                        drawable.setAlpha(255);
                    }
                }
            }
        });
        startBaseReqTask(this, null);
    }


    @Override
    public void onRequestData() {
        String id = getIntent().getStringExtra(ContentValue.IntentKey.IntentKeyStr);
        MyVolley.getInstance(this).get(BaiduMusicApi.GeDan.geDanInfo(id), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    GeDanDetailInfo info = JSON.parseObject(jsonObject.toString(), GeDanDetailInfo.class);
                    adapter.setData(info);
                } else {
                    onFailure("未查询到该歌单信息");
                }
                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
                finish();
            }
        });
    }
}
