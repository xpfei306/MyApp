package xpfei.myapp.model;

import java.util.List;

/**
 * Description: 歌单详情
 * Author: xpfei
 * Date:   2017/09/18
 */
public class GeDanDetailInfo {
    private String listid;
    private String title;
    private String pic_500;
    private String tag;
    private String desc;
    private List<Song> content;

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic_500() {
        return pic_500;
    }

    public void setPic_500(String pic_500) {
        this.pic_500 = pic_500;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Song> getContent() {
        return content;
    }

    public void setContent(List<Song> content) {
        this.content = content;
    }
}
