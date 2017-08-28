package xpfei.myapp.model;

import java.util.List;

/**
 * Description: 电台列表
 * Author: xpfei
 * Date:   2017/08/28
 */
public class RadioInfo {

    private int cid;
    private String title;
    private List<RadioListInfo> channellist;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RadioListInfo> getChannellist() {
        return channellist;
    }

    public void setChannellist(List<RadioListInfo> channellist) {
        this.channellist = channellist;
    }
}
