
package xpfei.myapp;
import xpfei.myapp.IMusicCallBack;
import xpfei.myapp.model.Song;

interface IMusicPlayerInterface {

     //注册回调接口
     void registerCallBack(IMusicCallBack cb);
     //注销回调接口
     void unregisterCallBack(IMusicCallBack cb);
     //添加多首歌曲
     void setSongList(in List<Song> list,boolean isPlay);
     //添加单单首歌曲
     void setSong(in Song song,boolean isPlay);
     //动作 （播放、上一曲、下一曲等）
     void doAction(String action);
     //设置播放模式（0列表循环1单曲循环2随机）
     void setPlayMode(int mode);
     //获取当前播放模式（0列表循环1单曲循环2随机）
     int getPlayMode();
     //获取当前播放列表
     List<Song> getSongList();
     //滑动seekbar
     void seekTo(int progress);
}
