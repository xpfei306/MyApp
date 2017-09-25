package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.SingerDetailAdapter;
import xpfei.myapp.databinding.ActivityAlbumdetailBinding;
import xpfei.myapp.model.AlbumInfo;
import xpfei.myapp.model.ArtInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.view.reclyview.RecyclerViewDivider;

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
        onSetTitle(info.getName());
        initView();
    }

    private void initView() {
        drawable = binding.llAlbumDeatailTop.getBackground();
        if (drawable != null) {
            drawable.setAlpha(0);
        }
        getArtSong();
        getArtAlbum();
        startBaseReqTask(this);
        adapter = new SingerDetailAdapter(this);
        layoutManger = new LinearLayoutManager(this);
        binding.MyRv.setLayoutManager(layoutManger);
        binding.MyRv.setAdapter(adapter);
        binding.MyRv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL,
                1, getResources().getColor(R.color.colorGray)));
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
                if (jsonObject != null) {
                    ArtInfo info = JSON.parseObject(jsonObject.toString(), ArtInfo.class);
                    adapter.setArtInfo(info);
                }
                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(null);
            }
        });
    }

    private void getArtSong() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Artist.artistSongList(info.getTing_uid(), info.getArtist_id(), 0, 4), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                if (code == ContentValue.Json.Successcode) {
                    JSONArray array = jsonObject.optJSONArray(ContentValue.Json.SongList);
                    List<Song> list = JSON.parseArray(array.toString(), Song.class);
                    adapter.setArtSongInfo(list);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void getArtAlbum() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Artist.artAlbumList(info.getTing_uid(), 0, 4), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (jsonObject != null) {
                    JSONArray array = jsonObject.optJSONArray(ContentValue.Json.AlbumList);
                    List<AlbumInfo> list = JSON.parseArray(array.toString(), AlbumInfo.class);
                    adapter.setArtAlbumInfo(list);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}