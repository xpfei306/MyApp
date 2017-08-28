package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.graphics.Palette;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONObject;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityPlayerBinding;
import xpfei.myapp.model.SongPlayerInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.AppLog;

/**
 * Description: 播放页面
 * Author: xpfei
 * Date:   2017/08/11
 */
public class PlayerActivity extends MyBaseActivity {
    private ActivityPlayerBinding binding;
    private String Song_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        Song_id = getIntent().getStringExtra(ContentValue.IntentKeyStr);
        startBaseReqTask(this, null);
        onSetLeft(true);
    }

    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Song.songInfo(Song_id), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    SongPlayerInfo info = JSON.parseObject(jsonObject.toString(), SongPlayerInfo.class);
                    if (info.getError_code() == ContentValue.Json.Successcode) {
                        binding.txtTitle.setText(info.getSonginfo().getTitle());
                        Glide.with(PlayerActivity.this).load(info.getSonginfo().getPic_premium()).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                binding.imgSong.setImageBitmap(resource);
                                Palette.Builder builder = Palette.from(resource);
                                builder.generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                                        if (vibrant != null)
                                            binding.llBottom.setBackgroundColor(vibrant.getRgb());
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    AppLog.Loge(e.getMessage());
                    onFailure("");
                }
                onDialogSuccess(null);
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
            }
        });
    }
}
