package xpfei.myapp.model;

import java.util.List;

/**
 * Description: 专辑详情
 * Author: xpfei
 * Date:   2017/09/13
 */
public class AlbumDeatilInfo {
    private AlbumInfo albumInfo;
    private List<Song> songlist;

    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.albumInfo = albumInfo;
    }

    public List<Song> getSonglist() {
        return songlist;
    }

    public void setSonglist(List<Song> songlist) {
        this.songlist = songlist;
    }
}
