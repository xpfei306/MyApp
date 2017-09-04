package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.File;

import xpfei.myapp.IMusicCallBack;
import xpfei.myapp.IMusicPlayerInterface;
import xpfei.myapp.MyBaseApplication;
import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityPlayerBinding;
import xpfei.myapp.db.SongDbManager;
import xpfei.myapp.model.Song;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.view.MusicListPopupWindow;
import xpfei.mylibrary.net.MyOkHttp;
import xpfei.mylibrary.net.MyVolley;
import xpfei.mylibrary.net.response.DownloadResponseHandler;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.CommonUtil;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: 播放页面
 * Author: xpfei
 * Date:   2017/08/11
 */
public class PlayerActivity extends MyBaseActivity {
    private ActivityPlayerBinding binding;
    private String Song_id;
    private IMusicPlayerInterface mService;
    private boolean ispaused;
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

        @Override
        public void onError() throws RemoteException {
            CommonUtil.showToast(PlayerActivity.this, "未找到播放地址");
        }
    };
    private MusicListPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationIcon(R.drawable.back);
        Song_id = getIntent().getStringExtra(ContentValue.IntentKeyStr);
        onSetLeft(true);
        mService = MyBaseApplication.application.getmService();
        init();
    }

    private void init() {
        int mode = 0;
        try {
            mService.registerCallBack(callBack);
            mode = mService.getPlayMode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        switch (mode) {
            case 1:
                binding.ivPlayState.setImageResource(R.drawable.dqxh);
                break;
            case 2:
                binding.ivPlayState.setImageResource(R.drawable.sjbf);
                break;
            default:
                binding.ivPlayState.setImageResource(R.drawable.lbxh);
                break;
        }
        binding.SkbPlayer.setPadding(0, 0, 0, 0);
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
        binding.setOnMyClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ivCollection:
                        break;
                    case R.id.ivDownLoad:
                        break;
                    case R.id.ivShare:
                        break;
                    case R.id.ivDel:
                        break;
                    case R.id.ivPlayList:
                        if (popupWindow == null) {
                            popupWindow = new MusicListPopupWindow(PlayerActivity.this);
                        }
                        try {
                            popupWindow.show(binding.llMain, mService.getSongList(), mService.getPlayMode());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            popupWindow.show(binding.llMain, null, 0);
                        }
                        break;
                    case R.id.ivPlayState:
                        try {
                            int mode = mService.getPlayMode();
                            switch (mode) {
                                case 1:
                                    mode = 2;
                                    binding.ivPlayState.setImageResource(R.drawable.sjbf);
                                    break;
                                case 2:
                                    mode = 0;
                                    binding.ivPlayState.setImageResource(R.drawable.lbxh);
                                    break;
                                default:
                                    mode = 1;
                                    binding.ivPlayState.setImageResource(R.drawable.dqxh);
                                    break;
                            }
                            mService.setPlayMode(mode);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
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
                }
            }
        });
        startBaseReqTask(this, null);
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
                            onFailure("播放失败");
                        }
                    }
                } catch (Exception e) {
                    AppLog.Loge(e.getMessage());
                    onFailure("播放失败");
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
                onDialogFailure("播放失败，请检查网络设置");
                return true;
            }
        }
        onDialogFailure("播放失败，请检查网络设置");
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mService.unregisterCallBack(callBack);
            callBack = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void play(Song info, boolean isPlay) throws RemoteException {

        if (info == null) {
            if (isPlay) {
                mService.setSong(info, true);
            }
            downLrc(info);
            getSupportActionBar().setTitle(info.getTitle());
            getSupportActionBar().setSubtitle(info.getAuthor());
        } else {
            getSupportActionBar().setTitle("未知");
            getSupportActionBar().setSubtitle("未知");
        }
        binding.SkbPlayer.setProgress(0);
        binding.txtSongTime.setText("00:00");
        binding.txtPlayerTime.setText("00:00");
    }

    private void downLrc(final Song info) {
        if (!StringUtil.isEmpty(info.getLrclink_local())) {
            binding.lrcView.loadLrc(new File(info.getLrclink_local()));
        } else {
            MyOkHttp.getInstance().download(this, info.getLrclink(), info.getSong_id(), new DownloadResponseHandler() {
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
}
