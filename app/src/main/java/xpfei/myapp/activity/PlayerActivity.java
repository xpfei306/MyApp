package xpfei.myapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
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
import xpfei.myapp.util.DisplayUtil;
import xpfei.myapp.util.FastBlurUtil;
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
    private boolean ispaused, isBind;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (mService == null) {
                mService = IMusicPlayerInterface.Stub.asInterface(iBinder);
            }
            try {
                isBind = true;
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

        @Override
        public void getCurrent(Song song) throws RemoteException {
            play(song, false);
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
        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationIcon(R.drawable.back);
        binding.SkbPlayer.setPadding(0, 0, 0, 0);
        onSetLeft(true);
        binding.ivPlay.setOnClickListener(this);
        binding.ivUp.setOnClickListener(this);
        binding.ivNext.setOnClickListener(this);
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
    protected void onStart() {
        super.onStart();
        if (isBind) {
            startBaseReqTask(this, null);
        } else {
            bindService();
        }
        Song_id = getIntent().getStringExtra(ContentValue.IntentKeyStr);
    }

    @Override
    public void onRequestData() {
        if (!isRequest()) {
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
                                play(info, true);
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
                play(info, true);
                onDialogSuccess(null);
                return false;
            } catch (RemoteException e) {
                e.printStackTrace();
                onDialogSuccess(null);
                return true;
            }
        }
        onDialogSuccess(null);
        return true;
    }

    private void play(Song info, boolean isPlay) throws RemoteException {
        if (isPlay) {
            mService.setSong(info, true);
        }
        downLrc(info);
        getSupportActionBar().setTitle(info.getTitle());
        getSupportActionBar().setSubtitle(info.getAuthor());
        Glide.with(PlayerActivity.this).load(info.getPic_small()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                final Drawable foregroundDrawable = getForegroundDrawable(resource);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.llMain.setForeground(foregroundDrawable);
                                binding.llMain.beginAnimation();
                            }
                        });
                    }
                }).start();
            }
        });
        binding.SkbPlayer.setProgress(0);
        binding.txtSongTime.setText("00:00");
        binding.txtPlayerTime.setText("00:00");
    }

    private void downLrc(final Song info) {
        if (!StringUtil.isEmpty(info.getLrclink_local())) {
            binding.lrcView.loadLrc(new File(info.getLrclink_local()));
        } else {
            MyOkHttp.getInstance().download(this, info.getLrclink(), new DownloadResponseHandler() {
                @Override
                public void onFinish(File download_file) {
                    info.setLrclink_local(download_file.getAbsolutePath());
                    new SongDbManager().insertOrReplace(info);
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
    }

    private Drawable getForegroundDrawable(Bitmap bitmap) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (DisplayUtil.getScreenWidth(this)
                * 1.0 / DisplayUtil.getScreenHeight(this) * 1.0);
        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                .getHeight() / 50, false);
        /*模糊化*/
        Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true);
        Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCollection:
                break;
            case R.id.ivUp:
                try {
                    mService.doAction(ContentValue.PlayAction.Last);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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
                try {
                    mService.doAction(ContentValue.PlayAction.Next);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivDownLoad:
                break;
        }
    }

}
