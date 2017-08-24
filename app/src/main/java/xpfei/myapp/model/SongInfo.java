package xpfei.myapp.model;

/**
 * Description: 歌曲javabean
 * Author: xpfei
 * Date:   2017/08/22
 */
public class SongInfo {
    private String album_title;//专辑名称
    private String title;//歌曲名称
    private String author;//作者
    private String pic_small;//图片
    private String song_id;//歌曲id

    public SongInfo() {
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }
}
