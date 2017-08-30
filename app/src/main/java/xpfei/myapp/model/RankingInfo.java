package xpfei.myapp.model;

import java.util.List;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/25
 */
public class RankingInfo {
    private int count;
    private int type;
    private String name;
    private String pic_s260;
    private List<Song> content;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_s260() {
        return pic_s260;
    }

    public void setPic_s260(String pic_s260) {
        this.pic_s260 = pic_s260;
    }

    public List<Song> getContent() {
        return content;
    }

    public void setContent(List<Song> content) {
        this.content = content;
    }
}
