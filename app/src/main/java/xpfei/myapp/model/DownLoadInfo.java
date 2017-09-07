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
    private boolean isPause;//是否暂停
    private boolean isFinish;//是否完成
    private String imgPath;//图片路劲
    private long song_id;//歌曲id

    protected DownLoadInfo(Parcel in) {
        taskId = in.readLong();
        downloadUrl = in.readString();
        fileName = in.readString();
        fileSize = in.readLong();
        filePath = in.readString();
        totalSize = in.readLong();
        isPause = in.readByte() != 0;
        isFinish = in.readByte() != 0;
        imgPath = in.readString();
        song_id = in.readLong();
    }

    @Generated(hash = 1034885698)
    public DownLoadInfo(Long taskId, String downloadUrl, String fileName, long fileSize,
                        String filePath, long totalSize, boolean isPause, boolean isFinish,
                        String imgPath, long song_id) {
        this.taskId = taskId;
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.totalSize = totalSize;
        this.isPause = isPause;
        this.isFinish = isFinish;
        this.imgPath = imgPath;
        this.song_id = song_id;
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
        dest.writeByte((byte) (isPause ? 1 : 0));
        dest.writeByte((byte) (isFinish ? 1 : 0));
        dest.writeString(imgPath);
        dest.writeLong(song_id);
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

    public boolean getIsPause() {
        return this.isPause;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }

    public boolean getIsFinish() {
        return this.isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
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
}
