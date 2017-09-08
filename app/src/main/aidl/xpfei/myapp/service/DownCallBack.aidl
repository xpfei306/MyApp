// DownCallBack.aidl
package xpfei.myapp.service;
import xpfei.myapp.model.DownLoadInfo;

interface DownCallBack {
     //下载过程
     void progress(in DownLoadInfo info, long currentBytes, long totalBytes);
     //开始下载
     void start(in DownLoadInfo info);
     //下载成功
     void success(in DownLoadInfo info, String path);
     //下载失败
     void fail(in DownLoadInfo info);
     //取消下载
     void cancelDown(in DownLoadInfo info);
}
