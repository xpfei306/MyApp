package xpfei.myapp.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import xpfei.myapp.db.SongDbManager;
import xpfei.myapp.model.Song;
import xpfei.mylibrary.utils.AppLog;
import xpfei.mylibrary.utils.StringUtil;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/30
 */
public class Player implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static volatile Player sInstance;
    private MediaPlayer mPlayer;
    private SongDbManager manager;
    private boolean isPaused;
    private int playingIndex = -1;
    private playChangeListener listener;
    private Handler handler = new Handler();
    private int musicMode;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null && !isPaused) {
                Song info = getPlayingSong();
                listener.onChange(info, mPlayer.getCurrentPosition(), mPlayer.getDuration());
                handler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * 当前音乐的播放的监听事件
     */
    public interface playChangeListener {
        void onChange(Song song, int CurrentPosition, int duration);

        void onError();
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
        mPlayer.setOnErrorListener(this);
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

    /**
     * 添加单首音乐
     *
     * @param info   歌曲信息
     * @param isPlay 是否立即播放
     */
    public void addMusic(Song info, boolean isPlay) {
        manager.insertOrReplace(info);
        if (isPlay) {
            play(info);
        }
    }

    public void addMusic(List<Song> list) {
        addMusic(list, false);
    }

    public void addMusic(List<Song> list, boolean isPlay) {
        addMusic(list, isPlay, false);
    }

    public void addMusic(List<Song> list, boolean isPlay, boolean isClear) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (isClear) {
            manager.deleteAll();
        }
        manager.insertOrReplaceList(list);
        if (isPlay) {
            playingIndex = 0;
            Song info = list.get(playingIndex);
            play(info);
        }
    }

    public void playLast() {
        isPaused = false;
        List<Song> list = manager.loadAll();
        int size = list.size();
        if (size > 0) {
            switch (musicMode) {
                case 1:
                    SinglesMusic();
                    break;
                case 2:
                    randomMusic();
                    break;
                default:
                    playingIndex--;
                    if (playingIndex < 0) {
                        playingIndex = size - 1;
                    }
                    Song info = list.get(playingIndex);
                    if (info != null) {
                        play(info);
                    }
                    break;
            }
        } else {
            if (listener != null) {
                listener.onError();
            }
        }
    }

    public void playNext() {
        isPaused = false;
        List<Song> list = manager.loadAll();
        int size = list.size();
        if (size > 0) {
            switch (musicMode) {
                case 1:
                    SinglesMusic();
                    break;
                case 2:
                    randomMusic();
                    break;
                default:
                    playingIndex++;
                    if (playingIndex > size - 1) {
                        playingIndex = 0;
                    }
                    Song info = list.get(playingIndex);
                    if (info != null) {
                        play(info);
                    }
                    break;
            }
        } else {
            if (listener != null) {
                listener.onError();
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

    public void play(Song info) {
        if (info == null) {
            if (listener != null) {
                listener.onError();
                return;
            }
        }
        String path;
        if (info.getIsLocal() == 1) {
            path = info.getFile_link_local();
        } else {
            path = info.getFile_link();
        }
        if (StringUtil.isEmpty(path)) {
            if (listener != null) {
                listener.onError();
                return;
            }
        }
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            handler.removeCallbacks(runnable);
            mPlayer.reset();
            isPaused = false;
            mPlayer.setDataSource(path);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            AppLog.Logd(e.getMessage());
        }
    }

    public Song getPlayingSong() {
        List<Song> list = manager.loadAll();
        if (playingIndex >= 0 && playingIndex <= list.size() - 1)
            return list.get(playingIndex);
        return null;
    }

    public List<Song> getSongList() {
        return manager.loadAll();
    }

    public void seekTo(int progress) {
        if (mPlayer.isPlaying()) {
            mPlayer.seekTo(progress);
        }
    }

    public void releasePlayer() {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
        handler.removeCallbacksAndMessages(null);
        mPlayer = null;
        sInstance = null;
        handler = null;
    }

    public boolean isPaused() {
        return mPlayer.isPlaying();
    }

    public void setPlayMode(int mode) {
        musicMode = mode;
    }

    public int getPlayMode() {
        return musicMode;
    }

    public void delAllSong() {
        manager.deleteAll();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public void delSong() {
        Song song = getPlayingSong();
        manager.deleteByKey(song.getSong_id());
        playNext();
    }

    private void randomMusic() {
        List<Song> list = manager.loadAll();
        if (list.size() > 0) {
            if (list.size() == 1) {
                playingIndex = 0;
            } else if (list.size() > 1) {
                Random random = new Random();
                playingIndex = random.nextInt(list.size() - 1);
            }
            Song song = list.get(playingIndex);
            play(song);
        } else {
            if (listener != null) {
                listener.onError();
            }
        }
    }

    private void SinglesMusic() {
        Song song = getPlayingSong();
        if (song != null) {
            play(song);
        } else {
            if (listener != null) {
                listener.onError();
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playNext();
        Song info = getPlayingSong();
        if (listener != null) {
            listener.onChange(info, 0, 0);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mPlayer.start();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (listener != null) {
            listener.onError();
        }
        handler.removeCallbacks(runnable);
        mPlayer.reset();
        return true;
    }
}
