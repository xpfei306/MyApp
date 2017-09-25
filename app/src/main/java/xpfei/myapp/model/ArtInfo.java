package xpfei.myapp.model;

import java.io.Serializable;

/**
 * Description:音乐人
 * Author: xpfei
 * Date:   2017/08/25
 */
public class ArtInfo implements Serializable {
    private String artist_id;
    private String avatar_big;
    private String firstchar;
    private String name;
    private String ting_uid;
    private int albums_total;
    private int songs_total;

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getAvatar_big() {
        return avatar_big;
    }

    public void setAvatar_big(String avatar_big) {
        this.avatar_big = avatar_big;
    }

    public String getFirstchar() {
        return firstchar;
    }

    public void setFirstchar(String firstchar) {
        this.firstchar = firstchar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTing_uid() {
        return ting_uid;
    }

    public void setTing_uid(String ting_uid) {
        this.ting_uid = ting_uid;
    }

    public int getAlbums_total() {
        return albums_total;
    }

    public void setAlbums_total(int albums_total) {
        this.albums_total = albums_total;
    }

    public int getSongs_total() {
        return songs_total;
    }

    public void setSongs_total(int songs_total) {
        this.songs_total = songs_total;
    }
}
