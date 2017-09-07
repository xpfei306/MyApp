// DownCallBack.aidl
package xpfei.myapp;


interface DownCallBack {
     //下载过程
     void progress(long id, long currentBytes, long totalBytes);
      //下载成功
     void success(long id, String path);
     //下载失败
     void fail(long id);
     //删除
     void del(long id);
}
