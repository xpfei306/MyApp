package xpfei.myapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.MyBaseApplication;
import xpfei.myapp.R;
import xpfei.myapp.activity.base.MyBaseActivity;
import xpfei.myapp.databinding.ActivityPlayerBinding;
import xpfei.myapp.db.SongDbManager;
import xpfei.myapp.model.DownInfo;
import xpfei.myapp.model.Song;
import xpfei.myapp.service.IMusicCallBack;
import xpfei.myapp.service.IMusicPlayerInterface;
import xpfei.myapp.util.BaiduMusicApi;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.view.DownListPopupWindow;
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
    private long Song_id;
    private IMusicPlayerInterface mService;
    private boolean ispaused;
    private MusicListPopupWindow popupWindow;
    private DownListPopupWindow downListPop;
    private int type;
    private IMusicCallBack callBack = new IMusicCallBack.Stub() {
        @Override
        public void callBack(Song song, int CurrentPosition, int duration) throws RemoteException {
            if (song != null) {
                getSupportActionBar().setTitle(song.getTitle());
                getSupportActionBar().setSubtitle(song.getAuthor());
            }
            binding.SkbPlayer.setMax(duration);
            binding.SkbPlayer.setProgress(CurrentPosition);
            binding.lrcView.updateTime(CurrentPosition);
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
            binding.SkbPlayer.setMax(0);
            binding.SkbPlayer.setProgress(0);
            binding.lrcView.updateTime(0);
            binding.lrcView.setLabel(null);
            binding.txtPlayerTime.setText(StringUtil.timeParse(0));
            binding.txtSongTime.setText(StringUtil.timeParse(0));
            getSupportActionBar().setTitle("未知");
            getSupportActionBar().setSubtitle("未知");
            binding.ivPlay.setImageResource(R.drawable.player);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationIcon(R.drawable.back);
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
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                try {
                    switch (view.getId()) {
                        case R.id.ivCollection:
                            Song info1 = mService.getSong();
                            if (info1.getIsLocal() == 1) {
                                CommonUtil.showToast(PlayerActivity.this, "暂不支持本地歌曲分享");
                                return;
                            }
                            break;
                        case R.id.ivDownLoad:
                            Song info = mService.getSong();
//                            if (info == null) {
//                                CommonUtil.showToast(PlayerActivity.this, "暂无下载链接");
//                                return;
//                            }
//                            if (info.getIsLocal() == 1) {
//                                CommonUtil.showToast(PlayerActivity.this, "本地歌曲不需要下载");
//                                return;
//                            }
                            getDownUrl(info.getSong_id() + "");
                            break;
                        case R.id.ivShare:
                            Song infos = mService.getSong();
                            if (infos.getIsLocal() == 1) {
                                CommonUtil.showToast(PlayerActivity.this, "暂不支持本地歌曲分享");
                                return;
                            }
                            CommonUtil.share(PlayerActivity.this, BaiduMusicApi.share.shareSong(infos.getSong_id() + ""), infos.getAuthor() + "-" + infos.getTitle());
                            break;
                        case R.id.ivDel:
                            mService.delSong();
                            break;
                        case R.id.ivPlayList:
                            if (popupWindow == null) {
                                popupWindow = new MusicListPopupWindow(PlayerActivity.this);
                                popupWindow.setonClearClickListener(new MusicListPopupWindow.onClearClickListener() {
                                    @Override
                                    public void clearClick() {
                                        try {
                                            mService.delAllSong();
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                popupWindow.setOnPopItemClickListener(new MusicListPopupWindow.onPopItemClickListener() {
                                    @Override
                                    public void popItemClick(Song info) {
                                        try {
                                            play(info, true);
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            popupWindow.show(binding.llMain, mService.getSongList(), mService.getPlayMode());
                            break;
                        case R.id.ivPlayState:

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

                            break;
                        case R.id.ivUp:
                            mService.doAction(ContentValue.PlayAction.Last);
                            break;
                        case R.id.ivPlay:
                            String action;
                            int drawbleId;
                            if (ispaused) {
                                action = ContentValue.PlayAction.Play;
                                drawbleId = R.drawable.paused;
                            } else {
                                action = ContentValue.PlayAction.Pause;
                                drawbleId = R.drawable.player;
                            }
                            mService.doAction(action);
                            binding.ivPlay.setImageResource(drawbleId);
                            break;
                        case R.id.ivNext:
                            mService.doAction(ContentValue.PlayAction.Next);
                            break;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        type = getIntent().getIntExtra(ContentValue.IntentKey.IntentKeyInt, 0);
        if (type == 1) {
            Song_id = getIntent().getLongExtra(ContentValue.IntentKey.IntentKeyStr, 0);
            startBaseReqTask(this, null);
        } else if (type == 2)

        {
            ArrayList<Song> list = getIntent().getParcelableArrayListExtra(ContentValue.IntentKey.IntentKeyList);
            int index = getIntent().getIntExtra(ContentValue.IntentKey.IntentKeyIndex, 0);
            try {
                mService.setSongList(list, true, true, index);
                if (list != null && list.size() > 0) {
                    Song info = list.get(index);
                    getSupportActionBar().setTitle(info.getTitle());
                    getSupportActionBar().setSubtitle(info.getAuthor());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestData() {
        if (!isRequest()) {
            return;
        }
        MyVolley.getInstance(this).get(BaiduMusicApi.Song.songPlayInfo(Song_id + ""), new MyVolley.MyCallBack() {
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
                getSupportActionBar().setTitle("未知");
                getSupportActionBar().setSubtitle("未知");
                binding.SkbPlayer.setProgress(0);
                binding.txtSongTime.setText("00:00");
                binding.txtPlayerTime.setText("00:00");
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
                AppLog.Loge("Error:" + e.getMessage());
                onDialogFailure("播放失败，请检查网络设置");
                return true;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mService.unregisterCallBack(callBack);
            callBack = null;
            mService = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void play(Song info, boolean isPlay) throws RemoteException {
        if (info != null) {
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
            MyOkHttp.getInstance().download(this, info.getLrclink(), info.getSong_id() + "", new DownloadResponseHandler() {
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

    private void getDownUrl(String id) {
        MyVolley.getInstance(this).get(BaiduMusicApi.Song.songInfo(id), new MyVolley.MyCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                int code = jsonObject.optInt(ContentValue.Json.ErrorCode);
                try {
                    if (code == ContentValue.Json.Successcode) {
                        JSONObject songurl = jsonObject.optJSONObject(ContentValue.Json.Songurl);
                        JSONArray url = songurl.optJSONArray(ContentValue.Json.Url);
                        List<DownInfo> downInfos = JSON.parseArray(url.toString(), DownInfo.class);
                        if (downListPop == null) {
                            downListPop = new DownListPopupWindow(PlayerActivity.this);
                            downListPop.setOnPopItemClickListener(new DownListPopupWindow.onPopItemClickListener() {
                                @Override
                                public void popItemClick(int position) {

                                }
                            });
                        }
                        downListPop.show(binding.llMain, downInfos);
                    } else {
                        onFailure("暂无下载地址");
                    }
                } catch (Exception e) {
                    AppLog.Loge(e.getMessage());
                    onFailure("暂无下载地址");
                }

            }

            @Override
            public void onFailure(String msg) {
                onDialogFailure(msg);
            }
        });
    }
}
