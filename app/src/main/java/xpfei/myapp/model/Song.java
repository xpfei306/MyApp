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
    @Id(autoincrement = true)
    private Long song_id;//歌曲id
    private String album_title;//专辑名称
    private String title;//歌曲名称
    private String author;//作者
    private String pic_small;//图片
    private String pic_huge;//高清图片
    private String lrclink;//歌词地址
    private String file_link;//歌曲播放地址
    private String lrclink_local;//歌词本地地址
    private String file_link_local;//歌曲播放本地地址
    private int isLocal;//是否是本地歌曲1是0否
    private long album_id;//专辑id

    protected Song(Parcel in) {
        song_id = in.readLong();
        album_title = in.readString();
        title = in.readString();
        author = in.readString();
        pic_small = in.readString();
        pic_huge = in.readString();
        lrclink = in.readString();
        file_link = in.readString();
        lrclink_local = in.readString();
        file_link_local = in.readString();
        isLocal = in.readInt();
        album_id = in.readLong();
    }

    @Generated(hash = 268770055)
    public Song(Long song_id, String album_title, String title, String author,
            String pic_small, String pic_huge, String lrclink, String file_link,
            String lrclink_local, String file_link_local, int isLocal,
            long album_id) {
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
        this.isLocal = isLocal;
        this.album_id = album_id;
    }

    @Generated(hash = 87031450)
    public Song() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(song_id);
        dest.writeString(album_title);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(pic_small);
        dest.writeString(pic_huge);
        dest.writeString(lrclink);
        dest.writeString(file_link);
        dest.writeString(lrclink_local);
        dest.writeString(file_link_local);
        dest.writeInt(isLocal);
        dest.writeLong(album_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getSong_id() {
        return this.song_id;
    }

    public void setSong_id(Long song_id) {
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

    public int getIsLocal() {
        return this.isLocal;
    }

    public void setIsLocal(int isLocal) {
        this.isLocal = isLocal;
    }

    public long getAlbum_id() {
        return this.album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
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
}
