package xpfei.myapp.model;

/**
 * Description: 歌曲下载信息
 * Author: xpfei
 * Date:   2017/09/15
 */
public class DownInfo {
    private boolean can_load;//是否可以下载
    private int file_bitrate;//音质
    private String file_extension;//音乐文件类型
    private String file_link;//下载地址
    private long file_size;//文件大小

    public boolean isCan_load() {
        return can_load;
    }

    public void setCan_load(boolean can_load) {
        this.can_load = can_load;
    }

    public int getFile_bitrate() {
        return file_bitrate;
    }

    public void setFile_bitrate(int file_bitrate) {
        this.file_bitrate = file_bitrate;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }
}
