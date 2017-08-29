package xpfei.myapp.util;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import xpfei.mylibrary.utils.AppLog;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/29
 */
public class Player implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer; // 媒体播放器
    private SeekBar seekBar; // 拖动条
    private Timer mTimer = new Timer(); // 计时器
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
        }
    };

    // 初始化播放器
    public Player(SeekBar seekBar) {
        super();
        this.seekBar = seekBar;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 每一秒触发一次
        mTimer.schedule(timerTask, 0, 1000);
    }

    // 计时器
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && !seekBar.isPressed()) {
                handler.sendEmptyMessage(0); // 发送消息
            }
        }
    };

    public void play() {
        mediaPlayer.start();
    }

    /**
     * @param url url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
        } catch (IllegalArgumentException | IOException | IllegalStateException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void playIn(AssetFileDescriptor descriptor) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength()); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 暂停
    public void pause() {
        mediaPlayer.pause();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 播放准备
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    // 播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
        AppLog.Logd("放完了~");
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }
}
