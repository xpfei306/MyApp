package xpfei.myapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import xpfei.myapp.model.Song;
import xpfei.myapp.util.ContentValue;
import xpfei.myapp.manager.Player;

/**
 * Description: 音乐播放的service
 * Author: xpfei
 * Date:   2017/08/30
 */
public class MusicPlayService extends Service {
    private List<CustomerClient> mClientsList = new ArrayList<>();
    private RemoteCallbackList<IMusicCallBack> mCallBacks = new RemoteCallbackList<>();
    private Player player;
    private IMusicPlayerInterface.Stub mBinder = new IMusicPlayerInterface.Stub() {
        @Override
        public void registerCallBack(IMusicCallBack cb) throws RemoteException {
            mCallBacks.register(cb);
        }

        @Override
        public void unregisterCallBack(IMusicCallBack cb) throws RemoteException {
            mCallBacks.unregister(cb);
        }

        @Override
        public void setSongList(List<Song> list, boolean isPlay) throws RemoteException {

        }

        @Override
        public void setSong(Song song, boolean isPlay) throws RemoteException {
            player.addMusic(song, isPlay);
        }

        @Override
        public void doAction(String action) throws RemoteException {
            if (ContentValue.PlayAction.Play.equals(action)) {
                player.start();
                notifyPlayState(player.isPaused());
            } else if (ContentValue.PlayAction.Pause.equals(action)) {
                player.pause();
                notifyPlayState(player.isPaused());
            } else if (ContentValue.PlayAction.Last.equals(action)) {
                player.playLast();
                notifySong(player.getPlayingSong());
            } else if (ContentValue.PlayAction.Next.equals(action)) {
                player.playNext();
                notifySong(player.getPlayingSong());
            }
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            player.setPlayMode(mode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return player.getPlayMode();
        }

        @Override
        public List<Song> getSongList() throws RemoteException {
            return player.getSongList();
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            player.seekTo(progress);
        }
    };

    private void notifyCallBack(int CurrentPosition, int duration) {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).callBack(CurrentPosition, duration);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    private void notifyPlayState(boolean isPaused) {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).doSome(isPaused);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    private void notifySong(Song song) {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).getCurrent(song);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    private void notifyError() {
        int len = mCallBacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                mCallBacks.getBroadcastItem(i).onError();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = Player.getInstance();
        player.setOnPlayChangeListener(new Player.playChangeListener() {
            @Override
            public void onChange(int CurrentPosition, int duration) {
                notifyCallBack(CurrentPosition, duration);
            }

            @Override
            public void onError() {
                notifyError();
            }
        });
    }

    @Override
    public void onDestroy() {
        //销毁回调资源否则要内存泄露
        mCallBacks.kill();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class CustomerClient implements IBinder.DeathRecipient {
        public final IBinder mToken;
        public final String mCustomerName;

        public CustomerClient(IBinder mToken, String mCustomerName) {
            this.mToken = mToken;
            this.mCustomerName = mCustomerName;
        }

        @Override
        public void binderDied() {
            if (mClientsList.indexOf(this) >= 0) {
                mClientsList.remove(this);
            }
        }
    }
}
