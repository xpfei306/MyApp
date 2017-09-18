package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.SingerDetailAdapter;
import xpfei.myapp.databinding.ActivityAlbumdetailBinding;
import xpfei.myapp.model.ArtInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.CommonUtil;

/**
 * Description: 歌手详情页
 * Author: xpfei
 * Date:   2017/09/18
 */
public class SingerDetailActivity extends MyBaseActivity {
    private ActivityAlbumdetailBinding binding;
    private Drawable drawable;
    private LinearLayoutManager layoutManger;
    private ArtInfo info;
    private SingerDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_albumdetail);
        info = (ArtInfo) getIntent().getSerializableExtra(ContentValue.IntentKey.IntentKeySer);
        if (info == null) {
            CommonUtil.showToast(this, "暂无该歌手信息");
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        startBaseReqTask(this);
        getArtSong();
        drawable = binding.llAlbumDeatailTop.getBackground();
        if (drawable != null) {
            drawable.setAlpha(0);
        }
        adapter = new SingerDetailAdapter(this);
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
    }

    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Artist.artistInfo(info.getTing_uid(), info.getArtist_id()), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void getArtSong() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Artist.artistSongList(info.getTing_uid(), info.getArtist_id(), 0, 50), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void getArtAlbum() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Artist.artAlbumList(info.getTing_uid(), 0, 50), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
