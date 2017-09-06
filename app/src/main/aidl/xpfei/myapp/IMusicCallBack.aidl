
package xpfei.myapp;
import xpfei.myapp.model.Song;

interface IMusicCallBack {
    //播放回调接口
    void callBack(int CurrentPosition,int duration);

    void doSome(boolean isPaused);

    void getCurrent(in Song song);

    void onError();
}
