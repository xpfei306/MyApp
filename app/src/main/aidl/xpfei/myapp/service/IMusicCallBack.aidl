
package xpfei.myapp.service;
import xpfei.myapp.model.Song;

interface IMusicCallBack {
    //播放回调接口
    void callBack(in Song song,int CurrentPosition,int duration);

    void doSome(boolean isPaused);

    void getCurrent(in Song song);

    void onError();
}
