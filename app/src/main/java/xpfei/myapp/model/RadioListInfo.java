package xpfei.myapp.model;

/**
 * Description: 电台列表页
 * Author: xpfei
 * Date:   2017/08/28
 */
public class RadioListInfo {
    private String cate_sname;
    private String ch_name;
    private String channelid;
    private String name;
    private String thumb;
    private String artistid;
    private String avatar;

    public String getCate_sname() {
        return cate_sname;
    }

    public void setCate_sname(String cate_sname) {
        this.cate_sname = cate_sname;
    }

    public String getCh_name() {
        return ch_name;
    }

    public void setCh_name(String ch_name) {
        this.ch_name = ch_name;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getArtistid() {
        return artistid;
    }

    public void setArtistid(String artistid) {
        this.artistid = artistid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
