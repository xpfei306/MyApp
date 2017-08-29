package xpfei.myapp.model;

/**
 * Description: (这里用一句话描述这个类的作用)
 * Author: xpfei
 * Date:   2017/08/28
 */
public class SongDownInfo {
    private boolean can_load;
    private int file_bitrate;
    private String file_extension;
    private String file_link;
    private int file_size;
    private int song_file_id;

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

    public int getFile_size() {
        return file_size;
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public int getSong_file_id() {
        return song_file_id;
    }

    public void setSong_file_id(int song_file_id) {
        this.song_file_id = song_file_id;
    }
}
