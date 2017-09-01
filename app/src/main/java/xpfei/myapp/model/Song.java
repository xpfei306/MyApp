package xpfei.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description: 歌曲javabean
 * Author: xpfei
 * Date:   2017/08/22
 */
@Entity
public class Song implements Parcelable {
    @Id
    private String song_id;//歌曲id
    private String album_title;//专辑名称
    private String title;//歌曲名称
    private String author;//作者
    private String pic_small;//图片
    private String pic_huge;//高清图片
    private String lrclink;//歌词地址
    private String file_link;//歌曲播放地址
    private String lrclink_local;//歌词本地地址
    private String file_link_local;//歌曲播放本地地址

    public Song() {
    }

    protected Song(Parcel in) {
        song_id = in.readString();
        album_title = in.readString();
        title = in.readString();
        author = in.readString();
        pic_small = in.readString();
        pic_huge = in.readString();
        lrclink = in.readString();
        file_link = in.readString();
        lrclink_local = in.readString();
        file_link_local = in.readString();
    }

    @Generated(hash = 1794492670)
    public Song(String song_id, String album_title, String title, String author,
            String pic_small, String pic_huge, String lrclink, String file_link,
            String lrclink_local, String file_link_local) {
        this.song_id = song_id;
        this.album_title = album_title;
        this.title = title;
        this.author = author;
        this.pic_small = pic_small;
        this.pic_huge = pic_huge;
        this.lrclink = lrclink;
        this.file_link = file_link;
        this.lrclink_local = lrclink_local;
        this.file_link_local = file_link_local;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(song_id);
        parcel.writeString(album_title);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(pic_small);
        parcel.writeString(pic_huge);
        parcel.writeString(lrclink);
        parcel.writeString(file_link);
        parcel.writeString(lrclink_local);
        parcel.writeString(file_link_local);
    }

    public String getSong_id() {
        return this.song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getAlbum_title() {
        return this.album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPic_small() {
        return this.pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getPic_huge() {
        return this.pic_huge;
    }

    public void setPic_huge(String pic_huge) {
        this.pic_huge = pic_huge;
    }

    public String getLrclink() {
        return this.lrclink;
    }

    public void setLrclink(String lrclink) {
        this.lrclink = lrclink;
    }

    public String getFile_link() {
        return this.file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getLrclink_local() {
        return this.lrclink_local;
    }

    public void setLrclink_local(String lrclink_local) {
        this.lrclink_local = lrclink_local;
    }

    public String getFile_link_local() {
        return this.file_link_local;
    }

    public void setFile_link_local(String file_link_local) {
        this.file_link_local = file_link_local;
    }
}
