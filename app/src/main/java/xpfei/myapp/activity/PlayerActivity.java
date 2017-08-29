package xpfei.myapp.activity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityPlayerBinding;
import xpfei.myapp.model.SongPlayerInfo;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.util.Player;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: 播放页面
 * Author: xpfei
 * Date:   2017/08/11
 */
public class PlayerActivity extends MyBaseActivity implements View.OnClickListener {
    private ActivityPlayerBinding binding;
    private String Song_id;
    private Player player;
    private int playState;
    private AssetManager manager;

    private class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            binding.lrcView.updateTime(player.mediaPlayer.getCurrentPosition());
            binding.txtPlayerTime.setText(StringUtil.timeParse(player.mediaPlayer.getCurrentPosition()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        Song_id = getIntent().getStringExtra(ContentValue.IntentKeyStr);
        startBaseReqTask(this, null);
        binding.SkbPlayer.setPadding(0, 0, 0, 0);
        binding.SkbPlayer.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        onSetLeft(true);
        player = new Player(binding.SkbPlayer);
        manager = getAssets();
        binding.ivPlay.setOnClickListener(this);
    }

    @Override
    public void onRequestData() {
        MyVolley.getInstance(this).get(BaiduMusicApi.Song.songInfo(Song_id), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    SongPlayerInfo info = JSON.parseObject(jsonObject.toString(), SongPlayerInfo.class);
                    if (info.getError_code() == ContentValue.Json.Successcode) {
//                        downLrc(info.getSonginfo().getLrclink());
//                        play(info.getSongurl().getUrl());
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
//    private void downLrc(String url) {
//        MyOkHttp.getInstance().download(this, url, new DownloadResponseHandler() {
//            @Override
//            public void onFinish(File download_file) {
//                binding.lrcView.loadLrc(download_file);
//            }
//
//            @Override
//            public void onProgress(long currentBytes, long totalBytes) {
//
//            }
//
//            @Override
//            public void onFailure(String error_msg) {
//
//            }
//        });
//    }
//
//    private void play(List<SongDownInfo> list) {
//        boolean isPlay = false;
//        if (list != null && list.size() > 0) {
//            for (SongDownInfo info : list) {
//                if (!StringUtil.isEmpty(info.getFile_link())) {
//                    player.playUrl(info.getFile_link());
//                    binding.txtSongTime.setText(StringUtil.timeParse(player.mediaPlayer.getDuration()));
//                    isPlay = true;
//                    break;
//                }
//            }
//        }
//        if (!isPlay) {
//            CommonUtil.showToast(this, "播放失败，请稍后再试！");
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMenu:
                break;
            case R.id.ivCollection:
                break;
            case R.id.ivUp:
                break;
            case R.id.ivPlay:
                if (playState == 0) {
                    paly();
                    binding.lrcView.loadLrc(getLrcText());
                    playState = 1;
                } else if (playState == 1) {
                    player.pause();
                    binding.lrcView.pause();
                    playState = 2;
                } else {
                    player.play();
                    binding.lrcView.resume(player.mediaPlayer.getCurrentPosition());
                }
                break;
            case R.id.ivNext:
                break;
            case R.id.ivDownLoad:
                break;
        }
    }

    private String getLrcText() {
        String lrcText = null;
        try {
            InputStream is = manager.open("cbg.lrc");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    private void paly() {
        try {
            AssetFileDescriptor descriptor = manager.openFd("cbg.mp3");
            player.playIn(descriptor);
            binding.txtSongTime.setText(StringUtil.timeParse(player.mediaPlayer.getDuration()));
            binding.SkbPlayer.setMax(player.mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
