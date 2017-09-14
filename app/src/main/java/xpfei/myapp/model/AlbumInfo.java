package xpfei.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description: 专辑javabean
 * Author: xpfei
 * Date:   2017/08/22
 */
public class AlbumInfo implements Parcelable {
    private String album_id;//专辑id
    private String title;//专辑名称
    private String author;//作者
    private String pic_radio;//图片
    private String country;//发行地
    private String publishtime;//发行日期

    public AlbumInfo() {
    }

    protected AlbumInfo(Parcel in) {
        album_id = in.readString();
        title = in.readString();
        author = in.readString();
        pic_radio = in.readString();
        country = in.readString();
        publishtime = in.readString();
    }

    public static final Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>() {
        @Override
        public AlbumInfo createFromParcel(Parcel in) {
            return new AlbumInfo(in);
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };

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

    public String getPic_radio() {
        return pic_radio;
    }

    public void setPic_radio(String pic_radio) {
        this.pic_radio = pic_radio;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(album_id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(pic_radio);
        dest.writeString(country);
        dest.writeString(publishtime);
    }
}
