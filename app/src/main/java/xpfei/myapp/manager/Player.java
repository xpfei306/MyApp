package xpfei.myapp.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;
import java.util.List;

import xpfei.myapp.db.SongDbManager;
import xpfei.myapp.model.Song;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/30
 */
public class Player implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static volatile Player sInstance;
    private MediaPlayer mPlayer;
    private SongDbManager manager;
    private boolean isPaused;
    private int playingIndex = 0;
    private playChangeListener listener;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null && !isPaused) {
                listener.onChange(mPlayer.getCurrentPosition(), mPlayer.getDuration());
            }
            handler.postDelayed(this, 1000);
        }
    };

    public interface playChangeListener {
        void onChange(int CurrentPosition, int duration);
    }

    public void setOnPlayChangeListener(playChangeListener listener) {
        this.listener = listener;
    }

    private Player() {
        mPlayer = new MediaPlayer();
        manager = new SongDbManager();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
    }

    public static Player getInstance() {
        if (sInstance == null) {
            synchronized (Player.class) {
                if (sInstance == null) {
                    sInstance = new Player();
                }
            }
        }
        return sInstance;
    }

    public void addMusic(Song info, boolean isPlay) {
        manager.insert(info);
        if (isPlay) {
            play(info.getFile_link());
        }
    }

    public void addMusic(List<Song> infos, boolean isPlay) {
        if (infos != null && infos.size() > 0) {
            manager.insertList(infos);
            if (isPlay) {
                play(infos.get(0).getFile_link());
            }
        }
    }

    public void playLast() {
        isPaused = false;
        int size = manager.loadAll().size();
        AppLog.Logd("前：" + playingIndex);
        if (size > 0) {
            playingIndex--;
            AppLog.Logd("中：" + playingIndex);
            if (playingIndex < 0) {
                playingIndex = size - 1;
            }
            AppLog.Logd("后：" + playingIndex);
            Song info = getPlayingSong();
            if (info != null) {
                play(info.getFile_link());
            }
        }
    }

    public void playNext() {
        isPaused = false;
        int size = manager.loadAll().size();
        if (size > 0) {
            playingIndex++;
            if (playingIndex > size - 1) {
                playingIndex = 0;
            }
            Song info = getPlayingSong();
            if (info != null) {
                play(info.getFile_link());
            }
        }
    }

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            isPaused = true;
        }
    }

    public void start() {
        if (isPaused) {
            mPlayer.start();
            isPaused = false;
        }
    }

    public void play(String path) {
        try {
            isPaused = false;
            if (StringUtil.isEmpty(path)) {
                List<Song> list = manager.loadAll();
                if (list.size() == 0) {
                    AppLog.Logd("本地没有歌曲");
                    return;
                }
                path = list.get(0).getFile_link();
            }
            mPlayer.reset();
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 1000);
        } catch (IOException e) {
            AppLog.Logd(e.getMessage());
        }
    }

    public Song getPlayingSong() {
        List<Song> list = manager.loadAll();
        return list.get(playingIndex);
    }

    public void seekTo(int progress) {
        if (mPlayer.isPlaying()) {
            mPlayer.seekTo(progress);
        }
    }

    public void releasePlayer() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        sInstance = null;
    }

    public boolean isPaused() {
        return mPlayer.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (listener != null) {
            listener.onChange(0, mediaPlayer.getDuration());
        }
        playNext();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
