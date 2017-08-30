package xpfei.myapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONObject;

import java.io.File;

import xpfei.myapp.IMusicCallBack;
import xpfei.myapp.IMusicPlayerInterface;
import xpfei.myapp.MusicPlayService;
import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityPlayerBinding;
import xpfei.myapp.db.SongDbManager;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.mylibrary.net.MyOkHttp;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.net.response.DownloadResponseHandler;
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
    private IMusicPlayerInterface mService;
    private boolean ispaused;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (mService == null) {
                mService = IMusicPlayerInterface.Stub.asInterface(iBinder);
            }
            try {
                mService.registerCallBack(callBack);
                startBaseReqTask(PlayerActivity.this, null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                mService.unregisterCallBack(callBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mService = null;
        }
    };
    private IMusicCallBack callBack = new IMusicCallBack.Stub() {
        @Override
        public void callBack(int CurrentPosition, int duration) throws RemoteException {
            binding.SkbPlayer.setMax(duration);
            binding.SkbPlayer.setProgress(CurrentPosition);
            if (CurrentPosition > 0) {
                binding.lrcView.updateTime(CurrentPosition);
            } else {
                binding.lrcView.onDrag(CurrentPosition);
            }
            binding.txtPlayerTime.setText(StringUtil.timeParse(CurrentPosition));
            binding.txtSongTime.setText(StringUtil.timeParse(duration));
        }

        @Override
        public void doSome(boolean isPaused) throws RemoteException {
            ispaused = !isPaused;
        }
    };

    private void bindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        Song_id = getIntent().getStringExtra(ContentValue.IntentKeyStr);
        bindService();
        binding.SkbPlayer.setPadding(0, 0, 0, 0);
        onSetLeft(true);
        binding.ivPlay.setOnClickListener(this);
        binding.SkbPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    mService.seekTo(seekBar.getProgress());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestData() {
        if (!isRequest()) {
            onDialogSuccess(null);
            return;
        }
        MyVolley.getInstance(this).get(BaiduMusicApi.Song.songPlayInfo(Song_id), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                onDialogSuccess(null);
                try {
                    int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                    if (code == ContentValue.Json.Successcode) {
                        Song info = JSON.parseObject(jsonObject.optJSONObject(ContentValue.Json.Songinfo).toString(), Song.class);
                        if (info != null) {
                            JSONObject object = jsonObject.optJSONObject(ContentValue.Json.Bitrate);
                            String file_link = object.optString("file_link");
                            if (!StringUtil.isEmpty(file_link)) {
                                info.setFile_link(file_link);
                                play(info);
                            }
                        } else {
                            onFailure("播放失败，即将播放下一首");
                        }
                    }
                } catch (Exception e) {
                    AppLog.Loge(e.getMessage());
                    onFailure("播放失败，即将播放下一首");
                }
            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
            }
        });
    }

    private boolean isRequest() {
        Song info = new SongDbManager().selectByPrimaryKey(Song_id);
        if (info != null && !StringUtil.isEmpty(info.getFile_link())) {
            try {
                play(info);
                return false;
            } catch (RemoteException e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    private void play(Song info) throws RemoteException {
        downLrc(info.getLrclink());
        mService.setSong(info, true);
        binding.txtTitle.setText(info.getTitle());
        Glide.with(PlayerActivity.this).load(info.getPic_small()).asBitmap().into(new SimpleTarget<Bitmap>() {
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

    private void downLrc(String url) {
        // TODO: 2017/08/30  需要更改 歌词重复下载的问题
        MyOkHttp.getInstance().download(this, url, new DownloadResponseHandler() {
            @Override
            public void onFinish(File download_file) {
                binding.lrcView.loadLrc(download_file);
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {

            }

            @Override
            public void onFailure(String error_msg) {

            }
        });
    }

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
                String action;
                int drawbleId;
                if (ispaused) {
                    action = ContentValue.PlayAction.Play;
                    drawbleId = R.drawable.player;
                } else {
                    action = ContentValue.PlayAction.Pause;
                    drawbleId = R.drawable.paused;
                }
                try {
                    mService.doAction(action);
                    binding.ivPlay.setImageResource(drawbleId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivNext:
                break;
            case R.id.ivDownLoad:
                break;
        }
    }
}
