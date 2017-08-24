package xpfei.myapp.model;

/**
 * Description: 专辑javabean
 * Author: xpfei
 * Date:   2017/08/22
 */
public class AlbumInfo {
    private String album_id;//专辑id
    private String title;//专辑名称
    private String author;//作者
    private String pic_small;//图片
    private String publishtime;//发行日期

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
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

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }
}
