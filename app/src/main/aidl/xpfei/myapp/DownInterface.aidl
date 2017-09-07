
package xpfei.myapp;
import xpfei.myapp.DownCallBack;
import xpfei.myapp.model.DownLoadInfo;
interface DownInterface {
     //注册回调接口
     void registerCallBack(DownCallBack cb);
     //注销回调接口
     void unregisterCallBack(DownCallBack cb);
     //开始下载
     void startDown(in DownLoadInfo info);
      //暂停下载
     void pauseDown(in DownLoadInfo info);
       //暂停下载
     void delDown(in DownLoadInfo info);
}
