
package xpfei.myapp.service;
import xpfei.myapp.service.IMusicCallBack;
import xpfei.myapp.model.Song;

interface IMusicPlayerInterface {

     //注册回调接口
     void registerCallBack(IMusicCallBack cb);
     //注销回调接口
     void unregisterCallBack(IMusicCallBack cb);
     //添加多首歌曲
     void setSongList(in List<Song> list,boolean isPlay,boolean isClear);
     //添加单首歌曲
     void setSong(in Song song,boolean isPlay);
     //动作 （播放、上一曲、下一曲等）
     void doAction(String action);
     //设置播放模式（0列表循环1单曲循环2随机）
     void setPlayMode(int mode);
     //获取当前播放模式（0列表循环1单曲循环2随机）
     int getPlayMode();
     //删除播放列表中所有歌曲
     void delAllSong();
     //删除播放正在播放的歌曲
     void delSong();
     //获取当前播放列表
     List<Song> getSongList();
     //获取当前播放歌曲
     Song getSong();
     //滑动seekbar
     void seekTo(int progress);
}
