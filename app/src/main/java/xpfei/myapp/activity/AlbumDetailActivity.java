package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.adapter.DetailAdapter;
import xpfei.myapp.databinding.ActivityAlbumdetailBinding;
import xpfei.myapp.model.AlbumDeatilInfo;
import xpfei.myapp.model.AlbumInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: 专辑详情
 * Author: xpfei
 * Date:   2017/09/13
 */
public class AlbumDetailActivity extends MyBaseActivity {
    private ActivityAlbumdetailBinding binding;
    private AlbumInfo albumInfo;
    private DetailAdapter adapter;
    private Drawable drawable;
    private LinearLayoutManager layoutManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_albumdetail);
        albumInfo = getIntent().getParcelableExtra(ContentValue.IntentKey.IntentKeySer);
        initView();
    }

    private void initView() {
        onSetLeft(true);
        onSetTitle(albumInfo.getTitle());
        drawable = binding.llAlbumDeatailTop.getBackground();
        if (drawable != null) {
            drawable.setAlpha(0);
        }
        adapter = new DetailAdapter(AlbumDetailActivity.this, new AlbumDeatilInfo());
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
        getData();
    }

    private void getData() {
        AlbumDeatilInfo albumInfo = new AlbumDeatilInfo();
        AlbumInfo info = new AlbumInfo();
        info.setAuthor("王逸洁,阿云嘎");
        info.setCountry("内地");
        info.setTitle("陈超古装电视剧歌曲集");
        info.setPic_radio("http://musicdata.baidu.com/data2/pic/254857322b60944589ce7a874ae00b28/556057707/556057707.jpg@s_1,w_300,h_300");
        info.setPublishtime("2017-09-14");
        albumInfo.setAlbumInfo(info);
        List<Song> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Song song = new Song();
            song.setPic_small("http://musicdata.baidu.com/data2/pic/254857322b60944589ce7a874ae00b28/556057707/556057707.jpg@s_1,w_90,h_90");
            song.setAuthor("王逸洁");
            song.setAlbum_title("陈超古装电视剧歌曲集");
            list.add(song);
        }
        albumInfo.setSonglist(list);
        adapter.setData(albumInfo);
    }

    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Album.albumInfo(albumInfo.getAlbum_id()), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (jsonObject != null && !StringUtil.isEmpty(jsonObject.toString())) {
                    AlbumDeatilInfo info = JSON.parseObject(jsonObject.toString(), AlbumDeatilInfo.class);
                    adapter.setData(info);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
