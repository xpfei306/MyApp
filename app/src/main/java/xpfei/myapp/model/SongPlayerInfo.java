package xpfei.myapp.model;

import java.util.List;

/**
 * Description: 音乐播放的javabean
 * Author: xpfei
 * Date:   2017/08/28
 */
public class SongPlayerInfo {
    private int error_code;
    private SongBaseInfo songinfo;
    private Url songurl;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public SongBaseInfo getSonginfo() {
        return songinfo;
    }

    public void setSonginfo(SongBaseInfo songinfo) {
        this.songinfo = songinfo;
    }

    public Url getSongurl() {
        return songurl;
    }

    public void setSongurl(Url songurl) {
        this.songurl = songurl;
    }

    public class Url {
        private List<SongDownInfo> url;

        public List<SongDownInfo> getUrl() {
            return url;
        }

        public void setUrl(List<SongDownInfo> url) {
            this.url = url;
        }
    }
}
