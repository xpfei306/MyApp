package xpfei.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description: 下载javabean
 * Author: xpfei
 * Date:   2017/09/07
 */
@Entity
public class DownLoadInfo implements Parcelable {
    @Id(autoincrement = true)
    private Long taskId;//下载id
    private String downloadUrl;//下载地址
    private String fileName;//文件名称
    private long fileSize;//文件大小
    private String filePath;//存储路劲
    private long totalSize;//下载的文件大小
    private String imgPath;//图片路劲
    private long song_id;//歌曲id
    private int state;//0未知状态1开始下载2下载中3取消下载4下载错误5下载完成

    protected DownLoadInfo(Parcel in) {
        taskId = in.readLong();
        downloadUrl = in.readString();
        fileName = in.readString();
        fileSize = in.readLong();
        filePath = in.readString();
        totalSize = in.readLong();
        imgPath = in.readString();
        song_id = in.readLong();
        state = in.readInt();
    }

    @Generated(hash = 2097532767)
    public DownLoadInfo(Long taskId, String downloadUrl, String fileName, long fileSize,
                        String filePath, long totalSize, String imgPath, long song_id, int state) {
        this.taskId = taskId;
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.totalSize = totalSize;
        this.imgPath = imgPath;
        this.song_id = song_id;
        this.state = state;
    }

    @Generated(hash = 1743687477)
    public DownLoadInfo() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(taskId);
        dest.writeString(downloadUrl);
        dest.writeString(fileName);
        dest.writeLong(fileSize);
        dest.writeString(filePath);
        dest.writeLong(totalSize);
        dest.writeString(imgPath);
        dest.writeLong(song_id);
        dest.writeInt(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public long getSong_id() {
        return this.song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static final Creator<DownLoadInfo> CREATOR = new Creator<DownLoadInfo>() {
        @Override
        public DownLoadInfo createFromParcel(Parcel in) {
            return new DownLoadInfo(in);
        }

        @Override
        public DownLoadInfo[] newArray(int size) {
            return new DownLoadInfo[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DownLoadInfo) {
            DownLoadInfo info = (DownLoadInfo) obj;
            return info.getTaskId() == this.taskId;
        }
        return super.equals(obj);
    }
}
